package com.cidesign.jianghomestyle.viewlogic;

import java.io.File;
import java.util.List;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.cidesign.jianghomestyle.R;
import com.cidesign.jianghomestyle.entity.ContentEntity;
import com.cidesign.jianghomestyle.entity.LayoutEntity;
import com.cidesign.jianghomestyle.tools.LayoutMarginSetting;
import com.cidesign.jianghomestyle.tools.LoadingImageTools;
import com.cidesign.jianghomestyle.tools.TimeTools;
import com.cidesign.jianghomestyle.util.JiangFinalVariables;
import com.cidesign.jianghomestyle.util.StorageUtils;
/**
 * 
* @Title: CategoryDataLoadingLogic.java 
* @Package com.cidesign.jianghomestyle.viewlogic 
* @Description: Head data layout
* @author liling  
* @date 2013年8月14日 下午1:37:09 
* @version V2.0
 */
public class CategoryDataLoadingLogic
{	
	private AQuery aq = null;
	
	/**
	 * 根据数据布局头条面板控件
	 * @param articleList
	 * @param headlineLayout
	 * @param screen_width
	 * @param inflater
	 */
	public void loadHeadLineData(List<ContentEntity> articleList, LinearLayout headlineLayout, int screen_width,
			LayoutInflater inflater)
	{

		headlineLayout.removeAllViews();
		ContentEntity cEntity = null;
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

			aq = new AQuery(view);
			
			cEntity = articleList.get(i);
			String filePathDir = StorageUtils.THUMB_IMG_ROOT + cEntity.getServerID() + "/";
			File fileDir = new File(filePathDir);
			if (!fileDir.exists() || !fileDir.isDirectory())
				fileDir.mkdir();
			String fileName = filePathDir + cEntity.getServerID()+".jpg";
			File file = new File(fileName);
			if (file.exists())
			{
				aq.id(R.id.headLineThumb).image(file,400);
			}
			else
			{
				String url = cEntity.getProfile_path().substring(0,  cEntity.getProfile_path().length() - 4) + JiangFinalVariables.SQUARE_400;            
				
				File target = new File(fileDir, cEntity.getServerID()+".jpg");              

				aq.download(url, target, new AjaxCallback<File>(){
				        
				        public void callback(String url, File file, AjaxStatus status) {
				                
				        	aq.id(R.id.headLineThumb).image(file,400); 
				        }
				        
				});
			}
			view.findViewById(R.id.headLineThumb).setLayoutParams(imageViewLayout);
			TextView tv1 = (TextView) view.findViewById(R.id.headLineTitle);
			tv1.setText(cEntity.getTitle());
			TextView tv2 = (TextView) view.findViewById(R.id.headLineTime);
			tv2.setText(TimeTools.getTimeByTimestap(Long.parseLong(cEntity.getTimestamp())));
			view.setTag(cEntity);
			headlineLayout.addView(view);

		}
	}
}
