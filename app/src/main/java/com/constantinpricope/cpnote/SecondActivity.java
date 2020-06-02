package com.constantinpricope.cpnote;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class SecondActivity  extends AppCompatActivity {
    DataBaseInterface dataBaseInterface;
    ArrayList<String> all_notes;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_layout);
        all_notes= new ArrayList<String>();


        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        // Capture the layout's TextView and set the string as its text
        TextView textView = findViewById(R.id.textView);
        writeToFile("Ce ai salvat in fisier este: ", this);

        String result =  readFromFile(this);

        dataBaseInterface = new DataBaseInterface(this);

        dataBaseInterface.writeToDatabase("item" + dataBaseInterface.getLast_id(), message);

        textView.setText(message);
        long i= dataBaseInterface.getLast_id();
//        while(i>=0)
//        {
//            all_notes.add(dataBaseInterface.readFromDatabase("item"+i));
//        }



    }

    public void writeToFile(String data, Context context) {
        Log.e("Exception", "Into writeTofile ");
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("config.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public String readFromFile(Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("config.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append("\n").append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

    public void addANewNote(View view) {
        Intent intent = new Intent(this, AddNote.class);
//        String message = "First title in Rss Feed is:" +" " + titles.get(0);
//        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
}

