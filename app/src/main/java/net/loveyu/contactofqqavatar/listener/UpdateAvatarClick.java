package net.loveyu.contactofqqavatar.listener;

import net.loveyu.contactofqqavatar.AvatarAction;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

public class UpdateAvatarClick implements OnClickListener {
	private String id;
	private String qq;
	private Context c;
	private Dialog d;
	

	public UpdateAvatarClick(String id,String qq, Context c, Dialog d) {
		this.id = id;
		this.c = c;
		this.d = d;
		this.qq = qq;
	}

	@Override
	public void onClick(View v) {
		AvatarAction aa = new AvatarAction(c);
		aa.ChangeAvatar(id, qq);
		if (d != null) {
			d.dismiss();
		}
	}
}
