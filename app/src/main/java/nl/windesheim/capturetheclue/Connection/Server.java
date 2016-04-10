package nl.windesheim.capturetheclue.Connection;

import android.content.Intent;
import android.util.Log;
import android.util.Pair;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import nl.windesheim.capturetheclue.Account.User;
import nl.windesheim.capturetheclue.Account_Activity;
import nl.windesheim.capturetheclue.MainActivity;

/**
 * Created by Peter on 4/6/2016.
 */
public class Server {

    //static final String SERVER_URL = "http://46.129.41.143:666";  // Remote server, doesnt work from Windesheim
    //static final String SERVER_URL = "http://localhost:8080";   // Local server, doesnt work on emu
    static final String SERVER_URL = "http://10.0.2.2:8080";      // Local server, Emulator ip


    public void testConnection() { new JSONParser().execute(); }

    public void testLogin(String p, String w) {
        new DoLogin(p,w).execute();
    }

    public static void setLoginCredentials(JSONObject user) throws JSONException {

            if(user != null)
            {
                Log.d("DUMP", user.getString("status"));
                if(user.getString("status").contentEquals("OK")) {
                    Log.d("DEBUG", user.getString("token"));
                    User u = new User(user.getString("username"), user.getString("token"));
                    Account_Activity.handleLogin(u);
                }
                else
                {
                    throw new JSONException("Data is corrupted");
                }
            }
            else
            {
                throw new JSONException("Data couldnt be retrieved");
            }
        }

    public static void setResult(JSONObject j){

        try {
            MainActivity.tv.setText(j.getString("status"));
        } catch (JSONException e) {
            Log.d("ERROR", "Couldnt get status");
        }
    }

    public static String getQuery(List<Pair> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (Pair pair : params)
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.first.toString(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.second.toString(), "UTF-8"));
        }

        return result.toString();
    }
}
