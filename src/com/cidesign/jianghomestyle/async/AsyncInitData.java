package com.cidesign.jianghomestyle.async;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.cidesign.jianghomestyle.R;
import com.cidesign.jianghomestyle.adapter.CommunityViewpagerAdapter;
import com.cidesign.jianghomestyle.adapter.HumanityViewpagerAdapter;
import com.cidesign.jianghomestyle.adapter.LandscapeViewpagerAdapter;
import com.cidesign.jianghomestyle.adapter.LayoutCaculateAdapter;
import com.cidesign.jianghomestyle.adapter.StoryViewpagerAdapter;
import com.cidesign.jianghomestyle.db.DatabaseHelper;
import com.cidesign.jianghomestyle.entity.ContentEntity;
import com.cidesign.jianghomestyle.tools.LoadingImageTools;
import com.cidesign.jianghomestyle.tools.TimeTools;
import com.cidesign.jianghomestyle.util.JiangCategory;
import com.cidesign.jianghomestyle.util.StorageUtils;
import com.cidesign.jianghomestyle.viewlogic.CategoryDataLoadingLogic;
import com.cidesign.jianghomestyle.viewlogic.FloatViewLogic;
import com.cidesign.jianghomestyle.viewlogic.LoadingDataFromDB;
import com.cidesign.jianghomestyle.widget.CommunityRelativeLayout;
import com.cidesign.jianghomestyle.widget.HumanityRelativeLayout;
import com.cidesign.jianghomestyle.widget.LandscapeRelativeLayout;
import com.cidesign.jianghomestyle.widget.StoryRelativeLayout;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

/**
 * 
* @Title: AsyncInitData.java 
* @Package com.cidesign.jianghomestyle.async 
* @Description: asynchronous get data and reveal on the page 
* @author liling  
* @date 2013年8月14日 下午1:50:39 
* @version V2.0
 */
public class AsyncInitData extends AsyncTask<Void, Void, Object[]>
{
	private Activity activity;
	private int screenWidth;
	private LoadingDataFromDB loadingDataFromDB = null;
	private DatabaseHelper dbHelper;
	private LayoutInflater inflater = null;

	private TextView home_title;
	private TextView homeArticleTime;
	private LinearLayout homeLinearLayout;
	private ImageView homeBgImg;
	private LinearLayout headlineLayout;
	private VideoView mVideoView;

	// 风景内容面板控件
	private ImageButton landscapePreClick;
	private ImageButton landscapeNextClick;
	private ViewPager landscapeViewPager;
	private LandscapeViewpagerAdapter landscapeViewpagerAdapter;

	// 人文
	private ImageButton humanityPreClick;
	private ImageButton humanityNextClick;
	private ViewPager humanityViewPager;
	private HumanityViewpagerAdapter humanityViewpagerAdapter;

	// 物语内容面板控件
	private ImageButton storyPreClick;
	private ImageButton storyNextClick;
	private ViewPager storyViewPager;
	private StoryViewpagerAdapter storyViewpagerAdapter;

	// 小记内容面板控件
	private ImageButton communityPreClick;
	private ImageButton communityNextClick;
	private ViewPager communityViewPager;
	private CommunityViewpagerAdapter communityViewpagerAdapter;

	private ProgressBar progressBar = null;

	private FloatViewLogic floatLogic;

	public AsyncInitData(Activity activity, DatabaseHelper dbHelper, LinearLayout headlineLayout, LayoutInflater inflater,
			int screenWidth, FloatViewLogic floatLogic)
	{
		this.activity = activity;
		this.dbHelper = dbHelper;
		this.headlineLayout = headlineLayout;
		this.inflater = inflater;
		this.screenWidth = screenWidth;
		this.floatLogic = floatLogic;
	}

