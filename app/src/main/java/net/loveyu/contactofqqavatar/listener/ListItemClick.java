package net.loveyu.contactofqqavatar.listener;

import net.loveyu.contactofqqavatar.Contact;
import net.loveyu.contactofqqavatar.R;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.AdapterView.OnItemClickListener;

public class ListItemClick implements OnItemClickListener {
    private Context context;

    public ListItemClick(Context context) {
        this.context = context;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Contact c = Contact.selfList.get(position);
        String qq = c.getQq();
        if (qq == null) {
            // 添加邮箱
            Dialog addMail = new Dialog(context);
            addMail.setTitle(context.getResources().getString(R.string.add_new_qq_email).toString());
            View dv = View.inflate(context, R.layout.dialog_add_email, null);
            AddEmailClick ae = new AddEmailClick(context, dv, c, addMail, position);
            ((Button) dv.findViewById(R.id.buttonAddQQEmail)).setOnClickListener(ae);
            addMail.setContentView(dv);
            addMail.show();
        } else {
            // 下载确认
            Dialog addMail = new Dialog(context);
            addMail.setTitle(context.getResources().getString(R.string.action_chose));
            View dv = View.inflate(context, R.layout.dialog_confirm_download, null);
            ((Button) dv.findViewById(R.id.ButtonRemoveAvatar)).setOnClickListener(new RemoveAvatarClick(c.getId(),
                    context.getContentResolver(), addMail));
            ((Button) dv.findViewById(R.id.ButtonUpdateAvatar)).setOnClickListener(new UpdateAvatarClick(c.getId(), c
                    .getQq(), context, addMail));
            ((Button) dv.findViewById(R.id.ButtonEditQQ))
                    .setOnClickListener(new EditQQClick(position, context, addMail));
            addMail.setContentView(dv);
            addMail.show();
        }
    }
}
