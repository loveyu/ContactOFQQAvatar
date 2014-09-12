package net.loveyu.contactofqqavatar.listener;

import net.loveyu.contactofqqavatar.Contact;
import net.loveyu.contactofqqavatar.ContactAction;
import net.loveyu.contactofqqavatar.MainActivity;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

public class DeleteMoreEmailClick implements OnClickListener {
    private Context c;

    public DeleteMoreEmailClick(Context context) {
        c = context;
    }

    @Override
    public void onClick(View v) {
        for (int i : Contact.selectList) {
            Contact contact = Contact.selfList.get(i);
            String qq = contact.getQq();
            if (qq != null) {
                String email = contact.findQqEmail(qq);
                if (ContactAction.DeleteEmail(c.getContentResolver(), contact.getId(), email)) {
                    contact.getEmail().remove(email);
                    Contact.selfList.set(i, contact);
                }
            }
        }
        MainActivity.NotifyUpdate();
    }
}
