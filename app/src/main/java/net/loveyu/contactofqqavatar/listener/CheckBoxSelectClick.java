package net.loveyu.contactofqqavatar.listener;

import net.loveyu.contactofqqavatar.Contact;
import net.loveyu.contactofqqavatar.MainActivity;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;

public class CheckBoxSelectClick implements OnClickListener {
    private int position;

    private static boolean showSelectButton = false;

    public CheckBoxSelectClick(int position) {
        this.position = position;
    }

    @Override
    public void onClick(View v) {
        int i = Contact.selectList.indexOf((Integer) position);
        if (((CheckBox) v).isChecked()) {
            if (i == -1) {
                Contact.selectList.add((Integer) position);
            }
        } else {
            if (i > -1) {
                Contact.selectList.remove(i);
            }
        }
        if (Contact.selectList.size() > 0) {
            setSelectButtonStatus(true);
        } else {
            setSelectButtonStatus(false);
        }
    }

    public static void NoticeSelectButtonStatus() {
        if (Contact.selectList.size() > 0) {
            setSelectButtonStatus(true);
        } else {
            setSelectButtonStatus(false);
        }
    }

    private static void setSelectButtonStatus(boolean flag) {
        if (flag) {
            if (!showSelectButton) {
                // 显示
                showSelectButton = true;
                MainActivity.NoticeChangeSelectBox(true);
            }
        } else {
            if (showSelectButton) {
                // 关闭
                MainActivity.NoticeChangeSelectBox(false);
                showSelectButton = false;
            }
        }
    }
}
