package net.loveyu.contactofqqavatar;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.webkit.WebView;
import android.app.Activity;

public class AdActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad);
        ((WebView) findViewById(R.id.webView)).loadUrl("http://www.loveyu.org/?form=on_contactOfQQAvatar&v=" + getVersion());
    }

    private String getVersion() {
        try {
            PackageInfo pi = getPackageManager().getPackageInfo(getPackageName(), 0);
            return pi.versionName + "(" + pi.versionCode + ")";
        } catch (PackageManager.NameNotFoundException e) {
            return "1.0 (1)";
        }
    }

    @Override
    public void finish() {
        super.finish();
        MainActivity.NotifyShowAd(0);
    }
}
