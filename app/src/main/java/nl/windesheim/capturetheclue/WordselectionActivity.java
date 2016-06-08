package nl.windesheim.capturetheclue;


import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.ColorRes;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;


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
    private static final String TAG_ID = "id";


    JSONArray words;
    int matchID;

    ArrayList<HashMap<String, String>> wordList;

    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wordselection);

        getSupportActionBar().setDisplayShowTitleEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        SpannableString s = new SpannableString("Capture the Clue");
        s.setSpan(new nl.windesheim.capturetheclue.TypefaceSpan(this, "olivier_demo.ttf"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        SpannableString sb = new SpannableString("Choose the word!");
        sb.setSpan(new nl.windesheim.capturetheclue.TypefaceSpan(this, "olivier_demo.ttf"), 0, sb.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        // Update the action bar title with the TypefaceSpan instance
        getSupportActionBar().setTitle(s);
        getSupportActionBar().setSubtitle(sb);


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
                String id = c.getString(TAG_ID);

                HashMap<String, String> words = new HashMap<String, String>();

                words.put(TAG_WORD, word);
                words.put(TAG_ID,id);

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
                    Log.d("debug", words.toString());

                    Match m = new Match();
                    m.setID(matchID);
                    Word w = new Word();
                    w.setWord(selectedWord);
                    w.setID(1);
                    m.setWord(w);
                    m.setStatus("take_picture1");
                    m.save();

                    Intent gameStartIntent = new Intent(getApplicationContext(), GameFirstActivity.class);
                    gameStartIntent.putExtra("selectedWord",selectedWord);
                    gameStartIntent.putExtra("matchId", matchID);
                    startActivity(gameStartIntent);
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
}