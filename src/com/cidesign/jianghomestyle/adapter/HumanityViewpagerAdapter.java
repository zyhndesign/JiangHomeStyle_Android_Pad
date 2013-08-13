package com.cidesign.jianghomestyle.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.cidesign.jianghomestyle.R;
import com.cidesign.jianghomestyle.adapter.CommunityViewpagerAdapter.AsyncLoadImage;
import com.cidesign.jianghomestyle.entity.ArticleEntity;
import com.cidesign.jianghomestyle.tools.LoadingImageTools;
import com.cidesign.jianghomestyle.tools.TimeTools;
import com.cidesign.jianghomestyle.util.StorageUtils;
import com.cidesign.jianghomestyle.viewlogic.FloatViewLogic;

public class HumanityViewpagerAdapter extends PagerAdapter
{
	private static final String TAG = LandscapeViewpagerAdapter.class.getSimpleName();
	
	private static final int COLUMN_NUM = 3;
	
	private HashMap<Integer,Bitmap> bitHashMap = new HashMap<Integer,Bitmap>();
	
	private List<ArticleEntity> list = new ArrayList<ArticleEntity>();
		
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
		LinearLayout.LayoutParams globalImageViewLayout = LayoutCaculateAdapter.getLinearLayout(screenWidth - 200, 6);
		LinearLayout.LayoutParams wholeHeightViewLayout = LayoutCaculateAdapter.getLinearWholeHeightLayout(screenWidth - 200, 6);
		
		LinearLayout linearLayout = new LinearLayout(activity.getApplicationContext());
		linearLayout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		linearLayout.setOrientation(LinearLayout.HORIZONTAL);
		
		LayoutInflater inflater = LayoutInflater.from(activity.getApplicationContext());
		ArticleEntity aEntity = null;
			
		Bitmap bitMap = null;
		
		int size = list.size();
		
		try
		{
			if (position * COLUMN_NUM < size)
			{
				View view = inflater.inflate(R.layout.humanity_template, null);
				aEntity = list.get(position * COLUMN_NUM);
				ImageView humanityBgImg = (ImageView)activity.findViewById(R.id.humanityBgImg);
				if (position == 0 && aEntity.getMax_bg_img() != null && !aEntity.getMax_bg_img().equals(""))
				{				
					bitMap = LoadingImageTools.getImageBitmap(StorageUtils.FILE_ROOT + aEntity.getServerID() + "/" + aEntity.getMax_bg_img());
				}
				else
				{
					bitMap = LoadingImageTools.loadingNative(activity.getApplicationContext(),"bg2.jpg");				
				}
				if (bitMap != null)
				{
					humanityBgImg.setImageBitmap(bitMap);				
				}
				
				ImageView firstImg = (ImageView) view.findViewById(R.id.humanityImg);
				new AsyncLoadImage(firstImg,position * COLUMN_NUM).execute(StorageUtils.FILE_ROOT + aEntity.getServerID() + "/" + aEntity.getProfile_path());
				bitMap = LoadingImageTools.getImageBitmap(StorageUtils.FILE_ROOT + aEntity.getServerID() + "/" + aEntity.getProfile_path());
								
				firstImg.setLayoutParams(globalImageViewLayout);

				TextView tv1 = (TextView) view.findViewById(R.id.humanityTitle);
				tv1.setText(aEntity.getTitle());
				TextView tv2 = (TextView) view.findViewById(R.id.humanityTime);
				tv2.setText(TimeTools.getTimeByTimestap(Long.parseLong(aEntity.getPost_date())));
				TextView tv3 = (TextView) view.findViewById(R.id.humanityContent);
				tv3.setText(aEntity.getDescription());
				tv3.setLayoutParams(wholeHeightViewLayout);

				LinearLayout humanityLayout = (LinearLayout) view.findViewById(R.id.humanityLayout);
				humanityLayout.setTag(aEntity);
				humanityLayout.setOnClickListener(new ClickPop());
				humanityLayout.setVisibility(View.VISIBLE);	
				
				linearLayout.addView(view);
			}

			if ((position * COLUMN_NUM + 1) < size)
			{
				View view = inflater.inflate(R.layout.humanity_template, null);
				aEntity = list.get(position * COLUMN_NUM + 1);
				ImageView firstImg = (ImageView) view.findViewById(R.id.humanityImg);
				
				new AsyncLoadImage(firstImg,position * COLUMN_NUM + 1).execute(StorageUtils.FILE_ROOT + aEntity.getServerID() + "/" + aEntity.getProfile_path());
				
				firstImg.setLayoutParams(globalImageViewLayout);

				TextView tv1 = (TextView) view.findViewById(R.id.humanityTitle);
				tv1.setText(aEntity.getTitle());
				TextView tv2 = (TextView) view.findViewById(R.id.humanityTime);
				tv2.setText(TimeTools.getTimeByTimestap(Long.parseLong(aEntity.getPost_date())));
				TextView tv3 = (TextView) view.findViewById(R.id.humanityContent);
				tv3.setText(aEntity.getDescription());
				tv3.setLayoutParams(wholeHeightViewLayout);

				LinearLayout humanityLayout = (LinearLayout) view.findViewById(R.id.humanityLayout);
				humanityLayout.setTag(aEntity);
				humanityLayout.setOnClickListener(new ClickPop());
				humanityLayout.setVisibility(View.VISIBLE);	
				linearLayout.addView(view);
			}
			if ((position * COLUMN_NUM + 2) < size)
			{
				View view = inflater.inflate(R.layout.humanity_template, null);
				aEntity = list.get(position * COLUMN_NUM + 2);
				ImageView firstImg = (ImageView) view.findViewById(R.id.humanityImg);
				
				new AsyncLoadImage(firstImg,position * COLUMN_NUM + 2).execute(StorageUtils.FILE_ROOT + aEntity.getServerID() + "/" + aEntity.getProfile_path());
				firstImg.setLayoutParams(globalImageViewLayout);

				TextView tv1 = (TextView) view.findViewById(R.id.humanityTitle);
				tv1.setText(aEntity.getTitle());
				TextView tv2 = (TextView) view.findViewById(R.id.humanityTime);
				tv2.setText(TimeTools.getTimeByTimestap(Long.parseLong(aEntity.getPost_date())));
				TextView tv3 = (TextView) view.findViewById(R.id.humanityContent);
				tv3.setText(aEntity.getDescription());
				tv3.setLayoutParams(wholeHeightViewLayout);

				LinearLayout humanityLayout = (LinearLayout) view.findViewById(R.id.humanityLayout);
				humanityLayout.setTag(aEntity);
				humanityLayout.setOnClickListener(new ClickPop());
				humanityLayout.setVisibility(View.VISIBLE);	
				linearLayout.addView(view);
			}
			((ViewPager) context).addView(linearLayout);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}		
		
		return linearLayout;
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
