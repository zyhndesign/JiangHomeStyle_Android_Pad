package com.cidesign.jianghomestyle.tools;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import com.cidesign.jianghomestyle.util.StorageUtils;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

public class FileOperationTools
{
	private static final String TAG = FileOperationTools.class.getSimpleName();

	/**
	 * 判断SD卡是否存在 true：存在 false:不存在
	 * 
	 * @return
	 */
	public static boolean hasSdcard()
	{
		String status = Environment.getExternalStorageState();

		if (status.equals(Environment.MEDIA_MOUNTED))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * 
	 * @param path
	 *            文件夹路径
	 */
	public static void isExist(String path)
	{
		File file = new File(path);
		// 判断文件夹是否存在,如果不存在则创建文件夹
		if (!file.exists())
		{
			file.mkdir();
		}
	}

	/**
	 * 获取SD卡的剩余容量
	 * 
	 * @return
	 */
	public long getSDFreeSize()
	{
		// 取得SD卡文件路径
		File path = Environment.getExternalStorageDirectory();
		StatFs sf = new StatFs(path.getPath());
		// 获取单个数据块的大小(Byte)
		long blockSize = sf.getBlockSize();
		// 空闲的数据块的数量
		long freeBlocks = sf.getAvailableBlocks();
		// 返回SD卡空闲大小
		// return freeBlocks * blockSize; //单位Byte
		// return (freeBlocks * blockSize)/1024; //单位KB
		return (freeBlocks * blockSize) / 1024 / 1024; // 单位MB
	}

	/**
	 * SD卡总容量
	 * 
	 * @return
	 */
	public long getSDAllSize()
	{
		// 取得SD卡文件路径
		File path = Environment.getExternalStorageDirectory();
		StatFs sf = new StatFs(path.getPath());
		// 获取单个数据块的大小(Byte)
		long blockSize = sf.getBlockSize();
		// 获取所有数据块数
		long allBlocks = sf.getBlockCount();
		// 返回SD卡大小
		// return allBlocks * blockSize; //单位Byte
		// return (allBlocks * blockSize)/1024; //单位KB
		return (allBlocks * blockSize) / 1024 / 1024; // 单位MB
	}

	private static void createDirectory(String directory, String subDirectory)
	{
		String dir[];
		File fl = new File(directory);
		try
		{
			if (subDirectory == "" && fl.exists() != true)
				fl.mkdir();
			else if (subDirectory != "")
			{
				dir = subDirectory.replace('\\', '/').split("/");
				for (int i = 0; i < dir.length; i++)
				{
					File subFile = new File(directory + File.separator + dir[i]);
					if (subFile.exists() == false)
						subFile.mkdir();
					directory += File.separator + dir[i];
				}
			}
		}
		catch (Exception ex)
		{
			System.out.println(ex.getMessage());
		}
	}

	public static void unZip(String zipFileName, String outputDirectory)
	{
		org.apache.tools.zip.ZipFile zipFile = null;
		try
		{
			zipFile = new org.apache.tools.zip.ZipFile(zipFileName);
			java.util.Enumeration e = zipFile.getEntries();
			org.apache.tools.zip.ZipEntry zipEntry = null;
			createDirectory(outputDirectory, "");
			while (e.hasMoreElements())
			{
				zipEntry = (org.apache.tools.zip.ZipEntry) e.nextElement();

				if (zipEntry.isDirectory())
				{
					String name = zipEntry.getName();
					name = name.substring(0, name.length() - 1);
					File f = new File(outputDirectory + File.separator + name);
					f.mkdir();
				}
				else
				{
					String fileName = zipEntry.getName();
					fileName = fileName.replace('\\', '/');
					if (fileName.indexOf("/") != -1)
					{
						createDirectory(outputDirectory, fileName.substring(0, fileName.lastIndexOf("/")));
						fileName = fileName.substring(fileName.lastIndexOf("/") + 1, fileName.length());
					}

					File f = new File(outputDirectory + File.separator + zipEntry.getName());

					f.createNewFile();
					InputStream in = zipFile.getInputStream(zipEntry);
					FileOutputStream out = new FileOutputStream(f);

					byte[] by = new byte[1024];
					int c;
					while ((c = in.read(by)) != -1)
					{
						out.write(by, 0, c);
					}
					out.close();
					in.close();
					
				}
			}
		}
		catch (Exception ex)
		{
			System.out.println(ex.getMessage());
		}
		finally
		{
			if (zipFile != null)
			{
				try
				{
					zipFile.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}
