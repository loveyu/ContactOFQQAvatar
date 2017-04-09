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

        if(Permissions.getInstance(this).check()){
            finish();
            return;
        }

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

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Permissions.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void goMain() {
        startActivity(intent);
        finish();
    }
}
