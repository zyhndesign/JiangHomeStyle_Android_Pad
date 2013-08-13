package com.cidesign.jianghomestyle.viewlogic;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.cidesign.jianghomestyle.db.DatabaseHelper;
import com.cidesign.jianghomestyle.entity.MusicEntity;
import com.cidesign.jianghomestyle.tools.NetworksTool;
import com.cidesign.jianghomestylerelease.R;

public class MusicViewLogic
{
	private static final String TAG = MusicViewLogic.class.getSimpleName();

	private List<MusicEntity> musicList = null;
	private MediaPlayer mediaPlayer;

	private Context ctx;
	private SeekBar musicSeekbar = null;
	private TextView musicName = null;
	private TextView musicAuthor = null;
	private int PALYING_NUM = 0;
	private ImageView startMusic = null;

	private boolean isPause = false; // 当前播放是否暂停状态

	public MediaPlayer getMediaPlayer()
	{
		return mediaPlayer;
	}

	
	public SeekBar getMusicSeekbar()
	{
		return musicSeekbar;
	}


	public MusicViewLogic(Context context, DatabaseHelper dbHelper, View view, List<MusicEntity> _musicList)
	{
		this.ctx = context;
		this.musicSeekbar = (SeekBar) view.findViewById(R.id.music_seekbar);
		this.musicName = (TextView) view.findViewById(R.id.music_name);
		this.musicAuthor = (TextView) view.findViewById(R.id.music_author);
		ImageView nextMusic = (ImageView) view.findViewById(R.id.music_next);
		nextMusic.setOnClickListener(new MusicPlayClick());

		this.musicList = _musicList;

		// music_list = dbHelper.getMusicDataDao().queryForAll();;

		startMusic = (ImageView) view.findViewById(R.id.music_play);
		startMusic.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				if (mediaPlayer != null && mediaPlayer.isPlaying())
				{
					mediaPlayer.pause();
					isPause = true;
					startMusic.setBackgroundResource(R.drawable.music_btn_pause);
				}
				else if (mediaPlayer == null)
				{
					if (musicList != null && musicList.size() > 0)
					{
						if(NetworksTool.isNetworkAvailable(ctx)) //播放时先检查网络是否正常
						{
							playMusic(0);
						}
						else //网络出现问题则播放默认歌曲
						{
							mediaPlayer = MediaPlayer.create(ctx, R.raw.music_1);
							mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
							mediaPlayer.start();
							mediaPlayer.setOnCompletionListener(new OnCompletionListener()
							{
								@Override
								public void onCompletion(MediaPlayer mp)
								{
									musicSeekbar.setProgress(0);
								}
							});
							
							musicSeekbar.setMax(mediaPlayer.getDuration() / 1000);
							// music_time.setText(TimeTools.getTime(mediaPlayer.getDuration()));
							handler.post(updateThread);
						}
					}
					else
					{
						// 播放默认歌曲
						if (musicList == null || musicList.size() == 0)
						{
							mediaPlayer = MediaPlayer.create(ctx, R.raw.music_1);
							mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
							mediaPlayer.start();
							mediaPlayer.setOnCompletionListener(new OnCompletionListener()
							{
								@Override
								public void onCompletion(MediaPlayer mp)
								{
									musicSeekbar.setProgress(0);
								}
							});
							
							musicSeekbar.setMax(mediaPlayer.getDuration() / 1000);
							// music_time.setText(TimeTools.getTime(mediaPlayer.getDuration()));
							handler.post(updateThread);
						}
					}
				}
				else
				{
					if (isPause)
					{
						mediaPlayer.start();
						startMusic.setBackgroundResource(R.drawable.music_btn_play);
					}
					else
					{
						isPause = false;
						startMusic.setBackgroundResource(R.drawable.music_btn_play);
					}
				}
			}
		});

		musicSeekbar.setOnSeekBarChangeListener(new MusicSeekBar());
	}

	/**
	 * 播放第 i 首歌曲
	 * 
	 * @param i
	 */
	private void playMusic(int i)
	{
		if (mediaPlayer != null)
		{
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
			musicSeekbar.setProgress(0);
		}
		MusicEntity mEntity = musicList.get(i);
		musicName.setText(mEntity.getMusic_title());
		musicAuthor.setText(mEntity.getMusic_author());
		Uri myUri = Uri.parse(mEntity.getMusic_path()); // initialize Uri here
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		try
		{
			mediaPlayer.setDataSource(ctx, myUri);
			mediaPlayer.prepare();
			mediaPlayer.start();
			musicSeekbar.setMax(mediaPlayer.getDuration() / 1000);
			// music_time.setText(TimeTools.getTime(mediaPlayer.getDuration()));
			handler.post(updateThread);
			mediaPlayer.setOnCompletionListener(new OnCompletionListener()
			{

				@Override
				public void onCompletion(MediaPlayer mp)
				{
					Log.d(TAG, "complete the music, if have next then paly next");
					musicSeekbar.setProgress(0);
					playNextMusic();
				}
			});
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		catch (SecurityException e)
		{
			e.printStackTrace();
		}
		catch (IllegalStateException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 播放下一首
	 */
	private void playNextMusic()
	{
		PALYING_NUM++;
		if (musicList != null)
		{
			if (PALYING_NUM >= musicList.size())
			{
				PALYING_NUM = 0;
			}
			playMusic(PALYING_NUM);
		}
		else
		{
			Log.d(TAG, "playing down...");
			musicSeekbar.setProgress(0);
			// music_time.setText("00:00:00");
		}
	}

	/**
	 * 播放歌曲
	 * 
	 * @param target
	 */
	private class MusicPlayClick implements View.OnClickListener
	{
		@Override
		public void onClick(final View v)
		{
			if (mediaPlayer != null)
			{
				if (isPause)
				{
					isPause = false;
					startMusic.setBackgroundResource(R.drawable.music_btn_play);
				}
				playNextMusic();
				mediaPlayer.setOnCompletionListener(new OnCompletionListener()
				{

					@Override
					public void onCompletion(MediaPlayer mp)
					{
						if (mediaPlayer != null)
						{
							mediaPlayer.release();
							mediaPlayer = null;
						}

						musicSeekbar.setProgress(0);
						// music_time.setText("00:00:00");
						// v.setBackgroundResource(R.drawable.music_play);
						Log.d(TAG, "complete the music, if have next then paly next");
						playNextMusic();
					}
				});
			}
		}
	}

	Handler handler = new Handler();
	Runnable updateThread = new Runnable()
	{
		public void run()
		{
			// 获得歌曲现在播放位置并设置成播放进度条的值
			if (mediaPlayer != null)
			{
				try
				{
					musicSeekbar.setProgress(mediaPlayer.getCurrentPosition() / 1000);
					// 每次延迟100毫秒再启动线程
					// music_time.setText(TimeTools.getTime(mediaPlayer.getCurrentPosition()));
					handler.postDelayed(updateThread, 100);
				}
				catch(IllegalStateException e)
				{
					
				}
			}
		}
	};

	class MusicSeekBar implements OnSeekBarChangeListener
	{

		@Override
		public void onProgressChanged(SeekBar arg0, int progress, boolean fromUser)
		{
			if (fromUser == true && mediaPlayer != null)
			{
				mediaPlayer.seekTo(progress);
			}
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar)
		{
			// isChanging = true;
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar)
		{
			if (mediaPlayer != null)
			{
				int dest = seekBar.getProgress();

				int mMax = mediaPlayer.getDuration();
				int sMax = musicSeekbar.getMax();

				mediaPlayer.seekTo(mMax * dest / sMax);
				// isChanging = false;
			}

		}
	}

	public void releaseMediaPlayer()
	{
		if (mediaPlayer != null)
		{
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}
}
