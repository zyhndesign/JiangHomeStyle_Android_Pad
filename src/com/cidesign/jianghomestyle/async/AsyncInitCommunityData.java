package com.cidesign.jianghomestyle.async;

import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;

import com.cidesign.jianghomestyle.R;
import com.cidesign.jianghomestyle.adapter.CommunityViewpagerAdapter;
import com.cidesign.jianghomestyle.db.DatabaseHelper;
import com.cidesign.jianghomestyle.entity.ArticleEntity;
import com.cidesign.jianghomestyle.viewlogic.FloatViewLogic;
import com.cidesign.jianghomestyle.viewlogic.LoadingDataFromDB;

public class AsyncInitCommunityData extends AsyncTask<Void, Void, List<ArticleEntity>>
{
	private Activity activity;
	private LoadingDataFromDB loadingDataFromDB = null;
	private DatabaseHelper dbHelper;

	// 小记内容面板控件
	private ViewPager communityViewPager;
	private CommunityViewpagerAdapter communityViewpagerAdapter;
	private int screenWidth;
	private FloatViewLogic floatLogic;
	
	public AsyncInitCommunityData(Activity activity, DatabaseHelper dbHelper,int screenWidth,FloatViewLogic floatLogic)
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
		communityViewPager = (ViewPager) activity.findViewById(R.id.communityViewPager);
	}

	@Override
	protected List<ArticleEntity> doInBackground(Void... arg0)
	{
		return loadingDataFromDB.loadCommunityArticle(dbHelper.getArticleListDataDao());
	}
	
	@Override
	protected void onPostExecute(List<ArticleEntity> list)
	{
		if (list != null && list.size() > 0)
		{
			communityViewpagerAdapter = new CommunityViewpagerAdapter();
			communityViewpagerAdapter.setActivity(activity);
			communityViewpagerAdapter.setScreenWidth(screenWidth);
			communityViewpagerAdapter.getList().addAll(list);
			communityViewpagerAdapter.setFloatLogic(floatLogic);
			communityViewPager.setAdapter(communityViewpagerAdapter);
		}
	}
}
