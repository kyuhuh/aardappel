package nl.windesheim.capturetheclue;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import nl.windesheim.capturetheclue.Connection.Server;

public class MainActivity extends AppCompatActivity {

    public static String serverStatus = "";
    public static TextView tv;
    public static TextView userNameDisplay;
    public static boolean loggedIn = false;
    public static final String PREFS_NAME = "prefs";
    SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.status);
        tv.setText(serverStatus);
        settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        userNameDisplay = (TextView) findViewById(R.id.userNameDisplay);
        String username = settings.getString("username", "not_found");
        userNameDisplay.setText(username);

        // Test connection to Server, currently disabled because it takes a long time when the server is down.
        new Server().testConnection();
        if (username == "not_found") {
            //
            Intent myIntent = new Intent(MainActivity.this, Account_Activity.class);
            //myIntent.putExtra("key", value); //Optional parameters
            MainActivity.this.startActivity(myIntent);
        }


    }

    public void onClickMain(View view) {
        switch (view.getId())
        {
            case R.id.start:
                setContentView(R.layout.activity_start);
                break;

            case R.id.currentgame:
                //
                break;

            case R.id.settings:
                //
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


        }
    }
}
