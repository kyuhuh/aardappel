package nl.windesheim.capturetheclue;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import nl.windesheim.capturetheclue.Connection.Server;

public class MainActivity extends AppCompatActivity {

    public static String serverStatus = "";
    public static TextView tv;
    public static Context mContext;
    public static TextView userNameDisplay;
    public static boolean loggedIn = false;
    public static final String PREFS_NAME = "prefs";
    SharedPreferences settings;

    ImageView imageView;
    File outputFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.status);
        mContext = this;
        tv.setText(serverStatus);
        settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        userNameDisplay = (TextView) findViewById(R.id.userNameDisplay);
        String username = settings.getString("username", "not_found");
        userNameDisplay.setText(username);

        // Test connection to Server, currently disabled because it takes a long time when the server is down.
        //new Server().testConnection();
        if (username.equals("not_found")) {
            //
            Intent myIntent = new Intent(MainActivity.this, Account_Activity.class);
            //myIntent.putExtra("key", value); //Optional parameters
            MainActivity.this.startActivity(myIntent);
        }

//camera
        imageView = (ImageView) findViewById(R.id.imageView);
        File storageDir = Environment.getExternalStorageDirectory();
        outputFile = new File(storageDir, "output.jpg");
    }

    //for camera
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1001) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            Bitmap bitmap = BitmapFactory.decodeFile(outputFile.getAbsolutePath(), options);
            imageView.setImageBitmap(bitmap);
        }
    }

    public static void showPopup(String text) {
        Log.d("Debug", "Showing popup");
        Toast.makeText(mContext, text, Toast.LENGTH_LONG).show();
    }



    public void onClickMain(View view) {
        switch (view.getId()) {
            case R.id.start:
                Log.d("DEBUG", "Clicked start game");
                //Intent startIntent = new Intent(getApplicationContext(), StartActivity.class);
                //startActivity(startIntent);
                setContentView(R.layout.activity_start);
                break;

            case R.id.currentgame:
                //setContentView(R.layout.);
                Log.d("DEBUG", "Clicked current game");
                new Server().getMatch(1);

                break;

            case R.id.settings:
                setContentView(R.layout.activity_camera);
                break;


            case R.id.camerabutton:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outputFile));
                startActivityForResult(intent, 1001);
                break;


            case R.id.logoutButton:

                Context c = this;
                settings = c.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();

                editor.clear();
                editor.commit();
                loggedIn = false;

                Intent myIntent = new Intent(MainActivity.this, Account_Activity.class);
                //myIntent.putExtra("key", value); //Optional parameters
                MainActivity.this.startActivity(myIntent);
                break;

            case R.id.openPrefsDialogButton:
                Dialog d = new Dialog(this);
                d.setTitle("Saved Preferences");
                d.setContentView(R.layout.activity_friend);
                d.show();
                break;

            case R.id.friendGame:
                //setContentView(R.layout.activity_friend);
                Intent friendIntent = new Intent(getApplicationContext(), FriendActivity.class);
                startActivity(friendIntent);
                break;

            case R.id.randomGame:
                //setContentView(R.layout.activity_wordselection);
                Intent randomIntent = new Intent(getApplicationContext(), WordSelectionActivity.class);
                startActivity(randomIntent);
                break;


        }
    }
}
