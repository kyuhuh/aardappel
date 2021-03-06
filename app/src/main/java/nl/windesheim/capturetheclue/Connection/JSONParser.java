package nl.windesheim.capturetheclue.Connection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.*;

import nl.windesheim.capturetheclue.MainActivity;
import nl.windesheim.capturetheclue.TestMatchActivity;

//JSON PARSER 작동안하는듯함
public class JSONParser extends AsyncTask<String, String, JSONObject> {

    HttpURLConnection urlConnection;

    @Override
    protected JSONObject doInBackground(String... args) {
        Log.d("DEBUG", "HELLO FROM THE OTHER SIDE????? HOI????");
        StringBuilder result = new StringBuilder();
        String status = null;
        String error = null;
        JSONObject json;

        // Try to connect to the server, if it doesnt set status accordingly.
        try {
            URL url = new URL(Server.SERVER_URL);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(10000); //set timeout to 5 seconds
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

        } catch (SocketTimeoutException ste) {
            Log.d("DEBUG", "IT HAPPENED! :o ");
            status = "Timed Out";
            error = ste.getMessage();
        } catch (Exception e) {
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
            Log.d("STRING TEST!!!!!! ", JSONresult);
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
        Server.setResult(json); //null point exception

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
        Log.d("DEBUG","★ DoLogin checked");
        StringBuilder result = new StringBuilder();
        String status = "";
        String error = null;
        JSONObject json = new JSONObject();

        // Try to connect to the server, if it doesnt set status accordingly.
        try {
            Log.d("DEBUG","★ Connection checking");
            // Connection part
            URL url = new URL(Server.SERVER_URL + "/login.php");
            urlConnection = (HttpURLConnection) url.openConnection();
            Log.d("DEBUG","★ "+urlConnection.toString());

            List<Pair> params = new ArrayList<Pair>();
            params.add(new Pair("user", pass));
            params.add(new Pair("pass", mail));

            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setRequestProperty("Content-Language", "en-US");
            urlConnection.setUseCaches(false);
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream (urlConnection.getOutputStream ());
            wr.writeBytes (Server.getQuery(params)); //params를 전달하지 않음
            Log.d("★DEBUG","SERVER.GETQUERY BLAH BLAH   "+Server.getQuery(params).toString());
            wr.flush ();
            wr.close ();
            Log.d("DEBUG", "send request end");


            // Response part //이부분이 안도는것같다
            /*
            InputStream is = urlConnection.getInputStream();
            Log.d("DEBUG","ONE MORE TIME");
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder();
            String line;
            while((line = rd.readLine()) != null) {
                Log.d("DEBUG", line);
                response.append(line);
                response.append('\r');
            }
            //return response.toString();
            Log.d("DEBUG", "Server response: " + response.toString());
            rd.close();
        } catch (Exception e) {
            error = "Could not connect to server: " + e.getMessage();
            e.printStackTrace();
        } finally {
//            Log.d("DEBUG", error);
            if (urlConnection != null) {
                urlConnection.disconnect();
                Log.d("DEBUG","disconnected");
            }
        }
        */
            //added on 01:07 09-nov-16
            InputStream is = urlConnection.getInputStream();
            int nullcheck = is.available();
            Log.d("DEBUG","inputstream null check " +nullcheck);
            StringBuffer response = new StringBuffer(urlConnection.getResponseMessage());
            Log.d("DEBUG","- server - StringBuffer response : "+response.toString());

            BufferedReader rd = new BufferedReader (new InputStreamReader(is)); //inside rd is nothing
//            Log.d("DEBUG","- server "+Util.streamToString(is));

            StringBuffer sb = new StringBuffer("");
            String line = "";

            while ((line = rd.readLine())!=null){
                sb.append(line);
            }
            rd.close();
            status=sb.toString();
            Log.d("DEBUG"," - server - status : "+status);

            //return response.toString();
            Log.d("DEBUG", "Server response: " + response.toString()); //reslonse: OK
            rd.close();
        }
        //catch (Exception e) {
        catch (IOException e) {
            error = "Could not connect to server: " + e.getMessage();
            Log.d("DEBUG","♥♥♥♥♥♥catch error♥♥♥♥♥♥: "+error);
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
                Log.d("DEBUG","disconnected");
            }
        }
//        Log.d("DEBUG", error);

        // Check if any errors where encountered.
        //dunno why json is null, what should jason be?
        String JSONresult="";
        Log.d("DEBUG","ERROR? "+error);


//error is null, this if statement doesnt work
        if(error != null) {
        JSONresult = "{ \"status\" : \" " + status + " \"}";
            Log.d("DEBUG","\"{ \\\"status\\\" : \\\" \" + status + \" \\\"}\" "+JSONresult.toString());
        try {
            json = new JSONObject(JSONresult.toString());
            Log.d("DEBUG","error!=null json: "+json.toString());
            return json;
        } catch (JSONException j) {
            error = "Could not convert error message to JSON Object, " + j.getMessage();
            Log.d("ERROR", "Error parsing JSON Object");
        }
        Log.d("DEBUG","ERROR IF NULL? "+error);
    }
    Log.d("DEBUG","ERROR SECOND? "+error);

    Log.d("DEBUG",json.toString()+"*****this is above return gggggggggggggggg");
    return json;
}

