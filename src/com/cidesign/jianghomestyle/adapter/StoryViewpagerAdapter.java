package com.cidesign.jianghomestyle.adapter;

import java.io.File;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.cidesign.jianghomestyle.R;
import com.cidesign.jianghomestyle.entity.ContentEntity;
import com.cidesign.jianghomestyle.entity.RelativeLayoutRulesEntity;
import com.cidesign.jianghomestyle.tools.LoadingImageTools;
import com.cidesign.jianghomestyle.util.StorageUtils;
import com.cidesign.jianghomestyle.viewlogic.FloatViewLogic;

public class StoryViewpagerAdapter extends PagerAdapter
{
	private static final String TAG = LandscapeViewpagerAdapter.class.getSimpleName();
	
	private HashMap<Integer,Bitmap> bitHashMap = new HashMap<Integer,Bitmap>();
	
	private List<ContentEntity> list = new ArrayList<ContentEntity>();
	
	private static final int COLUMN_NUM = 6;
		
	private int screenWidth;
		
	private Activity activity;
	
	private FloatViewLogic floatLogic;
		
	private AQuery aq = null;
	
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

	public List<ContentEntity> getList()
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
		
		LayoutInflater inflater = LayoutInflater.from(activity.getApplicationContext());
		ContentEntity cEntity = null;
			
		Bitmap bitMap = null;
		
		View view = inflater.inflate(R.layout.story_template, null);
		aq = new AQuery(view);
		
		int size = list.size();
		
