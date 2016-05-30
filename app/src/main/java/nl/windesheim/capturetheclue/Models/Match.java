package nl.windesheim.capturetheclue.Models;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
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

    public Boolean save() {
        new saveMatch(this).execute();
        return true;
    }
}

class saveMatch extends AsyncTask<String, String, JSONObject> {

    HttpURLConnection urlConnection;
    private Match match;

    public saveMatch(Match m) {
        match = m;
    }

    @Override
    protected JSONObject doInBackground(String... args) {


        String status = null;
        String error = null;
        JSONObject json = new JSONObject();

        // Try to connect to the server, if it doesnt set status accordingly.
        try {

            // Connection part
            URL url = new URL(Server.SERVER_URL + "/saveMatch.php?mid=" + match.getID());
            urlConnection = (HttpURLConnection) url.openConnection();

            List<Pair> params = new ArrayList<Pair>();
            params.add(new Pair("json", match));

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
            wr.writeBytes(Server.getQuery(params));
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

            Log.d("DEBUG", "Response: " + response.toString());

            try {
                json = new JSONObject(response.toString());
            } catch (JSONException j) {
                error = "Could not parse JSON, " + j.getMessage();
                Log.d("ERROR", "Couldnt parse JSON. Is it valid?");
            }

        } catch (Exception e) {
            error = "Could not connect to server: " + e.getLocalizedMessage();

        } finally {

            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        // Check if any errors where encountered.
        String JSONresult;
        if (error != null) {
            JSONresult = "{ \"status\" : \" " + status + " \", " +
                    " \"error\" : \" " + error + " \" }";
            try {
                json = new JSONObject(JSONresult.toString());
            } catch (JSONException j) {
                error = "Could not convert error message to JSON Object, " + j.getMessage();
                Log.d("ERROR", "Error parsing JSON Object");
            }
        }
        else {
            Log.d("DEBUG", error);
        }

        return json;
    }

    protected void onPostExecute(JSONObject json) {

        Log.d("DEBUG", "Response from trying to save match: " + json.toString());

    }
}
