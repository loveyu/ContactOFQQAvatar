package net.loveyu.contactofqqavatar;

import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.text.util.Linkify;
import android.widget.TextView;
import android.app.Activity;
import android.content.pm.PackageManager.NameNotFoundException;

public class AboutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setAboutContent((TextView) findViewById(R.id.textViewAbout));
    }

    private void setAboutContent(TextView tv) {
        String html;
        html = "<div><h3>程序信息</h3>"
                + "<p>针对系统联系人的QQ头像解决方案<p>"
                + "<p>版本:"
                + getVersion()
                + "</p></div>"
                + "<div><h3>权限说明</h3>"
                + "<p>系统需要读取联系人信息，但绝不会上传联系人信息。对应的其他权限为广告获取需要。应用实际用到的权限仅为网络权限和联系人读取。</p></div>"
                + "<div><h3>功能说明</h3>"
                + "<p>可以通过批量导入的方式导入联系人QQ号，前提是要获取对应的信息，详见：<a href=\"http://www.loveyu.org/3151.html?form=contactOfQq\">http://www.loveyu.org/3151.html</a></p>"
                + "<!--p>其中广告屏蔽为一次有效，请谅解！要生存啊！</p--></div>" + "<div><h3>反馈</h3>" + "<p>作者:<strong>恋羽</strong></p>"
                + "<p>博客地址: <a href=\"http://www.loveyu.org/?form=contactOfQq\">http://www.loveyu.org</a> </p></div>";

        tv.setMovementMethod(ScrollingMovementMethod.getInstance());
        tv.setAutoLinkMask(Linkify.ALL);
        tv.setText(Html.fromHtml(html));
    }

    private String getVersion() {
        try {
            PackageInfo pi = getPackageManager().getPackageInfo(getPackageName(), 0);
            return pi.versionName + "(" + pi.versionCode + ")";
        } catch (NameNotFoundException e) {
            return "1.0 (1)";
        }
    }
}
