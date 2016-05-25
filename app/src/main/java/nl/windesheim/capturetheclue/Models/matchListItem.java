package nl.windesheim.capturetheclue.Models;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * Created by Peter on 5/17/2016.
 */
public class matchListItem {

    private int avatar;
    private int match_id;
    private String opponent;
    private String turn_status;
    private boolean clickable;

    public matchListItem(int a, String o, String t) {
        match_id = a;
        opponent = o;
        turn_status = t;
    }

    public int getMatchID() {
        return match_id;
    }

    public String getOpponentInfo() {
        return opponent;
    }

    public void setOpponentInfo(String s) {
        opponent = s;
    }

    public String getTurnInfo() {
        return turn_status;
    }

    public void setTurnInfo(String s) {
        turn_status = s;
    }

    public void setClickable(boolean c) {
        clickable = c;
    }

    public boolean getClickable() {
        return clickable;
    }


}
