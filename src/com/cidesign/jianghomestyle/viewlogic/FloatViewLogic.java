package com.cidesign.jianghomestyle.viewlogic;

import com.cidesign.jianghomestyle.entity.ArticleEntity;
import com.cidesign.jianghomestyle.util.StorageUtils;
import com.cidesign.jianghomestylerelease.R;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ProgressBar;

public class FloatViewLogic
{
	private static final String TAG = FloatViewLogic.class.getSimpleName();

	private Activity activity;

	private View view; // top view

	private View floatView;
	private PopupWindow pw = null;
	private WebView detail_webview = null;
	private View overLayer;

	private int screen_width;
	private int screen_height;

	private WindowManager wm = null;
	private WindowManager.LayoutParams wmParams = null;

	private ProgressBar webLoadingProgressBar = null;

	public FloatViewLogic(Activity activity, View view, int screen_width, int screen_height)
	{
		this.activity = activity;
		this.view = view;
		this.screen_height = screen_height;
		this.screen_width = screen_width;
	}

	public WindowManager createView()
	{
		// ��ȡWindowManager
		wm = (WindowManager) activity.getApplicationContext().getSystemService("window");
		// ����LayoutParams(ȫ�ֱ�������ز���
		wmParams = new WindowManager.LayoutParams();
		wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;// �������ṩ���û���������������Ӧ�ó����Ϸ���������״̬������
		wmParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_NOT_FOCUSABLE;

		wmParams.gravity = Gravity.TOP; // �����������������Ͻ�
		// ����Ļ���Ͻ�Ϊԭ�㣬����x��y��ʼֵ
		wmParams.x = 0;
		wmParams.y = 0;
		// �����������ڳ�������
		wmParams.width = WindowManager.LayoutParams.MATCH_PARENT;
		wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		wmParams.format = PixelFormat.RGBA_8888;
		// ��ʼ�����ֲ����б�

		wm.addView(view, wmParams);
		return wm;
	}

	/**
	 * ������������ĸ������
	 * 
	 * @param view
	 * @param templateName
	 */
	public void createFloatWin(final View v, String templateName)
	{
		overLayer = activity.findViewById(R.id.overLayer);
		overLayer.getBackground().setAlpha(160);
		overLayer.setVisibility(View.VISIBLE);
		LayoutInflater inflater = LayoutInflater.from(activity);

		floatView = inflater.inflate(R.layout.template1, null);

		pw = new PopupWindow(floatView, screen_width, screen_height);

		pw.setAnimationStyle(R.style.mydialog);

		pw.setFocusable(true);

		pw.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.black));

		ImageView iv = (ImageView) floatView.findViewById(R.id.backToMainPanel);
		iv.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				if (pw != null)
				{
					if (detail_webview.canGoBack())
					{
						detail_webview.goBack();
					}
					else
					{
						if (pw.isShowing())
						{
							pw.dismiss();
						}
					}
				}
			}
		});

		pw.setOnDismissListener(new OnDismissListener()// ��ʧ����
		{
			public void onDismiss()
			{
				overLayer.setVisibility(View.INVISIBLE);
			}

		});
		detail_webview = (WebView) floatView.findViewById(R.id.detail_webview);

		detail_webview.setHorizontalScrollBarEnabled(true);
		WebSettings ws = detail_webview.getSettings();
		// ws.setBuiltInZoomControls(true); // ������ʾ���Ű�ť
		// ws.setSupportZoom(true); // ֧������
		// ws.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
		// ws.setDefaultTextEncodingName("utf-8"); // �����ı�����

		floatView.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				if (pw != null)
				{
					pw.dismiss();
				}
			}
		});

		webLoadingProgressBar = (ProgressBar) floatView.findViewById(R.id.webLoadingProgressBar);
		WebSettings webSettings = detail_webview.getSettings();
		webSettings.setJavaScriptEnabled(true);
		detail_webview.addJavascriptInterface(this, "OpenMedia");
		detail_webview.setWebChromeClient(m_chromeClient);
		detail_webview.setWebViewClient(new WebViewClient()
		{

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url)
			{
				view.loadUrl(url);
				return true;
			}
		});

		ArticleEntity aEntity = (ArticleEntity) v.getTag();
		if (aEntity != null)
		{
			String url = StorageUtils.FILE_ROOT + aEntity.getServerID() + "/" + aEntity.getMain_file_path();
			detail_webview.loadUrl("file://" + url);
		}

		pw.showAsDropDown(view, 0, -view.getHeight());
	}

	private WebChromeClient m_chromeClient = new WebChromeClient()
	{
		@Override
		public void onShowCustomView(View view, CustomViewCallback callback)
		{
			// TODO Auto-generated method stub
		}

		public void onProgressChanged(WebView view, int progress)
		{
			// setTitle("ҳ������У����Ժ�..." + progress + "%");
			// setProgress(progress * 100);

			if (progress == 100)
			{
				if (webLoadingProgressBar != null)
				{
					webLoadingProgressBar.setVisibility(View.INVISIBLE);
				}
			}
		}
	};
}
