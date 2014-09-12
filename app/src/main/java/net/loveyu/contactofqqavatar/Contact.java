package net.loveyu.contactofqqavatar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;

public class Contact {
    private String id;
    private String name;
    private HashMap<String, String> phone;
    private HashMap<String, String> email;
    public static List<Contact> selfList;
    public static ArrayList<Integer> selectList;
    private String p_c = null;
    private String e_c = null;

    public Contact(String id, String name) {
        this.id = id;
        this.name = name;
        phone = new HashMap<String, String>();
        email = new HashMap<String, String>();
    }

    public void setPhone(String type, String number) {
        p_c = null;
        phone.put(number, type);
    }

    public void setEmail(String type, String email) {
        e_c = null;
        this.email.put(email, type);
    }

    public String getId() {
        return id;
    }

    public String emailCache(boolean cache) {
        if (!cache || e_c == null) {
            String EmailStr = "";
            if (email.size() > 0) {
                Iterator<Entry<String, String>> iter = email.entrySet().iterator();
                while (iter.hasNext()) {
                    Entry<String, String> entry = iter.next();
                    String str = entry.getKey().toLowerCase(Locale.getDefault());
                    if (str.indexOf("@qq.com") > 0) {
                        EmailStr = str;
                    }
                }
            } else {
                EmailStr = "---";
            }
            e_c = EmailStr;
        }
        return e_c;
    }

    public String phoneCahce(boolean cache) {
        if (!cache || p_c == null) {
            String PhoneStr = "";
            if (phone.size() > 0) {
                Iterator<Entry<String, String>> iter = phone.entrySet().iterator();
                while (iter.hasNext()) {
                    Entry<String, String> entry = iter.next();
                    if ("".equals(PhoneStr)) {
                        PhoneStr = (String) entry.getKey();
                    } else {
                        PhoneStr += ", ..";
                    }
                }
            } else {
                PhoneStr = "---";
            }
            p_c = PhoneStr;
        }
        return p_c;
    }

    public String getName() {
        return name;
    }

    public HashMap<String, String> getPhone() {
        p_c = null;
        return phone;
    }

    public HashMap<String, String> getEmail() {
        e_c = null;
        return email;
    }

    public String findQqEmail(String qq) {
        qq = qq.trim();
        Iterator<Entry<String, String>> iter = email.entrySet().iterator();
        while (iter.hasNext()) {
            Entry<String, String> entry = iter.next();
            String str = entry.getKey().trim();
            if (str.indexOf(qq) == 0) {
                return str;
            }
        }
        return null;
    }

    public String getQq() {
        Iterator<Entry<String, String>> iter = email.entrySet().iterator();
        int i;
        while (iter.hasNext()) {
            Entry<String, String> entry = iter.next();
            String str = entry.getKey().toLowerCase(Locale.US);
            if ((i = str.indexOf("@qq.com")) > 0) {
                return str.substring(0, i);
            }
        }
        return null;
    }

}
