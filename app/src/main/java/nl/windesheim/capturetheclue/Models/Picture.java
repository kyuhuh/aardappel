package nl.windesheim.capturetheclue.Models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

import nl.windesheim.capturetheclue.Connection.Server;

/**
 * Created by Peter on 4/12/2016.
 */
public class Picture {

    private int id;
    private String fileurl;
    private String datecreated;
    private int flagged;
    private int uid;


    public String getFileurl() {

        return fileurl;
    }
}


