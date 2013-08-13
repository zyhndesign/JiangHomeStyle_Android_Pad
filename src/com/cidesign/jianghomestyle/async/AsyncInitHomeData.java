package com.cidesign.jianghomestyle.async;

import java.util.List;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.cidesign.jianghomestyle.R;
import com.cidesign.jianghomestyle.db.DatabaseHelper;
import com.cidesign.jianghomestyle.entity.ArticleEntity;
import com.cidesign.jianghomestyle.tools.LoadingImageTools;
import com.cidesign.jianghomestyle.tools.TimeTools;
import com.cidesign.jianghomestyle.util.StorageUtils;
import com.cidesign.jianghomestyle.viewlogic.CategoryDataLoadingLogic;
import com.cidesign.jianghomestyle.viewlogic.LoadingDataFromDB;

public class AsyncInitHomeData extends AsyncTask<Void, Void, List<ArticleEntity>>
{
	private Activity activity;
	private int screen_width;
	private LoadingDataFromDB loadingDataFromDB = null;
	private DatabaseHelper dbHelper;
	private LayoutInflater inflater = null;

	private TextView home_title;
	private TextView homeArticleTime;
	private LinearLayout homeLinearLayout;
	private ImageView homeBgImg;
	private LinearLayout headlineLayout;
	private VideoView mVideoView;

	public AsyncInitHomeData(Activity activity, DatabaseHelper dbHelper, LinearLayout headlineLayout, LayoutInflater inflater,
			int screen_width)
	{
		this.activity = activity;
		this.dbHelper = dbHelper;
		this.headlineLayout = headlineLayout;
		this.inflater = inflater;
		this.screen_width = screen_width;
	}

	@Override
	protected void onPreExecute()
	{
		loadingDataFromDB = new LoadingDataFromDB();
		homeLinearLayout = (LinearLayout) activity.findViewById(R.id.homeLinearLayout);
		home_title = (TextView) activity.findViewById(R.id.home_title);
		homeArticleTime = (TextView) activity.findViewById(R.id.homeArticleTime);
		homeBgImg = (ImageView) activity.findViewById(R.id.homeBgImg);
		mVideoView = (VideoView) activity.findViewById(R.id.videoView);
	}

	@Override
	protected List<ArticleEntity> doInBackground(Void... arg0)
	{
		// 读取数据库加载数据
		return loadingDataFromDB.loadTopFourArticle(dbHelper.getArticleListDataDao());
	}

	@Override
	protected void onPostExecute(List<ArticleEntity> list)
	{
		if (list != null && list.size() >= 1)
		{
			ArticleEntity aEntity = list.get(0);
			
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
					final String videoPath = "file://"+StorageUtils.FILE_ROOT + aEntity.getServerID() + "/" + aEntity.getMax_bg_img();
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
			else
			{
				homeBgImg.setVisibility(View.VISIBLE);
				loadingImg
						.loadingImage(homeBgImg, StorageUtils.FILE_ROOT + aEntity.getServerID() + "/" + aEntity.getMax_bg_img());
			}
			home_title.setText(aEntity.getTitle());
			homeArticleTime.setText(TimeTools.getTimeByTimestap(Long.parseLong(aEntity.getPost_date())));
			homeLinearLayout.setTag(aEntity);
			if (list.size() >= 2)
			{
				CategoryDataLoadingLogic.loadHeadLineData(list, headlineLayout, screen_width, inflater); // 初始化头条
			}
		}
	}
}
