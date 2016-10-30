package nl.windesheim.capturetheclue.Connection;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;
import android.util.Pair;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nl.windesheim.capturetheclue.Account.User;
import nl.windesheim.capturetheclue.Account_Activity;
import nl.windesheim.capturetheclue.MainActivity;
import nl.windesheim.capturetheclue.Models.Match;
import nl.windesheim.capturetheclue.Models.Picture;
import nl.windesheim.capturetheclue.TestMatchActivity;
import nl.windesheim.capturetheclue.WordselectionActivity;

public class Server {

    //static final String SERVER_URL = "http://46.129.41.143:666";  // Remote server, doesnt work from Windesheim
    //static final String SERVER_URL = "http://localhost:8080";   // Local server, doesnt work on emu
    //static final String SERVER_URL = "http://10.0.2.2:8080";      // Local server, Emulator ip
    public static final String SERVER_URL = "http://patatjes.esy.es";      // Webserver


    public void testConnection() { new JSONParser().execute(); }

    public void testLogin(String p, String w) {
        new DoLogin(p,w).execute();
    }

    public void getMatch(int id) {
        Log.d("Debug", "Retrieving match in server");
        new retrieveMatch(id).execute();
    }

    public static void setLoginCredentials(JSONObject user) {
        try {
            if (user != null) {
                Log.d("DUMP", user.getString("status"));
                if (user.getString("status").contentEquals("OK")) {
                    Log.d("DEBUG", user.getString("token"));
                    User u = new User(user.getString("username"), user.getString("token"));
                    u.setUserID(user.getString("id"));
                    Account_Activity.handleLogin(u);

                } else if (user.getString("status").contentEquals("User does not exist.")) {
                    Account_Activity.showPopup("Username does not exist. Did you type it correctly?");
                } else if (user.getString("status").contentEquals("Passwords do not match.")) {
                    Account_Activity.showPopup("Passwords do not match.");
                } else {
                    throw new JSONException("Data is corrupted");
                }
            } else {
                throw new JSONException("Data couldnt be retrieved");
            }
        } catch (JSONException je) {
            Account_Activity.showPopup("Could not log in, server didn't respond on time. Please try again later.");
        }
        Account_Activity.progDia.hide();
    }

    public static void getUserMatches(int id) {
        Log.d("Debug", "Retrieving matches for user [SERVER.java]");
        new retrieveMatchesForUser(id).execute();
    }

    public static void startNewMatch(int i, int s) {
        Log.d("Debug", "Creating match for user [SERVER.java]");
        new startNewMatch(i, s).execute();
    }

    public static void setResult(JSONObject j){

        try {
            MainActivity.tv.setText(j.getString("status"));
        } catch (JSONException e) {
            Log.d("ERROR", "Couldnt get status");
        }
    }

    public static void returnMatch(JSONObject response) throws JSONException {
        if (response != null) {

            if (response.getString("status").contentEquals("OK")) {

                // Parse the server response into a Match object
                Match m = new Gson().fromJson(response.toString(), Match.class);
                // Do something with the gamestate
                if (m.getStatus().equals("set_word")) {
                    // Word selection called
                    MainActivity.startWordSelection(m.getID());
                }
                else if (m.getStatus().equals("take_picture1")) {
                    // Start first picture activity
                    MainActivity.takePicture(m.getID());
                }
                else if (m.getStatus().equals("game_started")) {
                    TestMatchActivity.startMatch(m);
                    Intent inent = new Intent(MainActivity.mContext, TestMatchActivity.class);
                    MainActivity.mContext.startActivity(inent);
                }
            } else if (response.getString("status").contentEquals("Match not found.")) {
                Log.d("DEBUG", "Match not found");
                // Do something useful here
            }
        } else {
            throw new JSONException("Data couldnt be retrieved");
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

    public static void downloadImage(ImageView iv, Picture p) {
        String url = Server.SERVER_URL + "/pictures/" + p.getFileurl();
        new DownloadImageTask(iv)
                .execute(url);
    }
}