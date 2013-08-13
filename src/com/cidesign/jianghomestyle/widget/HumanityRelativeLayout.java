package com.cidesign.jianghomestyle.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class HumanityRelativeLayout extends RelativeLayout
{
	private ImageButton humanityPreClick;
	private ImageButton humanityNextClick;
	
	public HumanityRelativeLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public void setHumanityPreClick(ImageButton humanityPreClick)
	{
		this.humanityPreClick = humanityPreClick;
	}

	public void setHumanityNextClick(ImageButton humanityNextClick)
	{
		this.humanityNextClick = humanityNextClick;
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev)
	{
		int action = ev.getAction();
		switch (action)
		{
		case MotionEvent.ACTION_DOWN:
			if (humanityPreClick != null && humanityNextClick != null)
			{
				humanityPreClick.setVisibility(View.VISIBLE);
				humanityPreClick.bringToFront();
				humanityNextClick.setVisibility(View.VISIBLE);
			}			
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		case MotionEvent.ACTION_UP:
			if (humanityPreClick != null && humanityNextClick != null)
			{
				humanityPreClick.setVisibility(View.INVISIBLE);
				humanityNextClick.setVisibility(View.INVISIBLE);
			}			
			break;
		}
		return super.onInterceptTouchEvent(ev);
	}
	
}
