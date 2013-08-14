package com.cidesign.jianghomestyle.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
/*
* @Title: ContentEntity.java 
* @Package com.cidesign.jianghomestyle.entity 
* @Description: article relative content entity class and insert or update into database 
* @author liling  
* @date 2013年8月14日 上午10:44:35 
* @version V2.0
 */
@DatabaseTable(tableName = "contentlist")
public class ContentEntity
{
	@DatabaseField(generatedId = true)
	int id;
	
	@DatabaseField
	String serverID;
	
	@DatabaseField
	String name; //文章名称
	
	@DatabaseField
	int size; //大小（字节）
	
	@DatabaseField
	String url; // 该文章下载Url
	
	@DatabaseField
	String timestamp;
	
	@DatabaseField
	String md5; //文件压缩包的md5

	@DatabaseField
	int downloadFlag; //0：未下载   1：已下载
	
	@DatabaseField
	String insertDate;
	
	@DatabaseField
	private String title; //标题
	
	@DatabaseField
	private String profile_path; //展示图片位置
	
	@DatabaseField
	private String post_date; //发布时间
	
	@DatabaseField
	private String author; //作者
	
	@DatabaseField
	private String description; //描述
	
	@DatabaseField
	private int category; //分类 0:印象， 1：物语， 2：小记，3：
	
	@DatabaseField
	private String main_file_path; //保存在SD卡路径

	@DatabaseField
	private String max_bg_img; //栏目背景大图

	@DatabaseField
	private int isHeadline; //0:否 1：为头条
	
	char operation;

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getServerID()
	{
		return serverID;
	}

	public void setServerID(String serverID)
	{
		this.serverID = serverID;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getSize()
	{
		return size;
	}

	public void setSize(int size)
	{
		this.size = size;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public String getTimestamp()
	{
		return timestamp;
	}

	public void setTimestamp(String timestamp)
	{
		this.timestamp = timestamp;
	}

	public String getMd5()
	{
		return md5;
	}

	public void setMd5(String md5)
	{
		this.md5 = md5;
	}

	public int getDownloadFlag()
	{
		return downloadFlag;
	}

	public void setDownloadFlag(int downloadFlag)
	{
		this.downloadFlag = downloadFlag;
	}

	public String getInsertDate()
	{
		return insertDate;
	}

	public void setInsertDate(String insertDate)
	{
		this.insertDate = insertDate;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getProfile_path()
	{
		return profile_path;
	}

	public void setProfile_path(String profile_path)
	{
		this.profile_path = profile_path;
	}

	public String getPost_date()
	{
		return post_date;
	}

	public void setPost_date(String post_date)
	{
		this.post_date = post_date;
	}

	public String getAuthor()
	{
		return author;
	}

	public void setAuthor(String author)
	{
		this.author = author;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public int getCategory()
	{
		return category;
	}

	public void setCategory(int category)
	{
		this.category = category;
	}

	public String getMain_file_path()
	{
		return main_file_path;
	}

	public void setMain_file_path(String main_file_path)
	{
		this.main_file_path = main_file_path;
	}

	public String getMax_bg_img()
	{
		return max_bg_img;
	}

	public void setMax_bg_img(String max_bg_img)
	{
		this.max_bg_img = max_bg_img;
	}

	public int getIsHeadline()
	{
		return isHeadline;
	}

	public void setIsHeadline(int isHeadline)
	{
		this.isHeadline = isHeadline;
	}

	public char getOperation()
	{
		return operation;
	}

	public void setOperation(char operation)
	{
		this.operation = operation;
	}	
}
