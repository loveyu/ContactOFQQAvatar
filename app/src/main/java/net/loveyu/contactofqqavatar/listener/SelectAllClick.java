package net.loveyu.contactofqqavatar.listener;

import net.loveyu.contactofqqavatar.Contact;
import net.loveyu.contactofqqavatar.MainActivity;
import android.view.View;
import android.view.View.OnClickListener;

public class SelectAllClick implements OnClickListener {
	@Override
	public void onClick(View v) {
		if (Contact.selectList.size() != Contact.selfList.size()) {
			for (int i = 0; i < Contact.selfList.size(); i++) {
				if (!Contact.selectList.contains((Integer) i)) {
					Contact.selectList.add(i);
				}
			}
		} else {
			Contact.selectList.clear();
		}
		CheckBoxSelectClick.NoticeSelectButtonStatus();
		MainActivity.NotifyUpdate();
	}
}
