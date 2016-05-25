package nl.windesheim.capturetheclue.Models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Peter on 4/12/2016.
 */
public class Match {

    private String game_status;
    private int id;
    private int ratingid;
    private int player1id;
    private List<Picture> pictures;
    private Word word;


    public String toString() {
        return "Match #" + id + " has pictures " + pictures.toString() + " and expresses word '" + word.getWord() + "'";
    }

    public int getID() {
        return id;
    }

    public String getWord() {
        return word.getWord();
    }

    public Picture getFirstPicture() {
        return pictures.get(0);
    }

    public Picture getSecondPicture() {
        return pictures.get(1);
    }

    public Picture getThirdPicture() {
        return pictures.get(2);
    }

    public String getStatus() {
        return game_status;
    }
}
