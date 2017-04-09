package net.loveyu.contactofqqavatar;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;

public class LaunchActivity extends Activity {
    Intent intent;

    private boolean startCheck = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lanuch);
        intent = new Intent(this, MainActivity.class);

        startCheck = Permissions.getInstance(this).check();

        new Thread(new Runnable() {

            @Override
            public void run() {
                if (!LaunchActivity.this.startCheck) {
                    while (Permissions.check_status == 0) {
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if (Permissions.check_status == 1) {
                        LaunchActivity.this.startCheck = true;
                    }
                }
                if(LaunchActivity.this.startCheck) {
                    if (Contact.selfList == null || Contact.selfList.size() < 1) {
                        Contact.selfList = ContactAction.getContact(getContentResolver());
                        Contact.selectList = new ArrayList<Integer>();
                    } else {
                        MainActivity.NotifyDataRefresh();
                    }
                }
                goMain();
            }
        }).start();
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Permissions.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void goMain() {
        if (startCheck) {
            startActivity(intent);
        }else{

        }
        finish();
    }
}
