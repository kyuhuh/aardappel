package nl.windesheim.capturetheclue.Models;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import nl.windesheim.capturetheclue.Connection.Server;
import nl.windesheim.capturetheclue.MainActivity;

/**
 * Created by Peter on 4/12/2016.
 */
public class Match {

    private String game_status;
    private int id;
    private int ratingid;
    private int player1id;
    private List<Picture> pictures;
    private Word word;

    public String toString() {
        return "Match #" + id;
    }

    public int getID() {
        return id;
    }

    // todo: remove this function
    public void setID(int i) {
        id = i;
    }

    public String getWord() {
        return word.getWord();
    }

    public int getWordID() {
        return word.getID();
    }

    public void setWord(Word w) { word = w; }

    public Picture getFirstPicture() {
        return pictures.get(0);
    }

    public Picture getSecondPicture() {
        return pictures.get(1);
    }

    public Picture getThirdPicture() {
        return pictures.get(2);
    }

    public String getStatus() {
        return game_status;
    }

    public void setStatus(String S) { game_status = S; }

    public Boolean save() {

        com.google.gson.Gson gson = new GsonBuilder().registerTypeAdapter(Match.class, new MatchSerializer())
                .create();
        String request = gson.toJson(this);

        Log.d("REQUEST", request);

        new saveMatch(request).execute();
        return true;
    }
}

class MatchSerializer implements JsonSerializer<Match> {
    public JsonElement serialize(final Match match, final Type type, final JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        result.add("id", new JsonPrimitive(match.getID()));
        result.add("word", new JsonPrimitive(match.getWordID()));
        result.add("game_status", new JsonPrimitive(match.getStatus()));
        return result;
    }
}

class saveMatch extends AsyncTask<String, String, String> {

    HttpURLConnection urlConnection;
    private String match;

    public saveMatch(String m) {
        match = m;
    }

    @Override
    protected String doInBackground(String... args) {


        String status = null;
        String error = null;
        JSONObject json = new JSONObject();

        // Try to connect to the server, if it doesnt set status accordingly.
        try {

            // Connection part
            URL url = new URL(Server.SERVER_URL + "/saveMatch.php");
            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");

            urlConnection.setUseCaches(false);
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream(
                    urlConnection.getOutputStream());
            wr.writeBytes(match);
            wr.flush();
            wr.close();

            // Response part

            InputStream is = urlConnection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();

            try {
                json = new JSONObject(response.toString());
                if(json.getString("status").equals("OK")) {
                    status = "OK";
                }
                else {
                    status = "FAILED";
                }
            } catch (JSONException j) {
                error = "Could not parse JSON, " + j.getMessage();
                Log.d("ERROR", "Couldnt parse JSON. Is it valid?");
                status = "FAILED";
            }

        } catch (Exception e) {
            error = "Could not connect to server: " + e.getLocalizedMessage();
            status = "FAILED";

        } finally {

            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return status;
    }

    protected void onPostExecute(String s) {

        if(s.equals("OK")) {
            Log.d("DEBUG", "Match object saved.");
        }
        else {
            Log.d("DEBUG", "Something went wrong.");
        }

    }
}
