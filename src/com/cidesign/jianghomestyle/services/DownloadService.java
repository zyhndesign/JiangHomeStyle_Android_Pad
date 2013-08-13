package com.cidesign.jianghomestyle.services;

import java.io.IOException;
import java.util.List;

import com.cidesign.jianghomestyle.async.AsyncDownTask;
import com.cidesign.jianghomestyle.db.DatabaseHelper;
import com.cidesign.jianghomestyle.entity.ArticleEntity;
import com.cidesign.jianghomestyle.entity.FileListEntity;
import com.cidesign.jianghomestyle.util.StorageUtils;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class DownloadService extends Service
{
	private static final String TAG = DownloadService.class.getSimpleName();
	private DatabaseHelper dbHelper;
	public static final String BROADCAST_UPDATE_DATA_ACTION = "BROADCAST_UPDATE_DATA_ACTION";
	
	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}

	@Override
	public void onCreate()
	{
		super.onCreate();
		
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) 
	{		
		dbHelper = new DatabaseHelper(this);
		RuntimeExceptionDao<FileListEntity, Integer>  dao = dbHelper.getFileListDataDao();	
		List<FileListEntity> listFile = dao.queryForEq("downloadFlag", 0);
		Log.d(TAG, "需要下载的文件数为："+ listFile.size());
		if (StorageUtils.isSDCardPresent())
		{
			if(!StorageUtils.checkAvailableStorage())
			{
				Toast.makeText(this, "SD卡容量不足，请换卡或者清除不用的文件！", Toast.LENGTH_LONG).show();
			}
			else
			{						
				try
				{
					StorageUtils.deleteDirectory(StorageUtils.FILE_TEMP_ROOT);
					
					StorageUtils.mkdir();
				}
				catch (IOException e)
				{
					e.printStackTrace();
					Toast.makeText(this, "创建文件夹不成功！", Toast.LENGTH_LONG).show();
				}
				dbHelper = new DatabaseHelper(this);
				
				new AsyncDownTask(this,dbHelper).execute(listFile);					
			}
		}
		else
		{
			Toast.makeText(this, "请插入SD卡", Toast.LENGTH_LONG).show();
		}
	    return START_STICKY;
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
	}
}
