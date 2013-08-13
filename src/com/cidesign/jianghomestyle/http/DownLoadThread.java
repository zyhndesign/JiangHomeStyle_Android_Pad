package com.cidesign.jianghomestyle.http;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.cidesign.jianghomestyle.entity.ArticleEntity;
import com.cidesign.jianghomestyle.entity.FileListEntity;
import com.cidesign.jianghomestyle.services.DownloadService;
import com.cidesign.jianghomestyle.tools.FileOperationTools;
import com.cidesign.jianghomestyle.tools.MD5Tools;
import com.cidesign.jianghomestyle.tools.XmlParseTools;
import com.cidesign.jianghomestyle.util.StorageUtils;
import com.j256.ormlite.dao.RuntimeExceptionDao;

public class DownLoadThread extends Thread
{
	private static final String TAG = DownLoadThread.class.getSimpleName();
	
	private FileListEntity flEntity;
	private RuntimeExceptionDao<FileListEntity, Integer> fileDao = null;
	private RuntimeExceptionDao<ArticleEntity, Integer> articleDao = null;
	private Context ctx;
	
	public DownLoadThread(Context ctx,RuntimeExceptionDao<FileListEntity, Integer> fileDao,
			RuntimeExceptionDao<ArticleEntity, Integer> articleDao, FileListEntity flEntity)
	{
		this.ctx = ctx;
		this.fileDao = fileDao;
		this.articleDao = articleDao;
		this.flEntity = flEntity;
	}

	@Override
	public void run()
	{
		String address = flEntity.getUrl();
		if (address != null && !address.equals(""))
		{
			File target = new File(StorageUtils.FILE_TEMP_ROOT + flEntity.getServerID() + ".zip");

			OutputStream output = null;
			try
			{
				URL url = new URL(address);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();

				InputStream input = conn.getInputStream();
				if (!target.exists())
				{
					target.createNewFile();// �½��ļ�
					output = new FileOutputStream(target);
					// ��ȡ���ļ�
					byte[] buffer = new byte[4 * 1024];
					int count = 0;
					while((count = input.read(buffer)) != -1)
			        {
						output.write(buffer, 0, count);
			        }
					
					output.flush();

					//У������ѹ������MD5ֵ
					
					if (flEntity.getMd5().equals(MD5Tools.getFileMD5String(target)))
					{
						// ���غ��ж��ļ����Ƿ����
						File file = new File(StorageUtils.FILE_ROOT + flEntity.getServerID());
						if (file.isDirectory())
						{
							Log.d(TAG, "ɾ���Ѿ����ڵ��ļ��У�" + flEntity.getServerID());
							StorageUtils.delete(file);
						}
											
						// ������Ϻ��ѹ�ļ�
						FileOperationTools.unZip(StorageUtils.FILE_TEMP_ROOT + flEntity.getServerID() + ".zip", StorageUtils.FILE_ROOT);
						
						// �������ݿ����Ϣ
						flEntity.setDownloadFlag(1);
						fileDao.update(flEntity);
						
						ArticleEntity articleEntity = XmlParseTools.readDocXML(StorageUtils.FILE_ROOT + "/" + flEntity.getServerID() + "/doc.xml");
						articleEntity.setServerID(flEntity.getServerID());
						articleEntity.setPost_date(flEntity.getTimestamp());
						if (articleEntity != null)
						{
							articleDao.createOrUpdate(articleEntity);
						}
						
						//���͹㲥����ǰ̨����
						Intent intent = new Intent(DownloadService.BROADCAST_UPDATE_DATA_ACTION);
						intent.putExtra("MODEL_TYPE", articleEntity.getCategory());
						intent.putExtra("serverID",articleEntity.getServerID());
						ctx.sendBroadcast(intent);
					}
					else
					{
						Log.d(TAG, "�´���������������"+flEntity.getServerID());
					}
					StorageUtils.delete(target);
				}

			}
			catch (MalformedURLException e)
			{
				e.printStackTrace();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			finally
			{
				try
				{
					if (output != null)
					{
						output.close();
					}
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}