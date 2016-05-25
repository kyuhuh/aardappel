package nl.windesheim.capturetheclue;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Dialog;
<<<<<<< HEAD
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.graphics.Typeface;
import android.os.Build;
=======
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
>>>>>>> kyu
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
<<<<<<< HEAD
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
=======
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
>>>>>>> kyu

import java.util.ArrayList;

import nl.windesheim.capturetheclue.Connection.Server;
<<<<<<< HEAD
import nl.windesheim.capturetheclue.Models.ClueDialog;
import nl.windesheim.capturetheclue.Models.matchListItem;
import nl.windesheim.capturetheclue.Models.matchListItemAdapter;
=======
>>>>>>> kyu

public class MainActivity extends AppCompatActivity {

    private static int currentUserID;
    public static String serverStatus = "";
    public static TextView tv;
    private static ListView matchesTable;
    public static Context mContext;
    public static TextView userNameDisplay;
    public static boolean loggedIn = false;
    private Dialog askSideDialog;
    public static final String PREFS_NAME = "prefs";
    SharedPreferences settings;
    private static Typeface Roboto;
    private static ArrayList<matchListItem> list;

    ImageView imageView;
    File outputFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        Roboto = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Black.ttf");
        matchesTable = (ListView) findViewById(R.id.matchesTable);
        settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // Set userID
        String idstring = settings.getString("uid", "not_found");
        if (idstring.equals("not_found")) {
            currentUserID = -1;
        } else {
            currentUserID = Integer.parseInt(idstring);
        }
        userNameDisplay = (TextView) findViewById(R.id.userNameDisplay);
        String username = settings.getString("username", "not_found");
        userNameDisplay.setTypeface(Roboto);
        userNameDisplay.setText(username);
        TextView matchesHeader = (TextView) findViewById(R.id.matchesHeaderText);
        matchesHeader.setTypeface(Roboto);
        Button startGame = (Button) findViewById(R.id.findmatch);
        startGame.setTypeface(Roboto);

        // Test connection to Server, currently disabled because it takes a long time when the server is down.
        //new Server().testConnection();
        if (username == "not_found") {
            //
            Intent myIntent = new Intent(MainActivity.this, Account_Activity.class);
            //myIntent.putExtra("key", value); //Optional parameters
            MainActivity.this.startActivity(myIntent);
        }

<<<<<<< HEAD
        new Server().getUserMatches(currentUserID);

=======
//camera
        imageView = (ImageView) findViewById(R.id.imageView);
        File storageDir = Environment.getExternalStorageDirectory();
        outputFile = new File(storageDir, "output.jpg");
    }
