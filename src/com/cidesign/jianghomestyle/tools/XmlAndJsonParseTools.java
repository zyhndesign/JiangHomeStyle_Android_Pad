package com.cidesign.jianghomestyle.tools;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import com.cidesign.jianghomestyle.entity.ContentEntity;
import com.cidesign.jianghomestyle.util.JiangCategory;

import android.util.Log;
import android.util.Xml;

/**
 * 
* @Title: XmlParseTools.java 
* @Package com.cidesign.jianghomestyle.tools 
* @Description: Parser data form server,example of xml data or json data 
* @author liling  
* @date 2013年8月14日 上午11:38:01 
* @version V2.0
 */
public class XmlAndJsonParseTools
{
	private static final String TAG = XmlAndJsonParseTools.class.getSimpleName();
	
	/**
	 * get the XML file from server through network and then parse article abstract 
	 * @param xmlStr
	 * @return
	 */
	public static List<ContentEntity> parseFileList(String xmlStr)
	{		
		List<ContentEntity> contentList = null;
		ContentEntity contentEntity = null;
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
					contentList = new ArrayList<ContentEntity>();
					break;
				case XmlPullParser.START_TAG:
					String name = parser.getName();
					if ("file".equals(name))
					{
						contentEntity = new ContentEntity();
					}
					if (contentEntity != null)
					{
						if ("id".equals(name))
						{
							contentEntity.setServerID(parser.nextText());
						}
						else if ("name".equals(name))
						{
							contentEntity.setName(parser.nextText());
							contentEntity.setInsertDate(date);
							contentEntity.setDownloadFlag(0);
						}
						else if ("size".equals(name))
						{
							String str = parser.nextText();
							if (str != null && !str.trim().equals(""))
							{
								contentEntity.setSize(Integer.parseInt(str));
							}
						}
						else if ("url".equals(name))
						{
							contentEntity.setUrl(parser.nextText());
						}
						else if ("timestamp".equals(name))
						{
							contentEntity.setTimestamp(parser.nextText());
						}
						else if ("md5".equals(name))
						{
							contentEntity.setMd5(parser.nextText());
						}
						else if ("op".equals(name))
						{
							contentEntity.setOperation(parser.nextText().charAt(0));
						}
						if ("title".equals(parser.getName()))
						{
							contentEntity.setTitle(parser.nextText());
						}
						else if ("profile".equals(parser.getName()))
						{
							contentEntity.setProfile_path(parser.nextText());
						}
						else if ("post_date".equals(parser.getName()))
						{
							contentEntity.setPost_date(parser.nextText());
						}
						else if ("author".equals(parser.getName()))
						{
							contentEntity.setAuthor(parser.nextText());
						}
						else if ("description".equals(parser.getName()))
						{
							contentEntity.setDescription(parser.nextText());
						}
						else if ("category".equals(parser.getName()))
						{
							//风景  人文  物语  社区 
							String category = parser.nextText();
							if (category.equals("1/3"))
							{
								contentEntity.setCategory(JiangCategory.LANDSCAPE);
							}
							else if (category.equals("1/2"))
							{
								contentEntity.setCategory(JiangCategory.HUMANITY);
							}
							else if (category.equals("1/5"))
							{
								contentEntity.setCategory(JiangCategory.STORY);
							}
							else if (category.equals("1/4"))
							{
								contentEntity.setCategory(JiangCategory.COMMUNITY);
							}
						}
						else if ("main_file".equals(parser.getName()))
						{
							contentEntity.setMain_file_path(parser.nextText());
						}
						else if ("background".equals(parser.getName()))
						{
							contentEntity.setMax_bg_img(parser.nextText());
						}
						else if ("headline".equals(parser.getName()))
						{
							String temp = parser.nextText();
							if (temp.equals("true"))
							{
								contentEntity.setIsHeadline(1);
							}
							else
							{
								contentEntity.setIsHeadline(0);
							}							
						}
					}
					break;
				case XmlPullParser.END_TAG:
					if ("file".equals(parser.getName()))
					{
						contentList.add(contentEntity);
						contentEntity = null;						
					}	
					break;
				}

				event = parser.next();
			}

		}
		catch (Exception e)
		{
			Log.d(TAG, "parser runtime exception.....");
			e.printStackTrace();			
		}
		return contentList;
	}
}
