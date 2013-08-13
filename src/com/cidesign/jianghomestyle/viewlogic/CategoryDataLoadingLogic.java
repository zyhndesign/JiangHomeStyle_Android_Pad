package com.cidesign.jianghomestyle.viewlogic;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cidesign.jianghomestyle.entity.ArticleEntity;
import com.cidesign.jianghomestyle.entity.LayoutEntity;
import com.cidesign.jianghomestyle.tools.LayoutMarginSetting;
import com.cidesign.jianghomestyle.tools.LoadingImageTools;
import com.cidesign.jianghomestyle.tools.TimeTools;
import com.cidesign.jianghomestyle.util.StorageUtils;
import com.cidesign.jianghomestylerelease.R;

public class CategoryDataLoadingLogic
{
	private static LoadingImageTools loadingImg = new LoadingImageTools();

	/**
	 * 根据数据布局头条面板控件
	 * 
	 * @param articleList
	 * @param headlineLayout
	 * @param screen_width
	 * @param inflater
	 */
	public static void loadHeadLineData(List<ArticleEntity> articleList, LinearLayout headlineLayout, int screen_width,
			LayoutInflater inflater)
	{

		headlineLayout.removeAllViews();
		ArticleEntity aEntity = null;
		int view_width = (screen_width - 400) / 3;
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.leftMargin = 15;
		lp.rightMargin = 15;
		lp.width = view_width;
		
		LayoutEntity layoutEntity = new LayoutEntity();
		layoutEntity.setWidth(view_width - 30);
		layoutEntity.setHeight(view_width - 30);
		layoutEntity.setLeftMargin(15);
		layoutEntity.setRightMargin(15);
		layoutEntity.setTopMargin(15);
		layoutEntity.setBottomMargin(15);
		LinearLayout.LayoutParams imageViewLayout = LayoutMarginSetting.getLinearLayoutParams(layoutEntity);
		
		for (int i = 1; i < articleList.size(); i++)
		{
			View view = inflater.inflate(R.layout.headline_content, null);
			
			view.setLayoutParams(lp);

			aEntity = articleList.get(i);
			ImageView iv = (ImageView) view.findViewById(R.id.headLineThumb);
			loadingImg.loadingImage(iv, StorageUtils.FILE_ROOT + aEntity.getServerID() + "/" + aEntity.getProfile_path());

			iv.setLayoutParams(imageViewLayout);
			TextView tv1 = (TextView) view.findViewById(R.id.headLineTitle);
			tv1.setText(aEntity.getTitle());
			TextView tv2 = (TextView) view.findViewById(R.id.headLineTime);
			tv2.setText(TimeTools.getTimeByTimestap(Long.parseLong(aEntity.getPost_date())));
			view.setTag(aEntity);
			headlineLayout.addView(view);

		}
	}
}
