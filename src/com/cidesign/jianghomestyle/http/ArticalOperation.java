package com.cidesign.jianghomestyle.http;

import java.io.File;
import java.util.List;

import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.cidesign.jianghomestyle.R;
import com.cidesign.jianghomestyle.db.DatabaseHelper;
import com.cidesign.jianghomestyle.entity.ContentEntity;
import com.cidesign.jianghomestyle.entity.MusicEntity;
import com.cidesign.jianghomestyle.tools.XmlAndJsonParseTools;
import com.cidesign.jianghomestyle.util.StorageUtils;
import com.cidesign.jianghomestyle.viewlogic.MusicViewLogic;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.dao.RuntimeExceptionDao;
/**
 * 
* @Title: ArticalOperation.java 
* @Package com.cidesign.jianghomestyle.http 
* @Description: about music operation and update data 
* @author liling  
* @date 2013年8月14日 下午2:57:55 
* @version V2.0
 */
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
	private List<ContentEntity> listFile = null;
	private RuntimeExceptionDao<ContentEntity, Integer> dao = null;
	
	public MusicViewLogic getMusicLogic()
	{
		return musicLogic;
	}

	public ArticalOperation(Activity activity, AQuery aq, DatabaseHelper dbHelper)
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
							musicList = gson.fromJson(json.getJSONArray("data").toString(), new TypeToken<List<MusicEntity>>()
							{
							}.getType());

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
		String url = request_address + "/travel/dataUpdate?lastUpdateDate=" + settings.getString("lastUpdateTime", "0")
				+ "&category=1";
		
		long expire = 15 * 1000;

		aq.ajax(url, String.class, expire, new AjaxCallback<String>()
		{

			@Override
			public void callback(String url, String html, AjaxStatus status)
			{

				if (HttpStatus.SC_OK == status.getCode())
				{
					listFile = XmlAndJsonParseTools.parseListByJson(html);

					if (listFile.size() > 0)
					{
						String time = "0";

						dao = dbHelper.getContentDataDao();

						for (ContentEntity cEntity : listFile)
						{
							if (cEntity.getOperation() == 'u')
							{
								Log.d(TAG, "更新文章" + cEntity.getServerID());
								cEntity.setDownloadFlag(0);
								dao.createOrUpdate(cEntity);
							}
							else if (cEntity.getOperation() == 'd')
							{
								Log.d(TAG, "删除文章" + cEntity.getServerID());
								// 删除文件记录表数据
								dao.delete(cEntity);

								// 删除SD卡对应文件夹
								File file = new File(StorageUtils.FILE_ROOT + cEntity.getServerID());
								if (file.isDirectory())
								{
									StorageUtils.delete(file);
								}

							}
							time = cEntity.getTimestamp();
						}

						// 更新最后一次更新时间
						SharedPreferences.Editor editor = settings.edit();
						editor.putString("lastUpdateTime", time);
						editor.commit();
					}
				}
			}
		});
	}
}
