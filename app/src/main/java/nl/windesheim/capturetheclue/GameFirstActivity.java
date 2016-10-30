package nl.windesheim.capturetheclue;

import android.app.Notification;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Set;

import nl.windesheim.capturetheclue.TypefaceSpan;

public class GameFirstActivity extends AppCompatActivity {

    public static File outputFile;
    public File showFile;
    public File showFileOG;
    ImageView imageView;
    public static TextView selectedWordDisplay;
    String mRootPath;
    static final String PICFOLDER = "CaptureTheClue";

    private int SEND_IMAGE_REQUEST = 1;

    public static Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamefirst);

        getSupportActionBar().setDisplayShowTitleEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        SpannableString s = new SpannableString("Capture the Clue");
        s.setSpan(new TypefaceSpan(GameFirstActivity.this, "olivier_demo.ttf"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        SpannableString sb = new SpannableString("Take a picture!");
        sb.setSpan(new TypefaceSpan(GameFirstActivity.this, "olivier_demo.ttf"), 0, sb.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);
        getSupportActionBar().setSubtitle(sb);


        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/olivier_demo.ttf");
        selectedWordDisplay = (TextView)findViewById(R.id.selectedWordDisplay);
        selectedWordDisplay.setTypeface(typeFace);


        imageView = (ImageView) findViewById(R.id.imageView);
//        String username = settings.getString("username", "not_found");
//        userNameDisplay.setText(username);

        //SHOW CHOSEN WORD
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("selectedWord");
            String[] parts = value.split(",");
            String part2 = parts[1];
            String newValue1 = part2.replaceAll("word=", " ");
            newValue1 = newValue1.substring(0,newValue1.length()-1);
            Log.d("DEBUG",value);
            Log.d("DEBUG",newValue1);

            selectedWordDisplay.setText(newValue1);
        }

        //SAVE DATA TO THE LOCAL STORAGE
        //Local storage: /mnt/sdcard/CaptureTheClue
        mRootPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + PICFOLDER;
        File fRoot = new File(mRootPath);
        if (fRoot.exists() == false) {
            if (fRoot.mkdir() == false) {
                Toast.makeText(this, "Folder doesn't exist!",Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
        }

        //MAKE THE FILE NAME BY DATE
        Calendar calendar = Calendar.getInstance();
        String FileName = String.format("CTC%02d%02d%02d-%02d%02d%02d.jpg",
                calendar.get(Calendar.YEAR) % 100, calendar.get(Calendar.MONTH)+1,
                calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
        String path = mRootPath + "/" + FileName;
        outputFile = new File(mRootPath, FileName);
        setShowFile(outputFile);
    }

    public void onClickFirst(View view) {
        switch(view.getId()){
            case R.id.firstPhoto:
                Log.d("DEBUG", "First photo button has been clicked.");
                cameraOpen();
                break;

            case R.id.sendFirstPhoto:
                Log.d("DEBUG", "Send button has been clicked.");
                uploadPhoto();
                setContentView(R.layout.activity_gamefirstuploaded);
                break;
        }
    }

    public void cameraOpen() {
        setShowFileOG(showFile);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(showFile));
        startActivityForResult(cameraIntent, SEND_IMAGE_REQUEST);
    }

    @Override
    public void onBackPressed() {
    }

    //SHOW PHOTO ON THE IMAGE VIEW
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("DEBUG", requestCode + "  " + resultCode);
        //if (requestCode == SEND_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
        if(showFileOG != null) {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse("file://" + showFileOG.getAbsolutePath()));
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, bao);
        byte [] ba = bao.toByteArray();
        String ba1 = Base64.encodeToString(ba, Base64.DEFAULT);
        Log.d("METHOD GETSTRINGIMAGE", "BAL1"); //WORKED!!!
        return ba1;
    }

    private void uploadPhoto(){
        Thread thread = new Thread(new Runnable() {
            public void run() {
                String uploadImage=getStringImage(bitmap); //NullPointerException

                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("image", uploadImage));
//                nameValuePairs.add(new BasicNameValuePair("filename", theFileName));

                try {
                    HttpClient client = new DefaultHttpClient();
                    HttpPost post = new HttpPost("http://patatjes.esy.es/upload.php");
                    post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse response = client.execute(post);
                    //HttpEntity entity = response.getEntity();

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public void setShowFile(File file){
        this.showFile = file;
    }

    public File setShowFileOG(File file){
        return this.showFileOG = file;
    }


}