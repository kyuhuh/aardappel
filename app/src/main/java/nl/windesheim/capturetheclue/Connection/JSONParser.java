package nl.windesheim.capturetheclue.Connection;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import nl.windesheim.capturetheclue.MainActivity;

/**
 * Created by Peter on 4/6/2016.
 */
public class JSONParser extends AsyncTask<String, String, JSONObject> {

    HttpURLConnection urlConnection;

    @Override
    protected JSONObject doInBackground(String... args) {

        StringBuilder result = new StringBuilder();
        String status = null;
        String error = null;
        JSONObject json;

        // Try to connect to the server, if it doesnt set status accordingly.
        try {
            URL url = new URL(Server.SERVER_URL);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

        }catch( Exception e) {
            status = "No connection.";
            error = e.getMessage();
        }
        finally {
            urlConnection.disconnect();
        }

        // Convert results of request to a JSONObject.
        String JSONresult;
        if(error != null) {
            JSONresult = "{ \"status\" : \" " + status + " \"}";
            //Log.d("STRING TEST", JSONresult);
        }
        else {
            JSONresult = result.toString();
        }

        // Try to convert our result to a JSONObject to return
        try {
            json = new JSONObject(JSONresult);
        } catch (JSONException j) {
            json = null;
            Log.d("ERROR", "Couldnt parse JSON. Is it valid?");
        }

        return json;
    }

    protected void onPostExecute(JSONObject json) {

        //Do something with the JSON string
        Log.d("SERVER RESPONSE", json.toString());
        Server.setResult(json);

    }

}

class DoLogin extends AsyncTask<String, String, JSONObject> {

    HttpURLConnection urlConnection;
    private String pass;
    private String mail;

    public DoLogin(String p, String w) {
        pass = p;
        mail = w;
    }

    @Override
    protected JSONObject doInBackground(String... args) {

        Log.d("DEBUG", "Trying to log in...");
        StringBuilder result = new StringBuilder();
        String status = null;
        String error = null;
        JSONObject json = new JSONObject();

        // Try to connect to the server, if it doesnt set status accordingly.
        try {

            // Connection part
            URL url = new URL(Server.SERVER_URL + "/login.php");
            urlConnection = (HttpURLConnection) url.openConnection();

            List<Pair> params = new ArrayList<Pair>();
            params.add(new Pair("user", pass));
            params.add(new Pair("pass", mail));

            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");

            //urlConnection.setRequestProperty("Content-Length", "" +
            //Integer.toString(params.));
            urlConnection.setRequestProperty("Content-Language", "en-US");

            urlConnection.setUseCaches(false);
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            // For debug purposes
            /*for(Pair p : params)
            {
                Log.d("PAIR", p.first + " and " + p.second);
            }*/

            //Send request
            DataOutputStream wr = new DataOutputStream (
                    urlConnection.getOutputStream ());
            wr.writeBytes (Server.getQuery(params));
            wr.flush ();
            wr.close ();

            // Response part

            InputStream is = urlConnection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();

            Log.d("RESPONSE", response.toString());
            Log.d("DEBUG", "Trying to parse response to JSON");

            try {
                json = new JSONObject(response.toString());
            } catch (JSONException j) {
                error = "Could not parse JSON, " + j.getMessage();
                Log.d("ERROR", "Couldnt parse JSON. Is it valid?");
            }
            //return response.toString();

        } catch (Exception e) {
            error = "Could not connect to server: " + e.getMessage();
            e.printStackTrace();

        } finally {

            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        // Check if any errors where encountered.
        String JSONresult;
        if(error != null) {
            JSONresult = "{ \"status\" : \" " + status + " \"}";
            try {
                json = new JSONObject(JSONresult.toString());
            } catch (JSONException j) {
                error = "Could not convert error message to JSON Object, " + j.getMessage();
                Log.d("ERROR", "Error parsing JSON Object");
            }
        }

        return json;
    }

    protected void onPostExecute(JSONObject json) {

        //Do something with the JSON string
        if(json != null) {
            try {
                Server.setLoginCredentials(json);

            } catch (JSONException e) {
                Log.d("ERROR", e.getMessage());
            }
        }

    }
}