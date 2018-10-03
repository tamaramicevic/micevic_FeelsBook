package com.example.tamar.micevic_feelsbook;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import java.io.Serializable;
import java.lang.reflect.Type;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static final String FILENAME = "SavedFeelings.sav";

    private static ArrayList<Feeling> recordedFeelings = new ArrayList<Feeling>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Button to open the menu that takes user to the history or statistics activity
        Button menuButton = (Button) findViewById(R.id.button_menu);
        menuButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                openMenu(v);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadFromFile();
    }

    // Send the selected feeling to be saved and adds an option to input a comment
    public void sendFeeling(View view) {
        String feeling = ((Button) view).getText().toString();
        Date date = Calendar.getInstance().getTime();

        // Adds selected feeling to list of feelings
        Feeling currFeeling = new Feeling(feeling, date);
        recordedFeelings.add(currFeeling);
        saveInFile();

        Intent intent = new Intent(this, SaveFeelingActivity.class);
        intent.putExtra("FEELING", currFeeling.getFeeling());
        intent.putExtra("DATE",currFeeling.getDateAsString());
        startActivity(intent);
    }

    // Takes user to see the history of their inputted feelings
    public void openHistory(View view) {
        Intent intent = new Intent(this, SeeHistoryActivity.class);
        startActivity(intent);
    }

    // Takes user to see the statistics of their inputted feelings
    public void openStats(View view) {
        Intent intent = new Intent(this, SeeStatisticsActivity.class);
        startActivity(intent);
    }

    // Take user to the menu where they have the option to see the history, statistics, or return
    // back to input a new feeling
    public void openMenu(View view) {
        final View v = view;
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Menu");
        builder.setItems(R.array.menu_items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    openHistory(v);
                } else if (which == 1) {
                    openStats(v);
                } else {
                    finish();
                }
            }
        });
        builder.create();
        builder.show();
    }

    // Citing loadfromFile() and saveinFile(), this is for all instances of these functions:
    // From Rosevear, https://github.com/Rosevear/lonelyTwitter, September 18, 2018
    // I could not find the specific code from the TA but I do have the code that I wrote in my own LonelyTwitter
    // function that I wrote from the TA in the Lab here :
    // https://github.com/tamaramicevic/lonelyTwitter/blob/master/app/src/main/java/ca/ualberta/cs/lonelytwitter/LonelyTwitterActivity.java
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

    private void saveInFile() {
        try {
            FileOutputStream fos = openFileOutput(FILENAME, 0);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter writer = new BufferedWriter(osw);
            Gson gson = new Gson();
            gson.toJson(recordedFeelings, writer);
            writer.flush();
            fos.close();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
