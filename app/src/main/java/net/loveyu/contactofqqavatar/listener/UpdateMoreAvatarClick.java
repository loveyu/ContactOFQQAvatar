package net.loveyu.contactofqqavatar.listener;

import net.loveyu.contactofqqavatar.AvatarAction;
import net.loveyu.contactofqqavatar.Contact;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

public class UpdateMoreAvatarClick implements OnClickListener {
    private Context c;

    public UpdateMoreAvatarClick(Context context) {
        c = context;
    }

    @Override
    public void onClick(View v) {
        AvatarAction aa = new AvatarAction(c);
        for (int i : Contact.selectList) {
            Contact cc = Contact.selfList.get(i);
            String qq = cc.getQq();
            if (qq != null) {
                aa.ChangeAvatar(cc.getId(), cc.getQq());
            }
        }
    }
}
