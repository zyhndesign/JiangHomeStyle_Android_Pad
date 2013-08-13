package com.cidesign.jianghomestyle.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;


public class CommunityRelativeLayout extends RelativeLayout
{
	private ImageButton communityPreClick;
	private ImageButton communityNextClick;

	public CommunityRelativeLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public void setCommunityPreClick(ImageButton communityPreClick)
	{
		this.communityPreClick = communityPreClick;
	}

	public void setCommunityNextClick(ImageButton communityNextClick)
	{
		this.communityNextClick = communityNextClick;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev)
	{
		int action = ev.getAction();
		switch (action)
		{
		case MotionEvent.ACTION_DOWN:
			if (communityPreClick != null && communityNextClick != null)
			{
				communityPreClick.setVisibility(View.VISIBLE);
				communityPreClick.bringToFront();
				communityNextClick.setVisibility(View.VISIBLE);
			}			
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		case MotionEvent.ACTION_UP:
			if (communityPreClick != null && communityNextClick != null)
			{
				communityPreClick.setVisibility(View.INVISIBLE);
				communityNextClick.setVisibility(View.INVISIBLE);
			}			
			break;
		}
		return super.onInterceptTouchEvent(ev);
	}
}
