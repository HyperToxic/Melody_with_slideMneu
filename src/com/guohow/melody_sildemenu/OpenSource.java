package com.guohow.melody_sildemenu;

import com.guohow.melody_sildemenu.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class OpenSource extends Activity {

	String url;
	WebView show;
	Dialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.open_source);
		getWebContent();
	}

	@SuppressLint("SetJavaScriptEnabled")
	public void getWebContent() {

		// 获取组件
		show = (WebView) findViewById(R.id.webView1);

		show.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});

		url = "http://github.com/HyperToxic/Melody_with_slideMneu/";
		// url="file:///android_asset/blog.html";
		// 设置WebView属性，能够执行Javascript脚本
		show.getSettings().setJavaScriptEnabled(true);
		WebSettings ws = show.getSettings();

		ws.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);

		// 让缩放显示的最小值为肇端
		// show.setInitialScale(99);
		// 设置支撑缩放
		ws.setSupportZoom(true);

		// 设置缩放工具的显示
		// ws.setBuiltInZoomControls(true);
		// 加载页面
		show.loadUrl(url);
		dialog = ProgressDialog.show(this, null, "加载中，请稍后..");
		show.reload();
		show.setWebViewClient(new MyWebViewClient(dialog));

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && show.canGoBack()) {
			show.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	


}
