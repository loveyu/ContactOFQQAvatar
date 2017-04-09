package net.loveyu.contactofqqavatar;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;

public class Permissions {

    public static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1000;

    public static int check_status = 0;

    private Activity context;

    private static Permissions instance = null;

    public static Permissions getInstance(Activity context) {
        check_status = 0;
        if (instance == null) {
            instance = new Permissions(context);
        }
        return instance;
    }

    private Permissions(Activity context) {
        this.context = context;
    }

    public boolean check() {
        check_status = 0;
        return PermissionGranted();
    }


    public boolean PermissionGranted() {
        boolean result =
                context.checkSelfPermission(Manifest.permission.READ_CONTACTS)
                        == PackageManager.PERMISSION_GRANTED &&
                        context.checkSelfPermission(Manifest.permission.WRITE_CONTACTS)
                                == PackageManager.PERMISSION_GRANTED;
        if (!result) {
            context.requestPermissions(new String[]{
                            Manifest.permission.READ_CONTACTS,
                            Manifest.permission.WRITE_CONTACTS
                    },
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        }

        return result;
    }

    public static void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        check_status = 3;
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("CHECK:", "B3");
                    check_status = 1;
                } else {
                    Log.d("CHECK:", "B4");
                    check_status = 2;
                }
            }
        }
    }
}
