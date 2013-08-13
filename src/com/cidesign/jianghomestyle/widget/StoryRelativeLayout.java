package com.cidesign.jianghomestyle.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class StoryRelativeLayout extends RelativeLayout
{
	private ImageButton storyPreClick;
	private ImageButton storyNextClick;

	public StoryRelativeLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public void setStoryPreClick(ImageButton storyPreClick)
	{
		this.storyPreClick = storyPreClick;
	}

	public void setStoryNextClick(ImageButton storyNextClick)
	{
		this.storyNextClick = storyNextClick;
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev)
	{
		int action = ev.getAction();
		switch (action)
		{
		case MotionEvent.ACTION_DOWN:
			if (storyPreClick != null && storyNextClick != null)
			{
				storyPreClick.setVisibility(View.VISIBLE);
				storyPreClick.bringToFront();
				storyNextClick.setVisibility(View.VISIBLE);
			}			
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		case MotionEvent.ACTION_UP:
			if (storyPreClick != null && storyNextClick != null)
			{
				storyPreClick.setVisibility(View.INVISIBLE);
				storyNextClick.setVisibility(View.INVISIBLE);
			}			
			break;
		}
		return super.onInterceptTouchEvent(ev);
	}
}
