package nl.windesheim.capturetheclue;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        }
    }
}
