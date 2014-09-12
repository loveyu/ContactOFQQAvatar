package net.loveyu.contactofqqavatar.listener;

import net.loveyu.contactofqqavatar.Contact;
import net.loveyu.contactofqqavatar.ContactAction;
import net.loveyu.contactofqqavatar.MainActivity;
import net.loveyu.contactofqqavatar.R;

import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class DeleteEmailClick implements OnClickListener {
    String email;
    Contact contact;
    Dialog dialog = null;
    int position;

    public DeleteEmailClick(String email, Contact contact, int position, Dialog dialog) {
        this.email = email;
        this.contact = contact;
        this.position = position;
        this.dialog = dialog;
    }

    @Override
    public void onClick(View v) {
        if (ContactAction.DeleteEmail(v.getContext().getContentResolver(), contact.getId(), email)) {
            contact.getEmail().remove(email);
            Contact.selfList.set(position, contact);
            Toast.makeText(v.getContext(), v.getResources().getString(R.string.delete_this_email_succ),
                    Toast.LENGTH_SHORT).show();
            MainActivity.NotifyUpdate();
            if (dialog != null) {
                dialog.dismiss();
            }
        } else {
            Toast.makeText(v.getContext(), v.getResources().getString(R.string.delete_this_email_error),
                    Toast.LENGTH_SHORT).show();
        }
    }
}
