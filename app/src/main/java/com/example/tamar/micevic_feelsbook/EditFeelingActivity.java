package com.example.tamar.micevic_feelsbook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

public class EditFeelingActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Feeling editedFeeling;
    private static ArrayList<Feeling> recordedFeelings = new ArrayList<Feeling>();
    private static final String FILENAME = "SavedFeelings.sav";
    public String newFeeling;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_feeling);

        Intent intent = getIntent();

        // getting the feeling that was specified to be edited from the intent
        editedFeeling = (Feeling) intent.getSerializableExtra("EDITFEELING");
        String currFeeling = editedFeeling.getFeeling();

        // define how each detail of a feeling is edited
        //      *feeling: has a spinner that allows user to choose from the list of feelings
        //      *date: has editText to change values of the date
        //      *comment: has editText to change the comment associated with the feeling
        Spinner spinner = (Spinner) findViewById(R.id.edit_feeling);
        spinner.setOnItemSelectedListener(this);

        EditText editTextDate = (EditText) findViewById(R.id.edit_date);
        editTextDate.setText(editedFeeling.getDateAsString());

        EditText editTextComment = (EditText) findViewById(R.id.edit_comment);
        editTextComment.setText(editedFeeling.getComment());

        // sets up the spinner for editing the feeling
        ArrayAdapter<CharSequence> adapterFeeling = ArrayAdapter.createFromResource(this,
                R.array.namesFeeling, android.R.layout.simple_spinner_item);
        adapterFeeling.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterFeeling);

        // when activity is first opened, the app shows what the current feeling is and not just
        // the first feeling that is in the array
        if (currFeeling.matches("Love")) spinner.setSelection(0);
        else if (currFeeling.matches("Joy")) spinner.setSelection(1);
        else if (currFeeling.matches("Surprise")) spinner.setSelection(2);
        else if (currFeeling.matches("Anger")) spinner.setSelection(3);
        else if (currFeeling.matches("Sadness")) spinner.setSelection(4);
        else if (currFeeling.matches("Fear")) spinner.setSelection(5);

    }

    @Override
    protected void onStart() {
        super.onStart();
        loadFromFile();
    }

    // gets the index of the specified feeling within the saved feelings list
    // does this by checking the dates as that is the Key of the Feeling class
    public int getPosition() {
        for (int i = 0; i < recordedFeelings.size(); i++) {
            Feeling feel = recordedFeelings.get(i);
            if (feel !=null && feel.getDateAsString().equals(editedFeeling.getDateAsString())) {
                return i;
            }
        }
        return -1;// not in array list of feelings
    }

    // applies changes made to the feeling and adds it to the saved feelings list
    // also saves it in the file to be kept
    public void sendEdits(View view) {

        EditText editTextDate = (EditText) findViewById(R.id.edit_date);
        String strDate = editTextDate.getText().toString();

        EditText editTextComment = (EditText) findViewById(R.id.edit_comment);
        String comment = editTextComment.getText().toString();

        int idx = getPosition();
        try {
            editedFeeling.setFeeling(newFeeling);
            editedFeeling.setDateAsDateObj(strDate);
            editedFeeling.setComment(comment);
            recordedFeelings.set(idx, editedFeeling);
            saveInFile();
        } catch (CommentTooLongException e) {
            e.printStackTrace();
        }
        finish();
    }

    // deletes the specified feeling and applies the change to the saved feelings list
    public void sendDelete(View view) {
        int idx = getPosition();
        recordedFeelings.remove(idx);
        saveInFile();
        finish();
    }

    // returns the new feeling selected from the spinner
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        newFeeling = (String) parent.getItemAtPosition(pos);
    }

    // returns nothing (the last feeling) from the spinner
    public void onNothingSelected(AdapterView<?> parent) {
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
