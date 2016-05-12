package nl.windesheim.capturetheclue;

import android.app.ActionBar;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import nl.windesheim.capturetheclue.Connection.Server;
import nl.windesheim.capturetheclue.Models.ClueDialog;
import nl.windesheim.capturetheclue.Models.Match;
import nl.windesheim.capturetheclue.Models.Picture;
import nl.windesheim.capturetheclue.Util.WordManager;

public class TestMatchActivity extends AppCompatActivity implements View.OnClickListener {

    private static Match currentMatch;
    private static ImageView image1, image2, image3;
    public static Context mContext;
    ArrayList<TextView> alphabet;
    ArrayList<TextView> input;
    private static int[] swapList = new int[20];
    String inputString = "";

    private boolean image1loaded = false, image2loaded = false, image3loaded = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_match);
        mContext = this;

        TextView nButton;
        Typeface Roboto = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Black.ttf");

        GridLayout GLinput = (GridLayout) findViewById(R.id.gridLetterInputView);
        //wordView.setText(currentMatch.getWord());

        // The WordManager generates extra letters etc
        WordManager wm = new WordManager();

        // Retrieve the word from match
        String word = currentMatch.getWord();

        // Populate the grid with empty squares to fill in
        List<String> letters = wm.getWordLetters(word, word.length());
        input = new ArrayList<TextView>();


        int i = 0;
        for (String c : letters) {
            nButton = new TextView(this);
            nButton.setText(" ");
            nButton.setTypeface(Roboto);
            nButton.setTextColor(Color.WHITE);
            nButton.setId(i);
            i++;
            Log.d("Debug", "Id is " + i);
            nButton.setOnClickListener(this);
            nButton.setBackgroundResource(R.drawable.button);
            nButton.setLayoutParams(new ViewGroup.LayoutParams(40, 40));
            nButton.setGravity(Gravity.CENTER);
            GLinput.addView(nButton);
            input.add(nButton);
        }


        // Get some extra letters for filler
        letters = wm.getWordLetters(currentMatch.getWord(), 14);
        // Populate the grid with empty squares to fill in
        GridLayout GL = (GridLayout) findViewById(R.id.gridLetterView);
        alphabet = new ArrayList<TextView>();

        for (String c : letters) {
            nButton = new TextView(this);
            nButton.setText(c.toUpperCase());
            nButton.setTypeface(Roboto);
            nButton.setTextColor(Color.WHITE);
            nButton.setId(i);
            i++;
            Log.d("Debug", "Id is " + i);
            nButton.setOnClickListener(this);
            nButton.setBackgroundResource(R.drawable.button);
            nButton.setLayoutParams(new ViewGroup.LayoutParams(40, 40));
            nButton.setGravity(Gravity.CENTER);
            alphabet.add(nButton);
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

    public void updateInput(String s) {

        if (inputString.length() == currentMatch.getWord().length()) {
            Log.d("Debug", "Vol");
        } else {
            inputString += s;
            input.get(inputString.length() - 1).setText(s);
            if (inputString.length() == currentMatch.getWord().length()) {
                Log.d("Debug", "Check word");
                checkWord();
            }
        }

    }

    public void checkWord() {
        ClueDialog d;
        if (inputString.equalsIgnoreCase(currentMatch.getWord())) {
            Log.d("Debug", "");
            d = new ClueDialog(this, "YOU WIN!");
            d.setImage(R.drawable.nice01);
            d.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    Intent myIntent = new Intent(TestMatchActivity.this, MainActivity.class);
                    startActivity(myIntent);
                }
            });

            // todo: What is the next action?
        } else {
            Log.d("Debug", "Nope!");
            d = new ClueDialog(this, "Nope");
            resetLetters();
        }
        d.show();
    }

    public void resetLetters() {
        inputString = "";
        for (TextView t : input) {
            t.setText(" ");
        }
        for (TextView i : alphabet) {
            i.setBackgroundResource(R.drawable.button);
            i.setClickable(true);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() > input.size() - 1) {
            v.setBackgroundResource(R.drawable.button_disabled);
            int id = v.getId() - input.size();
            String letter = alphabet.get(id).getText().toString();
            updateInput(letter);
            v.setClickable(false);
            swapList[inputString.length() - 1] = v.getId();
        } else {
            // Check if there is anything to put back
            // No actions need to be taken if there isn't.
            if (inputString.length() <= 0) {
            } else if (input.get(v.getId()).getText() == " ") {
            }
            // Ok, now lets remove the last letter
            else {
                int lastID = inputString.length() - 1;
                int alphabetIndex = swapList[lastID];

                input.get(lastID).setText(" ");
                inputString = inputString.substring(0, inputString.length() - 1);
                alphabetIndex = alphabetIndex - input.size();

                alphabet.get(alphabetIndex).setClickable(true);
                alphabet.get(alphabetIndex).setBackgroundResource(R.drawable.button);
            }
        }

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