>>>>>>> kyu

    //for camera
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1001) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            Bitmap bitmap = BitmapFactory.decodeFile(outputFile.getAbsolutePath(), options);
            imageView.setImageBitmap(bitmap);
        }
    }

    public static void showPopup(String text) {


        Log.d("Debug", "Showing popup");
        Toast.makeText(mContext, text, Toast.LENGTH_LONG).show();
    }

    public void createMatch(int i) {
        // i: 0 means guess, i: 1 means create

        String id = settings.getString("uid", "not_found");
        if (id.equals("not_found")) {
            Log.d("DEBUG", "User ID is not set :/");
        } else {
            int UID = Integer.parseInt(id);
            new Server().startNewMatch(UID, i);
            Log.d("DEBUG", "Requested match at side " + i + " for userid " + UID);
            new Server().getUserMatches(UID);
            Log.d("DEBUG", "Refreshed match list");
        }
    }


    public static void updateList(JSONObject j) throws JSONException {

        // Get the number of results as defined in the JSON response
        String ni = j.getString("num_items");
        int num_items = Integer.parseInt(ni) - 1;
        int i = 0;
        list = new ArrayList<>();

        // For every item in the response create a list item and add it.
        while (i <= num_items) {
            JSONObject currentObject = j.getJSONObject("" + i);

            String gameStatus = "";
            boolean clickable = true;
            String nowPlaying = "";
            // Get relevant data into strings
            if (currentObject.getString("player1name").equals("You")) {
                Log.d("DEBUG", "Ik ben speler 1");
                nowPlaying = "Now playing against " + currentObject.getString("player2name");
                if (currentObject.getString("game_status").equals("set_word")) {
                    Log.d("DEBUG", "Ik moet het woord kiezen");
                    gameStatus = "Jouw beurt: kies een woord!";
                    clickable = true;
                }
            }
            if (currentObject.getString("player2name").equals("You")) {
                Log.d("DEBUG", "Ik ben speler 2");
                nowPlaying = "Now playing against " + currentObject.getString("player1name");
                if (currentObject.getString("game_status").equals("set_word")) {
                    Log.d("DEBUG", "Ik moet wachten");
                    gameStatus = "Tegenstander is een woord aan het kiezen.";
                    clickable = false;
                }
            }

            String matchid = currentObject.getString("id");
            int id = Integer.parseInt(matchid);
            currentObject.getString("game_status");

            // Create an item with the strings, add it to the list.
            matchListItem MLI = new matchListItem(id, nowPlaying, gameStatus);
            MLI.setClickable(clickable);

            list.add(MLI);
            i++;
        }

        // Set the view to represent 'list' and set the onClick listener.
        matchesTable.setAdapter(new matchListItemAdapter(mContext, list));
        matchesTable.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {
                matchListItem m = (matchListItem) adapter.getItemAtPosition(position);
                new Server().getMatch(m.getMatchID());
            }
        });


    }

    public void onClickMain(View view) {
        switch (view.getId()) {
            case R.id.start:
<<<<<<< HEAD
                setContentView(R.layout.activity_play);
                break;

            case R.id.refreshButton:
                Log.d("DEBUG", "Refresh");
                new Server().getUserMatches(currentUserID);
                break;

            case R.id.settings:
                //
                Log.d("DEBUG", "Clicked settings");
                setContentView(R.layout.activity_settings);

                userNameDisplay = (TextView) findViewById(R.id.userNameDisplay);
                String username = settings.getString("username", "not_found");
                userNameDisplay.setText(username);

                TextView userIDDisplay = (TextView) findViewById(R.id.userIdDisplay);
                userIDDisplay.setText(currentUserID + "");

                break;

            case R.id.findmatch:
                //
                Log.d("DEBUG", "Doe iets met matchmaking...");
                askSideDialog = new Dialog(mContext);
                askSideDialog.setContentView(R.layout.dialog_askside);
                askSideDialog.setTitle("Which side?");
                askSideDialog.show();
                break;

            case R.id.buttonPickGuess:
                createMatch(0);
                askSideDialog.dismiss();
                break;

            case R.id.buttonPickCreate:
                createMatch(1);
                askSideDialog.dismiss();
=======
                Log.d("DEBUG", "Clicked start game");
                //Intent startIntent = new Intent(getApplicationContext(), StartActivity.class);
                //startActivity(startIntent);
                setContentView(R.layout.activity_start);
                break;

            case R.id.currentgame:
                //setContentView(R.layout.);
                Log.d("DEBUG", "Clicked current game");
                new Server().getMatch(1);

                break;

            case R.id.settings:
                setContentView(R.layout.activity_camera);
                break;


            case R.id.camerabutton:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outputFile));
                startActivityForResult(intent, 1001);
>>>>>>> kyu
                break;


            case R.id.logoutButton:

                Log.d("DEBUG", "Pompompom");
                Context c = this;
                settings = c.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();

                editor.clear();
                editor.commit();
                loggedIn = false;
                currentUserID = -1;

                Intent myIntent = new Intent(MainActivity.this, Account_Activity.class);
                //myIntent.putExtra("key", value); //Optional parameters
                MainActivity.this.startActivity(myIntent);
                break;
<<<<<<< HEAD
=======

            case R.id.openPrefsDialogButton:
                Dialog d = new Dialog(this);
                d.setTitle("Saved Preferences");
                d.setContentView(R.layout.activity_friend);
                d.show();
                break;

            case R.id.friendGame:
                //setContentView(R.layout.activity_friend);
                Intent friendIntent = new Intent(getApplicationContext(), FriendActivity.class);
                startActivity(friendIntent);
                break;

            case R.id.randomGame:
                //setContentView(R.layout.activity_wordselection);
                Intent randomIntent = new Intent(getApplicationContext(), WordSelectionActivity.class);
                startActivity(randomIntent);
                break;


>>>>>>> kyu
        }
    }
}
