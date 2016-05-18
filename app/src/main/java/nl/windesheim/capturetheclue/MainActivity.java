package nl.windesheim.capturetheclue;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

import java.util.ArrayList;

import nl.windesheim.capturetheclue.Connection.Server;
import nl.windesheim.capturetheclue.Models.matchListItem;
import nl.windesheim.capturetheclue.Models.matchListItemAdapter;

public class MainActivity extends AppCompatActivity {

    public static String serverStatus = "";
    public static TextView tv;
    private static ListView matchesTable;
    public static Context mContext;
    public static TextView userNameDisplay;
    public static boolean loggedIn = false;
    public static final String PREFS_NAME = "prefs";
    SharedPreferences settings;
    private static Typeface Roboto;
    private static ArrayList<matchListItem> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        Roboto = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Black.ttf");
        matchesTable = (ListView) findViewById(R.id.matchesTable);
        settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        userNameDisplay = (TextView) findViewById(R.id.userNameDisplay);
        String username = settings.getString("username", "not_found");
        userNameDisplay.setText(username);

        // Test connection to Server, currently disabled because it takes a long time when the server is down.
        //new Server().testConnection();
        if (username == "not_found") {
            //
            Intent myIntent = new Intent(MainActivity.this, Account_Activity.class);
            //myIntent.putExtra("key", value); //Optional parameters
            MainActivity.this.startActivity(myIntent);
        }


    }

    public static void showPopup(String text) {
        Log.d("Debug", "Showing popup");
        Toast.makeText(mContext, text, Toast.LENGTH_LONG).show();
    }


    public static void updateList(JSONObject j) throws JSONException {

        String ni = j.getString("num_items");
        int num_items = Integer.parseInt(ni) - 1;
        int i = 0;
        list = new ArrayList<>();

        while (i <= num_items) {
            JSONObject currentObject = j.getJSONObject("" + i);
            String nowPlaying = "Now playing against " + currentObject.getString("player2name");
            String matchid = currentObject.getString("id");
            Log.d("DEBUG", "Creating match with id " + matchid);
            int id = Integer.parseInt(matchid);
            String gameStatus = currentObject.getString("game_status");
            matchListItem MLI = new matchListItem(id, nowPlaying, gameStatus);
            list.add(MLI);
            i++;
        }

        matchesTable.setAdapter(new matchListItemAdapter(mContext, list));
        matchesTable.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {
                matchListItem m = (matchListItem) adapter.getItemAtPosition(position);
                Log.d("DEBUG", "Starting natch id: " + m.getMatchID());
                new Server().getMatch(m.getMatchID());
            }
        });


    }

    public void onClickMain(View view) {
        switch (view.getId())
        {
            case R.id.start:
                setContentView(R.layout.activity_play);
                break;

            case R.id.currentgame:
                //
                Log.d("DEBUG", "Clicked current game");
                new Server().getMatch(1);

                break;

            case R.id.findmatch:
                //
                Log.d("DEBUG", "Doe iets met matchmaking...");
                new Server().getUserMatches(2);
                break;

            case R.id.button_random:

                Log.d("DEBUG", "WHATTHEFUUUCK");

            case R.id.logoutButton:

                Context c = this;
                settings = c.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();

                editor.clear();
                editor.commit();
                loggedIn = false;

                Intent myIntent = new Intent(MainActivity.this, Account_Activity.class);
                //myIntent.putExtra("key", value); //Optional parameters
                MainActivity.this.startActivity(myIntent);
                break;
        }
    }
}
