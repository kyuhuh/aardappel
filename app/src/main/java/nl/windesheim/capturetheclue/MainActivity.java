package nl.windesheim.capturetheclue;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import nl.windesheim.capturetheclue.Connection.JSONParser;
import nl.windesheim.capturetheclue.Connection.Server;

public class MainActivity extends AppCompatActivity {

    public static String serverStatus = "Not connected";
    public static Button b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tv = (TextView) findViewById(R.id.status);
        b = (Button) findViewById(R.id.settings);
        b.setText(serverStatus);
        tv.setText(serverStatus);
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

                new Server().testConnection();
        }
    }
}
