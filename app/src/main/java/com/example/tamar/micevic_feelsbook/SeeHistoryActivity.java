package com.example.tamar.micevic_feelsbook;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.internal.bind.ArrayTypeAdapter;
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
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static android.provider.Telephony.Mms.Part.FILENAME;

public class SeeHistoryActivity extends AppCompatActivity {
    private static ArrayList<Feeling> recordedFeelings = new ArrayList<Feeling>();
    private static final String FILENAME = "SavedFeelings.sav";
    private FeelingsAdapter adapter;
    private static ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_history);

        loadFromFile();
        listView = (ListView) findViewById(R.id.hist_list);

        // single click to edit a single feeling
        listView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Feeling feeling = (Feeling) listView.getItemAtPosition(position);
                editFeeling(view, feeling);
            }
        });

        // long click to view the comment associated with the feeling
        listView.setOnItemLongClickListener( new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Feeling feeling = (Feeling) listView.getItemAtPosition(position);
                String comment = feeling.getComment();
                displayComment(view, comment);
                return true;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        // update list view with any changes made to data from EditFeelingActivity
        loadFromFile();

        // sorts the feelings according to date from most recent to least recent
        Collections.sort(recordedFeelings, new Comparator<Feeling>() {
            public int compare(Feeling f1, Feeling f2) {
                return f2.getDate().compareTo(f1.getDate());
            }
        });

        adapter = new FeelingsAdapter(this, recordedFeelings);
        listView.setAdapter(adapter);
    }

    // allows user to edit specified feeling
    public void editFeeling(View view, Feeling feeling) {
        Intent intent = new Intent(this, EditFeelingActivity.class);
        intent.putExtra("EDITFEELING", (Serializable) feeling);
        startActivity(intent);
    }

    // allows user to view the comment associated with specified feeling
    public void displayComment(View view, String comment){
        final View v = view;
        AlertDialog.Builder builder = new AlertDialog.Builder(SeeHistoryActivity.this);
        builder.setTitle("Comment Provided: ");
        TextView input = (TextView)getLayoutInflater().inflate(R.layout.activity_comment_display, null);
        builder.setView(input);
        input.setText(comment);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        builder.show();
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
            ArrayList<Feeling> tmp = gson.fromJson(reader, listFeelingType);
            recordedFeelings.addAll(tmp);
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
