package nl.windesheim.capturetheclue;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class GameFirstActivity extends AppCompatActivity {

    public static File outputFile;
    //todo: FILE NAMING PROBLEM
//    public static File showFile;
    ImageView imageView;
    public static TextView selectedWordDisplay;

    String mRootPath;
    static final String PICFOLDER = "CaptureTheClue";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamefirst);


        selectedWordDisplay = (TextView)findViewById(R.id.selectedWordDisplay);
//        String username = settings.getString("username", "not_found");
//        userNameDisplay.setText(username);

        //SHOW CHOSEN WORD
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("selectedWord");
            Log.d("DEBUG",value);
            selectedWordDisplay.setText(value);
        }

        //SHOW IMAGE
        imageView = (ImageView) findViewById(R.id.imageView);


        //SAVE DATA TO THE LOCAL STORAGE
        //Local storage: /mnt/sdcard/CaptureTheClue
        mRootPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + PICFOLDER;
//        mRootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + PICFOLDER;
        File fRoot = new File(mRootPath);
        if (fRoot.exists() == false) {
            if (fRoot.mkdir() == false) {
                Toast.makeText(this, "Folder doesn't exist!",Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
        }

        //Make the file name as the date
        Calendar calendar = Calendar.getInstance();
        String FileName = String.format("CTC%02d%02d%02d-%02d%02d%02d.jpg",
                calendar.get(Calendar.YEAR) % 100, calendar.get(Calendar.MONTH)+1,
                calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
        String path = mRootPath + "/" + FileName;
        outputFile = new File(mRootPath, FileName);
//        showFile = outputFile;

        //SEND DATA TO SERVER
//        new PostDataAsyncTask().execute();
    }

    public void onClickFirst(View view) {
        switch(view.getId()){
            case R.id.firstPhoto:
                Log.d("DEBUG","First photo button has been clicked.");
                    //todo:load camera activity
                Intent firstIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                firstIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outputFile));
                startActivityForResult(firstIntent, 1001);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1001) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            Bitmap bitmap = BitmapFactory.decodeFile(outputFile.getAbsolutePath(), options);
//            Bitmap bitmap = BitmapFactory.decodeFile(showFile.getAbsolutePath(), options);
            imageView.setImageBitmap(bitmap);
        }
    }

    //todo: post FILE to the server

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
                if(actionChoice==2){
                    postFile();
                } else {
//                    postText();
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

    /*
    private void postText(){
        //post text to the server
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
            selectedWords.add(new BasicNameValuePair("word", "WORD"));

            httpPost.setEntity(new UrlEncodedFormEntity(selectedWords));

            // execute HTTP post request
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity resEntity = response.getEntity();

            if (resEntity != null) {
                String responseStr = EntityUtils.toString(resEntity).trim();
                Log.d("DEBUG", "Response: " +  responseStr);

                // you can add an if statement here and do other actions based on the response
            }

        }  catch (IOException e) {
            e.printStackTrace();
        }
    }
    */

    // will post PHOTO file
    private void postFile(){
        try{
            // the file to be posted
            Log.d("DEBUG", "output of Photo: " + outputFile);
//            Log.d("DEBUG", "output of Photo: " + showFile);

            // the URL where the file will be posted
            String postReceiverUrl = "http://patatjes.esy.es/post.php";
            Log.d("DEBUG", "postURL: " + postReceiverUrl);

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(postReceiverUrl);

            FileBody fileBody = new FileBody(outputFile);
//            FileBody fileBody = new FileBody(showFile);

            MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            reqEntity.addPart("photo1", fileBody);
            httpPost.setEntity(reqEntity);

            // execute HTTP post request
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity resEntity = response.getEntity();

            if (resEntity != null) {
                String responseStr = EntityUtils.toString(resEntity).trim();
                Log.d("DEBUG", "Response: " +  responseStr);

                // you can add an if statement here and do other actions based on the response
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
