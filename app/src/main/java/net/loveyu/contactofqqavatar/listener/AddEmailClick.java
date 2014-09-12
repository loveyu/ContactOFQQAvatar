package net.loveyu.contactofqqavatar.listener;

import net.loveyu.contactofqqavatar.AvatarAction;
import net.loveyu.contactofqqavatar.Contact;
import net.loveyu.contactofqqavatar.ContactAction;
import net.loveyu.contactofqqavatar.MainActivity;
import net.loveyu.contactofqqavatar.R;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class AddEmailClick implements OnClickListener {
    private Context context;
    private View view;
    private Contact contact;
    private Dialog dialog;
    private int position;

    public AddEmailClick(Context context, View view, Contact c, Dialog dialog, int position) {
        this.context = context;
        this.view = view;
        contact = c;
        this.dialog = dialog;
        this.position = position;
    }

    @Override
    public void onClick(View v) {
        String qq = ((EditText) view.findViewById(R.id.editTextQQNumber)).getText().toString();
        if (qq.length() < 5) {
            Toast.makeText(context, context.getResources().getString(R.string.please_input_true_qq_number),
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (ContactAction.AddEmail(context.getContentResolver(), contact.getId(), qq)) {
            Toast.makeText(context, context.getResources().getString(R.string.add_qq_mail_ok), Toast.LENGTH_SHORT)
                    .show();
            contact.setEmail("qq", qq + "@qq.com");
            Contact.selfList.set(position, contact);
            dialog.dismiss();
            MainActivity.NotifyUpdate();
            if (!ContactAction.hasPhoto(contact.getId(), context.getContentResolver())) {
                AvatarAction aa = new AvatarAction(context);
                aa.ChangeAvatar(contact.getId(), qq);
            }
        } else {
            Toast.makeText(context, context.getResources().getString(R.string.add_qq_mail_error), Toast.LENGTH_SHORT)
                    .show();
        }
    }
}
