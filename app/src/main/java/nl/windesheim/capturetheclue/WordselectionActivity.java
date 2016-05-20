package nl.windesheim.capturetheclue;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class WordSelectionActivity extends AppCompatActivity {

    String myJSON;

    private static final String TAG_RESULTS = "result";
    private static final String TAG_ID = "id";
    private static final String TAG_WORD = "word";

    JSONArray words = null;

    ArrayList<HashMap<String, String>> wordList;

    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wordselection);

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
                String id = c.getString(TAG_ID);
                String word = c.getString(TAG_WORD);

                HashMap<String, String> words = new HashMap<String, String>();

                words.put(TAG_ID,id);
                words.put(TAG_WORD, word);

                wordList.add(words);
            }

            ListAdapter adapter = new SimpleAdapter(
                    WordSelectionActivity.this, wordList, R.layout.list_item,
                    new String[]{TAG_ID,TAG_WORD},
                    new int[]{R.id.id,R.id.word}
            );

            list.setAdapter(adapter);

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