package nl.windesheim.capturetheclue;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import nl.windesheim.capturetheclue.Models.Match;

//import nl.windesheim.capturetheclue.Connection.Server;

public class MatchCreateActivity
        extends AppCompatActivity
{
    private static final int CAMERA_REQUEST = 1888;
    public static Match currentMatch;
    private static int currentUpload = -1;
    private static ImageView image1View;
    private static ImageView image2View;
    private static ImageView image3View;
    public static TextView selectedWordDisplay;
    Button camButton;

    public static void requestFirstImage()
    {
//        new Server();
//        Server.downloadImage(image1View, currentMatch.getFirstPicture());
    }

    public static void requestImages()
    {
        Log.d("DEBUG", "Do requests");
        if (currentMatch != null)
        {
            setTitle();
            requestFirstImage();
            requestSecondImage();
            requestThirdImage();
        }
    }

    public static void requestSecondImage()
    {
//        new Server();
//        Server.downloadImage(image2View, currentMatch.getSecondPicture());
    }

    public static void requestThirdImage()
    {
//        new Server();
//        Server.downloadImage(image3View, currentMatch.getThirdPicture());
    }

    public static void setCurrentMatch(Match paramMatch)
    {
        currentMatch = paramMatch;
    }

    public static void setTitle()
    {
        selectedWordDisplay.setText(currentMatch.getWord());
    }

    public void cameraOpen()
    {
        startActivityForResult(new Intent("android.media.action.IMAGE_CAPTURE"), 1888);
    }

    protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
    {
        if ((CAMERA_REQUEST == 1888) && (currentUpload == -1))
        {
            paramIntent = (Bitmap)paramIntent.getExtras().get("data");
            Log.d("Debug", "Trying to save " + currentMatch.getID() + ": " + currentUpload);
//            new Server();
//            Server.uploadImage(currentMatch.getID(), paramIntent, currentUpload);
            if (currentUpload == 1) {
                image1View.setImageBitmap(paramIntent);
            }
            if (currentUpload == 2) {
                image2View.setImageBitmap(paramIntent);
            }
            if (currentUpload == 3)
            {
                this.camButton.setVisibility(4);
                image3View.setImageBitmap(paramIntent);
                requestImages();
            }
            currentUpload += 1;
            return;
        }
        Log.d("DEBUG", "Picture couldn't be made.");
    }

    public void onClickFirst(View paramView)
    {
        switch (paramView.getId())
        {
            case 2131492954:
            default:
                return;
            case 2131492957:
                cameraOpen();
                return;
            case 2131492953:
                paramView = new Intent(this, FullscreenActivity.class);
                image1View.buildDrawingCache();
                localBitmap = image1View.getDrawingCache();
                localBundle = new Bundle();
                localBundle.putParcelable("imagebitmap", localBitmap);
                paramView.putExtras(localBundle);
                startActivity(paramView);
                return;
            case 2131492955:
                paramView = new Intent(this, FullscreenActivity.class);
                image2View.buildDrawingCache();
                localBitmap = image2View.getDrawingCache();
                localBundle = new Bundle();
                localBundle.putParcelable("imagebitmap", localBitmap);
                paramView.putExtras(localBundle);
                startActivity(paramView);
                return;
        }
        paramView = new Intent(this, FullscreenActivity.class);
        image3View.buildDrawingCache();
        Bitmap localBitmap = image3View.getDrawingCache();
        Bundle localBundle = new Bundle();
        localBundle.putParcelable("imagebitmap", localBitmap);
        paramView.putExtras(localBundle);
        startActivity(paramView);
    }

    protected void onCreate(Bundle paramBundle)
    {
        super.onCreate(paramBundle);
        setContentView(2130968604);
        selectedWordDisplay = (TextView)findViewById(2131492952);
        paramBundle = new SpannableString("Capture the Clue");
        paramBundle.setSpan(new TypefaceSpan(this, "olivier_demo.ttf"), 0, paramBundle.length(), 33);
        SpannableString localSpannableString = new SpannableString("Take a picture!");
        localSpannableString.setSpan(new TypefaceSpan(this, "olivier_demo.ttf"), 0, localSpannableString.length(), 33);
        getSupportActionBar().setTitle(paramBundle);
        getSupportActionBar().setSubtitle(localSpannableString);
        paramBundle = Typeface.createFromAsset(getAssets(), "fonts/olivier_demo.ttf");
        selectedWordDisplay.setTypeface(paramBundle);
        this.camButton = ((Button)findViewById(2131492957));
        this.camButton.setVisibility(0);
        image1View = (ImageView)findViewById(2131492953);
        image2View = (ImageView)findViewById(2131492955);
        image3View = (ImageView)findViewById(2131492956);
        if (getIntent().getExtras() != null)
        {
            currentMatch = (Match)getIntent().getSerializableExtra("nl.windesheim.capturetheclue");
            if (currentUpload == -1) {
                currentUpload = currentMatch.getNumPics();
            }
            if (currentUpload == 4) {
                this.camButton.setVisibility(4);
            }
        }
        requestImages();
    }

    protected void onResume()
    {
        super.onResume();
        requestImages();
    }
}
