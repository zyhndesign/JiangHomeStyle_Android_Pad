package com.cidesign.jianghomestyle.util;

import android.content.res.Configuration;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class ScreenAdapterTools
{
	private static final String TAG = ScreenAdapterTools.class.getSimpleName();
	
	private static final float SCREEN_WIDTH_HEIGHT = 1280f;
	private static final float SCREEN_HEIGHT_WIDTH = 800f;

	// 横屏情况下的导航栏在1280*800下的高度
	private static final float NAV_LANSCAPE_BAR_HEIGHT = 48f;

	// 竖屏情况下的导航栏在1280*800下的高度
	private static final float NAV_PORTRAIT_BAR_HEIGHT = 68f;

	// landscape
	private static float HOME_IMAGE_LANDSCAPE_HEIGHT_RATIO = 720f / (SCREEN_HEIGHT_WIDTH - NAV_LANSCAPE_BAR_HEIGHT);

	private static float CATEGORY_IMAGE_LANDSCAPE_HEIGHT_RATIO = 720f / (SCREEN_HEIGHT_WIDTH - NAV_LANSCAPE_BAR_HEIGHT);
	
	private static float CATEGORY_PANEL_ANIMATION_HEIGHT_RATIO = 80f / (SCREEN_HEIGHT_WIDTH - NAV_LANSCAPE_BAR_HEIGHT);
	
	// portrait
	private static float HOME_IMAGE_PORTRAIT_HEIGHT_RATIO = 480f / (SCREEN_WIDTH_HEIGHT - NAV_PORTRAIT_BAR_HEIGHT);

	private static float POP_WIN_LANDSCAPE_HEIGHT_RATIO = 710f / (SCREEN_HEIGHT_WIDTH - NAV_LANSCAPE_BAR_HEIGHT) ;
	
	public static int getHomeImageHeight(int orientation,int screenHeight)
	{
		return (int)(HOME_IMAGE_LANDSCAPE_HEIGHT_RATIO * screenHeight);
	}
	
	public static int getPopWinHeight(int orientation,int screenHeight)
	{
		return (int)(POP_WIN_LANDSCAPE_HEIGHT_RATIO * screenHeight);
	}
	
	public static int getImageHeight(int orientation,int screenHeight)
	{		
		int result = 0;
		if (orientation == Configuration.ORIENTATION_LANDSCAPE) // 横屏
		{
			result = (int)(CATEGORY_IMAGE_LANDSCAPE_HEIGHT_RATIO * screenHeight);			
		}
		else if (orientation == Configuration.ORIENTATION_PORTRAIT) // 竖屏
		{
			result = (int)(HOME_IMAGE_PORTRAIT_HEIGHT_RATIO * screenHeight);
		}
		return result;
	}   
	
	public static int getAnimHeight(int orientation,int screenHeight)
	{		
		int result = 0;
		if (orientation == Configuration.ORIENTATION_LANDSCAPE) // 横屏
		{
			result = (int)(CATEGORY_PANEL_ANIMATION_HEIGHT_RATIO * screenHeight);			
		}
		else if (orientation == Configuration.ORIENTATION_PORTRAIT) // 竖屏
		{
			result = (int)(CATEGORY_PANEL_ANIMATION_HEIGHT_RATIO * screenHeight);
		}
		return result;
	}  
	
	public static void setStoryImageViewLayout(View view,int width)
	{
		LayoutParams para = view.getLayoutParams();		
		para.width = width;
		para.height = width;
		view.setLayoutParams(para);
	}
	
	public static void setStoryLayoutByOrientation(LinearLayout layout,int orientation,int width)
	{
		int view_width = 0;
		
		if (orientation == Configuration.ORIENTATION_LANDSCAPE) // 横屏
		{
			view_width = (width - 240) / 3;
		}
		else if (orientation == Configuration.ORIENTATION_PORTRAIT) // 竖屏
		{
			view_width = (width - 65) / 2;
		}
		Log.d(TAG,"StoryLayout Width:"+view_width);
		int count = layout.getChildCount();
		RelativeLayout realtive = null;
		ImageView imageView = null;
		for (int i = 0; i < count; i++)
		{
			realtive = (RelativeLayout)layout.getChildAt(i);			
			imageView = (ImageView)realtive.getChildAt(0);
			setStoryImageViewLayout(imageView,view_width);
		}
	}
	
	/**
	 * 适配首页的推荐栏目
	 * @param layout
	 * @param orientation
	 * @param width
	 */
	public static void setHeadLineLayout(LinearLayout layout,int orientation,int width)
	{
		int view_width = 0;
		
		if (orientation == Configuration.ORIENTATION_LANDSCAPE) // 横屏
		{
			view_width = (width - 400) / 3;
		}
		else if (orientation == Configuration.ORIENTATION_PORTRAIT) // 竖屏
		{
			view_width = (width - 150) / 3;
		}
				
		int count = layout.getChildCount();
		LinearLayout linearLayout = null;
		ImageView imageView = null;
		for (int i = 0; i < count; i++)
		{
			linearLayout = (LinearLayout)layout.getChildAt(i);
			imageView = (ImageView)linearLayout.getChildAt(0);
			setStoryImageViewLayout(imageView,view_width - 10);
		}
	}
}
