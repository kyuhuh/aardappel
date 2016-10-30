package nl.windesheim.capturetheclue.Models;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.view.menu.MenuView;
import android.support.v7.view.menu.MenuView.ItemView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import nl.windesheim.capturetheclue.R;


public class matchListItemAdapter extends ArrayAdapter<matchListItem> {

    matchListItem match;

    public matchListItemAdapter(Context context, ArrayList<matchListItem> matches) {
        super(context, 0, matches);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        match = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.match_list_item, parent, false);
        }
        // Lookup view for data population
        TextView oppo = (TextView) convertView.findViewById(R.id.firstLine);
        TextView state = (TextView) convertView.findViewById(R.id.secondLine);
        // Populate the data into the template view using the data object
        oppo.setText(match.getOpponentInfo());
        state.setText(match.getTurnInfo());
        if (!match.getClickable()) {
            convertView.setBackgroundColor(Color.LTGRAY);
            convertView.setEnabled(false);
            convertView.setOnClickListener(null);
        }
        // Return the completed view to render on screen

        return convertView;
    }
}
