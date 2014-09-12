package net.loveyu.contactofqqavatar.listener;

import net.loveyu.contactofqqavatar.Contact;
import net.loveyu.contactofqqavatar.ContactAction;

import android.content.Context;
import android.view.View.OnClickListener;

public class RemoveMoreAvatarClick implements OnClickListener {
    private Context c;

    public RemoveMoreAvatarClick(Context context) {
        c = context;
    }

    public void onClick(android.view.View v) {
        for (int i : Contact.selectList) {
            Contact cc = Contact.selfList.get(i);
            ContactAction.delContactPhoto(c.getContentResolver(), cc.getId());
        }
    }
}
