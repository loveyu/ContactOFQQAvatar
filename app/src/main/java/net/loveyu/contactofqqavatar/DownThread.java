package net.loveyu.contactofqqavatar;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;

public class DownThread implements Runnable {
    String id;
    String qq;
    Context context;
    boolean notify;

    public DownThread(String id, String qq, Context context, boolean notify) {
        this.id = id;
        this.qq = qq;
        this.notify = notify;
        this.context = context;
        MainActivity.DownloadProcess(true);
    }

    @Override
    public void run() {
        try {
            byte[] bs = null;
            for (int size : new int[]{640, 140, 100, 40}) {
                URL url = new URL("http://q1.qlogo.cn/g?b=qq&nk=" + qq + "&s=" + size + "&t="
                        + (System.currentTimeMillis() / 1000));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.connect();
                InputStream inputStream = conn.getInputStream();
                bs = readStream(inputStream);
                conn.disconnect();
                if (bs.length == 3707 || (size == 100 && bs.length == 7097)) {
                    if (size != 40) {
                        bs = null;
                    }
                } else {
                    break;
                }
            }
            if (bs != null && bs.length > 0) {
                ContactAction.setContactPhoto(context.getContentResolver(), bs, Long.parseLong(id), notify);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            MainActivity.DownloadProcess(false);
        }
    }

    public byte[] readStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        inStream.close();
        return outStream.toByteArray();
    }

}
