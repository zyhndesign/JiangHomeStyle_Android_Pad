package com.cidesign.jianghomestyle.http;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.cidesign.jianghomestyle.R;
import com.cidesign.jianghomestyle.db.DatabaseHelper;
import com.cidesign.jianghomestyle.entity.ArticleEntity;
import com.cidesign.jianghomestyle.entity.FileListEntity;
import com.cidesign.jianghomestyle.entity.MusicEntity;
import com.cidesign.jianghomestyle.services.DownloadService;
import com.cidesign.jianghomestyle.tools.XmlParseTools;
import com.cidesign.jianghomestyle.util.StorageUtils;
import com.cidesign.jianghomestyle.viewlogic.MusicViewLogic;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.DeleteBuilder;

public class ArticalOperation
{
	private static final String TAG = ArticalOperation.class.getSimpleName();
	
	private AQuery aq;
	public DatabaseHelper dbHelper;
	private SharedPreferences settings = null;
	private String request_address = "";
	public Activity activity;
	private List<MusicEntity> musicList = null;
	private MusicViewLogic musicLogic;
	
	public MusicViewLogic getMusicLogic()
	{
		return musicLogic;
	}

	public ArticalOperation(Activity activity,AQuery aq, DatabaseHelper dbHelper)
	{
		this.activity = activity;
		this.aq = aq;
		this.dbHelper = dbHelper;
		settings = activity.getSharedPreferences("LAST_UPDATE_TIME", Context.MODE_PRIVATE); 
		request_address = activity.getResources().getString(R.string.request_address);
	}

	public void initMusicInfo(final View view)
	{
		
		String url = request_address + "/travel/wp-admin/admin-ajax.php?action=zy_get_music&programId=1";
		aq.ajax(url, JSONObject.class, new AjaxCallback<JSONObject>()
		{

			@Override
			public void callback(String url, JSONObject json, AjaxStatus status)
			{
				if (HttpStatus.SC_OK == status.getCode())
		    	{
					if (json != null)
					{
						try
						{
							Gson gson = new Gson();
							musicList = gson.fromJson(json.getJSONArray("data").toString(), 
									new TypeToken<List<MusicEntity>>(){}.getType());
							
						}
						catch (JSONException e)
						{
							e.printStackTrace();
						}
						
					}					
		    	}
				musicLogic = new MusicViewLogic(activity, dbHelper, view, musicList);
			}
		}); 
	}
	
	public void getArticleInfo()
	{
		Log.d(TAG,"request start time = " + settings.getString("lastUpdateTime", "0"));
		String url = request_address + "/travel/dataUpdate?lastUpdateDate=" + settings.getString("lastUpdateTime", "0")+"&category=1";
		//String url = request_address + "/travel/dataUpdate?lastUpdateDate=0";
		
		long expire = 15 * 1000;

		aq.ajax(url, String.class, expire, new AjaxCallback<String>() {

		    @Override
		    public void callback(String url, String html, AjaxStatus status) {  

		    	if (HttpStatus.SC_OK == status.getCode())
		    	{	
		    		//Log.d(TAG,html);
		    		new InsertDBArticleDate().execute(html);     
		    	}		        
		    }
		});
	}
	
	class InsertDBArticleDate extends AsyncTask<String, Integer, String>
	{
		List<FileListEntity> listFile = null;
		RuntimeExceptionDao<FileListEntity, Integer>  dao = null;
		
		@Override  
        protected void onPreExecute() 
		{  
			
		}
		
		@Override
		protected String doInBackground(String... params)
		{			
			
			listFile = XmlParseTools.parseFileList(params[0]);

	        if (listFile.size() > 0)
	        {
	        	String time = "0";
	        	
				dao = dbHelper.getFileListDataDao(); 
		        
		        for (FileListEntity fileEntity : listFile)
		        {
		        	if(fileEntity.getOperation() == 'u')
		        	{		       
		        		Log.d(TAG, "更新文章" + fileEntity.getServerID());
		        		fileEntity.setDownloadFlag(0);
		        		dao.createOrUpdate(fileEntity);
		        	}
		        	else if (fileEntity.getOperation() == 'd')
		        	{
		        		Log.d(TAG, "删除文章" + fileEntity.getServerID());
		        		//删除文件记录表数据
		        		dao.delete(fileEntity);
		        		
		        		//删除SD卡对应文件夹
		        		File file = new File(StorageUtils.FILE_ROOT + fileEntity.getServerID());
						if (file.isDirectory())
						{
							StorageUtils.delete(file);
						}
						
						//删除文件详情表记录
						RuntimeExceptionDao<ArticleEntity, Integer> articleDao = dbHelper.getArticleListDataDao();
						DeleteBuilder<ArticleEntity,Integer> delBuilder= articleDao.deleteBuilder();
						try
						{
							delBuilder.where().eq("serverID", fileEntity.getServerID());
							delBuilder.delete();
						}
						catch (SQLException e)
						{							
							e.printStackTrace();
						}
						
		        	}
		        	time = fileEntity.getTimestamp();
		        }
		        
		        //更新最后一次更新时间
		        SharedPreferences.Editor editor = settings.edit();  
				editor.putString("lastUpdateTime", time);  
				editor.commit();
	        }
			return null;
		}

		@Override  
        protected void onPostExecute(String result) { 
			if (dao == null)
			{
				dao = dbHelper.getFileListDataDao();
			}
			List<FileListEntity> listFile = dao.queryForEq("downloadFlag", 0);

			if (listFile.size() > 0)
			{
				//启动服务进行下载
				Intent intent = new Intent(activity,DownloadService.class);
				activity.startService(intent);
			}
		}
	}
}
