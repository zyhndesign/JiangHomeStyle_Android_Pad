package com.cidesign.jianghomestyle.entity;

//@DatabaseTable(tableName = "music")
public class MusicEntity
{
	//@DatabaseField(generatedId = true)
	int id;
	//@DatabaseField(index = true)
	String music_title;
	//@DatabaseField
	String music_author;
	//@DatabaseField
	String music_path;
	
	//@DatabaseField
	//String download_path;

	public MusicEntity()
	{
		// needed by ormlite
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getMusic_title()
	{
		return music_title;
	}

	public void setMusic_title(String music_title)
	{
		this.music_title = music_title;
	}

	public String getMusic_author()
	{
		return music_author;
	}

	public void setMusic_author(String music_author)
	{
		this.music_author = music_author;
	}

	public String getMusic_path()
	{
		return music_path;
	}

	public void setMusic_path(String music_path)
	{
		this.music_path = music_path;
	}
}