    protected void onPostExecute(JSONObject json) {

        Log.d("★DEBUG",json.toString()+"★★★★★★★★★★★");
        //Do something with the JSON string
        if(json != null) {
                Server.setLoginCredentials(json);
            //json is null
            //json is user
            //user is null
            //so it doesnt login

            //make jason works
        }

    }
}


class retrieveMatch extends AsyncTask<String, String, JSONObject> {

    HttpURLConnection urlConnection;
    private int id;

    public retrieveMatch(int i) {
        id = i;
    }

    @Override
    protected JSONObject doInBackground(String... args) {

        Log.d("DEBUG", "Trying to get match...");
        StringBuilder result = new StringBuilder();
        String status = null;
        String error = null;
        JSONObject json = new JSONObject();

        // Try to connect to the server, if it doesnt set status accordingly.
        try {

            // Connection part
            URL url = new URL(Server.SERVER_URL + "/match.php?mid=" + id);
            urlConnection = (HttpURLConnection) url.openConnection();
            Log.d("Debug", url.toString());

            /*List<Pair> params = new ArrayList<Pair>();
            params.add(new Pair("user", pass));
            params.add(new Pair("pass", mail));*/

            urlConnection = (HttpURLConnection) url.openConnection();
            //urlConnection.setRequestMethod("POST");
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");

            //urlConnection.setRequestProperty("Content-Length", "" +
            //Integer.toString(params.));
            //urlConnection.setRequestProperty("Content-Language", "en-US");

            urlConnection.setUseCaches(false);
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            // For debug purposes
            /*for(Pair p : params)
            {
                Log.d("PAIR", p.first + " and " + p.second);
            }*/

            //Send request
            DataOutputStream wr = new DataOutputStream(
                    urlConnection.getOutputStream());
            //wr.writeBytes (Server.getQuery(params));
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
            } catch (JSONException j) {
                error = "Could not parse JSON, " + j.getMessage();
                Log.d("ERROR", "Couldnt parse JSON. Is it valid?");
            }
            //return response.toString();

        } catch (Exception e) {
            error = "Could not connect to server: " + e.getLocalizedMessage();
            //e.printStackTrace();

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

        return json;
    }

    protected void onPostExecute(JSONObject json) {

        Log.d("★DEBUG", json.toString());
        //Do something with the JSON string
        if (json != null) {
            try {
                Server.returnMatch(json);
            } catch (JSONException e) {

                Log.d("ERROR", e.getMessage());
            }
        }

    }
}




class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;

    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (ConnectException ce) {
            TestMatchActivity.showPopup("Not connected! (" + ce.getMessage() + ")");
        } catch (OutOfMemoryError oome) {
            TestMatchActivity.showPopup("Not enough memory! (" + oome.getMessage() + ")");
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}




class retrieveMatchesForUser extends AsyncTask<String, String, JSONObject> {

    HttpURLConnection urlConnection;
    private int id;

    public retrieveMatchesForUser(int i) {
        id = i;
    }

    @Override
    protected JSONObject doInBackground(String... args) {

        Log.d("DEBUG", "Trying to get matchlist for user " + id);
        String status = null;
        String error = null;
        JSONObject json = new JSONObject();

        // Try to connect to the server, if it doesnt set status accordingly.
        try {

            // Connection part
            URL url = new URL(Server.SERVER_URL + "/usermatches.php?num=" + id);
            urlConnection = (HttpURLConnection) url.openConnection();
            Log.d("Debug", url.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            //urlConnection.setRequestMethod("POST");
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");

            urlConnection.setUseCaches(false);
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream(
                    urlConnection.getOutputStream());
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

            Log.d("DEBUG", "Hier de response: " + response.toString());

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

        return json;
    }

    protected void onPostExecute(JSONObject json) {

        Log.d("DEBUG", "Post Execute on get matches json string: " + json.toString());
        try {
            MainActivity.updateList(json);
        } catch (JSONException je) {
            Log.d("DEBUG", "Something went from with parsing JSON in mainactivity: " + je.getMessage());
        }

    }
}



class startNewMatch extends AsyncTask<String, String, JSONObject> {

    HttpURLConnection urlConnection;
    private int id;
    private int side;

    public startNewMatch(int i, int s) {
        id = i;
        side = s;
    }

    @Override
    protected JSONObject doInBackground(String... args) {

        Log.d("DEBUG", "Trying to create match for user " + id);
        String status = null;
        String error = null;
        JSONObject json = new JSONObject();

        // Try to connect to the server, if it doesnt set status accordingly.
        try {

            // Connection part
            URL url = new URL(Server.SERVER_URL + "/queue.php?uid=" + id + "&side=" + side);
            urlConnection = (HttpURLConnection) url.openConnection();
            Log.d("Debug", url.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            //urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");

            urlConnection.setUseCaches(false);
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream(
                    urlConnection.getOutputStream());
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
            Log.d("DEBUG", JSONresult+"ttttttttttttttttttttttttttttttttttttttttttttt");
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

        Log.d("DEBUG", "Response from trying to create match: " + json.toString());
        /*try {
            MainActivity.updateList(json);
        } catch (JSONException je) {
            Log.d("DEBUG", "Something went from with parsing JSON in mainactivity: " + je.getMessage());
        }*/

    }
}