		try
		{
			if (position * COLUMN_NUM < size)
			{
				cEntity = list.get(position * COLUMN_NUM);
				ImageView storyBgImg = (ImageView) activity.findViewById(R.id.storyBgImg);
				if (position == 0 && cEntity.getMax_bg_img() != null && !cEntity.getMax_bg_img().equals(""))
				{
					bitMap = LoadingImageTools.getImageBitmap(StorageUtils.FILE_ROOT + cEntity.getServerID() + "/" + cEntity.getMax_bg_img());
				}
				else
				{
					bitMap = LoadingImageTools.loadingNative(activity.getApplicationContext(), "bg5.jpg");				
				}
				if (bitMap != null)
				{
					storyBgImg.setImageBitmap(bitMap);				
				}
				
				ImageView img = (ImageView) view.findViewById(R.id.storyFirstImg);
				getThumbImage(cEntity,img,R.id.storyFirstImg,position * COLUMN_NUM);
				TextView tv1 = (TextView) view.findViewById(R.id.storyFirstTitle);
				tv1.setText(cEntity.getTitle());
				RelativeLayout storyLayout1 = (RelativeLayout) view.findViewById(R.id.storyLayout1);
				RelativeLayout.LayoutParams bigImageViewLayout = LayoutCaculateAdapter.getRelativeLayout(screenWidth, 6);
				storyLayout1.setLayoutParams(bigImageViewLayout);
				storyLayout1.setTag(cEntity);
				storyLayout1.setOnClickListener(new ClickPop());
				storyLayout1.setVisibility(View.VISIBLE);
			}

			if ((position * COLUMN_NUM + 1) < size)
			{
				cEntity = list.get(position * COLUMN_NUM + 1);
				ImageView img = (ImageView) view.findViewById(R.id.storyTwoImg);
				getThumbImage(cEntity,img,R.id.storyTwoImg,position * COLUMN_NUM + 1);
				TextView tv1 = (TextView) view.findViewById(R.id.storyTwoTitle);
				tv1.setText(cEntity.getTitle());
				RelativeLayout storyLayout2 = (RelativeLayout) view.findViewById(R.id.storyLayout2);
				
				RelativeLayoutRulesEntity ruleEntity = new RelativeLayoutRulesEntity();
				ruleEntity.setRightOfVlaue(R.id.storyLayout1);
				RelativeLayout.LayoutParams bigImageViewLayout = LayoutCaculateAdapter.getBigRelativeLayoutOfParam(screenWidth, 6,ruleEntity);
				storyLayout2.setLayoutParams(bigImageViewLayout);
				storyLayout2.setTag(cEntity);
				storyLayout2.setOnClickListener(new ClickPop());
				storyLayout2.setVisibility(View.VISIBLE);
			}

			if ((position * COLUMN_NUM + 2) < size)
			{
				cEntity = list.get(position * COLUMN_NUM + 2);
				ImageView img = (ImageView) view.findViewById(R.id.storyThreeImg);
				getThumbImage(cEntity,img,R.id.storyThreeImg,position * COLUMN_NUM + 2);
				TextView tv1 = (TextView) view.findViewById(R.id.storyThreeTitle);
				tv1.setText(cEntity.getTitle());
				
				RelativeLayout storyLayout3 = (RelativeLayout) view.findViewById(R.id.storyLayout3);
				RelativeLayoutRulesEntity ruleEntity = new RelativeLayoutRulesEntity();
				ruleEntity.setRightOfVlaue(R.id.storyLayout2);
				RelativeLayout.LayoutParams smallImageViewLayout = LayoutCaculateAdapter.getSmallBottomRelativeLayoutOfParam(screenWidth, 6,ruleEntity);
				storyLayout3.setLayoutParams(smallImageViewLayout);
				storyLayout3.setVisibility(View.VISIBLE);
				storyLayout3.setTag(cEntity);
				storyLayout3.setOnClickListener(new ClickPop());
			}

			if ((position * COLUMN_NUM + 3) < size)
			{
				cEntity = list.get(position * COLUMN_NUM + 3);
				ImageView img = (ImageView) view.findViewById(R.id.storyFourImg);
				getThumbImage(cEntity,img,R.id.storyFourImg,position * COLUMN_NUM + 3);
				TextView tv1 = (TextView) view.findViewById(R.id.storyFourTitle);
				tv1.setText(cEntity.getTitle());
				RelativeLayout storyLayout4 = (RelativeLayout) view.findViewById(R.id.storyLayout4);
				RelativeLayoutRulesEntity ruleEntity = new RelativeLayoutRulesEntity();
				ruleEntity.setRightOfVlaue(R.id.storyLayout3);
				RelativeLayout.LayoutParams smallImageViewLayout = LayoutCaculateAdapter.getSmallBottomRelativeLayoutOfParam(screenWidth, 6,ruleEntity);
				storyLayout4.setLayoutParams(smallImageViewLayout);
				storyLayout4.setVisibility(View.VISIBLE);
				storyLayout4.setTag(cEntity);
				storyLayout4.setOnClickListener(new ClickPop());
			}

			if ((position * COLUMN_NUM  + 4) < size)
			{
				cEntity = list.get(position * COLUMN_NUM + 4);
				ImageView img = (ImageView) view.findViewById(R.id.storyFiveImg);
				getThumbImage(cEntity,img,R.id.storyFiveImg,position * COLUMN_NUM + 4);
				TextView tv1 = (TextView) view.findViewById(R.id.storyFiveTitle);
				tv1.setText(cEntity.getTitle());
				RelativeLayout storyLayout5 = (RelativeLayout) view.findViewById(R.id.storyLayout5);
				RelativeLayoutRulesEntity ruleEntity = new RelativeLayoutRulesEntity();
				ruleEntity.setRightOfVlaue(R.id.storyLayout2);
				ruleEntity.setBelowOfValue(R.id.storyLayout3);
				RelativeLayout.LayoutParams smallImageViewLayout = LayoutCaculateAdapter.getSmallTopRelativeLayoutOfParam(screenWidth, 6,ruleEntity);
				storyLayout5.setLayoutParams(smallImageViewLayout);
				storyLayout5.setVisibility(View.VISIBLE);
				storyLayout5.setTag(cEntity);
				storyLayout5.setOnClickListener(new ClickPop());
			}

			if ((position * COLUMN_NUM + 5) < size)
			{
				cEntity = list.get(position * COLUMN_NUM + 5);
				ImageView img = (ImageView) view.findViewById(R.id.storySixImg);
				getThumbImage(cEntity,img,R.id.storySixImg,position * COLUMN_NUM + 5);
				TextView tv1 = (TextView) view.findViewById(R.id.storySixTitle);
				tv1.setText(cEntity.getTitle());
				RelativeLayout storyLayout6 = (RelativeLayout) view.findViewById(R.id.storyLayout6);
				RelativeLayoutRulesEntity ruleEntity = new RelativeLayoutRulesEntity();
				ruleEntity.setRightOfVlaue(R.id.storyLayout5);
				ruleEntity.setBelowOfValue(R.id.storyLayout4);
				RelativeLayout.LayoutParams smallImageViewLayout = LayoutCaculateAdapter.getSmallTopRelativeLayoutOfParam(screenWidth, 6,ruleEntity);
				storyLayout6.setLayoutParams(smallImageViewLayout);
				storyLayout6.setVisibility(View.VISIBLE);
				storyLayout6.setTag(cEntity);
				storyLayout6.setOnClickListener(new ClickPop());
			}
			((ViewPager) context).addView(view);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return view;
	}
	
	class ClickPop implements View.OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			floatLogic.createFloatWin(v, "template1");
		}		
	}
	
	private void getThumbImage(final ContentEntity cEntity,final ImageView view,int id,final int position)
	{
		final String filePathDir = StorageUtils.THUMB_IMG_ROOT + cEntity.getServerID() + "/";
		File fileDir = new File(filePathDir);
		if (!fileDir.exists() || !fileDir.isDirectory())
			fileDir.mkdir();
		String fileName = filePathDir + cEntity.getServerID()+".jpg";
		File file = new File(fileName);
		if (file.exists())
		{
			aq.id(id).image(file,400);
		}
		else
		{
			String url = cEntity.getProfile_path();             
			
			File target = new File(fileDir, cEntity.getServerID()+".jpg");              

			aq.download(url, target, new AjaxCallback<File>(){
			        
			        public void callback(String url, File file, AjaxStatus status) {
			                
			        	Bitmap bm = LoadingImageTools.getDetailImageBitmap(activity.getApplicationContext(),filePathDir + cEntity.getServerID()+".jpg");
			        	if (bm != null && view != null)
						{
							view.setImageBitmap(bm);
							bitHashMap.put(position, bm);
						}
			        }
			        
			});
		}
	}
}
