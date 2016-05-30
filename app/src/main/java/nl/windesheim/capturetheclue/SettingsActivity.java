package nl.windesheim.capturetheclue;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        TextView userNameDisplay = (TextView) findViewById(R.id.userNameDisplay);
        String username = MainActivity.settings.getString("username", "not_found");
        userNameDisplay.setText(username);

        TextView userIDDisplay = (TextView) findViewById(R.id.userIdDisplay);
        userIDDisplay.setText(MainActivity.currentUserID + "");
    }

    public void onClickSettings(View view) {
        switch (view.getId()) {
            case R.id.backButton:
                finish();
                break;
            case R.id.logoutButton:
                Log.d("DEBUG", "Pompompom");
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
