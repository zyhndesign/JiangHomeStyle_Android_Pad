package com.cidesign.jianghomestyle.async;

import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.widget.ImageButton;

import com.cidesign.jianghomestyle.adapter.StoryViewpagerAdapter;
import com.cidesign.jianghomestyle.db.DatabaseHelper;
import com.cidesign.jianghomestyle.entity.ArticleEntity;
import com.cidesign.jianghomestyle.viewlogic.CategoryDataLoadingLogic;
import com.cidesign.jianghomestyle.viewlogic.FloatViewLogic;
import com.cidesign.jianghomestyle.viewlogic.HScrollViewTouchLogic;
import com.cidesign.jianghomestyle.viewlogic.LoadingDataFromDB;
import com.cidesign.jianghomestylerelease.R;

public class AsyncInitStoryData extends AsyncTask<Void, Void, List<ArticleEntity>>
{
	private Activity activity;
	private LoadingDataFromDB loadingDataFromDB = null;
	private DatabaseHelper dbHelper;

	// 物语内容面板控件
	private ViewPager storyViewPager;
	private StoryViewpagerAdapter storyViewpagerAdapter;
	private int screenWidth;
	private FloatViewLogic floatLogic;
	
	public AsyncInitStoryData(Activity activity, DatabaseHelper dbHelper,int screenWidth,FloatViewLogic floatLogic)
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
		storyViewPager = (ViewPager) activity.findViewById(R.id.storyViewPager);
	}

	@Override
	protected List<ArticleEntity> doInBackground(Void... arg0)
	{
		return loadingDataFromDB.loadStoryArticle(dbHelper.getArticleListDataDao());
	}
	
	@Override
	protected void onPostExecute(List<ArticleEntity> list)
	{
		if (list != null && list.size() > 0)
		{
			storyViewpagerAdapter = new StoryViewpagerAdapter();
			storyViewpagerAdapter.setActivity(activity);
			storyViewpagerAdapter.setScreenWidth(screenWidth);
			storyViewpagerAdapter.getList().addAll(list);
			storyViewpagerAdapter.setFloatLogic(floatLogic);
			storyViewPager.setAdapter(storyViewpagerAdapter);
		}
	}
}
