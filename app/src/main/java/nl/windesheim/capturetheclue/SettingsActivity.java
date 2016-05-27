package nl.windesheim.capturetheclue;

import android.app.Activity;
import android.os.Bundle;
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
        }
    }
}
