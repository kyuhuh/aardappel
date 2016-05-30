package nl.windesheim.capturetheclue;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nl.windesheim.capturetheclue.Models.Match;
import nl.windesheim.capturetheclue.Models.Word;

public class WordselectionActivity extends AppCompatActivity {

    String myJSON;

    private static final String TAG_RESULTS = "result";
    private static final String TAG_WORD = "word";

    JSONArray words;
    int matchID;

    ArrayList<HashMap<String, String>> wordList;

    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wordselection);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);


        Bundle extras = getIntent().getExtras();
        matchID = extras.getInt("matchid");

        list = (ListView) findViewById(R.id.listView);
        wordList = new ArrayList<HashMap<String, String>>();
        getData("http://patatjes.esy.es/words.php");
    }

    protected void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            words = jsonObj.getJSONArray(TAG_RESULTS);

            for (int i = 0; i < words.length(); i++) {
                JSONObject c = words.getJSONObject(i);
                String word = c.getString(TAG_WORD);

                HashMap<String, String> words = new HashMap<String, String>();

                words.put(TAG_WORD, word);

                wordList.add(words);
            }

            ListAdapter adapter = new SimpleAdapter(
                    WordselectionActivity.this, wordList, R.layout.list_item,
                    new String[]{TAG_WORD},
                    new int[]{R.id.word}
            );

            list.setAdapter(adapter);

            //onClick
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    String selectedWord = list.getItemAtPosition(position).toString();
                    Log.d("debug", selectedWord);

                    Match m = new Match();
                    m.setID(matchID);
                    Word w = new Word();
                    w.setWord(selectedWord);
                    m.setWord(w);
                    m.save();

                    //Intent gameStartIntent = new Intent(getApplicationContext(), GameFirstActivity.class);

                    // todo: instead of this, save the word to the match (based on ID) and continue to the first photo activity
                    //gameStartIntent.putExtra("selectedWord",selectedWord);
                    //startActivity(gameStartIntent);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getData(String url) {
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];

                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }

                    return sb.toString().trim();

                } catch (Exception e) {
                    return null;
                }

            }

            @Override
            protected void onPostExecute(String result) {
                myJSON = result;
                showList();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }





    // post text to server

    /*
    public class PostDataAsyncTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
            // do stuff before posting data
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                // 1 = post text data, 2 = post file
                int actionChoice = 2;

                // post a text data
                if(actionChoice==1){
                    postText();
                }

            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String lenghtOfFile) {
            // do stuff after posting data
        }
    }

    // this will post our text data
    private void postText(){
        try{
            // url where the data will be posted
            String postReceiverUrl = "http://patatjes.esy.es/post.php";
            Log.d("debug", "postURL: " + postReceiverUrl);

            // HttpClient
            HttpClient httpClient = new DefaultHttpClient();

            // post header
            HttpPost httpPost = new HttpPost(postReceiverUrl);

            // add your data
            List<NameValuePair> selectedWords = new ArrayList<NameValuePair>(2);
            selectedWords.add(new BasicNameValuePair("word", "Mike"));

            httpPost.setEntity(new UrlEncodedFormEntity(selectedWords));

            // execute HTTP post request
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity resEntity = response.getEntity();

            if (resEntity != null) {

                String responseStr = EntityUtils.toString(resEntity).trim();
                Log.v("debug", "Response: " +  responseStr);

                // you can add an if statement here and do other actions based on the response
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    */
}