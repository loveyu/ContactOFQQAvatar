package net.loveyu.contactofqqavatar;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

public class ContactAction {
	public static List<Contact> getContact(ContentResolver cr) {
		List<Contact> list = new ArrayList<Contact>();
		Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null,
				ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC");
		Cursor phones, emails;
		if (cur.moveToFirst()) {
			int idColumn = cur.getColumnIndex(ContactsContract.Contacts._ID);
			int displayNameColumn = cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
			do {
				// 获得联系人的ID号
				String contactId = cur.getString(idColumn);
				// 获得联系人姓名
				String disPlayName = cur.getString(displayNameColumn);
				Contact concat = new Contact(contactId, disPlayName);
				// 查看该联系人有多少个电话号码。如果没有这返回值为0
				int phoneCount = cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
				if (phoneCount > 0) {
					// 获得联系人的电话号码
					phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
					if (phones.moveToFirst()) {
						do {
							// 遍历所有的电话号码
							String phoneNumber = phones.getString(phones
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
							String phoneType = phones.getString(phones
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
							concat.setPhone(phoneType, phoneNumber);
						} while (phones.moveToNext());
					}
					phones.close();
				}

				// 获取该联系人邮箱
				emails = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
						ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
				if (emails.moveToFirst()) {
					do {
						// 遍历所有的邮箱
						String emailType = emails.getString(emails
								.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
						String emailValue = emails.getString(emails
								.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
						concat.setEmail(emailType, emailValue);
					} while (emails.moveToNext());
				}
				emails.close();

				list.add(concat);
			} while (cur.moveToNext());
		}
		cur.close();
		return list;
	}

	public static Bitmap getPhoto(String people_id, ContentResolver cr) {
		Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.parseLong(people_id));
		InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(cr, uri);
		if (input == null) {
			return null;
		}
		Bitmap photo = BitmapFactory.decodeStream(input);
		return photo;
	}

	public static boolean hasPhoto(String people_id, ContentResolver cr) {
		Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.parseLong(people_id));
		InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(cr, uri);
		if (input == null) {
			return false;
		}
		return true;
	}

	public static void delContactPhoto(ContentResolver c, String personId) {
		String where = ContactsContract.Data.CONTACT_ID + " = " + personId + " AND " + ContactsContract.Data.MIMETYPE
				+ "=='" + ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE + "'";
		c.delete(ContactsContract.Data.CONTENT_URI, where, null);
		MainActivity.NotifyUpdate();
	}

	public static void setContactPhoto(ContentResolver c, byte[] bytes, long personId, boolean notify) {
		ContentValues values = new ContentValues();
		int photoRow = -1;
		String where = ContactsContract.Data.CONTACT_ID + " = " + personId + " AND " + ContactsContract.Data.MIMETYPE
				+ "=='" + ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE + "'";
		Cursor cursor = c.query(ContactsContract.Data.CONTENT_URI, null, where, null, null);
		int raw = 0;
		if (cursor.moveToFirst()) {
			photoRow = cursor.getInt(cursor.getColumnIndexOrThrow(ContactsContract.Data._ID));
			raw = cursor.getInt(cursor.getColumnIndexOrThrow(ContactsContract.Data.RAW_CONTACT_ID));
		}
		cursor.close();
		if (raw <= 0) {
			cursor = c.query(ContactsContract.Data.CONTENT_URI, null, ContactsContract.Data.CONTACT_ID + " = "
					+ personId, null, null);
			if (cursor.moveToFirst()) {
				raw = cursor.getInt(cursor.getColumnIndexOrThrow(ContactsContract.Data.RAW_CONTACT_ID));
			}
			cursor.close();
		}
		if (raw <= 0) {
			Log.e("error", "RAW_CONTACT_ID GET ERROR");
			return;
		}
		values.put(ContactsContract.Data.RAW_CONTACT_ID, raw);
		values.put(ContactsContract.Data.IS_SUPER_PRIMARY, 1);
		values.put(ContactsContract.CommonDataKinds.Photo.PHOTO, bytes);
		values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE);
		if (photoRow >= 0) {
			c.update(ContactsContract.Data.CONTENT_URI, values, ContactsContract.Data._ID + " = " + photoRow, null);
		} else {
			c.insert(ContactsContract.Data.CONTENT_URI, values);
		}
		if (notify) {
			MainActivity.NotifyUpdate();
		}
	}

	public static boolean DeleteEmail(ContentResolver c, String contact_id, String email) {
		String where = ContactsContract.Data.CONTACT_ID + " = " + contact_id + " AND " + ContactsContract.Data.MIMETYPE
				+ "=='" + ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE + "' AND "
				+ ContactsContract.CommonDataKinds.Email.ADDRESS + " = '" + email + "'";
		if (c.delete(ContactsContract.Data.CONTENT_URI, where, null) > 0) {
			return true;
		}
		return false;
	}

	public static boolean EditEmail(ContentResolver c, String contact_id, String old_email, String new_qq) {
		int Row = -1;
		String where = ContactsContract.Data.CONTACT_ID + " = " + contact_id + " AND " + ContactsContract.Data.MIMETYPE
				+ "=='" + ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE + "' AND "
				+ ContactsContract.CommonDataKinds.Email.ADDRESS + " = '" + old_email + "'";
		Cursor cursor = c.query(ContactsContract.Data.CONTENT_URI, null, where, null, null);
		if (cursor.moveToFirst()) {
			Row = cursor.getInt(cursor.getColumnIndexOrThrow(ContactsContract.Data._ID));
		}
		cursor.close();
		if (Row <= 0) {
			return false;
		}
		ContentValues values = new ContentValues();
		values.put(ContactsContract.CommonDataKinds.Email.ADDRESS, new_qq + "@qq.com");
		if (c.update(ContactsContract.Data.CONTENT_URI, values, ContactsContract.Data._ID + " = " + Row, null) > 0) {
			return true;
		}
		return false;
	}

	public static boolean AddEmail(ContentResolver c, String contact_id, String qq) {
		Cursor cursor = c.query(ContactsContract.Data.CONTENT_URI, null, ContactsContract.Data.CONTACT_ID + " = "
				+ contact_id, null, null);
		int raw = 0;
		if (cursor.moveToFirst()) {
			raw = cursor.getInt(cursor.getColumnIndexOrThrow(ContactsContract.Data.RAW_CONTACT_ID));
		}
		cursor.close();
		if (raw < 1) {
			return false;
		}
		ContentValues values = new ContentValues();
		values.put(ContactsContract.Data.RAW_CONTACT_ID, raw);
		values.put(ContactsContract.CommonDataKinds.Email.ADDRESS, qq + "@qq.com");
		values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE);
		if (c.insert(ContactsContract.Data.CONTENT_URI, values) != null) {
			return true;
		}
		return false;
	}

	public static int AddMapEmail(ContentResolver c, HashMap<String, String> map) {
		int uu = 0;
		synchronized (Contact.selfList) {
			int n = Contact.selfList.size();
			Contact contact;
			String qq = null;
			for (int i = 0; i < n; i++) {
				contact = Contact.selfList.get(i);
				if (contact.getQq() == null) {
					if (map.containsKey(contact.getName())) {
						qq = map.get(contact.getName());
						if (AddEmail(c, contact.getId(), qq)) {
							uu++;
						}
					}
				}
			}
		}
		if (uu > 0) {
			MainActivity.NotifyDataRefresh();
		}
		return uu;
	}
}
