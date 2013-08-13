package com.cidesign.jianghomestylerelease;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.androidquery.AQuery;
import com.cidesign.jianghomestyle.db.DatabaseHelper;
import com.cidesign.jianghomestyle.entity.ArticleEntity;
import com.cidesign.jianghomestyle.entity.FileListEntity;
import com.cidesign.jianghomestyle.tools.FileOperationTools;
import com.cidesign.jianghomestyle.tools.MD5Tools;
import com.cidesign.jianghomestyle.tools.XmlParseTools;
import com.cidesign.jianghomestyle.util.StorageUtils;
import com.cidesign.jianghomestyle.version.VersionUpdate;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class SplashActivity extends Activity
{
	private static final String TAG = SplashActivity.class.getSimpleName();
	
	private SharedPreferences settings = null;
	private SharedPreferences downTimeSettings = null;
	
	private DatabaseHelper dbHelper;
	private RuntimeExceptionDao<FileListEntity, Integer>  fileListDao = null;
	private RuntimeExceptionDao<ArticleEntity, Integer> articleDao = null;
	private AQuery aq;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.splash);
		settings = getSharedPreferences("INIT_PARAMS", Context.MODE_PRIVATE);
		downTimeSettings = getSharedPreferences("LAST_UPDATE_TIME", Context.MODE_PRIVATE); 
		dbHelper = new DatabaseHelper(this);
		fileListDao = dbHelper.getFileListDataDao();
		articleDao = dbHelper.getArticleListDataDao();
		aq = new AQuery(this);
	}

	@Override
	public void onStart()
	{
		super.onStart();
		
		int initData = settings.getInt("initData", 0);
		if (initData == 0) // 未初始化
		{
			if (StorageUtils.isSDCardPresent())
			{
				try
				{					
					StorageUtils.mkdir();
					
					new InitDataTask().execute();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			else
			{
				Toast.makeText(this, "请插入SD卡，该应用需要SD卡保存数据！", Toast.LENGTH_LONG).show();
			}
		}
		else if (initData == 1) // 已经初始化
		{
			VersionUpdate vUpdate = new VersionUpdate(this.getApplicationContext(),SplashActivity.this,aq);
			vUpdate.getServerVerCode();
			
		}
	}

	class InitDataTask extends AsyncTask<Void, Void, String>
	{
		List<FileListEntity> listFile = null;
		RuntimeExceptionDao<FileListEntity, Integer>  dao = null;
		
		@Override
		protected void onPreExecute()
		{
			// 第一个执行方法
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Void... arg0)
		{
			String time = "0";
			FileOperationTools.copyAssets(getApplicationContext());
			//读取拷贝到SD卡的清单列表文件list.xml
			
			try
			{
				listFile = XmlParseTools.parseFileInputStreamList(getAssets().open("filelist.xml"));
				
				for (FileListEntity flEntity : listFile)
				{
					fileListDao.create(flEntity);
					File target = new File(StorageUtils.FILE_TEMP_ROOT + flEntity.getServerID() + ".zip");
					if (flEntity.getMd5().equals(MD5Tools.getFileMD5String(target)))
					{
						// 下载后判断文件夹是否存在
						File file = new File(StorageUtils.FILE_ROOT + flEntity.getServerID());
						if (file.isDirectory())
						{
							StorageUtils.delete(file);
						}
											
						// 下载完毕后解压文件
						FileOperationTools.unZip(StorageUtils.FILE_TEMP_ROOT + flEntity.getServerID() + ".zip", StorageUtils.FILE_ROOT);
						
						// 更新数据库表信息
						flEntity.setDownloadFlag(1);
						fileListDao.update(flEntity);
						
						ArticleEntity articleEntity = XmlParseTools.readDocXML(StorageUtils.FILE_ROOT + "/" + flEntity.getServerID() + "/doc.xml");
						articleEntity.setServerID(flEntity.getServerID());
						articleEntity.setPost_date(flEntity.getTimestamp());
						if (articleEntity != null)
						{
							articleDao.createOrUpdate(articleEntity);							
						}
						
						time = flEntity.getTimestamp();
						
						StorageUtils.delete(target);
					}
				}
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//拷贝完成后，读取所有文件列表
			return time;
		}

		@Override
		protected void onPostExecute(String result)
		{
			Log.d("","初始化完毕....");
			super.onPostExecute(result);
			settings.edit().putInt("initData", 1).commit();
			downTimeSettings.edit().putString("lastUpdateTime", result).commit();
						
			startActivity(new Intent(SplashActivity.this, MainActivity.class));
			finish();
		}
	}
}
