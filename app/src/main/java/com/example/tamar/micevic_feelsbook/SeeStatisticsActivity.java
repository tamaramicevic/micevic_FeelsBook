package com.example.tamar.micevic_feelsbook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

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
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class SeeStatisticsActivity extends AppCompatActivity {
    public static ArrayList<Feeling> recordedFeelings = new ArrayList<Feeling>();
    private static final String FILENAME = "SavedFeelings.sav";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_statistics);

        loadFromFile();

        // get counts for each feeling
        int countLove = countFeelings(recordedFeelings, "Love");
        int countJoy = countFeelings(recordedFeelings, "Joy");
        int countSurprise = countFeelings(recordedFeelings, "Surprise");
        int countAnger = countFeelings(recordedFeelings, "Anger");
        int countSadness = countFeelings(recordedFeelings, "Sadness");
        int countFear = countFeelings(recordedFeelings, "Fear");

        // display counts for each feeling
        TextView textViewLove = findViewById(R.id.count_love);
        textViewLove.setText(String.valueOf(countLove));

        TextView textViewJoy = findViewById(R.id.count_joy);
        textViewJoy.setText(String.valueOf(countJoy));

        TextView textViewSurp = findViewById(R.id.count_surprise);
        textViewSurp.setText(String.valueOf(countSurprise));

        TextView textViewAnger = findViewById(R.id.count_anger);
        textViewAnger.setText(String.valueOf(countAnger));

        TextView textViewSad = findViewById(R.id.count_sadness);
        textViewSad.setText(String.valueOf(countSadness));

        TextView textViewFear = findViewById(R.id.count_fear);
        textViewFear.setText(String.valueOf(countFear));
    }

    // count how many saved instances there are of a single feeling and return the value
    public int countFeelings(ArrayList<Feeling> recordedFeelings, String feeling) {
        int count = 0;
        for (Feeling feel : recordedFeelings) {
            if (feel.getFeeling().equals(feeling)) {
                count += 1;
            }
        }
        return count;
    }

    // citing for loadFromFile in MainActivity
    private void loadFromFile() {
        try {
            FileInputStream fis = openFileInput(FILENAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader reader = new BufferedReader(isr);
            Gson gson = new Gson();
            Type listFeelingType = new TypeToken<ArrayList<Feeling>>(){}.getType();
            recordedFeelings.clear();
            recordedFeelings = gson.fromJson(reader, listFeelingType);
            fis.close();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            recordedFeelings = new ArrayList<Feeling>();
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
