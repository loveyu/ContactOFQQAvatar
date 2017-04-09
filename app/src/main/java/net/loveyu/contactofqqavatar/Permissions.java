package net.loveyu.contactofqqavatar;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.util.Log;

import static android.R.attr.targetSdkVersion;

public class Permissions {

    public static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1000;

    private Activity context;

    private int targetSdkVersion = Build.VERSION_CODES.M;

    private static Permissions instance = null;

    public static Permissions getInstance(Activity context) {
        if (instance == null) {
            instance = new Permissions(context);
        }
        return instance;
    }

    private Permissions(Activity context) {
        this.context = context;
        try {
            final PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            targetSdkVersion = info.applicationInfo.targetSdkVersion;
        } catch (PackageManager.NameNotFoundException e) {
        }
    }

    public void check() {
        PermissionGranted();
    }


    public boolean PermissionGranted() {
        boolean result = true;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (targetSdkVersion >= Build.VERSION_CODES.M) {
                result = context.checkSelfPermission(Manifest.permission.READ_CONTACTS)
                        == PackageManager.PERMISSION_GRANTED &&
                        context.checkSelfPermission(Manifest.permission.WRITE_CONTACTS)
                        == PackageManager.PERMISSION_GRANTED;
                if (!result) {
                    ActivityCompat.requestPermissions(context,
                            new String[]{
                                    Manifest.permission.READ_CONTACTS,
                                    Manifest.permission.WRITE_CONTACTS
                            },
                            MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                }
            } else {
                result = PermissionChecker.
                        checkSelfPermission(context, Manifest.permission.READ_CONTACTS)
                        == PermissionChecker.PERMISSION_GRANTED &&
                        context.checkSelfPermission(Manifest.permission.WRITE_CONTACTS)
                        == PackageManager.PERMISSION_GRANTED;
            }
        }

        return result;
    }

    public static void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    is_deny = false;
                } else {
//                    is_deny = true;
                }
            }
        }
    }
}
