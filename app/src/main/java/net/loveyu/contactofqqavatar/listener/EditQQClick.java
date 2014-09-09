package net.loveyu.contactofqqavatar.listener;

import net.loveyu.contactofqqavatar.Contact;
import net.loveyu.contactofqqavatar.R;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class EditQQClick implements OnClickListener {
	private int postion;
	private Context context;
	private Dialog d;

	public EditQQClick(int postion, Context context, Dialog d) {
		this.postion = postion;
		this.context = context;
		this.d = d;
	}

	@Override
	public void onClick(View v) {
		Contact contact = Contact.selfList.get(postion);
		if (d != null) {
			d.dismiss();
		}
		String qq = contact.getQq();
		String email = null;
		if (qq != null) {
			email = contact.findQqEmail(qq);
			if (email == null) {
				Toast.makeText(context, context.getResources().getString(R.string.no_found_qq_email) + ":" + qq,
						Toast.LENGTH_SHORT);
				return;
			}
		} else {
			Toast.makeText(context, context.getResources().getString(R.string.no_found_qq_number), Toast.LENGTH_SHORT);
			return;
		}
		Dialog editQQ = new Dialog(context);
		editQQ.setTitle(context.getResources().getString(R.string.edit_qq_email));
		View lv = View.inflate(context, R.layout.dialog_edit_email, null);
		((TextView) lv.findViewById(R.id.textView_now_email)).setText(email);
		((TextView) lv.findViewById(R.id.editTextInputNewQQ)).setText(qq);
		Button del = (Button) lv.findViewById(R.id.ButtonDeleteEmail);
		Button edit = (Button) lv.findViewById(R.id.ButtonEditEmail);
		DeleteEmailClick dec = new DeleteEmailClick(email, contact, postion, editQQ);
		del.setOnClickListener(dec);
		EditEmailClick eec = new EditEmailClick(email, contact, postion, editQQ);
		edit.setOnClickListener(eec);
		editQQ.setContentView(lv);
		editQQ.show();
	}
}
