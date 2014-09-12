package net.loveyu.contactofqqavatar;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

import android.content.Context;
import android.provider.ContactsContract;
import android.widget.Toast;

public class AvatarAction {
    Context context;

    public AvatarAction(Context context) {
        this.context = context;
    }

    public void ChangeAvatar(String id, String qq) {
        DownThread dt = new DownThread(id, qq, context, true);
        new Thread(dt).start();
    }

    public void clearAllAvatar() {
        context.getContentResolver()
                .delete(ContactsContract.Data.CONTENT_URI,
                        ContactsContract.Data.MIMETYPE + "=='"
                                + ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE + "'", null);
        MainActivity.NotifyUpdate();
    }

    public void setAllAvatar() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                for (Contact c : Contact.selfList) {
                    String qq = c.getQq();
                    if (qq != null) {
                        DownThread dt = new DownThread(c.getId(), qq, context, false);
                        dt.run();
                        if (++i % 5 == 0) {
                            MainActivity.NotifyUpdate();
                        }
                    }
                }
                MainActivity.NotifyUpdate();
                MainActivity.NotifyToast(context.getResources().getString(R.string.download_avatar_finish));
            }
        }).start();
    }

    public HashMap<String, String> import_file(String path) {
        HashMap<String, String> map = new HashMap<String, String>();
        try {
            BufferedReader fr = new BufferedReader(new FileReader(path));
            String line = null;
            String[] ss;
            while ((line = fr.readLine()) != null) {
                ss = line.split("\t");
                if (ss.length == 2 && ss[0].matches("^[0-9]{5,11}$")) {
                    map.put(ss[1], ss[0]);
                }
            }
            fr.close();
        } catch (Exception e) {
            Toast.makeText(context, context.getResources().getString(R.string.open_file_error) + ":" + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
        return map;
    }
}
