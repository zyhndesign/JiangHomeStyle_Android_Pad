package com.cidesign.jianghomestyle.tools;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.cidesign.jianghomestyle.entity.ArticleEntity;
import com.cidesign.jianghomestyle.entity.FileListEntity;
import com.cidesign.jianghomestyle.util.JiangCategory;

import android.util.Log;
import android.util.Xml;


public class XmlParseTools
{
	private static final String TAG = XmlParseTools.class.getSimpleName();
	
	public static ArticleEntity readDocXML(String path)
	{
		ArticleEntity articleEntity = null;

		File file = new File(path);
		if (!file.exists())
		{
			return null;
		}

		InputStream inputStream = null;
		try
		{
			inputStream = new BufferedInputStream(new FileInputStream(file));
			List<ArticleEntity> list = XmlParseTools.parseArticleList(inputStream);
			if (list.size() > 0)
			{
				articleEntity = list.get(0);
			}
			return articleEntity;
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (inputStream != null)
				{
					inputStream.close();
				}
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * 解析获取的XML文件字符串，说明存在多少文章需要下载
	 * @param xmlStr
	 * @return
	 */
	public static List<FileListEntity> parseFileList(String xmlStr)
	{		
		List<FileListEntity> fileList = null;
		FileListEntity fileListEntity = null;
		String date = TimeTools.getCurrentDateTime();
		try
		{
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(new StringReader(xmlStr));
			int event = parser.getEventType();

			while (event != XmlPullParser.END_DOCUMENT)
			{
				switch (event)
				{
				case XmlPullParser.START_DOCUMENT:
					fileList = new ArrayList<FileListEntity>();
					break;
				case XmlPullParser.START_TAG:
					String name = parser.getName();
					if ("file".equals(name))
					{
						fileListEntity = new FileListEntity();
					}
					if (fileListEntity != null)
					{
						if ("id".equals(name))
						{
							fileListEntity.setServerID(parser.nextText());
						}
						else if ("name".equals(name))
						{
							fileListEntity.setName(parser.nextText());
							fileListEntity.setInsertDate(date);
							fileListEntity.setDownloadFlag(0);
						}
						else if ("size".equals(name))
						{
							String str = parser.nextText();
							if (str != null && !str.trim().equals(""))
							{
								fileListEntity.setSize(Integer.parseInt(str));
							}
						}
						else if ("url".equals(name))
						{
							fileListEntity.setUrl(parser.nextText());
						}
						else if ("timestamp".equals(name))
						{
							fileListEntity.setTimestamp(parser.nextText());
						}
						else if ("md5".equals(name))
						{
							fileListEntity.setMd5(parser.nextText());
						}
						else if ("op".equals(name))
						{
							fileListEntity.setOperation(parser.nextText().charAt(0));
						}
					}
					break;
				case XmlPullParser.END_TAG:
					if ("file".equals(parser.getName()))
					{
						fileList.add(fileListEntity);
						fileListEntity = null;						
					}	
					break;
				}

				event = parser.next();
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return fileList;
	}
	
	/**
	 * 解析获取的XML文件字符串，说明存在多少文章需要下载
	 * @param xmlStr
	 * @return
	 */
	public static List<FileListEntity> parseFileInputStreamList(InputStream inputStream)
	{		
		List<FileListEntity> fileList = null;
		FileListEntity fileListEntity = null;
		String date = TimeTools.getCurrentDateTime();
		try
		{
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(inputStream, "UTF-8"); 
			int event = parser.getEventType();

			while (event != XmlPullParser.END_DOCUMENT)
			{
				switch (event)
				{
				case XmlPullParser.START_DOCUMENT:
					fileList = new ArrayList<FileListEntity>();
					break;
				case XmlPullParser.START_TAG:
					String name = parser.getName();
					if ("file".equals(name))
					{
						fileListEntity = new FileListEntity();
					}
					if (fileListEntity != null)
					{
						if ("id".equals(name))
						{
							fileListEntity.setServerID(parser.nextText());
						}
						else if ("name".equals(name))
						{
							fileListEntity.setName(parser.nextText());
							fileListEntity.setInsertDate(date);
							fileListEntity.setDownloadFlag(0);
						}
						else if ("size".equals(name))
						{
							String str = parser.nextText();
							if (str != null && !str.trim().equals(""))
							{
								fileListEntity.setSize(Integer.parseInt(str));
							}
						}
						else if ("url".equals(name))
						{
							fileListEntity.setUrl(parser.nextText());
						}
						else if ("timestamp".equals(name))
						{
							fileListEntity.setTimestamp(parser.nextText());
						}
						else if ("md5".equals(name))
						{
							fileListEntity.setMd5(parser.nextText());
						}
						else if ("op".equals(name))
						{
							fileListEntity.setOperation(parser.nextText().charAt(0));
						}
					}
					break;
				case XmlPullParser.END_TAG:
					if ("file".equals(parser.getName()))
					{
						fileList.add(fileListEntity);
						fileListEntity = null;						
					}	
					break;
				}

				event = parser.next();
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return fileList;
	}
	
	/**
	 * 解析单个文件夹中的文件内容
	 * @param inputStream
	 * @return
	 */
	public static List<ArticleEntity> parseArticleList(InputStream inputStream)
	{
		List<ArticleEntity> articleList = null;
		try
		{			  
			ArticleEntity articleEntity = null;  
	        XmlPullParser parser = Xml.newPullParser();  
	        parser.setInput(inputStream, "UTF-8");  
	          
	        int event = parser.getEventType();//产生第一个事件  
			while (event != XmlPullParser.END_DOCUMENT)
			{
				switch (event)
				{
				case XmlPullParser.START_DOCUMENT:// 判断当前事件是否是文档开始事件
					articleList = new ArrayList<ArticleEntity>();// 初始化books集合
					break;
				case XmlPullParser.START_TAG:// 判断当前事件是否是标签元素开始事件
					if ("doc".equals(parser.getName()))
					{// 判断开始标签元素是否是book
						articleEntity = new ArticleEntity();
					}
					if (articleEntity != null)
					{
						if ("title".equals(parser.getName()))
						{
							articleEntity.setTitle(parser.nextText());
						}
						else if ("profile".equals(parser.getName()))
						{
							articleEntity.setProfile_path(parser.nextText());
						}
						else if ("post_date".equals(parser.getName()))
						{
							articleEntity.setPost_date(parser.nextText());
						}
						else if ("author".equals(parser.getName()))
						{
							articleEntity.setAuthor(parser.nextText());
						}
						else if ("description".equals(parser.getName()))
						{
							articleEntity.setDescription(parser.nextText());
						}
						else if ("category".equals(parser.getName()))
						{
							//风景  人文  物语  社区 
							String category = parser.nextText();
							if (category.equals("1/3"))
							{
								articleEntity.setCategory(JiangCategory.LANDSCAPE);
							}
							else if (category.equals("1/2"))
							{
								articleEntity.setCategory(JiangCategory.HUMANITY);
							}
							else if (category.equals("1/5"))
							{
								articleEntity.setCategory(JiangCategory.STORY);
							}
							else if (category.equals("1/4"))
							{
								articleEntity.setCategory(JiangCategory.COMMUNITY);
							}
						}
						else if ("main_file".equals(parser.getName()))
						{
							articleEntity.setMain_file_path(parser.nextText());
						}
						else if ("background".equals(parser.getName()))
						{
							articleEntity.setMax_bg_img(parser.nextText());
						}
						else if ("headline".equals(parser.getName()))
						{
							String temp = parser.nextText();
							if (temp.equals("true"))
							{
								articleEntity.setIsHeadline(1);
							}
							else
							{
								articleEntity.setIsHeadline(0);
							}							
						}
					}
					break;
				case XmlPullParser.END_TAG:// 判断当前事件是否是标签元素结束事件
					if ("doc".equals(parser.getName()))
					{// 判断结束标签元素是否是book
						articleList.add(articleEntity);// 将book添加到books集合
						articleEntity = null;
					}
					break;
				}
				event = parser.next();// 进入下一个元素并触发相应事件
			}// end while
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		catch (XmlPullParserException e)
		{
			e.printStackTrace();
		}
		return articleList;
	}
	
}
