package nl.windesheim.capturetheclue;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import java.io.File;

public class GameFirstActivity extends AppCompatActivity {

    //show the word on the top

    /*
    ImageView imageView;
    File outputFile;
*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        /*
        imageView = (ImageView) findViewById(R.id.imageView);
        File storageDir = Environment.getExternalStorageDirectory();
        outputFile = new File(storageDir, "output.jpg");
        */

        //error : save after crashed
    }

}
