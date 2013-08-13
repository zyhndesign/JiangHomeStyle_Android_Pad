package com.cidesign.jianghomestyle.tools;

import java.util.Queue;

import com.cidesign.jianghomestyle.entity.AnimationEntity;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.LinearLayout;

public class AnimationTools
{
	private static final String TAG = AnimationTools.class.getSimpleName();
	/**
	 * use the translate animation to move the view up and down through y axis
	 * 
	 * @param view
	 *            : the move view object
	 * @param animation_flag
	 *            : up or down flag
	 * @param y_axis_value
	 *            : move distance
	 */
	public static void animiationYPanel(final View view, final int animation_flag, final int y_axis_value)
	{

		AnimationSet animationSet = new AnimationSet(true);
		int top_y = 0;
		if (animation_flag == 0)
		{
			top_y = (int) view.getPivotY() - y_axis_value;
		}
		else
		{
			top_y = (int) view.getPivotY() + y_axis_value;
		}
		// final int position_y = top_y;

		TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, top_y);

		translateAnimation.setAnimationListener(new AnimationListener()
		{

			@Override
			public void onAnimationEnd(Animation animation)
			{
				view.clearAnimation();

				// view.layout(view.getLeft(), position_y, view.getRight(),view.getBottom());
				// ViewGroup.LayoutParams params =
				// im_panel_layout.getLayoutParams();
				LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) view.getLayoutParams();
				int marginRight = lp.rightMargin;

				if (animation_flag == 0)
				{
					lp.setMargins(lp.leftMargin, 0, marginRight, 0);
				}
				else
				{
					lp.setMargins(lp.leftMargin, y_axis_value, marginRight, 0);
				}
				view.setLayoutParams(lp);

				TranslateAnimation anim = new TranslateAnimation(0, 0, 0, 0);
				view.setAnimation(anim);
			}

			@Override
			public void onAnimationRepeat(Animation animation)
			{

			}

			@Override
			public void onAnimationStart(Animation animation)
			{

			}

		});
		// translateAnimation.setInterpolator(new
		// AccelerateDecelerateInterpolator());
		translateAnimation.setDuration(1000);
		// translateAnimation.setFillEnabled(true);
		// translateAnimation.setFillAfter(true);
		animationSet.addAnimation(translateAnimation);
		// animationSet.startNow();
		view.startAnimation(animationSet);
	}

	public static void animationMoveTask(final Queue<AnimationEntity> queue)
	{
		if (queue != null && queue.size() > 0)
		{
			TranslateAnimation translateAnimation = null;
			final AnimationEntity aEntity = queue.poll();
			final View view = aEntity.getView();
			
			final int animation_flag = aEntity.getMove_direction();
			AnimationSet animationSet = new AnimationSet(true);
			final int init_top = (int) view.getTop();
			final int init_bottom = (int)view.getBottom();

			if (animation_flag == 0)
			{							
				translateAnimation = new TranslateAnimation(
						0,
						0,
						view.getTop(),
						view.getTop() - aEntity.getMove_distance());
			}
			else
			{				
				translateAnimation = new TranslateAnimation(
						0,
						0,
						view.getTop(),
						view.getTop() + aEntity.getMove_distance() - 2);
			}
			final int distance = aEntity.getMove_distance();
			
			translateAnimation.setAnimationListener(new AnimationListener()
			{

				@Override
				public void onAnimationEnd(Animation animation)
				{
					view.clearAnimation();
										
					if (animation_flag == 0)
					{
						view.layout(view.getLeft(), init_top - distance, view.getRight(),init_bottom - distance );
					}
					else
					{						
						view.layout(view.getLeft(), init_top + distance, view.getRight(),init_bottom + distance);
					}
					
					TranslateAnimation anim = new TranslateAnimation(0, 0, 0, 0);
					view.setAnimation(anim);
					animationMoveTask(queue);
					
				}

				@Override
				public void onAnimationRepeat(Animation animation)
				{

				}

				@Override
				public void onAnimationStart(Animation animation)
				{
					Log.d(TAG, "animation start..."+animation.getDuration());
				}

			});
			// translateAnimation.setInterpolator(new
			// AccelerateDecelerateInterpolator());
			translateAnimation.setDuration(200);
			//translateAnimation.setFillEnabled(true);
			//translateAnimation.setFillAfter(true);
			animationSet.addAnimation(translateAnimation);
			// animationSet.startNow();
			view.startAnimation(animationSet);
		}
	}

	/**
	 * 
	 * @param view
	 * @param view_size
	 * @param screen_size
	 * @param animation_flag
	 */
	public static void animationRightLeft(final View parent_panel,final View view, final int view_size, final int screen_size, final int animation_flag)
	{
		AnimationSet animationSet = new AnimationSet(true);
		int top_x = 0;
		TranslateAnimation translateAnimation = null;
		if (animation_flag == 0)
		{
			top_x = view_size;
			translateAnimation = new TranslateAnimation(screen_size, view_size, 0, 0);
		}
		else
		{
			top_x = screen_size - view_size;
			translateAnimation = new TranslateAnimation(0, top_x, 0, 0);
		}
		// final int position_y = top_y;

		translateAnimation.setAnimationListener(new AnimationListener()
		{

			@Override
			public void onAnimationEnd(Animation animation)
			{
				view.clearAnimation();
//				if (animation_flag == 1)
//				{
//					parent_panel.setVisibility(View.INVISIBLE);
//				}
//				
				/*
				if (animation_flag == 0)
				{
					view.layout(screen_size - view_size, 100, screen_size,view.getBottom());
				}
				else
				{
					view.layout(screen_size, 100, screen_size + view_size,view.getBottom());
				}
				*/
				TranslateAnimation anim = new TranslateAnimation(0, 0, 0, 0);
				view.setAnimation(anim);
			}

			@Override
			public void onAnimationRepeat(Animation animation)
			{

			}

			@Override
			public void onAnimationStart(Animation animation)
			{

			}

		});
		// translateAnimation.setInterpolator(new
		// AccelerateDecelerateInterpolator());
		translateAnimation.setDuration(500);
		// translateAnimation.setFillEnabled(true);
		// translateAnimation.setFillAfter(true);
		animationSet.addAnimation(translateAnimation);
		// animationSet.startNow();
		view.startAnimation(animationSet);
	}
	
	
}
