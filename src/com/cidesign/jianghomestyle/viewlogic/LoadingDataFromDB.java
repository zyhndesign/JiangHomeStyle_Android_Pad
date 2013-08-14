package com.cidesign.jianghomestyle.viewlogic;

import java.sql.SQLException;
import java.util.List;

import com.cidesign.jianghomestyle.entity.ContentEntity;
import com.cidesign.jianghomestyle.util.JiangCategory;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

public class LoadingDataFromDB
{
	private static final String TAG = LoadingDataFromDB.class.getSimpleName();
		
	/**
	 * 获取头条数据
	 * @param articleDao
	 * @return
	 */
	public List<ContentEntity> loadTopFourArticle(RuntimeExceptionDao<ContentEntity, Integer> articleDao)
	{
		List<ContentEntity> artileList = null;
		
		try
		{
			QueryBuilder<ContentEntity, Integer> queryBuilder = articleDao.queryBuilder();
			queryBuilder.where().eq("isHeadline", 1);
			queryBuilder.orderBy("post_date", false);
			queryBuilder.limit(4L);
			PreparedQuery<ContentEntity> prepqredQuery;
			prepqredQuery = queryBuilder.prepare();
			artileList = articleDao.query(prepqredQuery);
			return artileList;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取所有数据
	 * @param articleDao
	 * @return
	 */
	public List<ContentEntity> loadAllArticle(RuntimeExceptionDao<ContentEntity, Integer> articleDao)
	{
		List<ContentEntity> artileList = null;
		QueryBuilder<ContentEntity, Integer> queryBuilder = articleDao.queryBuilder();
				
		try
		{
			queryBuilder.orderBy("post_date", false);
			PreparedQuery<ContentEntity> prepqredQuery;
			prepqredQuery = queryBuilder.prepare();
			artileList = articleDao.query(prepqredQuery);
			return artileList;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
}
