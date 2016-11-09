package nl.windesheim.capturetheclue;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import nl.windesheim.capturetheclue.Connection.Server;
import nl.windesheim.capturetheclue.Models.ClueDialog;
import nl.windesheim.capturetheclue.Models.Match;
import nl.windesheim.capturetheclue.Util.WordManager;

public class MatchGuessActivity
        extends AppCompatActivity
        implements View.OnClickListener
{
    private static Match currentMatch;
    private static ImageView image1;
    private static ImageView image2;
    private static ImageView image3;
    public static Context mContext;
    private static int numPics = -1;
    private static int[] swapList = new int[20];
    ArrayList<TextView> alphabet;
    public boolean image1loaded = false;
    public boolean image2loaded = false;
    public boolean image3loaded = false;
    ArrayList<TextView> input;
    String inputString = "";

    public static void showPopup(String paramString)
    {
        Log.d("Debug", "Showing popup");
        Toast.makeText(mContext, paramString, 1).show();
    }

    public static void startMatch(Match paramMatch)
    {
        Log.d("Debug", "Match starting");
        currentMatch = paramMatch;
    }

    public void checkWord()
    {
        ClueDialog localClueDialog;
        if (this.inputString.equalsIgnoreCase(currentMatch.getWord()))
        {
            localClueDialog = new ClueDialog(this);
            localClueDialog.setImage(2130837593);
            localClueDialog.setOnDismissListener(new DialogInterface.OnDismissListener()
            {
                public void onDismiss(DialogInterface paramAnonymousDialogInterface)
                {
                    MatchGuessActivity.currentMatch.setStatus("game_finished");
                    MatchGuessActivity.currentMatch.save();
                    paramAnonymousDialogInterface = new Intent(MatchGuessActivity.this, MainActivity.class);
                    MatchGuessActivity.this.startActivity(paramAnonymousDialogInterface);
                }
            });
        }
        for (;;)
        {
            localClueDialog.show();
            return;
            localClueDialog = new ClueDialog(this);
            resetLetters();
        }
    }

    public void onClick(View paramView)
    {
        if (paramView.getId() > this.input.size() - 1)
        {
            paramView.setBackgroundResource(2130837584);
            i = paramView.getId();
            j = this.input.size();
            updateInput(((TextView)this.alphabet.get(i - j)).getText().toString());
            paramView.setClickable(false);
            swapList[(this.inputString.length() - 1)] = paramView.getId();
        }
        while ((this.inputString.length() <= 0) || (((TextView)this.input.get(paramView.getId())).getText() == " ")) {
            return;
        }
        int i = this.inputString.length() - 1;
        int j = swapList[i];
        ((TextView)this.input.get(i)).setText(" ");
        this.inputString = this.inputString.substring(0, this.inputString.length() - 1);
        i = j - this.input.size();
        ((TextView)this.alphabet.get(i)).setClickable(true);
        ((TextView)this.alphabet.get(i)).setBackgroundResource(2130837583);
    }

    public void onClickTest(View paramView)
    {
        switch (paramView.getId())
        {
            case 2131492954:
            default:
                return;
            case 2131492953:
                paramView = new Intent(this, FullscreenActivity.class);
                image1.buildDrawingCache();
                localBitmap = image1.getDrawingCache();
                localBundle = new Bundle();
                localBundle.putParcelable("imagebitmap", localBitmap);
                paramView.putExtras(localBundle);
                startActivity(paramView);
                return;
            case 2131492955:
                paramView = new Intent(this, FullscreenActivity.class);
                image2.buildDrawingCache();
                localBitmap = image2.getDrawingCache();
                localBundle = new Bundle();
                localBundle.putParcelable("imagebitmap", localBitmap);
                paramView.putExtras(localBundle);
                startActivity(paramView);
                return;
        }
        paramView = new Intent(this, FullscreenActivity.class);
        image3.buildDrawingCache();
        Bitmap localBitmap = image3.getDrawingCache();
        Bundle localBundle = new Bundle();
        localBundle.putParcelable("imagebitmap", localBitmap);
        paramView.putExtras(localBundle);
        startActivity(paramView);
    }

    protected void onCreate(Bundle paramBundle)
    {
        super.onCreate(paramBundle);
        setContentView(2130968608);
        mContext = this;
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setElevation(0.0F);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        paramBundle = new SpannableString("Capture the Clue");
        paramBundle.setSpan(new TypefaceSpan(this, "olivier_demo.ttf"), 0, paramBundle.length(), 33);
        Object localObject1 = new SpannableString("Take a picture!");
        ((SpannableString)localObject1).setSpan(new TypefaceSpan(this, "olivier_demo.ttf"), 0, ((SpannableString)localObject1).length(), 33);
        getSupportActionBar().setTitle(paramBundle);
        getSupportActionBar().setSubtitle((CharSequence)localObject1);
        Typeface.createFromAsset(getAssets(), "fonts/olivier_demo.ttf");
        paramBundle = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Black.ttf");
        localObject1 = (GridLayout)findViewById(2131492962);
        Object localObject2 = new WordManager();
        if (getIntent().getExtras() != null)
        {
            currentMatch = (Match)getIntent().getSerializableExtra("nl.windesheim.capturetheclue");
            if (numPics == -1) {
                numPics = currentMatch.getNumPics();
            }
        }
        Object localObject3 = currentMatch.getWord();
        localObject3 = ((WordManager)localObject2).getWordLetters((String)localObject3, ((String)localObject3).length());
        this.input = new ArrayList();
        Object localObject4 = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics((DisplayMetrics)localObject4);
        Log.d("ApplicationTagName", "Display width in px is " + ((DisplayMetrics)localObject4).widthPixels);
        int k = ((DisplayMetrics)localObject4).widthPixels / 10;
        int j = ((DisplayMetrics)localObject4).widthPixels / 8;
        int i = 0;
        localObject3 = ((List)localObject3).iterator();
        while (((Iterator)localObject3).hasNext())
        {
            localObject4 = (String)((Iterator)localObject3).next();
            localObject4 = new TextView(this);
            ((TextView)localObject4).setText(" ");
            ((TextView)localObject4).setTypeface(paramBundle);
            ((TextView)localObject4).setTextColor(-1);
            ((TextView)localObject4).setId(i);
            i += 1;
            ((TextView)localObject4).setOnClickListener(this);
            ((TextView)localObject4).setBackgroundResource(2130837583);
            ((TextView)localObject4).setLayoutParams(new ViewGroup.LayoutParams(k, k));
            ((TextView)localObject4).setGravity(17);
            ((GridLayout)localObject1).addView((View)localObject4);
            this.input.add(localObject4);
        }
        localObject2 = ((WordManager)localObject2).getWordLetters(currentMatch.getWord(), 14);
        localObject1 = (GridLayout)findViewById(2131492963);
        this.alphabet = new ArrayList();
        localObject2 = ((List)localObject2).iterator();
        while (((Iterator)localObject2).hasNext())
        {
            localObject3 = (String)((Iterator)localObject2).next();
            localObject4 = new TextView(this);
            ((TextView)localObject4).setText(((String)localObject3).toUpperCase());
            ((TextView)localObject4).setTypeface(paramBundle);
            ((TextView)localObject4).setTextColor(-1);
            ((TextView)localObject4).setId(i);
            i += 1;
            ((TextView)localObject4).setOnClickListener(this);
            ((TextView)localObject4).setBackgroundResource(2130837583);
            ((TextView)localObject4).setLayoutParams(new ViewGroup.LayoutParams(j, j));
            ((TextView)localObject4).setGravity(17);
            this.alphabet.add(localObject4);
            ((GridLayout)localObject1).addView((View)localObject4);
        }
        image1 = (ImageView)findViewById(2131492953);
        image2 = (ImageView)findViewById(2131492955);
        image3 = (ImageView)findViewById(2131492956);
        requestFirstImage();
        requestSecondImage();
        requestThirdImage();
        paramBundle = currentMatch.getFirstPicture();
        new Server();
        Server.downloadImage(image1, paramBundle);
        this.image1loaded = true;
    }

    public void requestFirstImage()
    {
        new Server();
        Server.downloadImage(image1, currentMatch.getFirstPicture());
    }

    public void requestSecondImage()
    {
        new Server();
        Server.downloadImage(image2, currentMatch.getSecondPicture());
    }

    public void requestThirdImage()
    {
        new Server();
        Server.downloadImage(image3, currentMatch.getThirdPicture());
    }

    public void resetLetters()
    {
        this.inputString = "";
        Iterator localIterator = this.input.iterator();
        while (localIterator.hasNext()) {
            ((TextView)localIterator.next()).setText(" ");
        }
        localIterator = this.alphabet.iterator();
        while (localIterator.hasNext())
        {
            TextView localTextView = (TextView)localIterator.next();
            localTextView.setBackgroundResource(2130837583);
            localTextView.setClickable(true);
        }
    }

    public void updateInput(String paramString)
    {
        if (this.inputString.length() == currentMatch.getWord().length()) {
            Log.d("Debug", "Vol");
        }
        do
        {
            return;
            this.inputString += paramString;
            ((TextView)this.input.get(this.inputString.length() - 1)).setText(paramString);
        } while (this.inputString.length() != currentMatch.getWord().length());
        Log.d("Debug", "Check word");
        checkWord();
    }
}
