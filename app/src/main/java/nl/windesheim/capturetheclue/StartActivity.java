package nl.windesheim.capturetheclue;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    public void onClickStart(View view) {
        switch (view.getId())
        {
            case R.id.friendgame:
                setContentView(R.layout.activity_friend);
                break;

            case R.id.randomgame:
                setContentView(R.layout.activity_wordselection);
                break;

        }
    }
}
