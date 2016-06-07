package nl.windesheim.capturetheclue;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
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

public class GameFirstActivity extends AppCompatActivity {

    public static File outputFile;
    public File showFile;
    public File showFileOG;
    ImageView imageView;
    public static TextView selectedWordDisplay;
    String mRootPath;
    static final String PICFOLDER = "CaptureTheClue";

    public static final String UPLOAD_URL = "http://http://patatjes.esy.es/upload.php";
    public static final String UPLOAD_KEY = "image";

    private int SEND_IMAGE_REQUEST = 1;

    public static Bitmap bitmap;
    public String theFileName;

    private Uri filePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamefirst);

        selectedWordDisplay = (TextView)findViewById(R.id.selectedWordDisplay);
        imageView = (ImageView) findViewById(R.id.imageView);
//        String username = settings.getString("username", "not_found");
//        userNameDisplay.setText(username);

        //SHOW CHOSEN WORD
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("selectedWord");
            Log.d("DEBUG",value);
            selectedWordDisplay.setText(value);
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
//        theFileName = outputFile.toString();
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
//                uploadImage();
                uploadPhoto();
                setContentView(R.layout.activity_gamefirstuploaded);
//                Log.d("DEBUG", "image uploading... ?????");
                break;
        }
    }

    public void cameraOpen() {
        setShowFileOG(showFile);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(showFile));
        startActivityForResult(cameraIntent, SEND_IMAGE_REQUEST);
    }

    //SHOW PHOTO ON THE IMAGE VIEW
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//        byte[] imageBytes = baos.toByteArray();
//        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
//        return encodedImage;
    }
//
//    private void uploadImage() {
//        class UploadImage extends AsyncTask<Bitmap, Void, String> {
//
//            ProgressDialog loading;
//            RequestHandler rh = new RequestHandler();
//
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//                loading = ProgressDialog.show(GameFirstActivity.this, "Uploading Image", "Please wait...", true, true);
//            }
//
//            @Override
//            protected void onPostExecute(String s) {
//                super.onPostExecute(s);
//                loading.dismiss();
//                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            protected String doInBackground(Bitmap... params) {
//                Bitmap bitmap = params[0];
//                String uploadImage = getStringImage(bitmap);
//
//                HashMap<String, String> data = new HashMap<>();
//                data.put(UPLOAD_KEY, uploadImage);
//
//                String result = rh.sendPostRequest(UPLOAD_URL, data);
//
//                return result;
//            }
//        }
//    }

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
