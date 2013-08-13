package com.cidesign.jianghomestyle.viewlogic;

import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;

public class HScrollViewTouchLogic implements View.OnTouchListener
{
	private static final String TAG = HScrollViewTouchLogic.class.getSimpleName();
	
	private ImageButton leftBtn;
	private ImageButton rightBtn;

	private boolean leftBtnVisiable = false;
	private boolean rightBtnVisiable = false;
	private HorizontalScrollView scrollView;
	
	public HScrollViewTouchLogic(ImageButton leftBtn, ImageButton rightBtn,HorizontalScrollView scrollView)
	{
		this.leftBtn = leftBtn;
		this.rightBtn = rightBtn;
		this.scrollView = scrollView;
	}

	public boolean isLeftBtnVisiable()
	{
		return leftBtnVisiable;
	}

	public void setLeftBtnVisiable(boolean leftBtnVisiable)
	{
		this.leftBtnVisiable = leftBtnVisiable;
	}

	public boolean isRightBtnVisiable()
	{
		return rightBtnVisiable;
	}

	public void setRightBtnVisiable(boolean rightBtnVisiable)
	{
		this.rightBtnVisiable = rightBtnVisiable;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
		if (event.getAction() == MotionEvent.ACTION_DOWN)
		{
			View view = (View) scrollView.getChildAt(scrollView.getChildCount() - 1);
			int subViewWidth = view.getRight();
			int x = scrollView.getScrollX();
			if (subViewWidth - x - scrollView.getWidth() == 0)
			{
				rightBtn.setVisibility(View.INVISIBLE);
				leftBtn.bringToFront();
				leftBtn.setVisibility(View.VISIBLE);
			}
			else
			{
				rightBtn.setVisibility(View.VISIBLE);
			}
			
			if (x > 100)
			{
				leftBtn.setVisibility(View.VISIBLE);
			}
		}
		else if (event.getAction() == MotionEvent.ACTION_UP)
		{
			leftBtn.setVisibility(View.INVISIBLE);
			rightBtn.setVisibility(View.INVISIBLE);
		}
		
		return false;
	}
}
