package com.cidesign.jianghomestyle.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;

public class StorageUtils
{
	private static final String TAG = StorageUtils.class.getSimpleName();

	private static final String SDCARD_ROOT = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
	public static final String APP_ROOT = SDCARD_ROOT + "JiangHomeStyle/";
	public static final String FILE_ROOT = APP_ROOT + "articles/";
	public static final String FILE_TEMP_ROOT = APP_ROOT + "temp/";
	public static final String THUMB_IMG_ROOT = APP_ROOT + "thumb/";
	
	private static final long LOW_STORAGE_THRESHOLD = 1024 * 1024 * 10;

	public static boolean isSdCardWrittenable()
	{
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
		{
			return true;
		}
		return false;
	}

	public static long getAvailableStorage()
	{

		String storageDirectory = null;
		storageDirectory = Environment.getExternalStorageDirectory().toString();

		try
		{
			StatFs stat = new StatFs(storageDirectory);
			long avaliableSize = ((long) stat.getAvailableBlocks() * (long) stat.getBlockSize());
			return avaliableSize;
		}
		catch (RuntimeException ex)
		{
			return 0;
		}
	}

	public static boolean checkAvailableStorage()
	{
		if (getAvailableStorage() < LOW_STORAGE_THRESHOLD)
		{
			return false;
		}

		return true;
	}

	public static boolean isSDCardPresent()
	{
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}

	public static void mkdir() throws IOException
	{
		File file = new File(APP_ROOT);
		if (!file.exists() || !file.isDirectory())
			file.mkdir();

		file = new File(FILE_ROOT);
		if (!file.exists() || !file.isDirectory())
			file.mkdir();

		file = new File(FILE_TEMP_ROOT);
		if (!file.exists() || !file.isDirectory())
			file.mkdir();
		
		file = new File(THUMB_IMG_ROOT);
		if (!file.exists() || !file.isDirectory())
			file.mkdir();
	}

	/**
	 * 根据路径删除指定的目录或文件，无论存在与否
	 * 
	 * @param sPath
	 *            要删除的目录或文件
	 * @return 删除成功返回 true，否则返回 false。
	 */
	public static boolean DeleteFolder(String sPath)
	{
		boolean flag = false;
		File file = new File(sPath);
		// 判断目录或文件是否存在
		if (!file.exists())
		{ // 不存在返回 false
			return flag;
		}
		else
		{
			// 判断是否为文件
			if (file.isFile())
			{ // 为文件时调用删除文件方法
				return deleteFile(sPath);
			}
			else
			{ // 为目录时调用删除目录方法
				return deleteDirectory(sPath);
			}
		}
	}

	/**
	 * 删除单个文件
	 * 
	 * @param sPath
	 *            被删除文件的文件名
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	public static boolean deleteFile(String sPath)
	{
		boolean flag = false;
		File file = new File(sPath);
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists())
		{
			file.delete();
			flag = true;
		}
		return flag;
	}

	public static boolean deleteDirectory(String sPath)
	{
		// 如果sPath不以文件分隔符结尾，自动添加文件分隔符
		if (!sPath.endsWith(File.separator))
		{
			sPath = sPath + File.separator;
		}
		File dirFile = new File(sPath);
		// 如果dir对应的文件不存在，或者不是一个目录，则退出
		if (!dirFile.exists() || !dirFile.isDirectory())
		{
			return false;
		}
		boolean flag = true;
		// 删除文件夹下的所有文件(包括子目录)
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++)
		{
			// 删除子文件
			if (files[i].isFile())
			{
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag)
					break;
			} // 删除子目录
			else
			{
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag)
					break;
			}
		}
		if (!flag)
			return false;
		// 删除当前目录
		if (dirFile.delete())
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public static Bitmap getLoacalBitmap(String url)
	{

		try
		{
			FileInputStream fis = new FileInputStream(url);
			return BitmapFactory.decodeStream(fis);

		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public static String size(long size)
	{

		if (size / (1024 * 1024) > 0)
		{
			float tmpSize = (float) (size) / (float) (1024 * 1024);
			DecimalFormat df = new DecimalFormat("#.##");
			return "" + df.format(tmpSize) + "MB";
		}
		else if (size / 1024 > 0)
		{
			return "" + (size / (1024)) + "KB";
		}
		else
			return "" + size + "B";
	}

	public static boolean delete(File path)
	{

		boolean result = true;
		if (path.exists())
		{
			if (path.isDirectory())
			{
				for (File child : path.listFiles())
				{
					result &= delete(child);
				}
				result &= path.delete(); // Delete empty directory.
			}
			if (path.isFile())
			{
				result &= path.delete();
			}
			if (!result)
			{
				Log.e(null, "Delete failed;");
			}
			return result;
		}
		else
		{
			Log.e(null, "File does not exist.");
			return false;
		}
	}
}
