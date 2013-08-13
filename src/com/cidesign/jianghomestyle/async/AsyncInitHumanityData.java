package com.cidesign.jianghomestyle.async;

import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;

import com.cidesign.jianghomestyle.adapter.HumanityViewpagerAdapter;
import com.cidesign.jianghomestyle.db.DatabaseHelper;
import com.cidesign.jianghomestyle.entity.ArticleEntity;
import com.cidesign.jianghomestyle.viewlogic.FloatViewLogic;
import com.cidesign.jianghomestyle.viewlogic.LoadingDataFromDB;
import com.cidesign.jianghomestylerelease.R;

public class AsyncInitHumanityData extends AsyncTask<Void, Void, List<ArticleEntity>>
{
	private Activity activity;
	private LoadingDataFromDB loadingDataFromDB = null;
	private DatabaseHelper dbHelper;

	// 人文
	private ViewPager humanityViewPager;
	private HumanityViewpagerAdapter humanityViewpagerAdapter;

	private int screenWidth;
	private FloatViewLogic floatLogic;
	
	public AsyncInitHumanityData(Activity activity, DatabaseHelper dbHelper,int screenWidth, FloatViewLogic floatLogic)
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
		// 人文
		humanityViewPager = (ViewPager) activity.findViewById(R.id.humanityViewPager);
	}

	@Override
	protected List<ArticleEntity> doInBackground(Void... arg0)
	{
		return loadingDataFromDB.loadHumanityArticle(dbHelper.getArticleListDataDao());
	}

	@Override
	protected void onPostExecute(List<ArticleEntity> list)
	{
		if (list != null && list.size() > 0)
		{
			humanityViewpagerAdapter = new HumanityViewpagerAdapter();
			humanityViewpagerAdapter.setActivity(activity);
			humanityViewpagerAdapter.setScreenWidth(screenWidth);
			humanityViewpagerAdapter.getList().addAll(list);
			humanityViewpagerAdapter.setFloatLogic(floatLogic);
			humanityViewPager.setAdapter(humanityViewpagerAdapter);
		}
	}
}
