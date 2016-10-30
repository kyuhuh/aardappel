
package nl.windesheim.capturetheclue;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView userNameDisplay = (TextView) findViewById(R.id.userNameDisplay);
        String username = MainActivity.settings.getString("username", "not_found");
        userNameDisplay.setText(username);

        TextView userIDDisplay = (TextView) findViewById(R.id.userIdDisplay);
        userIDDisplay.setText(MainActivity.currentUserID + "");
    }

    public void onClickSettings(View view) {
        switch (view.getId()) {
            case R.id.logoutButton:
                Context c = this;

                SharedPreferences.Editor editor = MainActivity.settings.edit();
                editor.clear();
                editor.commit();
                MainActivity.loggedIn = false;
                MainActivity.currentUserID = -1;

                Intent myIntent = new Intent(SettingsActivity.this, MainActivity.class);
                //myIntent.putExtra("key", value); //Optional parameters
                SettingsActivity.this.startActivity(myIntent);
                break;
        }
    }
}
