package net.loveyu.contactofqqavatar;

import java.util.HashMap;
import java.util.Locale;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class ImportActivity extends Activity {
    final int FILE_CHOSE = 1;
    public static final int IMPORT_FILE_FINISH = 2;
    public static final int IMPORT_CONTACT_FINISH = 3;
    public static HashMap<String, String> import_list;
    public static Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import);
        ((Button) findViewById(R.id.buttonSelectQQListFile)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
        ((Button) findViewById(R.id.buttonBeginImportContact)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TextView) findViewById(R.id.textViewImportFileStatus)).setText("正在开始匹配数据中，请稍后....");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int n = ContactAction.AddMapEmail(getContentResolver(), import_list);
                        ImportActivity.handler.sendMessage(ImportActivity.handler.obtainMessage(IMPORT_CONTACT_FINISH,
                                n));
                    }
                }).start();
            }
        });
        ((TextView) findViewById(R.id.textViewGetHelpOfGetQQList)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setData(Uri.parse("http://www.loveyu.org/3151.html/?form=contactofqqavatar"));
                intent.setAction(Intent.ACTION_VIEW);
                ImportActivity.this.startActivity(intent);
            }
        });
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case IMPORT_FILE_FINISH:
                        // 文件导入完成
                        ((TextView) findViewById(R.id.textViewImportFileStatus)).setText(getResources().getString(
                                R.string.record)
                                + ":" + import_list.size());
                        ((Button) findViewById(R.id.buttonBeginImportContact)).setVisibility(Button.VISIBLE);
                        break;
                    case IMPORT_CONTACT_FINISH:
                        int n = (Integer) msg.obj;
                        if (n > 0) {
                            ((TextView) findViewById(R.id.textViewImportFileStatus)).setText("导入数据完成, 导入" + n + "条匹配数据");
                        } else {
                            ((TextView) findViewById(R.id.textViewImportFileStatus)).setText("导入数据完成, 未找到任何匹配数据");
                        }
                        break;
                }
            }
        };
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(
                    Intent.createChooser(intent, getResources().getString(R.string.select_your_qq_list_file)),
                    FILE_CHOSE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, getResources().getString(R.string.please_install_a_file_manager), Toast.LENGTH_SHORT)
                    .show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case FILE_CHOSE:
                if (resultCode == RESULT_OK) {
                    Uri uri = intent.getData();
                    if (uri == null) {
                        Toast.makeText(this, getResources().getString(R.string.not_chose_any_file), Toast.LENGTH_SHORT)
                                .show();
                        return;
                    }
                    String scheme = uri.getScheme().toLowerCase(Locale.getDefault());
                    Log.e("e", "e:" + scheme + ",u:" + uri.getPath());
                    if (scheme != null && ("file".endsWith(scheme) || "content".endsWith(scheme))) {
                        String path = uri.getPath();// 文件路径
                        ((TextView) findViewById(R.id.textViewNowChoseFilePath)).setText(path);
                        ((TextView) findViewById(R.id.textViewImportFileStatus)).setText(getResources().getString(
                                R.string.importing_file));
                        Toast.makeText(this, path, Toast.LENGTH_SHORT).show();
                        new Thread(new ImportThread(this, path)).start();

                    } else {
                        Log.e("e", "路径失败:" + scheme);
                        Toast.makeText(this, getResources().getString(R.string.chose_file_error), Toast.LENGTH_SHORT)
                                .show();
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }
}

class ImportThread implements Runnable {
    private Context context;
    private String path;

    public ImportThread(Context context, String path) {
        this.context = context;
        this.path = path;
    }

    @Override
    public void run() {
        AvatarAction aa = new AvatarAction(context);
        ImportActivity.import_list = aa.import_file(path);
        ImportActivity.handler.sendMessage(ImportActivity.handler.obtainMessage(ImportActivity.IMPORT_FILE_FINISH));
    }
}
