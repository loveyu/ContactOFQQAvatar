package net.loveyu.contactofqqavatar;

import android.os.Bundle;
import android.webkit.WebView;
import android.app.Activity;

public class AdActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ad);
		((WebView)findViewById(R.id.webView)).loadUrl("http://www.loveyu.org/?form=on_contactOfQQAvatar");
	}

	@Override
	public void finish() {
		super.finish();
		MainActivity.NotifyShowAd(0);
	}
}
