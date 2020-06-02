package com.constantinpricope.cpnote;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class AddNote extends AppCompatActivity {

    Button button_add_to_database;
    Button button_add_to_file;
    Button button_show_file;
    TextView textViewDisplayFile;
    Context actualContext = this;
    EditText notita;
    DataBaseInterface dataBaseInterface;
    long i = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_note_layout);
        button_add_to_database = (Button) findViewById(R.id.button_add_to_database);
        button_add_to_file = (Button) findViewById(R.id.button_add_to_file);
        button_show_file = (Button) findViewById(R.id.button_show_file);
        textViewDisplayFile = (TextView) findViewById(R.id.textViewDisplayFile);
        notita = (EditText) findViewById(R.id.notita);
        dataBaseInterface = new DataBaseInterface(this);

        button_show_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewDisplayFile.setText(readFromFile(actualContext));
//                textViewDisplayFile.setText(dataBaseInterface.readFromDatabase("item0"));
            }
        });

        button_add_to_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textToWrite = notita.getText().toString();
                writeToFile(textToWrite, actualContext);
            }
        });

        button_add_to_database.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textToWrite = notita.getText().toString();
                dataBaseInterface.writeToDatabase("item" + dataBaseInterface.getLast_id(), textToWrite);

            }
        });
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

    public void writeFile(String mValue) {

        try {
            String mFileName = "config.txt";
            String filename = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + mFileName;
            FileWriter fw = new FileWriter(filename, true);
            fw.write(mValue + "\n\n");
            fw.close();
        } catch (IOException ioe) {
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
}
