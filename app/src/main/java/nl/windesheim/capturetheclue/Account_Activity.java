package nl.windesheim.capturetheclue;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;

import nl.windesheim.capturetheclue.Account.User;
import nl.windesheim.capturetheclue.Connection.Server;
import nl.windesheim.capturetheclue.R;

public class Account_Activity extends AppCompatActivity {

    private static Context mContext;
    EditText emailText;
    String email;
    EditText passText;
    String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        mContext = this;

        // Get login params
        emailText = (EditText) findViewById(R.id.emailBox);
        email = emailText.getText().toString();
        passText = (EditText) findViewById(R.id.passwordBox);
        pass = passText.getText().toString();
    }

    public void onClickMain(View view) {
        switch (view.getId()) {
            case R.id.loginButton:

                // Show login dialog until closed by response
                MainActivity.pD = new ProgressDialog(this);
                MainActivity.pD.setMessage("Logging in");
                MainActivity.pD.show();
                Log.d("Debug", "Logging in...");

                // Try to actually log in
                new Server().testLogin(email, pass);
                finish();


        }
    }

    public static void handleLogin(User user) {

        Log.d("DEBUG", "Logged in");
        user.storeData(mContext);
        String userName = user.username;
        MainActivity.userNameDisplay.setText(userName);
        MainActivity.pD.dismiss();
        MainActivity.loggedIn = true;

    }
}
