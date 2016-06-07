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

    private Bitmap bitmap;

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
                uploadPhoto(bitmap);
                setContentView(R.layout.activity_gamefirstuploaded);
                Log.d("DEBUG", "image uploading... ?????");
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
//
//    public String getStringImage(Bitmap bmp){
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//        byte[] imageBytes = baos.toByteArray();
//        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
//        return encodedImage;
//    }
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

    private void uploadPhoto(final Bitmap bitmap){

        Thread thread = new Thread(new Runnable() {

            public void run() {
                ByteArrayOutputStream bao = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bao);
                byte [] ba = bao.toByteArray();
                String ba1 = Base64.encodeToString(ba, Base64.DEFAULT);
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("image", ba1));
                try {
                    HttpClient client = new DefaultHttpClient();
                    HttpPost post = new HttpPost("http://http://patatjes.esy.es/upload.php");
                    post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse response = client.execute(post);
                    //HttpEntity entity = response.getEntity();

                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        });
        thread.start();
        Log.d("DEBUG", "it works?");
    }



    /*
    try {

     FTPClient mFTP = new FTPClient();

     mFTP.connect("123.123.123.123", 21);  // ftp로 접속
     mFTP.login("ftpuser", "password"); // ftp 로그인 계정/비번
     mFTP.setFileType(FTP.BINARY_FILE_TYPE); // 바이너리 파일
     mFTP.setBufferSize(1024 * 1024); // 버퍼 사이즈
     mFTP.enterLocalPassiveMode(); 패시브 모드로 접속



    // 업로드 경로 수정 (선택 사항 )

     mFTP.cwd("public"); // ftp 상의 업로드 디렉토리
     mFTP.mkd("files"); // public아래로 files 디렉토리를 만든다
     mFTP.cwd("files"); // public/files 로 이동 (이 디렉토리로 업로드가 진행)


     File path = new File("/sdcard/dcim/camera/"); // 업로드 할 파일이 있는 경로(예제는 sd카드 사진 폴더)
     if (path.listFiles().length > 0) { // 폴더를 가지고와 폴더 내부 파일 리스트를 만든다
          for (File file : path.listFiles()) {
               if (file.isFile()) {
                    FileInputStream ifile = new FileInputStream(file)

                    mFTP.rest(file.getName());  // ftp에 해당 파일이있다면 이어쓰기

                    mFTP.appendFile(file.getName(), ifile); // ftp 해당 파일이 없다면 새로쓰기
               }
          }
     }

     mFTP.disconnect(); // ftp disconnect

     } catch (SocketException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
     } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
     }
}
     */

    public void setShowFile(File file){
        this.showFile = file;
    }

    public void setShowFileOG(File file){
        this.showFileOG = file;
    }
}
