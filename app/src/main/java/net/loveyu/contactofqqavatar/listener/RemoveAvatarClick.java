package net.loveyu.contactofqqavatar.listener;

import net.loveyu.contactofqqavatar.ContactAction;
import android.app.Dialog;
import android.content.ContentResolver;
import android.view.View;
import android.view.View.OnClickListener;

public class RemoveAvatarClick implements OnClickListener {
	private String id;
	private ContentResolver c;
	private Dialog d;

	public RemoveAvatarClick(String id, ContentResolver c, Dialog d) {
		this.id = id;
		this.c = c;
		this.d = d;
	}

	@Override
	public void onClick(View v) {
		ContactAction.delContactPhoto(c, id);
		if (d != null) {
			d.dismiss();
		}
	}
}
