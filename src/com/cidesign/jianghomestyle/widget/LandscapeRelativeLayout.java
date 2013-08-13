package com.cidesign.jianghomestyle.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class LandscapeRelativeLayout extends RelativeLayout
{
	private ImageButton landscapePreClick;
	private ImageButton landscapeNextClick;
	
	public void setLandscapePreClick(ImageButton landscapePreClick)
	{
		this.landscapePreClick = landscapePreClick;
	}

	public void setLandscapeNextClick(ImageButton landscapeNextClick)
	{
		this.landscapeNextClick = landscapeNextClick;
	}

	public LandscapeRelativeLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev)
	{
		int action = ev.getAction();
		switch (action)
		{
		case MotionEvent.ACTION_DOWN:
			if (landscapePreClick != null && landscapeNextClick != null)
			{
				landscapePreClick.setVisibility(View.VISIBLE);
				landscapePreClick.bringToFront();
				landscapeNextClick.setVisibility(View.VISIBLE);
			}			
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		case MotionEvent.ACTION_UP:
			if (landscapePreClick != null && landscapeNextClick != null)
			{
				landscapePreClick.setVisibility(View.INVISIBLE);
				landscapeNextClick.setVisibility(View.INVISIBLE);
			}			
			break;
		}
		return super.onInterceptTouchEvent(ev);
	}
}