	@Override
	protected void onPreExecute()
	{
		progressBar = (ProgressBar) activity.findViewById(R.id.loadingProgressBar);

		loadingDataFromDB = new LoadingDataFromDB();
		homeLinearLayout = (LinearLayout) activity.findViewById(R.id.homeLinearLayout);
		home_title = (TextView) activity.findViewById(R.id.home_title);
		homeArticleTime = (TextView) activity.findViewById(R.id.homeArticleTime);
		homeBgImg = (ImageView) activity.findViewById(R.id.homeBgImg);
		mVideoView = (VideoView) activity.findViewById(R.id.videoView);

		// 风景
		landscapePreClick = (ImageButton) activity.findViewById(R.id.landscapePreClick);
		landscapeNextClick = (ImageButton) activity.findViewById(R.id.landscapeNextClick);
		landscapeViewPager = (ViewPager) activity.findViewById(R.id.landscapeViewPager);
		
		LandscapeRelativeLayout landscapeRelativeLayout = (LandscapeRelativeLayout)activity.findViewById(R.id.landscapeRelativeLayout);
		landscapeRelativeLayout.setLandscapePreClick(landscapePreClick);
		landscapeRelativeLayout.setLandscapeNextClick(landscapeNextClick);
		
		// 人文
		humanityPreClick = (ImageButton) activity.findViewById(R.id.humanityPreClick);
		humanityNextClick = (ImageButton) activity.findViewById(R.id.humanityNextClick);
		humanityViewPager = (ViewPager) activity.findViewById(R.id.humanityViewPager);
		HumanityRelativeLayout humanityRelativeLayout = (HumanityRelativeLayout)activity.findViewById(R.id.humanityRelativeLayout);
		humanityRelativeLayout.setHumanityPreClick(humanityPreClick);
		humanityRelativeLayout.setHumanityNextClick(humanityNextClick);
		
		// 物语
		storyPreClick = (ImageButton) activity.findViewById(R.id.storyPreClick);
		storyNextClick = (ImageButton) activity.findViewById(R.id.storyNextClick);
		storyViewPager = (ViewPager) activity.findViewById(R.id.storyViewPager);
		StoryRelativeLayout storyRelativeLayout = (StoryRelativeLayout)activity.findViewById(R.id.storyRelativeLayout);
		storyRelativeLayout.setStoryPreClick(storyPreClick);
		storyRelativeLayout.setStoryNextClick(storyNextClick);
		
		// 社区
		communityPreClick = (ImageButton) activity.findViewById(R.id.communityPreClick);
		communityNextClick = (ImageButton) activity.findViewById(R.id.communityNextClick);
		communityViewPager = (ViewPager) activity.findViewById(R.id.communityViewPager);
		CommunityRelativeLayout communityRelativeLayout = (CommunityRelativeLayout)activity.findViewById(R.id.communityRelativeLayout);
		communityRelativeLayout.setCommunityPreClick(communityPreClick);
		communityRelativeLayout.setCommunityNextClick(communityNextClick);
	}

	@Override
	protected Object[] doInBackground(Void... arg0)
	{
		Object[] objectArray = new Object[5];
		// 读取数据库加载数据
		List<ContentEntity> topFourList = loadingDataFromDB.loadTopFourArticle(dbHelper.getContentDataDao());
		objectArray[0] = topFourList;

		// 初始化风景
		List<ContentEntity> landscapeList = new ArrayList<ContentEntity>();
		// 初始化人文
		List<ContentEntity> humanityList = new ArrayList<ContentEntity>();	
		// 初始化物语
		List<ContentEntity> storyList = new ArrayList<ContentEntity>();
		// 初始化社区
		List<ContentEntity> communityList = new ArrayList<ContentEntity>();
		
		List<ContentEntity> allArticleList = loadingDataFromDB.loadAllArticle(dbHelper.getContentDataDao());
		
		for (ContentEntity cEntity : allArticleList)
		{
			if (cEntity.getCategory() == JiangCategory.LANDSCAPE)
			{
				landscapeList.add(cEntity);
			}
			else if (cEntity.getCategory() == JiangCategory.HUMANITY)
			{
				humanityList.add(cEntity);
			}
			else if (cEntity.getCategory() == JiangCategory.COMMUNITY)
			{
				storyList.add(cEntity);
			}
			else if (cEntity.getCategory() == JiangCategory.STORY)
			{
				communityList.add(cEntity);
			}
		}
		
		objectArray[1] = landscapeList;

		objectArray[2] = humanityList;

		objectArray[3] = storyList;

		objectArray[4] = communityList;
		
		return objectArray;
	}

