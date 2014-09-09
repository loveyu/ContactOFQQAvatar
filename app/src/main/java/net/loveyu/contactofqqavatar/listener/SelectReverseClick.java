package net.loveyu.contactofqqavatar.listener;

import net.loveyu.contactofqqavatar.Contact;
import net.loveyu.contactofqqavatar.MainActivity;
import android.view.View;
import android.view.View.OnClickListener;

public class SelectReverseClick implements OnClickListener {
	@Override
	public void onClick(View v) {
		synchronized (Contact.selectList) {
			for (int i = 0; i < Contact.selfList.size(); i++) {
				if (Contact.selectList.contains(i)) {
					Contact.selectList.remove((Integer) i);
				} else {
					Contact.selectList.add(i);
				}
			}
			MainActivity.NotifyUpdate();
		}
		CheckBoxSelectClick.NoticeSelectButtonStatus();
	}
}
