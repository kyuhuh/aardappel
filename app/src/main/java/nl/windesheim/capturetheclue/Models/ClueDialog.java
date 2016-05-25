package nl.windesheim.capturetheclue.Models;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import nl.windesheim.capturetheclue.R;

/**
 * Created by Peter on 4/19/2016.
 */
public class ClueDialog extends Dialog implements View.OnClickListener {

    ImageView img;

    public ClueDialog(Context context, String text) {
        super(context);
        setContentView(R.layout.cluedialog);
        TextView message = (TextView) findViewById(R.id.message);
        Button OK = (Button) findViewById(R.id.ok_button);
        OK.setOnClickListener(this);
        message.setText(text);
        getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        img = (ImageView) findViewById(R.id.resultImage);
    }

    public void setImage(int d) {

        img.setImageResource(d);
    }

    @Override
    public void onClick(View v) {

        this.dismiss();
    }


}
