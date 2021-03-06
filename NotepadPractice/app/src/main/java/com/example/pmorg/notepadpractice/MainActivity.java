package com.example.pmorg.notepadpractice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
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
import java.util.Comparator;
import java.util.List;

import es.dmoral.toasty.Toasty;

/**
 * This is the "home screen" activity of the app, so to speak.
 * This uses a ListView to show a list of notes that the user has created.
 *
 * There's probably A TON of bad practices in this project that I don't know about since I'm new
 *
 * The end goal of this app is to keep updating it so I can learn more about Android development
 * in the process
 *
 * Later on I want to convert this entire thing to Kotlin.
 *
 * Next on the todo list: add a button to change the way your list is sorted.
 * How to do this: Literally just implement different sorting methods. By default though
 * it will sort by last modified.
 *
 *
 *
 * Created by Paul Morgan III on 1/6/2018
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    /**
     * I made all these variable static because I wanted to use them throughout the project
     * in each activity.
     *
     * Bad practice? Probably.
     *
     */

    public static boolean isSortedByNewest;
    public static boolean isSortedByOldest;
    public static boolean isSortedAlphabetically;
    public static boolean isSortedReverseAlphabetically;
    public static int noteNumber;
    public static ArrayList<Notes> notesList = new ArrayList<>();
    public static ArrayList<String> titleList = new ArrayList<>();
    public static String oldFileTitle;
    private static File[] files;
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

                //This will hold the number of the position of the selected note in the
                // notesList array.
                noteNumber = position;

                Intent goToNoteIntent = new Intent(MainActivity.this,
                        ReadNoteActivity.class);

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
                //Toast.makeText(MainActivity.this,"The button was at least pressed",
                // Toast.LENGTH_LONG).show();
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                  //      .setAction("Action", null).show();
            }
        });


        //Makes the slidy menu thingy
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
     * Sorts the array by Oldest to newest hopefully
     */
    public static void sortByOldest()
    {
        /**
         * Low key don't know if how i'm supposed to make sure the app doesn't switch back to
         * sorting by first modified everytime it goes back to this activity and everytime the
         * loadFiles() method is called.
         *
         * Hmmmmmm,
         *
         * \
         */
        Arrays.sort(files, (a, b) -> Long.compare(a.lastModified(), b.lastModified()));
        File[] tempFiles = new File[files.length];
        int count = 0;
        for(int i = files.length-1; i >= 0; i--)
        {
            tempFiles[count] = files[i];
            count++;
        }

        count = 0;
        for(int i = 0; i < files.length; i++)
        {
            files[i] = tempFiles[i];
        }

    }

    /**
     * This method probably sorts the files newest first
     */
    public static void sortByNewest()
    {
        Arrays.sort(files, (a, b) -> Long.compare(a.lastModified(), b.lastModified()));
    }


    /**
     * SHOULD sort alphabetically but I am unsure if it will
     */
    public static void sortReverseAlphabetically()
    {


        Arrays.sort(files);
    }

    /**
     * Hypothetically this will sort reverse alphabetically
     */
    public static void sortAlphabetically()
    {
        File[] tempFiles = new File[files.length];
        Arrays.sort(files);
        int count = 0;
        for(int i = files.length-1; i >= 0; i--)
        {
            tempFiles[count] = files[i];
            count++;
        }

        count = 0;
        for (int i = 0; i <= files.length-1; i++)
        {
            files[i] = tempFiles[i];
        }
    }

    /**
     * Allows saved notes to be loaded into the ListView.
     */
    public void loadFiles()
    {
        File directory = getFilesDir();
        files = directory.listFiles();
        //Sees what settings the user has chosen. and will organize as such. Will default to
        //sorting by last modified.
        if(isSortedReverseAlphabetically) {
            sortReverseAlphabetically();
        } else if (isSortedAlphabetically) {
            sortAlphabetically();
        } else if (isSortedByOldest) {
            sortByOldest();
        } else if (isSortedByNewest) {
            sortByNewest();
        } else Arrays.sort(files, (a, b) -> Long.compare(a.lastModified(), b.lastModified()));

        String filename;
        notesList.clear();
        titleList.clear();
        for (int i = 0; i < files.length; i++)
        {
            try {


                filename = files[i].getName();
                Notes note = new Notes(filename, openFile(filename));
                notesList.add(0, note);
                titleList.add(0, filename);
            }
            catch (NullPointerException e)
            {
                Toasty.error(this,
                        "There was a NullPointerException one of your files is MISSING!!!",
                        Toast.LENGTH_LONG)
                        .show();
            }
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
            //Toast.makeText(this, "Exception: " + t.toString(), Toast.LENGTH_LONG).show();
            Toasty.error(this, "Exception: " + t.toString(),
                    Toast.LENGTH_LONG)
                    .show();
        }

        return content;
    }




    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //Refreshes the notes list.
    public void onTheMenuItemClick()
    {
        Intent intent = getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        recreate();
        //finish();
        //startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.menu_reverse_alphabetical) {
            MainActivity.isSortedByNewest = false;
            MainActivity.isSortedByOldest = false;
            MainActivity.isSortedAlphabetically = false;
            MainActivity.isSortedReverseAlphabetically = true;
            onTheMenuItemClick();
        } else if (id == R.id.menu_alphabetical) {
            MainActivity.isSortedByNewest = false;
            MainActivity.isSortedByOldest = false;
            MainActivity.isSortedAlphabetically = true;
            MainActivity.isSortedReverseAlphabetically = false;
            onTheMenuItemClick();
        } else if (id == R.id.menu_oldest) {
            MainActivity.isSortedByNewest = false;
            MainActivity.isSortedByOldest = true;
            MainActivity.isSortedAlphabetically = false;
            MainActivity.isSortedReverseAlphabetically = false;
            onTheMenuItemClick();
        } else if (id == R.id.menu_newest) {
            MainActivity.isSortedByNewest = true;
            MainActivity.isSortedByOldest = false;
            MainActivity.isSortedAlphabetically = false;
            MainActivity.isSortedReverseAlphabetically = false;
            onTheMenuItemClick();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
