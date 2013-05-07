package com.example.morningreport.cardcreators;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class RSSCardOnClickListener implements OnClickListener {

	private String url;
	private Context context;
	
	public RSSCardOnClickListener(Context context, String url) {
		this.url = url;
		this.context = context;
	}
	@Override
	public void onClick(View v) {		
		WebView webview = new WebView(this.context);

        webview.setWebViewClient(new WebViewClient());
        webview.getSettings().setJavaScriptEnabled(true); // enable javascript
        webview.getSettings().setBuiltInZoomControls(true);
        webview .loadUrl(this.url);
        
        ((Activity) this.context).setContentView(webview );

	}

}
