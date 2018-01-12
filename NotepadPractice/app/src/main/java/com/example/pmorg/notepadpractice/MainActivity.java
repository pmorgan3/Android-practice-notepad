package com.example.pmorg.notepadpractice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * This is the "home screen" activity of the app, so to speak.
 * This uses a ListView to show a list of notes that the user has created.
 *
 * There's probably A TON of bad practices in this project that I don't know about since I'm new
 *
 * The end goal of this app is to keep updating it so I can learn more about Android development in the process
 *
 * Later on I want to convert this entire thing to Kotlin.
 *
 * Created by Paul Morgan III on 1/6/2018
 */
public class MainActivity extends AppCompatActivity {
    /**
     * I made all these variable static because I wanted to use them throughout the project in each activity.
     *
     * Bad practice? Probably.
     */
    public static int noteNumber;
    public static ArrayList<Notes> notesList = new ArrayList<>();
    public static ArrayList<String> titleList = new ArrayList<>();
    public static String oldFileTitle;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        //Creates the ListView and sets a corresponding adapter
        listView = (ListView)findViewById(R.id.note_list_view);
        ArrayAdapter<String> notesArrayAdapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1,titleList);
        listView.setAdapter(notesArrayAdapter);

        //Loads the files
        loadFiles();

        //This line allows for the clicking of each individual item.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //This will hold the number of the position of the selected note in the notesList array.
                noteNumber = position;

                Intent goToNoteIntent = new Intent(MainActivity.this, ReadNoteActivity.class);

                oldFileTitle = notesList.get(noteNumber).getTitle();
                startActivity(goToNoteIntent);
            }
        });

        //sets the add button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //goes to the new intent
                Intent myIntent = new Intent(MainActivity.this, NoteEdit.class);
                startActivity(myIntent);
                //The toast makes sure that the button was pressed
                //Toast.makeText(MainActivity.this,"The button was at least pressed", Toast.LENGTH_LONG).show();
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                  //      .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //This will return the ArrayList of notes.
    public ArrayList<Notes> getNotesList()
    {
        return notesList;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * Allows saved notes to be loaded into the ListView.
     */
    public void loadFiles()
    {
        File directory = getFilesDir();
        File[] files = directory.listFiles();
        Arrays.sort(files, (a, b) -> Long.compare(a.lastModified(), b.lastModified()));
        String filename;
        notesList.clear();
        titleList.clear();
        for (int i = 1; i <= files.length-1; i++)
        {
            filename = files[i].getName();
            Notes note = new Notes(filename, openFile(filename));
            notesList.add(0,note);
            titleList.add(0,filename);
        }

    }

    /**
     * Opens each file and reads the contents of the file and returns the body of the file
     * @param file
     * @return the content of the file
     */
    public String openFile(String file)
    {
        String content = "";
        try {
            InputStream in = openFileInput(file);
            if ( in != null) {
                InputStreamReader tmp = new InputStreamReader( in );
                BufferedReader reader = new BufferedReader(tmp);
                String str;
                StringBuilder buf = new StringBuilder();
                while ((str = reader.readLine()) != null) {
                    buf.append(str + "\n");
                } in .close();

                content = buf.toString();
            }
        } catch (java.io.FileNotFoundException e) {} catch (Throwable t) {
            Toast.makeText(this, "Exception: " + t.toString(), Toast.LENGTH_LONG).show();
        }

        return content;
    }

}
