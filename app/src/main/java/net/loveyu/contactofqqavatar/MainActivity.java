package net.loveyu.contactofqqavatar;

import java.util.ArrayList;

import net.loveyu.contactofqqavatar.listener.DeleteMoreEmailClick;
import net.loveyu.contactofqqavatar.listener.RemoveMoreAvatarClick;
import net.loveyu.contactofqqavatar.listener.SelectReverseClick;
import net.loveyu.contactofqqavatar.listener.ListItemClick;
import net.loveyu.contactofqqavatar.listener.ListItemLongClick;
import net.loveyu.contactofqqavatar.listener.SelectAllClick;
import net.loveyu.contactofqqavatar.listener.UpdateMoreAvatarClick;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private AdapterContact ac;
    private static MainActivity self;
    final static int ListUpdate = 1;
    final static int ListDataRefresh = 2;
    final static int ToastMessage = 3;
    final static int ShowSelectBox = 4;
    final static int ShowAdMsg = 5;
    final static int DownloadProcess = 6;
    final static int VersionUpdate = 7;
    final static int OpenUri = 8;
    public Handler handler;
    public static boolean Refresh = false;
    private Button bsa;
    private Button bsx;
    private TextView tvdp;
    private RelativeLayout rl;
    private ListView lv;
    private static boolean ShowAd = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        self = this;
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        lv = (ListView) findViewById(R.id.ConcatlistView);
        bsa = (Button) findViewById(R.id.buttonSelectAll);
        bsa.setVisibility(Button.GONE);
        bsx = (Button) findViewById(R.id.buttonSelectX);
        bsx.setVisibility(Button.GONE);
        tvdp = (TextView) findViewById(R.id.download_process);
        tvdp.setVisibility(TextView.GONE);
        rl = (RelativeLayout) findViewById(R.id.SelectContactAction);
        rl.setVisibility(RelativeLayout.GONE);
        if (ShowAd) {
            // 广告显示
        }

        bsa.setOnClickListener(new SelectAllClick());
        bsx.setOnClickListener(new SelectReverseClick());
        ac = new AdapterContact(this);
        lv.setAdapter(ac);
        lv.setOnItemClickListener(new ListItemClick(this));
        lv.setOnItemLongClickListener(new ListItemLongClick(this));
        ((Button) findViewById(R.id.buttonSelectActionUpdateAvatar))
                .setOnClickListener(new UpdateMoreAvatarClick(this));
        ((Button) findViewById(R.id.buttonSelectActionDeleteAvatar))
                .setOnClickListener(new RemoveMoreAvatarClick(this));
        ((Button) findViewById(R.id.buttonSelectActionDeleteEmail)).setOnClickListener(new DeleteMoreEmailClick(this));
        ((TextView) findViewById(R.id.textViewCopyright)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setData(Uri.parse("http://www.loveyu.org/?form=contactofqqavatar"));
                intent.setAction(Intent.ACTION_VIEW);
                MainActivity.this.startActivity(intent);
            }
        });
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case OpenUri:
                        Intent intent = new Intent();
                        intent.setData(Uri.parse((String) msg.obj));
                        intent.setAction(Intent.ACTION_VIEW);
                        MainActivity.this.startActivity(intent);
                        break;
                    case ListUpdate:
                        ac.notifyDataSetChanged();
                        break;
                    case ListDataRefresh:
                        new Thread(new Runnable() {
                            public void run() {
                                Contact.selfList = ContactAction.getContact(getContentResolver());
                                Contact.selectList = new ArrayList<Integer>();
                                NotifyUpdate();
                                NotifyToast(self.getResources().getString(R.string.contact_list_is_already_refresh));
                            }
                        }).start();
                        break;
                    case ToastMessage:
                        Toast.makeText(self, (String) msg.obj, Toast.LENGTH_SHORT).show();
                        break;
                    case ShowSelectBox:
                        ChangeSelectBox("true".equals((String) msg.obj));
                        break;
                    case ShowAdMsg:
                        if ((Integer) msg.obj != 0) {
                            // show
                            if (!ShowAd) {
                                // 广告添加
                                // ((RelativeLayout)
                                // findViewById(R.id.adView)).addView(new
                                // AdView(MainActivity.this));
                                ShowAd = true;
                            }
                        } else {
                            // hide
                            if (ShowAd) {
                                //广告移除
                                // ((RelativeLayout)
                                // findViewById(R.id.adView)).removeAllViews();
                                ShowAd = false;
                            }
                        }
                        break;
                    case VersionUpdate:
                        ((Report) msg.obj).open_dialog(MainActivity.this);
                        break;
                    case DownloadProcess:
                        String[] value = tvdp.getText().toString().split("/");
                        int offset = (Integer) msg.obj;
                        if (value.length != 2) {
                            if (offset == 1) {
                                tvdp.setText("0/" + offset);
                            }
                        } else {
                            int v0 = Integer.parseInt(value[0]);
                            int v1 = Integer.parseInt(value[1]);
                            if (offset == 1) {
                                ++v1;
                            } else if (offset == -1) {
                                ++v0;
                            }
                            if (v0 == v1) {
                                tvdp.setText("");
                            } else {
                                tvdp.setText(v0 + "/" + v1);
                            }
                        }
                        break;
                }
            }
        };
        new Thread(new Report(getApplicationContext())).start();
    }

    /**
     * 通知进度条更新
     *
     * @param add 是否添加一个下载，true时为添加下载，false为完成一个下载
     */
    public static void DownloadProcess(boolean add) {
        Message msg = self.handler.obtainMessage(DownloadProcess, add ? 1 : -1);
        self.handler.sendMessage(msg);
    }

    public static void NotifyShowAd(int flag) {
        Message msg = self.handler.obtainMessage(ShowAdMsg, flag);
        self.handler.sendMessage(msg);
    }

    public static void NotifyUpdate() {
        Message msg = self.handler.obtainMessage(ListUpdate);
        self.handler.sendMessage(msg);
    }

    public static void NotifyVersionUpdate(Report report) {
        Message msg = self.handler.obtainMessage(VersionUpdate);
        msg.obj = report;
        self.handler.sendMessage(msg);
    }

    public static void NotifyOpenUri(String uri) {
        Message msg = self.handler.obtainMessage(OpenUri);
        msg.obj = uri;
        self.handler.sendMessage(msg);
    }

    public static MainActivity getInstance() {
        return self;
    }

    public static void NotifyDataRefresh() {
        Message msg = self.handler.obtainMessage(ListDataRefresh);
        self.handler.sendMessage(msg);
    }

    public static void NoticeChangeSelectBox(boolean flag) {
        new Thread(new ShowSelectBoxThread(flag)).start();
    }

    public void ChangeSelectBox(boolean flag) {
        if (flag) {
            bsa.setVisibility(Button.VISIBLE);
            bsx.setVisibility(Button.VISIBLE);
            rl.setVisibility(RelativeLayout.VISIBLE);
            tvdp.setVisibility(TextView.VISIBLE);
        } else {
            bsa.setVisibility(Button.GONE);
            bsx.setVisibility(Button.GONE);
            rl.setVisibility(RelativeLayout.GONE);
            tvdp.setVisibility(TextView.GONE);
        }
    }

    public static void NotifyToast(String data) {
        Message msg = self.handler.obtainMessage(ToastMessage);
        msg.obj = data;
        self.handler.sendMessage(msg);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_clear_all:
                new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.delete_all_avatar_confirm))
                        .setMessage(getResources().getString(R.string.delete_all_avatar_msg))
                        .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(MainActivity.this,
                                        getResources().getString(R.string.clear_all_contact_avatar), Toast.LENGTH_SHORT)
                                        .show();
                                new AvatarAction(MainActivity.this).clearAllAvatar();
                            }
                        }).setNegativeButton(getResources().getString(R.string.cancel), null).show();
                break;
            case R.id.action_set_all:
                new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.download_all_avatar_confirm))
                        .setMessage(getResources().getString(R.string.download_all_avatar_msg))
                        .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(MainActivity.this,
                                        getResources().getString(R.string.downloading_all_contact_avatar),
                                        Toast.LENGTH_SHORT).show();
                                new AvatarAction(MainActivity.this).setAllAvatar();
                            }
                        }).setNegativeButton(getResources().getString(R.string.cancel), null).show();

                break;
            case R.id.action_refresh:
                NotifyDataRefresh();
                break;
            case R.id.action_import_list:
                Intent intent = new Intent(this, ImportActivity.class);
                startActivity(intent);
                break;
            case R.id.action_exit:
                Contact.selectList = null;
                Contact.selfList = null;
                finish();
                break;
            case R.id.action_hide_ad:
                // NotifyShowAd(0);
                Intent ad = new Intent(this, AdActivity.class);
                startActivity(ad);
                break;
            case R.id.action_about:
                Intent about = new Intent(this, AboutActivity.class);
                startActivity(about);
                break;
        }
        return true;
    }
}

class ShowSelectBoxThread implements Runnable {
    private boolean flag;

    public ShowSelectBoxThread(boolean flag) {
        this.flag = flag;
    }

    @Override
    public void run() {
        Message msg = MainActivity.getInstance().handler.obtainMessage(MainActivity.ShowSelectBox);
        msg.obj = flag ? "true" : "false";
        MainActivity.getInstance().handler.sendMessage(msg);
    }
}
