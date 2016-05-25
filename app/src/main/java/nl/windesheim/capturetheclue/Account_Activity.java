package nl.windesheim.capturetheclue;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import nl.windesheim.capturetheclue.Account.User;
import nl.windesheim.capturetheclue.Connection.Server;

public class Account_Activity extends AppCompatActivity {

    public static Context mContext;
    EditText emailText;
    String email;
    EditText passText;
    String pass;
    public static ProgressDialog progDia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progDia = new ProgressDialog(this);
        setContentView(R.layout.activity_account);
        mContext = this;
    }

    public static void showPopup(String text) {
        Log.d("Debug", "Showing popup");
        Toast.makeText(mContext, text, Toast.LENGTH_LONG).show();
    }

<<<<<<< HEAD
    public void onAccountClickMain(View view) {
=======
    public void onClickAccount(View view) {
>>>>>>> kyu
        switch (view.getId()) {
            case R.id.loginButton:

                progDia.setMessage("Trying to log in");
                progDia.show();

                // Get login params
                emailText = (EditText) findViewById(R.id.emailBox);
                email = emailText.getText().toString();
                passText = (EditText) findViewById(R.id.passwordBox);
                pass = passText.getText().toString();

                // Some debug
                Log.d("Debug", "Logging in...");
                Log.d("Debug", "User " + email + " with password " + pass);

                // Try to actually log in
                new Server().testLogin(email, pass);
                if (MainActivity.loggedIn) {
                    finish();
                }


        }
    }

    public static void handleLogin(User user) {

        Log.d("DEBUG", "Logged in");
        user.storeData(mContext);
        String userName = user.username;
        MainActivity.userNameDisplay.setText(userName);
        MainActivity.loggedIn = true;
        

    }
}
