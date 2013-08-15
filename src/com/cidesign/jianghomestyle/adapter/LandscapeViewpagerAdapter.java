package com.cidesign.jianghomestyle.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.cidesign.jianghomestyle.R;
import com.cidesign.jianghomestyle.entity.ContentEntity;
import com.cidesign.jianghomestyle.tools.LoadingImageTools;
import com.cidesign.jianghomestyle.tools.TimeTools;
import com.cidesign.jianghomestyle.util.JiangFinalVariables;
import com.cidesign.jianghomestyle.util.StorageUtils;
import com.cidesign.jianghomestyle.viewlogic.FloatViewLogic;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 
* @Title: LandscapeViewpagerAdapter.java 
* @Package com.cidesign.jianghomestyle.adapter 
* @Description: landscape model view pager data adapter
* @author liling  
* @date 2013年8月14日 下午1:39:58 
* @version V2.0
 */
public class LandscapeViewpagerAdapter extends PagerAdapter
{
	private static final String TAG = LandscapeViewpagerAdapter.class.getSimpleName();
	
	private HashMap<Integer,Bitmap> bitHashMap = new HashMap<Integer,Bitmap>();
	
	private List<ContentEntity> list = new ArrayList<ContentEntity>();
	
	private static final int COLUMN_NUM = 3;
		
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
		aq = new AQuery(view);
		
		try
		{
			ContentEntity cEntity = null;
			
			Bitmap bitMap = null;
			
			int size = list.size();
			
			if (position * COLUMN_NUM < size)
			{
				cEntity = list.get(position * COLUMN_NUM);
				ImageView landscapeBgImg = (ImageView) activity.findViewById(R.id.landscapeBgImg);
				
				if (position == 0 && cEntity.getMax_bg_img() != null && !cEntity.getMax_bg_img().equals(""))
				{
					String fileDir = StorageUtils.FILE_ROOT + cEntity.getServerID() + "/" + cEntity.getMax_bg_img();
					File file = new File(fileDir);
					if (file.exists() && fileDir.endsWith(".jpg"))
					{
						bitMap = LoadingImageTools.getImageBitmap(fileDir);
					}
					else
					{
						bitMap = LoadingImageTools.loadingNative(activity.getApplicationContext(), "bg4.jpg");
					}
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
				
				getThumbImage(cEntity,firstImg,R.id.landscapeFirstBgImg,position * COLUMN_NUM);
				
				firstImg.setLayoutParams(imageViewLayout);

				TextView tv1 = (TextView) view.findViewById(R.id.landscapeFirstTitle);
				tv1.setText(cEntity.getTitle());
				TextView tv2 = (TextView) view.findViewById(R.id.landscapeFirstTime);
				tv2.setText(TimeTools.getTimeByTimestap(Long.parseLong(cEntity.getTimestamp())));
				TextView tv3 = (TextView) view.findViewById(R.id.landscapeFirstContent);
				tv3.setText(cEntity.getDescription());
				firstImg.setTag(cEntity);
				firstImg.setOnClickListener(new ClickPop());
				
				LinearLayout landscapeFirstLayout = (LinearLayout) view.findViewById(R.id.landscapeFirstLayout);
				landscapeFirstLayout.setTag(cEntity);
				landscapeFirstLayout.setOnClickListener(new ClickPop());
				landscapeFirstLayout.setLayoutParams(LayoutCaculateAdapter.getLinearWholeHeightLayout(screenWidth - 110, 5));
			}

			if ((position * COLUMN_NUM + 1) < size)
			{
				cEntity = list.get(position * COLUMN_NUM + 1);
				ImageView twoImg = (ImageView) view.findViewById(R.id.landscapeTwoImg);
				getThumbImage(cEntity,twoImg,R.id.landscapeTwoImg,position * COLUMN_NUM + 1);
				
				TextView tv1 = (TextView) view.findViewById(R.id.landscapeTwoTitle);
				tv1.setText(cEntity.getTitle());
				TextView tv2 = (TextView) view.findViewById(R.id.landscapeTwoTime);
				tv2.setText(TimeTools.getTimeByTimestap(Long.parseLong(cEntity.getTimestamp())));
				TextView tv3 = (TextView) view.findViewById(R.id.landscapeTwoContent);
				tv3.setText(cEntity.getDescription());
				twoImg.setLayoutParams(globalImageViewLayout);

				LinearLayout landscapeTwoLayout = (LinearLayout) view.findViewById(R.id.landscapeTwoLayout);
				landscapeTwoLayout.setTag(cEntity);
				landscapeTwoLayout.setOnClickListener(new ClickPop());
				View v1 = view.findViewById(R.id.landHorTwoLine);
				v1.setVisibility(View.VISIBLE);
				View v2 = view.findViewById(R.id.landVerTwoLine);
				v2.setVisibility(View.VISIBLE);
			}
			if ((position * COLUMN_NUM + 2) < size)
			{
				cEntity = list.get(position * COLUMN_NUM + 2);
				ImageView threeImg = (ImageView) view.findViewById(R.id.landscapeThreeImg);
				getThumbImage(cEntity,threeImg,R.id.landscapeThreeImg,position * COLUMN_NUM + 2);
				
				TextView tv1 = (TextView) view.findViewById(R.id.landscapeThreeTitle);
				tv1.setText(cEntity.getTitle());
				TextView tv2 = (TextView) view.findViewById(R.id.landscapeThreeTime);
				tv2.setText(TimeTools.getTimeByTimestap(Long.parseLong(cEntity.getTimestamp())));
				TextView tv3 = (TextView) view.findViewById(R.id.landscapeThreeContent);
				tv3.setText(cEntity.getDescription());
				threeImg.setLayoutParams(globalImageViewLayout);

				LinearLayout landscapeTextLayout = (LinearLayout) view.findViewById(R.id.landscapeTextLayout);
				landscapeTextLayout.setLayoutParams(globalImageViewLayout);
				LinearLayout landscapeThreeLayout = (LinearLayout) view.findViewById(R.id.landscapeThreeLayout);
				landscapeThreeLayout.setTag(cEntity);
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
			String url = cEntity.getProfile_path().substring(0,  cEntity.getProfile_path().length() - 4) + JiangFinalVariables.SQUARE_400;             
			
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
