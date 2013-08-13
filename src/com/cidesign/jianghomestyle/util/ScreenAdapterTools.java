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

	// ��������µĵ�������1280*800�µĸ߶�
	private static final float NAV_LANSCAPE_BAR_HEIGHT = 48f;

	// ��������µĵ�������1280*800�µĸ߶�
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
		if (orientation == Configuration.ORIENTATION_LANDSCAPE) // ����
		{
			result = (int)(CATEGORY_IMAGE_LANDSCAPE_HEIGHT_RATIO * screenHeight);			
		}
		else if (orientation == Configuration.ORIENTATION_PORTRAIT) // ����
		{
			result = (int)(HOME_IMAGE_PORTRAIT_HEIGHT_RATIO * screenHeight);
		}
		return result;
	}   
	
	public static int getAnimHeight(int orientation,int screenHeight)
	{		
		int result = 0;
		if (orientation == Configuration.ORIENTATION_LANDSCAPE) // ����
		{
			result = (int)(CATEGORY_PANEL_ANIMATION_HEIGHT_RATIO * screenHeight);			
		}
		else if (orientation == Configuration.ORIENTATION_PORTRAIT) // ����
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
		
		if (orientation == Configuration.ORIENTATION_LANDSCAPE) // ����
		{
			view_width = (width - 240) / 3;
		}
		else if (orientation == Configuration.ORIENTATION_PORTRAIT) // ����
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
	 * ������ҳ���Ƽ���Ŀ
	 * @param layout
	 * @param orientation
	 * @param width
	 */
	public static void setHeadLineLayout(LinearLayout layout,int orientation,int width)
	{
		int view_width = 0;
		
		if (orientation == Configuration.ORIENTATION_LANDSCAPE) // ����
		{
			view_width = (width - 400) / 3;
		}
		else if (orientation == Configuration.ORIENTATION_PORTRAIT) // ����
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
