package nl.windesheim.capturetheclue;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickMain(View view) {
        switch (view.getId())
        {
            case R.id.friendgame:
                setContentView(R.layout.activity_friend);
                break;

            case R.id.randomgame:
                //start game
                //setContentView(R.layout.);
                break;

        }
    }
}
