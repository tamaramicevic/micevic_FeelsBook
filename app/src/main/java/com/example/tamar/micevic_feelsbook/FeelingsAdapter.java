package com.example.tamar.micevic_feelsbook;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FeelingsAdapter extends ArrayAdapter<Feeling> {

    private Context context;
    private ArrayList<Feeling> recordedFeelings;

    // created a custom adapter for the Feeling class
    public FeelingsAdapter (Context context, ArrayList<Feeling> recordedFeelings) {
        super(context, 0, recordedFeelings);
        this.context = context;
        this.recordedFeelings = recordedFeelings;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;

        if (listItem == null) {
            listItem = LayoutInflater.from(context).inflate(R.layout.activity_hist_listview, parent, false);
        }
        Feeling current_feeling = recordedFeelings.get(position);

        // set feeling and date to be viewed by user for each item in the listview in the SeeHistoryActivity
        TextView emotion = (TextView) listItem.findViewById(R.id.Feeling);
        emotion.setText(current_feeling.getFeeling());

        TextView date = (TextView) listItem.findViewById(R.id.Date);
        date.setText(current_feeling.getDateAsString());

        return listItem;
    }
}