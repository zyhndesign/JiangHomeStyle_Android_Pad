package com.cidesign.jianghomestyle.adapter;

import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.cidesign.jianghomestyle.entity.LayoutEntity;
import com.cidesign.jianghomestyle.entity.RelativeLayoutRulesEntity;
import com.cidesign.jianghomestyle.tools.LayoutMarginSetting;

public class LayoutCaculateAdapter
{
	public static LinearLayout.LayoutParams getLinearLayout(int screenWidth,int moiety)
	{
		int columnWidth = (screenWidth) / moiety;		
		LayoutEntity layoutEntity = new LayoutEntity();
		layoutEntity.setWidth(columnWidth);
		layoutEntity.setHeight(columnWidth);
		layoutEntity.setLeftMargin(5);
		layoutEntity.setRightMargin(5);
		layoutEntity.setTopMargin(5);
		layoutEntity.setBottomMargin(5);
		return LayoutMarginSetting.getLinearLayoutParams(layoutEntity);
	}
	
	public static LinearLayout.LayoutParams getLinearWholeHeightLayout(int screenWidth,int moiety)
	{
		int columnWidth = (screenWidth) / moiety;		
		LayoutEntity layoutEntity = new LayoutEntity();
		layoutEntity.setWidth(columnWidth);
		layoutEntity.setHeight(columnWidth * 2);
		layoutEntity.setLeftMargin(5);
		layoutEntity.setRightMargin(5);
		layoutEntity.setTopMargin(5);
		layoutEntity.setBottomMargin(5);
		return LayoutMarginSetting.getLinearLayoutParams(layoutEntity);
	}
	
	public static RelativeLayout.LayoutParams getRelativeLayout(int screenWidth,int moiety)
	{
		int columnWidth = (screenWidth - 75) / moiety;		
		LayoutEntity layoutEntity = new LayoutEntity();
		layoutEntity.setWidth(columnWidth * 2);
		layoutEntity.setHeight(columnWidth * 2);
		layoutEntity.setLeftMargin(5);
		layoutEntity.setRightMargin(5);
		layoutEntity.setTopMargin(10);
		layoutEntity.setBottomMargin(5);
		return LayoutMarginSetting.getRelativeLayoutParams(layoutEntity);
	}
	
	public static RelativeLayout.LayoutParams getBigRelativeLayoutOfParam(int screenWidth,int moiety,RelativeLayoutRulesEntity rulesEntity)
	{
		int columnWidth = (screenWidth - 70) / moiety;		
		LayoutEntity layoutEntity = new LayoutEntity();
		layoutEntity.setWidth(columnWidth * 2);
		layoutEntity.setHeight(columnWidth * 2);
		layoutEntity.setLeftMargin(5);
		layoutEntity.setRightMargin(5);
		layoutEntity.setTopMargin(10);
		layoutEntity.setBottomMargin(5);
		return LayoutMarginSetting.getRelativeLayoutDetailParams(layoutEntity,rulesEntity);
	}
	
	public static RelativeLayout.LayoutParams getRelativeLayoutOfParam(int screenWidth,int moiety,RelativeLayoutRulesEntity rulesEntity)
	{
		int columnWidth = (screenWidth - 70) / moiety;		
		LayoutEntity layoutEntity = new LayoutEntity();
		layoutEntity.setWidth(columnWidth - 8);
		layoutEntity.setHeight(columnWidth - 8);
		layoutEntity.setLeftMargin(5);
		layoutEntity.setRightMargin(5);
		layoutEntity.setTopMargin(10);
		layoutEntity.setBottomMargin(5);
		return LayoutMarginSetting.getRelativeLayoutDetailParams(layoutEntity,rulesEntity);
	}
	
	public static RelativeLayout.LayoutParams getSmallBottomRelativeLayoutOfParam(int screenWidth,int moiety,RelativeLayoutRulesEntity rulesEntity)
	{
		int columnWidth = (screenWidth - 56) / moiety;		
		LayoutEntity layoutEntity = new LayoutEntity();
		layoutEntity.setWidth(columnWidth - 8);
		layoutEntity.setHeight(columnWidth - 8);
		layoutEntity.setLeftMargin(5);
		layoutEntity.setRightMargin(5);
		layoutEntity.setTopMargin(10);
		layoutEntity.setBottomMargin(5);
		return LayoutMarginSetting.getRelativeLayoutDetailParams(layoutEntity,rulesEntity);
	}
	
	public static RelativeLayout.LayoutParams getSmallTopRelativeLayoutOfParam(int screenWidth,int moiety,RelativeLayoutRulesEntity rulesEntity)
	{
		int columnWidth = (screenWidth - 56) / moiety;		
		LayoutEntity layoutEntity = new LayoutEntity();
		layoutEntity.setWidth(columnWidth - 8);
		layoutEntity.setHeight(columnWidth - 8);
		layoutEntity.setLeftMargin(5);
		layoutEntity.setRightMargin(5);
		layoutEntity.setTopMargin(5);
		layoutEntity.setBottomMargin(0);
		return LayoutMarginSetting.getRelativeLayoutDetailParams(layoutEntity,rulesEntity);
	}
	
	public static RelativeLayout.LayoutParams getViewPagerRelativeLayout(int screenWidth,int moiety)
	{
		int columnWidth = (screenWidth) / moiety;		
		LayoutEntity layoutEntity = new LayoutEntity();
		layoutEntity.setWidth(screenWidth);
		layoutEntity.setHeight(columnWidth * 2);
		return LayoutMarginSetting.getRelativeLayoutParams(layoutEntity);
	}
}
