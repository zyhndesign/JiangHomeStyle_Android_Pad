package com.cidesign.jianghomestyle.tools;

import com.cidesign.jianghomestyle.entity.LayoutEntity;
import com.cidesign.jianghomestyle.entity.RelativeLayoutRulesEntity;

import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class LayoutMarginSetting
{
	
	/**
	 * 
	 * @param layoutEntity
	 * @return
	 */
	public static LinearLayout.LayoutParams getLinearLayoutParams(LayoutEntity layoutEntity)
	{
		LinearLayout.LayoutParams globalImageViewLayout = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		globalImageViewLayout.width = layoutEntity.getWidth();
		globalImageViewLayout.height = layoutEntity.getHeight();
		globalImageViewLayout.leftMargin = layoutEntity.getLeftMargin();
		globalImageViewLayout.rightMargin = layoutEntity.getRightMargin();
		globalImageViewLayout.topMargin = layoutEntity.getTopMargin();
		globalImageViewLayout.bottomMargin = layoutEntity.getBottomMargin();
		return globalImageViewLayout;
	}
	
	/**
	 * 
	 * @param layoutEntity
	 * @return
	 */
	public static RelativeLayout.LayoutParams getRelativeLayoutParams(LayoutEntity layoutEntity)
	{
		RelativeLayout.LayoutParams bigImageViewLayout = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		bigImageViewLayout.width = layoutEntity.getWidth();
		bigImageViewLayout.height = layoutEntity.getHeight();
		bigImageViewLayout.leftMargin = layoutEntity.getLeftMargin();
		bigImageViewLayout.rightMargin = layoutEntity.getRightMargin();
		bigImageViewLayout.topMargin = layoutEntity.getTopMargin();
		bigImageViewLayout.bottomMargin = layoutEntity.getBottomMargin();		
		return bigImageViewLayout;
	}
	
	/**
	 * 
	 * @param layoutEntity
	 * @param ruleENtity
	 * @return
	 */
	public static RelativeLayout.LayoutParams getRelativeLayoutDetailParams(LayoutEntity layoutEntity,RelativeLayoutRulesEntity ruleENtity)
	{
		RelativeLayout.LayoutParams bigImageViewLayout = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		bigImageViewLayout.width = layoutEntity.getWidth();
		bigImageViewLayout.height = layoutEntity.getHeight();
		bigImageViewLayout.leftMargin = layoutEntity.getLeftMargin();
		bigImageViewLayout.rightMargin = layoutEntity.getRightMargin();
		bigImageViewLayout.topMargin = layoutEntity.getTopMargin();
		bigImageViewLayout.bottomMargin = layoutEntity.getBottomMargin();
		if (ruleENtity.getBelowOfValue() != 0)
		{
			bigImageViewLayout.addRule(RelativeLayout.BELOW,ruleENtity.getBelowOfValue());
		}
		
		if (ruleENtity.getRightOfVlaue() != 0)
		{
			bigImageViewLayout.addRule(RelativeLayout.RIGHT_OF,ruleENtity.getRightOfVlaue());
		}
		
		if (ruleENtity.getLeftOfValue() != 0)
		{
			bigImageViewLayout.addRule(RelativeLayout.LEFT_OF,ruleENtity.getRightOfVlaue());
		}
		
		return bigImageViewLayout;
	}
}
