package nl.windesheim.capturetheclue;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class FriendActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
    }

    //load user's friend setting automatically
    //if user does not log in with facebook, connect to fb

}
