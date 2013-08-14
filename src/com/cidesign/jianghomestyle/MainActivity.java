package com.cidesign.jianghomestyle;

import com.androidquery.AQuery;
import com.cidesign.jianghomestyle.async.AsyncInitData;
import com.cidesign.jianghomestyle.db.DatabaseHelper;
import com.cidesign.jianghomestyle.http.ArticalOperation;
import com.cidesign.jianghomestyle.util.ScreenAdapterTools;
import com.cidesign.jianghomestyle.viewlogic.FloatViewLogic;
import com.cidesign.jianghomestyle.viewlogic.MusicViewLogic;
import com.cidesign.jianghomestyle.widget.CustomScrollView;
import com.google.analytics.tracking.android.EasyTracker;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.content.res.Configuration;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.VideoView;
/**
* @Title: MainActivity.java 
* @Package com.cidesign.jianghomestyle 
* @Description: app main page and reveal all model data on the page 
* @author liling  
* @date 2013年8月14日 下午2:47:57 
* @version V2.0
 */
public class MainActivity extends JiangActivity
{
	private static final String TAG = MainActivity.class.getSimpleName();

	private AQuery aq;
	private DatabaseHelper dbHelper;

	private WindowManager wm = null;

	private View view;
	private CustomScrollView scrollView;

	private LinearLayout headlineLayout;

	private LayoutInflater inflater = null;

	private VideoView mVideoView;
	private ImageView homeBgImg;
	private ImageView landscapeBgImg;
	private ImageView storyBgImg;
	private ImageView humanityBgImg;
	private ImageView communityBgImg;

	private Configuration config = null;

	private ArticalOperation aOper = null;

	private RelativeLayout landscapeAnimPanel;
	private RelativeLayout humanityAnimPanel;
	private RelativeLayout storyAnimPanel;
	private RelativeLayout communityAnimPanel;

	private FloatViewLogic floatLogic = null;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		EasyTracker.getInstance().setContext(this);

		aq = new AQuery(this);
		dbHelper = new DatabaseHelper(this);

		inflater = LayoutInflater.from(this);

		view = inflater.inflate(R.layout.top_layer, null);
		ImageView logoBtnClick = (ImageView) view.findViewById(R.id.logoBtnClick);
		logoBtnClick.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				scrollView.smoothScrollTo(0, 0);
			}
		});

		floatLogic = new FloatViewLogic(MainActivity.this, view, screenWidth, screenHeight);
		// 创建顶部浮动窗口
		wm = floatLogic.createView();

		initComponent();

		config = getResources().getConfiguration();
		CalculateByScreenOrantation(config);

		new AsyncInitData(MainActivity.this, dbHelper, headlineLayout, inflater, screenWidth,floatLogic).execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onStart()
	{
		super.onStart();
		EasyTracker.getInstance().activityStart(this);
		// 检测文章是否有更新，存在更新则进行下载更新
		aOper = new ArticalOperation(this, aq, dbHelper);
		aOper.initMusicInfo(view);
	}

	@Override
	public void onResume()
	{
		super.onResume();
		if (wm == null)
		{
			wm = floatLogic.createView();
		}
	}

	@Override
	public void onPause()
	{
		super.onPause();
	}

	@Override
	public void onStop()
	{
		super.onStop();
		EasyTracker.getInstance().activityStop(this);
		if (wm != null)
		{
			wm.removeView(view);
			wm = null;
		}

		MusicViewLogic musicViewLogic = aOper.getMusicLogic();

		if (musicViewLogic != null)
		{
			MediaPlayer mPlayer = musicViewLogic.getMediaPlayer();
			if (musicViewLogic.getMusicSeekbar() != null)
			{
				musicViewLogic.getMusicSeekbar().setProgress(0);
			}
			if (mPlayer != null)
			{
				mPlayer.stop();
				mPlayer.release();
				mPlayer = null;
			}
		}
	}

	private void initComponent()
	{
		scrollView = (CustomScrollView) this.findViewById(R.id.scrollView);
		mVideoView = (VideoView) findViewById(R.id.videoView);
		headlineLayout = (LinearLayout) this.findViewById(R.id.headlineLayout);
		homeBgImg = (ImageView) this.findViewById(R.id.homeBgImg);
		landscapeBgImg = (ImageView) this.findViewById(R.id.landscapeBgImg);
		storyBgImg = (ImageView) this.findViewById(R.id.storyBgImg);
		communityBgImg = (ImageView) this.findViewById(R.id.communityBgImg);
		humanityBgImg = (ImageView) this.findViewById(R.id.humanityBgImg);
		landscapeAnimPanel = (RelativeLayout) this.findViewById(R.id.landscapeAnimPanel);
		humanityAnimPanel = (RelativeLayout) this.findViewById(R.id.humanityAnimPanel);
		storyAnimPanel = (RelativeLayout) this.findViewById(R.id.storyAnimPanel);
		communityAnimPanel = (RelativeLayout) this.findViewById(R.id.communityAnimPanel);
		scrollView.setHeadlineLayout(headlineLayout);
		scrollView.setLandscapeAnimPanel(landscapeAnimPanel);
		scrollView.setHumanityAnimPanel(humanityAnimPanel);
		scrollView.setStoryAnimPanel(storyAnimPanel);
		scrollView.setCommunityAnimPanel(communityAnimPanel);
		scrollView.setScreenHeight(screenHeight);
	}

	private void CalculateByScreenOrantation(Configuration newConfig)
	{
		int imageHeight = ScreenAdapterTools.getImageHeight(Configuration.ORIENTATION_LANDSCAPE, screenHeight);
		int homeImageHeight = ScreenAdapterTools.getHomeImageHeight(Configuration.ORIENTATION_LANDSCAPE, screenHeight);

		homeBgImg.getLayoutParams().height = homeImageHeight;
		mVideoView.getLayoutParams().height = homeImageHeight;
		landscapeBgImg.getLayoutParams().height = imageHeight;
		storyBgImg.getLayoutParams().height = imageHeight;
		humanityBgImg.getLayoutParams().height = imageHeight;
		communityBgImg.getLayoutParams().height = imageHeight;
		ScreenAdapterTools.setHeadLineLayout((LinearLayout) this.findViewById(R.id.headlineLayout), newConfig.orientation,
				screenWidth);
	}

	public void LandScapeBtnClick(View target)
	{
		scrollView.post(new Runnable()
		{
			@Override
			public void run()
			{
				scrollView.smoothScrollTo(0, (int) landscapeBgImg.getY());
			}
		});
	}

	public void HumanityBtnClick(View target)
	{
		scrollView.post(new Runnable()
		{
			@Override
			public void run()
			{
				scrollView.smoothScrollTo(0, (int) humanityBgImg.getY());
			}
		});
	}

	public void StoryBtnClick(final View target)
	{
		scrollView.post(new Runnable()
		{
			@Override
			public void run()
			{
				scrollView.smoothScrollTo(0, (int) storyBgImg.getY());
			}
		});
	}

	public void CommunityBtnClick(final View target)
	{
		scrollView.post(new Runnable()
		{
			@Override
			public void run()
			{
				scrollView.smoothScrollTo(0, (int) communityBgImg.getY());
			}
		});
	}

	public void DetailContentClick(View target)
	{
		floatLogic.createFloatWin(target, "template1");
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
		{
			this.finish();
			return true;
		}
		return false;
	}
}
