package com.cidesign.jianghomestyle.tools;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class LayoutTools
{
	public static final int LINEARLAYOUT_MARGIN_LEFT = 0;
	public static final int LINEARLAYOUT_MARGIN_TOP = 1;
	public static final int LINEARLAYOUT_MARGIN_RIGHT = 2;
	public static final int LINEARLAYOUT_MARGIN_BOTTOM = 3;
	public static final int LINEARLAYOUT_WIDTH = 4;
	public static final int LINEARLAYOUT_HEIGHT = 5;
	
	public static final int RELATIVELAYOUT_MARGIN_LEFT = 0;
	public static final int RELATIVELAYOUT_MARGIN_TOP = 1;
	public static final int RELATIVELAYOUT_MARGIN_RIGHT = 2;
	public static final int RELATIVELAYOUT_MARGIN_BOTTOM = 3;
	public static final int RELATIVELAYOUT_WIDTH = 4;
	public static final int RELATIVELAYOUT_HEIGHT = 5;
	
	public static int getLinearViewProperty(View view,int property_type)
	{
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)view.getLayoutParams();
		if (property_type == LINEARLAYOUT_MARGIN_LEFT)
		{
			return lp.leftMargin;
		}
		else if (property_type == LINEARLAYOUT_MARGIN_TOP)
		{
			return lp.topMargin;
		}
		else if (property_type == LINEARLAYOUT_MARGIN_RIGHT)
		{
			return lp.rightMargin;
		}
		else if (property_type == LINEARLAYOUT_MARGIN_BOTTOM)
		{
			return lp.bottomMargin;
		}
		else if (property_type == LINEARLAYOUT_WIDTH)
		{
			return lp.width;
		}
		else if (property_type == LINEARLAYOUT_HEIGHT)
		{
			return lp.height;
		}
		return 0;
	}
	
	public static int getRelativeViewProperty(View view,int property_type)
	{
		RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)view.getLayoutParams();
		if (property_type == RELATIVELAYOUT_MARGIN_LEFT)
		{
			return lp.leftMargin;
		}
		else if (property_type == RELATIVELAYOUT_MARGIN_TOP)
		{
			return lp.topMargin;
		}
		else if (property_type == RELATIVELAYOUT_MARGIN_RIGHT)
		{
			return lp.rightMargin;
		}
		else if (property_type == RELATIVELAYOUT_MARGIN_BOTTOM)
		{
			return lp.bottomMargin;
		}
		else if (property_type == RELATIVELAYOUT_WIDTH)
		{
			return lp.width;
		}
		else if (property_type == RELATIVELAYOUT_HEIGHT)
		{
			return lp.height;
		}
		return 0;
	}
	
	public static void setLinearLayoutViewProperty(View view, int height, int width, int leftMargin, int rightMargin,int topMargin)
	{
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)view.getLayoutParams();
		lp.width = width;
		lp.height = height;
		lp.leftMargin = leftMargin;
		lp.rightMargin = rightMargin;
		lp.topMargin = topMargin;
		view.setLayoutParams(lp);
	}
	
	public static void setRelativeLayoutViewProperty(View view, int height, int width, int leftMargin, int rightMargin,int topMargin)
	{
		RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)view.getLayoutParams();
		lp.width = width;
		lp.height = height;
		lp.leftMargin = leftMargin;
		lp.rightMargin = rightMargin;
		lp.topMargin = topMargin;
		view.setLayoutParams(lp);
	}
	
	public static void setFrameLayoutViewProperty(View view, int height, int width, int leftMargin, int rightMargin,int topMargin)
	{
		FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams)view.getLayoutParams();
		lp.width = width;
		lp.height = height;
		lp.leftMargin = leftMargin;
		lp.rightMargin = rightMargin;
		lp.topMargin = topMargin;
		view.setLayoutParams(lp);
	}
}
