package net.loveyu.contactofqqavatar;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;

public class LaunchActivity extends Activity {
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lanuch);
        intent = new Intent(this, MainActivity.class);
        new Thread(new Runnable() {

            @Override
            public void run() {
                if (Contact.selfList == null || Contact.selfList.size() < 1) {
                    Contact.selfList = ContactAction.getContact(getContentResolver());
                    Contact.selectList = new ArrayList<Integer>();
                } else {
                    MainActivity.NotifyDataRefresh();
                }
                goMain();
            }
        }).start();
    }
    private void goMain() {
        startActivity(intent);
        finish();
    }
}
