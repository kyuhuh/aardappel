package nl.windesheim.capturetheclue;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import nl.windesheim.capturetheclue.Connection.JSONParser;
import nl.windesheim.capturetheclue.Connection.Server;
import nl.windesheim.capturetheclue.Models.Match;
import nl.windesheim.capturetheclue.Models.Picture;
import nl.windesheim.capturetheclue.Util.WordManager;

public class TestMatchActivity extends AppCompatActivity {

    private static Match currentMatch;
    private static ImageView image1, image2, image3;
    public static Context mContext;

    private boolean image1loaded = false, image2loaded = false, image3loaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_match);
        mContext = this;
        TextView wordView = (TextView) findViewById(R.id.wordTextView);
        //wordView.setText(currentMatch.getWord());

        WordManager wm = new WordManager();
        List<String> letters = wm.getWordLetters(currentMatch.getWord(), 14);

        GridLayout GL = (GridLayout) findViewById(R.id.gridLetterView);

        TextView nButton;

        for (String c : letters) {
            nButton = new TextView(this);
            nButton.setText(c);
            nButton.setBackgroundResource(R.drawable.button);
            nButton.setLayoutParams(new ViewGroup.LayoutParams(50, 50));
            GL.addView(nButton);
        }

        image1 = (ImageView) findViewById(R.id.image1View);
        image2 = (ImageView) findViewById(R.id.image2View);
        image3 = (ImageView) findViewById(R.id.image3View);

        Picture pic1 = currentMatch.getFirstPicture();

        new Server().downloadImage(image1, pic1);
        // todo: Find a better place to do this check!
        image1loaded = true;


    }

    public static void showPopup(String text) {
        Log.d("Debug", "Showing popup");
        Toast.makeText(mContext, text, Toast.LENGTH_LONG).show();
    }

    public void requestFirstImage() {
        if (!image1loaded) {

            showPopup("First image wasn't loaded yet!");
        } else {
            showLargeImage(currentMatch.getSecondPicture());
        }
    }

    public void requestSecondImage() {
        if (!image2loaded) {
            if (image1loaded) {
                Log.d("DEBUG", "OK for loading img 2");

                new Server().downloadImage(image2, currentMatch.getSecondPicture());
                // todo: Find a better place to do this check!
                image2loaded = true;
            } else {
                Log.d("DEBUG", "Not OK for loading img 2");
                showPopup("First image wasn't loaded yet!");
            }
        } else {
            showLargeImage(currentMatch.getSecondPicture());
        }
    }

    public void requestThirdImage() {
        if (!image3loaded) {
            if (image2loaded) {
                Log.d("DEBUG", "OK for loading img 3");
                Picture pic3 = currentMatch.getThirdPicture();
                new Server().downloadImage(image3, pic3);
                // todo: Find a better place to do this check!
                image3loaded = true;
            } else {
                Log.d("DEBUG", "Not OK for loading img 3");
                showPopup("Second image wasn't loaded yet!");
            }
        } else {
            showLargeImage(currentMatch.getThirdPicture());
        }
    }

    public static void showLargeImage(Picture p) {
        Log.d("DEBUG", "Show " + p.toString() + " enlarged");
    }

    public static void startMatch(Match m) {
        Log.d("Debug", "Match starting");
        currentMatch = m;
    }

    public void onClickMain(View view) {
        Log.d("DEBUG", "Click");
        switch (view.getId()) {
            case R.id.image1View:
                requestFirstImage();
                break;
            case R.id.image2View:
                requestSecondImage();
                break;
            case R.id.image3View:
                requestThirdImage();
                break;
        }
    }
}
