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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

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
/**
 * 
* @Title: CommunityViewpagerAdapter.java 
* @Package com.cidesign.jianghomestyle.adapter 
* @Description: community model data adapter 
* @author liling  
* @date 2013年8月14日 下午1:49:48 
* @version V2.0
 */
public class CommunityViewpagerAdapter extends PagerAdapter
{
	private static final String TAG = LandscapeViewpagerAdapter.class.getSimpleName();
	
	private static final int COLUMN_NUM = 3;
	
	private HashMap<Integer,Bitmap> bitHashMap = new HashMap<Integer,Bitmap>();
	
	private List<ContentEntity> list = new ArrayList<ContentEntity>();
		
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
		LinearLayout.LayoutParams globalImageViewLayout = LayoutCaculateAdapter.getLinearLayout(screenWidth,7);
		LinearLayout.LayoutParams wholeHeightViewLayout = LayoutCaculateAdapter.getLinearWholeHeightLayout(screenWidth - 200, 6);
		
		LinearLayout linearLayout = new LinearLayout(activity.getApplicationContext());
		linearLayout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		linearLayout.setOrientation(LinearLayout.HORIZONTAL);
		
		LayoutInflater inflater = LayoutInflater.from(activity.getApplicationContext());
		ContentEntity cEntity = null;
			
		Bitmap bitMap = null;
		
		int size = list.size();
		
		try
		{
			if (position * COLUMN_NUM < size)
			{
				View view = inflater.inflate(R.layout.community_template, null);
				aq = new AQuery(view);
				cEntity = list.get(position * COLUMN_NUM);
				ImageView communityBgImg = (ImageView) activity.findViewById(R.id.communityBgImg);
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
						bitMap = LoadingImageTools.loadingNative(activity.getApplicationContext(), "bg3.jpg");
					}	
				}
				else
				{
					bitMap = LoadingImageTools.loadingNative(activity.getApplicationContext(),"bg3.jpg");				
				}
				if (bitMap != null)
				{
					communityBgImg.setImageBitmap(bitMap);				
				}
				
				ImageView firstImg = (ImageView) view.findViewById(R.id.communityImg);				
				getThumbImage(cEntity,firstImg,R.id.communityImg,position * COLUMN_NUM);
				firstImg.setLayoutParams(globalImageViewLayout);

				TextView tv1 = (TextView) view.findViewById(R.id.communityTitle);
				tv1.setText(cEntity.getTitle());
				TextView tv2 = (TextView) view.findViewById(R.id.communityTime);
				tv2.setText(TimeTools.getTimeByTimestap(Long.parseLong(cEntity.getTimestamp())));
				TextView tv3 = (TextView) view.findViewById(R.id.communityContent);
				tv3.setText(cEntity.getDescription());
				tv3.setLayoutParams(wholeHeightViewLayout);

				LinearLayout communityLayout = (LinearLayout) view.findViewById(R.id.communityLayout);
				communityLayout.setTag(cEntity);
				communityLayout.setVisibility(View.VISIBLE);
				communityLayout.setOnClickListener(new ClickPop());
				linearLayout.addView(view);
			}

			if ((position * COLUMN_NUM + 1) < size)
			{
				View view = inflater.inflate(R.layout.community_template, null);
				aq = new AQuery(view);
				cEntity = list.get(position * COLUMN_NUM + 1);
				ImageView firstImg = (ImageView) view.findViewById(R.id.communityImg);
				
				getThumbImage(cEntity,firstImg,R.id.communityImg,position * COLUMN_NUM + 1);
				firstImg.setLayoutParams(globalImageViewLayout);

				TextView tv1 = (TextView) view.findViewById(R.id.communityTitle);
				tv1.setText(cEntity.getTitle());
				TextView tv2 = (TextView) view.findViewById(R.id.communityTime);
				tv2.setText(TimeTools.getTimeByTimestap(Long.parseLong(cEntity.getTimestamp())));
				TextView tv3 = (TextView) view.findViewById(R.id.communityContent);
				tv3.setText(cEntity.getDescription());
				tv3.setLayoutParams(wholeHeightViewLayout);

				LinearLayout communityLayout = (LinearLayout) view.findViewById(R.id.communityLayout);
				communityLayout.setTag(cEntity);
				communityLayout.setVisibility(View.VISIBLE);
				communityLayout.setOnClickListener(new ClickPop());
				linearLayout.addView(view);
			}

			if ((position * COLUMN_NUM + 2) < size)
			{
				View view = inflater.inflate(R.layout.community_template, null);
				aq = new AQuery(view);
				cEntity = list.get(position * COLUMN_NUM + 2);
				ImageView firstImg = (ImageView) view.findViewById(R.id.communityImg);				
				getThumbImage(cEntity,firstImg,R.id.communityImg,position * COLUMN_NUM + 2);
				firstImg.setLayoutParams(globalImageViewLayout);

				TextView tv1 = (TextView) view.findViewById(R.id.communityTitle);
				tv1.setText(cEntity.getTitle());
				TextView tv2 = (TextView) view.findViewById(R.id.communityTime);
				tv2.setText(TimeTools.getTimeByTimestap(Long.parseLong(cEntity.getTimestamp())));
				TextView tv3 = (TextView) view.findViewById(R.id.communityContent);
				tv3.setText(cEntity.getDescription());
				tv3.setLayoutParams(wholeHeightViewLayout);

				LinearLayout communityLayout = (LinearLayout) view.findViewById(R.id.communityLayout);
				communityLayout.setTag(cEntity);
				communityLayout.setVisibility(View.VISIBLE);
				communityLayout.setOnClickListener(new ClickPop());
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
			Log.d(TAG, url);
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