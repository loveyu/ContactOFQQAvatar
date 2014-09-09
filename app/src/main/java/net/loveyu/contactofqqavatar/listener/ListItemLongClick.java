package net.loveyu.contactofqqavatar.listener;

import net.loveyu.contactofqqavatar.AvatarAction;
import net.loveyu.contactofqqavatar.Contact;
import net.loveyu.contactofqqavatar.R;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.AdapterView.OnItemLongClickListener;

public class ListItemLongClick implements OnItemLongClickListener {
	private Context context;

	public ListItemLongClick(Context context) {
		this.context = context;
	}

	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
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
			AvatarAction aa = new AvatarAction(context);
			aa.ChangeAvatar(c.getId(), qq);
		}
		return true;
	}
}
