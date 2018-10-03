package com.example.tamar.micevic_feelsbook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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
import java.lang.reflect.Type;
import java.util.ArrayList;

public class SaveFeelingActivity extends AppCompatActivity {
    private static final String COMMENT = "com.example.tamar.micevic-feelsbook.COMMENT";
    private static final String FILENAME = "SavedFeelings.sav";
    private static ArrayList<Feeling> recordedFeelings = new ArrayList<Feeling>();
    private static Feeling currFeeling = new Feeling();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_feeling);

        Intent intent = getIntent();
        String feeling = intent.getStringExtra("FEELING");
        String date = intent.getStringExtra("DATE");
        currFeeling.setFeeling(feeling);
        currFeeling.setDateAsDateObj(date);

        String message = feeling + " on " + date;

        // Capture the layout's TextView and set the string as its text
        TextView textView = findViewById(R.id.curr_feeling_display);
        textView.setText(message);
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadFromFile();
    }

    // adds ability to add an optional comment to the recently added feeling
    public void sendOptionalComment(View view) {
        EditText editText = (EditText) findViewById(R.id.enter_comment);
        String comment = editText.getText().toString();
        // if comment provided then add comment and save
        if (!comment.isEmpty()) {
            Toast.makeText(SaveFeelingActivity.this, "Your comment has been saved!", Toast.LENGTH_LONG).show();
            try {
                for (Feeling feeling:recordedFeelings) {
                    if (currFeeling.getDateAsString().equals(feeling.getDateAsString())) {
                        feeling.setComment(comment);
                        saveInFile();
                    }
                }
            } catch (CommentTooLongException e) {
                e.printStackTrace();
            }
        }
        finish();
    }

    // citing for loadFromFile and SaveInFile in MainActivity
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
