package nl.windesheim.capturetheclue.Account;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Peter on 4/7/2016.
 */
public class User {

    public String username;
    public String token;
    private int userID = -1;
    private SharedPreferences sharedPreferences;
    private static String PREF_NAME = "prefs";

    public User(String uname, String t) {
        username = uname;
        token = t;
    }

    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static String getUsername(Context context) {
        return getPrefs(context).getString("username", "default_username");
    }

    public void storeData(Context context) {
        SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putString("username", username);
        editor.putString("token", token);
        editor.putString("uid", userID + "");
        editor.commit();
    }

    public void setUserID(String i) {
        userID = Integer.parseInt(i);
    }


    public String getToken(Context context) {
        return getPrefs(context).getString("token", "no_token");
    }
}
