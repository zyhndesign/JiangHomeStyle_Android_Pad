package com.cidesign.jianghomestylerelease.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.cidesign.jianghomestyle.entity.ArticleEntity;
import com.cidesign.jianghomestyle.tools.LoadingImageTools;
import com.cidesign.jianghomestyle.tools.TimeTools;
import com.cidesign.jianghomestyle.util.StorageUtils;
import com.cidesign.jianghomestyle.viewlogic.FloatViewLogic;
import com.cidesign.jianghomestylerelease.R;
import com.cidesign.jianghomestylerelease.adapter.HumanityViewpagerAdapter.AsyncLoadImage;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LandscapeViewpagerAdapter extends PagerAdapter
{
	private static final String TAG = LandscapeViewpagerAdapter.class.getSimpleName();
	
	private HashMap<Integer,Bitmap> bitHashMap = new HashMap<Integer,Bitmap>();
	
	private List<ArticleEntity> list = new ArrayList<ArticleEntity>();
	
	private static final int COLUMN_NUM = 3;
		
	private int screenWidth;
		
	private Activity activity;
		
	private FloatViewLogic floatLogic;
		
	public void setScreenWidth(int screenWidth)
	{
		this.screenWidth = screenWidth;
	}

	public void setActivity(Activity activity)
	{
		this.activity = activity;
	}
	
	public void setFloatLogic(FloatViewLogic floatLogic)
	{
		this.floatLogic = floatLogic;
	}

	public List<ArticleEntity> getList()
	{
		return list;
	}
	
	@Override
	public int getCount()
	{
		int size = list.size();
		if (size % COLUMN_NUM == 0)
		{
			size = size / COLUMN_NUM;
		}
		else
		{
			size = size / COLUMN_NUM + 1;
		}
		return size;
	}

	@Override
	public boolean isViewFromObject(View view, Object object)
	{		
		return view == object;
	}

	@Override
	public void finishUpdate(View view)
	{

	}
	
	@Override
	public void destroyItem(View collection, int position, Object o)
	{
		Log.d(TAG, "destory the resources...");
		for (int i = 0; i < COLUMN_NUM; i++)
		{
			Bitmap bitmap = bitHashMap.get(position + i);
			if (bitmap != null && !bitmap.isRecycled())
			{
				Log.d(TAG, "recycle the bitmap...");
				bitmap.recycle();
				bitmap = null;
			}
		}
		
		View view = (View) o;
		((ViewPager) collection).removeView(view);
		view = null;
	}
	
	@Override
	public Object instantiateItem(View context, int position)
	{
		LinearLayout.LayoutParams globalImageViewLayout = LayoutCaculateAdapter.getLinearLayout(screenWidth - 160, 5);		
		RelativeLayout.LayoutParams imageViewLayout = LayoutCaculateAdapter.getRelativeLayout(screenWidth - 95, 5);
		LayoutInflater inflater = LayoutInflater.from(activity.getApplicationContext());
		View view = inflater.inflate(R.layout.landscape_template, null);
		try
		{
			ArticleEntity aEntity = null;
			
			Bitmap bitMap = null;
			
			int size = list.size();
			
			if (position * COLUMN_NUM < size)
			{
				aEntity = list.get(position * COLUMN_NUM);
				ImageView landscapeBgImg = (ImageView) activity.findViewById(R.id.landscapeBgImg);
				
				if (position == 0 && aEntity.getMax_bg_img() != null && !aEntity.getMax_bg_img().equals(""))
				{
					bitMap = LoadingImageTools.getImageBitmap(StorageUtils.FILE_ROOT + aEntity.getServerID() + "/" + aEntity.getMax_bg_img());
				}
				else
				{
					bitMap = LoadingImageTools.loadingNative(activity.getApplicationContext(),"bg4.jpg");					
				}
				if (bitMap != null)
				{
					landscapeBgImg.setImageBitmap(bitMap);				
				}
				
				ImageView firstImg = (ImageView) view.findViewById(R.id.landscapeFirstBgImg);
				
				new AsyncLoadImage(firstImg,position * COLUMN_NUM).execute(StorageUtils.FILE_ROOT + aEntity.getServerID() + "/" + aEntity.getProfile_path());		
				
				firstImg.setLayoutParams(imageViewLayout);

				TextView tv1 = (TextView) view.findViewById(R.id.landscapeFirstTitle);
				tv1.setText(aEntity.getTitle());
				TextView tv2 = (TextView) view.findViewById(R.id.landscapeFirstTime);
				tv2.setText(TimeTools.getTimeByTimestap(Long.parseLong(aEntity.getPost_date())));
				TextView tv3 = (TextView) view.findViewById(R.id.landscapeFirstContent);
				tv3.setText(aEntity.getDescription());
				firstImg.setTag(aEntity);
				firstImg.setOnClickListener(new ClickPop());
				
				LinearLayout landscapeFirstLayout = (LinearLayout) view.findViewById(R.id.landscapeFirstLayout);
				landscapeFirstLayout.setTag(aEntity);
				landscapeFirstLayout.setOnClickListener(new ClickPop());
				landscapeFirstLayout.setLayoutParams(LayoutCaculateAdapter.getLinearWholeHeightLayout(screenWidth - 110, 5));
			}

			if ((position * COLUMN_NUM + 1) < size)
			{
				aEntity = list.get(position * COLUMN_NUM + 1);
				ImageView twoImg = (ImageView) view.findViewById(R.id.landscapeTwoImg);
				
				new AsyncLoadImage(twoImg,position * COLUMN_NUM + 1).execute(StorageUtils.FILE_ROOT + aEntity.getServerID() + "/" + aEntity.getProfile_path());
				TextView tv1 = (TextView) view.findViewById(R.id.landscapeTwoTitle);
				tv1.setText(aEntity.getTitle());
				TextView tv2 = (TextView) view.findViewById(R.id.landscapeTwoTime);
				tv2.setText(TimeTools.getTimeByTimestap(Long.parseLong(aEntity.getPost_date())));
				TextView tv3 = (TextView) view.findViewById(R.id.landscapeTwoContent);
				tv3.setText(aEntity.getDescription());
				twoImg.setLayoutParams(globalImageViewLayout);

				LinearLayout landscapeTwoLayout = (LinearLayout) view.findViewById(R.id.landscapeTwoLayout);
				landscapeTwoLayout.setTag(aEntity);
				landscapeTwoLayout.setOnClickListener(new ClickPop());
				View v1 = view.findViewById(R.id.landHorTwoLine);
				v1.setVisibility(View.VISIBLE);
				View v2 = view.findViewById(R.id.landVerTwoLine);
				v2.setVisibility(View.VISIBLE);
			}
			if ((position * COLUMN_NUM + 2) < size)
			{
				aEntity = list.get(position * COLUMN_NUM + 2);
				ImageView threeImg = (ImageView) view.findViewById(R.id.landscapeThreeImg);
				new AsyncLoadImage(threeImg,position * COLUMN_NUM + 2).execute(StorageUtils.FILE_ROOT + aEntity.getServerID() + "/" + aEntity.getProfile_path());
				TextView tv1 = (TextView) view.findViewById(R.id.landscapeThreeTitle);
				tv1.setText(aEntity.getTitle());
				TextView tv2 = (TextView) view.findViewById(R.id.landscapeThreeTime);
				tv2.setText(TimeTools.getTimeByTimestap(Long.parseLong(aEntity.getPost_date())));
				TextView tv3 = (TextView) view.findViewById(R.id.landscapeThreeContent);
				tv3.setText(aEntity.getDescription());
				threeImg.setLayoutParams(globalImageViewLayout);

				LinearLayout landscapeTextLayout = (LinearLayout) view.findViewById(R.id.landscapeTextLayout);
				landscapeTextLayout.setLayoutParams(globalImageViewLayout);
				LinearLayout landscapeThreeLayout = (LinearLayout) view.findViewById(R.id.landscapeThreeLayout);
				landscapeThreeLayout.setTag(aEntity);
				landscapeThreeLayout.setOnClickListener(new ClickPop());
				View v1 = view.findViewById(R.id.landHorThreeLine);
				v1.setVisibility(View.VISIBLE);
				View v2 = view.findViewById(R.id.landVerThreeLine);
				v2.setVisibility(View.VISIBLE);
			}
			((ViewPager)context).addView(view);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return view;
	}
	
	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1)
	{

	}

	@Override
	public Parcelable saveState()
	{
		return null;
	}

	@Override
	public void startUpdate(View view)
	{

	}
	
	class ClickPop implements View.OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			floatLogic.createFloatWin(v, "template1");
		}		
	}
	
	class AsyncLoadImage extends AsyncTask<String, Void, Bitmap>
	{
		private ImageView view;
		private int position;
		
		public AsyncLoadImage(ImageView view,int position)
		{
			this.view = view;
			this.position = position;
		}

		@Override
		protected void onPreExecute()
		{
			// 第一个执行方法
			super.onPreExecute();
		}

		@Override
		protected Bitmap doInBackground(String... params)
		{
			return LoadingImageTools.getDetailImageBitmap(activity.getApplicationContext(),params[0]);
		}

		@Override
		protected void onPostExecute(Bitmap bm)
		{
			if (bm != null && view != null)
			{
				view.setImageBitmap(bm);
				bitHashMap.put(position, bm);
			}
		}
	}
}
