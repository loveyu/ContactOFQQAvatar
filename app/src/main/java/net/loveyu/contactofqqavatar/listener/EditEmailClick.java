package net.loveyu.contactofqqavatar.listener;

import net.loveyu.contactofqqavatar.Contact;
import net.loveyu.contactofqqavatar.ContactAction;
import net.loveyu.contactofqqavatar.MainActivity;
import net.loveyu.contactofqqavatar.R;
import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class EditEmailClick implements OnClickListener {
	String email;
	Contact contact;
	Dialog dialog = null;
	int position;

	public EditEmailClick(String email, Contact contact, int position, Dialog dialog) {
		this.email = email;
		this.contact = contact;
		this.position = position;
		this.dialog = dialog;
	}

	@Override
	public void onClick(View v) {
		EditText et = (EditText) dialog.findViewById(R.id.editTextInputNewQQ);
		String newQQ = et.getText().toString();
		if (newQQ.equals(contact.getQq())) {
			Toast.makeText(dialog.getContext(),
					dialog.getContext().getResources().getString(R.string.the_qq_number_is_not_change),
					Toast.LENGTH_SHORT).show();
		} else {
			if (ContactAction.EditEmail(dialog.getContext().getContentResolver(), contact.getId(), email, newQQ)) {
				String type = contact.getEmail().get(email);
				contact.getEmail().remove(email);
				contact.setEmail(type, newQQ + "@qq.com");
				Contact.selfList.set(position, contact);
				MainActivity.NotifyUpdate();
				Toast.makeText(dialog.getContext(),
						dialog.getContext().getResources().getString(R.string.edit_email_succ), Toast.LENGTH_SHORT)
						.show();
				dialog.dismiss();
			} else {
				Toast.makeText(dialog.getContext(),
						dialog.getContext().getResources().getString(R.string.edit_email_error), Toast.LENGTH_SHORT)
						.show();
			}
		}
	}
}
