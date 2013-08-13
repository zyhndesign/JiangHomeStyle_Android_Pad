package com.cidesign.jianghomestyle.async;

import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;

import com.cidesign.jianghomestyle.adapter.LandscapeViewpagerAdapter;
import com.cidesign.jianghomestyle.db.DatabaseHelper;
import com.cidesign.jianghomestyle.entity.ArticleEntity;
import com.cidesign.jianghomestyle.viewlogic.FloatViewLogic;
import com.cidesign.jianghomestyle.viewlogic.LoadingDataFromDB;
import com.cidesign.jianghomestylerelease.R;

public class AsyncInitLandscapeData extends AsyncTask<Void, Void, List<ArticleEntity>>
{
	private Activity activity;
	private LoadingDataFromDB loadingDataFromDB = null;
	private DatabaseHelper dbHelper;
	private int screenWidth;
	private FloatViewLogic floatLogic;
	// 风景内容面板控件
	private ViewPager landscapeViewPager;
	private LandscapeViewpagerAdapter landscapeViewpagerAdapter;

	public AsyncInitLandscapeData(Activity activity, DatabaseHelper dbHelper,int screenWidth,FloatViewLogic floatLogic)
	{
		this.activity = activity;
		this.dbHelper = dbHelper;
		this.screenWidth = screenWidth;
		this.floatLogic = floatLogic;
	}

	@Override
	protected void onPreExecute()
	{
		loadingDataFromDB = new LoadingDataFromDB();
		// 风景
		landscapeViewPager = (ViewPager) activity.findViewById(R.id.landscapeViewPager);
	}

	@Override
	protected List<ArticleEntity> doInBackground(Void... arg0)
	{
		return loadingDataFromDB.loadLandscapeArticle(dbHelper.getArticleListDataDao());
	}

	@Override
	protected void onPostExecute(List<ArticleEntity> list)
	{
		if (list != null && list.size() > 0)
		{
			landscapeViewpagerAdapter = new LandscapeViewpagerAdapter();
			landscapeViewpagerAdapter.setActivity(activity);
			landscapeViewpagerAdapter.setScreenWidth(screenWidth);
			landscapeViewpagerAdapter.setFloatLogic(floatLogic);
			landscapeViewpagerAdapter.getList().clear();
			landscapeViewpagerAdapter.getList().addAll(list);
			landscapeViewPager.setAdapter(landscapeViewpagerAdapter);
		}
	}
}
