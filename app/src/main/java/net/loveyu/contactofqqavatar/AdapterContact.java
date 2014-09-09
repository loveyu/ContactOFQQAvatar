package net.loveyu.contactofqqavatar;

import net.loveyu.contactofqqavatar.listener.CheckBoxSelectClick;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterContact extends BaseAdapter {
	private Context context;

	public AdapterContact(Context context) {
		super();
		this.context = context;
	}

	@Override
	public int getCount() {
		return Contact.selfList.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		Contact c = Contact.selfList.get(position);
		if (convertView != null) {
			holder = (ViewHolder) convertView.getTag();
		} else {
			convertView = View.inflate(context, R.layout.list_item_contcat_show, null);
			holder = new ViewHolder();
			holder.tve = (TextView) convertView.findViewById(R.id.ContactEmail);
			holder.tvp = (TextView) convertView.findViewById(R.id.ContactPhone);
			holder.tvn = (TextView) convertView.findViewById(R.id.ContactName);
			holder.avatar = (ImageView) convertView.findViewById(R.id.ContactAvatar);
			holder.chk = (CheckBox) convertView.findViewById(R.id.checkBoxOfList);
			convertView.setTag(holder);
		}
		if (Contact.selectList.contains((Integer) position)) {
			holder.chk.setChecked(true);
		} else {
			holder.chk.setChecked(false);
		}
		holder.tvn.setText(c.getName());
		holder.chk.setOnClickListener(new CheckBoxSelectClick(position));
		holder.tvp.setText(c.phoneCahce(true));
		holder.tve.setText(c.emailCache(true));
		Bitmap bm = ContactAction.getPhoto(c.getId(), context.getContentResolver());
		if (bm != null) {
			holder.avatar.setImageBitmap(bm);
		} else {
			holder.avatar.setImageResource(R.drawable.avatar);
		}
		return convertView;
	}

	class ViewHolder {
		TextView tvp;
		TextView tve;
		TextView tvn;
		ImageView avatar;
		CheckBox chk;
	}
}
