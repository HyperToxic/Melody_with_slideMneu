/**
 * 
 */
package com.guohow.melody_sildemenu;

import android.app.Dialog;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * @author guohao
 * 
 */
public class MyWebViewClient extends WebViewClient {

	Dialog dialog;

	public MyWebViewClient(Dialog dialog) {
		this.dialog = dialog;
	}

	@Override
	public void onPageFinished(WebView view, String url) {
		// TODO Auto-generated method stub
		if (dialog != null)
			dialog.dismiss();
		super.onPageFinished(view, url);
	}

}
