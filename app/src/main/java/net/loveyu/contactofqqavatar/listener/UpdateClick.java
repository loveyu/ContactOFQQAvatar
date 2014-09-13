package net.loveyu.contactofqqavatar.listener;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import net.loveyu.contactofqqavatar.MainActivity;

public class UpdateClick implements View.OnClickListener {
    Dialog dialog;
    String url;

    public UpdateClick(Dialog dialog, String url) {
        this.dialog = dialog;
        this.url = url;
    }

    @Override
    public void onClick(View v) {
        dialog.cancel();
        MainActivity.NotifyOpenUri(url);
    }
}
