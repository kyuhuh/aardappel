package nl.windesheim.capturetheclue;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class FullscreenActivity
        extends Activity
{
    @SuppressLint({"NewApi"})
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);
        Bitmap b = (Bitmap)getIntent().getExtras().getParcelable("imagebitmap");
        ImageView imgdisplay = (ImageView)findViewById(R.id.imgDisplay);
        ((Button)findViewById(R.id.btnClose)).setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View paramAnonymousView)
            {
                FullscreenActivity.this.finish();
            }
        });
        imgdisplay.setImageBitmap(b);
    }
}