	@Override
	protected void onPostExecute(Object[] result)
	{
		@SuppressWarnings("unchecked")
		List<ContentEntity> topFourList = (List<ContentEntity>) result[0];
		@SuppressWarnings("unchecked")
		List<ContentEntity> landscapeList = (List<ContentEntity>) result[1];
		@SuppressWarnings("unchecked")
		List<ContentEntity> humanityList = (List<ContentEntity>) result[2];
		@SuppressWarnings("unchecked")
		List<ContentEntity> storyList = (List<ContentEntity>) result[3];
		@SuppressWarnings("unchecked")
		List<ContentEntity> communityList = (List<ContentEntity>) result[4];

		if (topFourList.size() >= 1)
		{
			ContentEntity aEntity = topFourList.get(0);
			LoadingImageTools loadingImg = new LoadingImageTools();
			
			if (aEntity.getMax_bg_img() == null || aEntity.getMax_bg_img().equals(""))
			{
				homeBgImg.setVisibility(View.VISIBLE);
				loadingImg.loadingNativeImage(activity, homeBgImg, "bg1.jpg");
			}
			else if (aEntity.getMax_bg_img().endsWith(".mp4"))
			{
				if (mVideoView != null)
				{
					final String videoPath = "file://" + StorageUtils.FILE_ROOT + aEntity.getServerID() + "/"
							+ aEntity.getMax_bg_img();
					File file = new File(videoPath);
					if (file.exists())
					{
						homeBgImg.setVisibility(View.INVISIBLE);
						mVideoView.setVisibility(View.VISIBLE);
						mVideoView.setVideoPath(videoPath);
						mVideoView.start();
						mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
						{

							@Override
							public void onPrepared(MediaPlayer mp)
							{
								mp.start();
								mp.setLooping(true);

							}
						});

						mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
						{

							@Override
							public void onCompletion(MediaPlayer mp)
							{
								mVideoView.setVideoPath(videoPath);
								mVideoView.start();

							}
						});

						mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener()
						{

							@Override
							public boolean onError(MediaPlayer mp, int what, int extra)
							{
								Toast.makeText(activity, "无法播放背景视频文件!", Toast.LENGTH_LONG).show();
								return true;
							}
						});
					}					
				}
			}
			else
			{
				homeBgImg.setVisibility(View.VISIBLE);
				String filePath = StorageUtils.FILE_ROOT + aEntity.getServerID() + "/" + aEntity.getMax_bg_img();
				File file = new File(filePath);
				if(file.exists())
				{
					loadingImg.loadingImage(homeBgImg, filePath);
				}
			}
			home_title.setText(aEntity.getTitle());
			homeArticleTime.setText(TimeTools.getTimeByTimestap(Long.parseLong(aEntity.getPost_date())));
			homeLinearLayout.setTag(aEntity);
			if (topFourList.size() >= 2)
			{
				CategoryDataLoadingLogic data = new CategoryDataLoadingLogic();
				data.loadHeadLineData(topFourList, headlineLayout, screenWidth, inflater); // 初始化头条
			}
		}

		if (landscapeList != null && landscapeList.size() > 0)
		{
			landscapeViewPager.setLayoutParams(LayoutCaculateAdapter.getViewPagerRelativeLayout(screenWidth, 5));
			landscapeViewpagerAdapter = new LandscapeViewpagerAdapter();
			landscapeViewpagerAdapter.setActivity(activity);
			landscapeViewpagerAdapter.setScreenWidth(screenWidth);
			landscapeViewpagerAdapter.getList().addAll(landscapeList);
			landscapeViewpagerAdapter.setFloatLogic(floatLogic);
			landscapeViewPager.setAdapter(landscapeViewpagerAdapter);
		}

		if (humanityList != null && humanityList.size() > 0)
		{
			humanityViewPager.setLayoutParams(LayoutCaculateAdapter.getViewPagerRelativeLayout(screenWidth, 6));
			humanityViewpagerAdapter = new HumanityViewpagerAdapter();
			humanityViewpagerAdapter.setActivity(activity);
			humanityViewpagerAdapter.setScreenWidth(screenWidth);
			humanityViewpagerAdapter.getList().addAll(humanityList);
			humanityViewpagerAdapter.setFloatLogic(floatLogic);
			humanityViewPager.setAdapter(humanityViewpagerAdapter);
		}

		if (storyList != null && storyList.size() > 0)
		{
			storyViewPager.setLayoutParams(LayoutCaculateAdapter.getViewPagerRelativeLayout(screenWidth, 5));
			storyViewpagerAdapter = new StoryViewpagerAdapter();
			storyViewpagerAdapter.setActivity(activity);
			storyViewpagerAdapter.setScreenWidth(screenWidth);
			storyViewpagerAdapter.getList().addAll(storyList);
			storyViewpagerAdapter.setFloatLogic(floatLogic);
			storyViewPager.setAdapter(storyViewpagerAdapter);
		}

		if (communityList != null && communityList.size() > 0)
		{
			communityViewPager.setLayoutParams(LayoutCaculateAdapter.getViewPagerRelativeLayout(screenWidth, 6));
			communityViewpagerAdapter = new CommunityViewpagerAdapter();
			communityViewpagerAdapter.setActivity(activity);
			communityViewpagerAdapter.setScreenWidth(screenWidth);
			communityViewpagerAdapter.getList().addAll(communityList);
			communityViewpagerAdapter.setFloatLogic(floatLogic);
			communityViewPager.setAdapter(communityViewpagerAdapter);
		}
		progressBar.setVisibility(View.INVISIBLE);

	}
}
