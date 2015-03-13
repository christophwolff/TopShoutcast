package com.coffeecodeandcreativity.topshoutcast;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class ShowFacebook extends Activity {
	WebView wvfacebook;
	ProgressBar pg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.showfb);
		wvfacebook = (WebView) findViewById(R.id.wbFacebook);
		pg = (ProgressBar) findViewById(R.id.progressBar1);
		wvfacebook.setWebViewClient(new myWebClient());

		wvfacebook
				.loadUrl("https://m.facebook.com/pages/Free-Live-Sports-Stream/694499667275694?");
	}
	
	
	public class myWebClient extends WebViewClient {
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			// TODO Auto-generated method stub

			super.onPageStarted(view, url, favicon);

		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// TODO Auto-generated method stub

				
			
				view.loadUrl(url);
			
			return true;
		      
		       
		    
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
		

			super.onPageFinished(view, url);
			
			//view.setVisibility(View.VISIBLE);
			pg.setVisibility(View.GONE);

		}
	}

}
