package nl.windesheim.capturetheclue;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GameFirstActivity extends AppCompatActivity {

    //show the word on the top


//    ImageView imageView;
//    File outputFile;
    public static TextView selectedWordDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamefirst);

        selectedWordDisplay = (TextView)findViewById(R.id.selectedWordDisplay);
//        String username = settings.getString("username", "not_found");
//        userNameDisplay.setText(username);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("selectedWord");
            Log.d("debug",value);
            selectedWordDisplay.setText(value);
        }

        //ADD CAMERA FUNCTIE
        //SEND DATA TO SERVER

//        new PostDataAsyncTask().execute();
    }



    //post text or file to the server
//
//    public class PostDataAsyncTask extends AsyncTask<String, String, String> {
//
//        protected void onPreExecute() {
//            super.onPreExecute();
//            // do stuff before posting data
//        }
//
//        @Override
//        protected String doInBackground(String... strings) {
//            try {
//                // 1 = post text data, 2 = post file
//                int actionChoice = 2;
//
//                // post a text data
//                if(actionChoice==2){
//                    postFile();
//                } else {
//                    postText();
//                }
//
//            } catch (NullPointerException e) {
//                e.printStackTrace();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String lenghtOfFile) {
//            // do stuff after posting data
//        }
//    }
//
//    private void postText(){
//        //post text to the server
//        try{
//            // url where the data will be posted
//            String postReceiverUrl = "http://patatjes.esy.es/post.php";
//            Log.d("debug", "postURL: " + postReceiverUrl);
//
//            // HttpClient
//            HttpClient httpClient = new DefaultHttpClient();
//
//            // post header
//            HttpPost httpPost = new HttpPost(postReceiverUrl);
//
//            // add your data
//            List<NameValuePair> selectedWords = new ArrayList<NameValuePair>(2);
//            selectedWords.add(new BasicNameValuePair("word", "WORD"));
//
//            httpPost.setEntity(new UrlEncodedFormEntity(selectedWords));
//
//            // execute HTTP post request
//            HttpResponse response = httpClient.execute(httpPost);
//            HttpEntity resEntity = response.getEntity();
//
//            if (resEntity != null) {
//
//                String responseStr = EntityUtils.toString(resEntity).trim();
//                Log.v("debug", "Response: " +  responseStr);
//
//                // you can add an if statement here and do other actions based on the response
//            }
//
//        }  catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    // will post our text file
//    private void postFile(){
//        try{
//
//            // the file to be posted
//            String textFile = Environment.getExternalStorageDirectory() + "/sample.txt";
//            Log.d("debug", "textFile: " + textFile);
//
//            // the URL where the file will be posted
//            String postReceiverUrl = "http://patatjes.esy.es/post.php";
//            Log.d("debug", "postURL: " + postReceiverUrl);
//
//            // new HttpClient
//            HttpClient httpClient = new DefaultHttpClient();
//
//            // post header
//            HttpPost httpPost = new HttpPost(postReceiverUrl);
//
//            File file = new File(textFile);
//            FileBody fileBody = new FileBody(file);
//
//            MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
//            reqEntity.addPart("file", fileBody);
//            httpPost.setEntity(reqEntity);
//
//            // execute HTTP post request
//            HttpResponse response = httpClient.execute(httpPost);
//            HttpEntity resEntity = response.getEntity();
//
//            if (resEntity != null) {
//
//                String responseStr = EntityUtils.toString(resEntity).trim();
//                Log.d("debug", "Response: " +  responseStr);
//
//                // you can add an if statement here and do other actions based on the response
//            }
//
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}
