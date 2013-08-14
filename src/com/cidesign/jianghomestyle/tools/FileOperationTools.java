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
	 * �ж�SD���Ƿ���� true������ false:������
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
	 *            �ļ���·��
	 */
	public static void isExist(String path)
	{
		File file = new File(path);
		// �ж��ļ����Ƿ����,����������򴴽��ļ���
		if (!file.exists())
		{
			file.mkdir();
		}
	}

	/**
	 * ��ȡSD����ʣ������
	 * 
	 * @return
	 */
	public long getSDFreeSize()
	{
		// ȡ��SD���ļ�·��
		File path = Environment.getExternalStorageDirectory();
		StatFs sf = new StatFs(path.getPath());
		// ��ȡ�������ݿ�Ĵ�С(Byte)
		long blockSize = sf.getBlockSize();
		// ���е����ݿ������
		long freeBlocks = sf.getAvailableBlocks();
		// ����SD�����д�С
		// return freeBlocks * blockSize; //��λByte
		// return (freeBlocks * blockSize)/1024; //��λKB
		return (freeBlocks * blockSize) / 1024 / 1024; // ��λMB
	}

	/**
	 * SD��������
	 * 
	 * @return
	 */
	public long getSDAllSize()
	{
		// ȡ��SD���ļ�·��
		File path = Environment.getExternalStorageDirectory();
		StatFs sf = new StatFs(path.getPath());
		// ��ȡ�������ݿ�Ĵ�С(Byte)
		long blockSize = sf.getBlockSize();
		// ��ȡ�������ݿ���
		long allBlocks = sf.getBlockCount();
		// ����SD����С
		// return allBlocks * blockSize; //��λByte
		// return (allBlocks * blockSize)/1024; //��λKB
		return (allBlocks * blockSize) / 1024 / 1024; // ��λMB
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
