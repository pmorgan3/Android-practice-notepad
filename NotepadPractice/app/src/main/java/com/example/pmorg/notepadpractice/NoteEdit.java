package com.example.pmorg.notepadpractice;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import es.dmoral.toasty.Toasty;

import static com.example.pmorg.notepadpractice.MainActivity.notesList;

/**
 * This class will be the activity for creating a new note.
 */
public class NoteEdit extends AppCompatActivity {

    EditText editTitle;
    EditText editBody;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_edit);
        editTitle = (EditText)findViewById(R.id.edit_title);
        editBody = (EditText)findViewById(R.id.edit_note);
        FloatingActionButton saveButton = (FloatingActionButton)findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //This makes sure that the note has a title. Notes HAVE to have a title
                //This is because we live in a civilised society and are not savages
                if(editTitle.getText().toString().equals("") ||
                        editTitle.getText().toString().equals(null))
                {
                    //Tells the user to give the note a title
                    //Toast.makeText(getApplicationContext(), "Please give the note a title",
                    // Toast.LENGTH_LONG).show();
                    Toasty.error(getApplicationContext(), "Please give the note a title",
                            Toast.LENGTH_LONG).show();
                }
                else {
                    //If the note already has a title then the file is saved, regardless
                    // if there's a body, and the Activity switches
                    //back to the main screen
                    saveFile();
                    Intent someIntent = new Intent(NoteEdit.this, MainActivity.class);
                    startActivity(someIntent);
                }

            }
        });
    }

    /**
     * This will save the file to the internal storage.
     */
    public void saveFile()
    {
        String fileTitle = editTitle.getText().toString(); //Gets the title of the new note
        String fileBody = editBody.getText().toString(); //Gets the body of the new note
        File file = new File(getBaseContext().getFilesDir(), fileTitle); //Makes a new file with
        // the title of the note
        MainActivity.notesList.add(0,new Notes(fileTitle,fileBody)); //Adds our new note to
        // our notesList array
        String tempTitleString = notesList.get(notesList.size()-1).getTitle(); //takes the title
                                                                               // of the newest note
        MainActivity.titleList.add(0,tempTitleString); //Adds the title to the title array

        //Writes to a file
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(fileTitle, Context.MODE_PRIVATE);
            outputStream.write(fileBody.getBytes());
            outputStream.close();
            //Success Toasty
            Toasty.success(getApplicationContext(), "Note saved", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException F) {
            F.printStackTrace();
            //Error Toasty
            Toasty.error(getApplicationContext(), "There was a file not found exception",
                    Toast.LENGTH_LONG).show();
        } catch (IOException I) {
            I.printStackTrace();
            //Error Toasty
            Toasty.error(getApplicationContext(), "There was an IOException",
                    Toast.LENGTH_LONG).show();
        } catch (IllegalArgumentException Il) {
            StringBuilder stringBuilder = new StringBuilder();
            for(int i = 0; i < fileTitle.length(); i++)
            {
                if (fileTitle.charAt(i) == '/')
                {
                    stringBuilder.append('-');
                }
                else {
                    stringBuilder.append(fileTitle.charAt(i));
                }
            }
            fileTitle = stringBuilder.toString();
            try {
                outputStream = openFileOutput(fileTitle, Context.MODE_PRIVATE);
                outputStream.write(fileBody.getBytes());
                outputStream.close();
                //Success Toasty
                Toasty.success(getApplicationContext(), "Note saved",
                        Toast.LENGTH_SHORT).show();
            } catch (Exception E) {
                E.printStackTrace();
                //Error toasty
                Toasty.error(getApplicationContext(),
                        "There was an illegal argument exception and your catch didn't work",
                        Toast.LENGTH_LONG).show();
            }
        }

    }


}
