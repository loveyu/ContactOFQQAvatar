package net.loveyu.contactofqqavatar.listener;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import net.loveyu.contactofqqavatar.ContactAction;
import net.loveyu.contactofqqavatar.R;

public class AvatarClick implements View.OnClickListener {
    private Context context;
    private String id;

    public AvatarClick(Context context, String id) {
        this.context = context;
        this.id = id;
    }

    @Override
    public void onClick(View v) {
        Dialog d = new Dialog(context);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View dv = View.inflate(context, R.layout.dialog_avatar_display, null);
        Bitmap bm = ContactAction.getHighPhoto(id, context.getContentResolver());
        if (bm != null) {
            ((ImageView) dv.findViewById(R.id.imageView)).setImageBitmap(bm);
        } else {
            ((ImageView) dv.findViewById(R.id.imageView)).setImageResource(R.drawable.avatar);
        }
        d.setContentView(dv);
        d.show();
    }
}
