package com.example.pmorg.notepadpractice;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

import static com.example.pmorg.notepadpractice.MainActivity.noteNumber;
import static com.example.pmorg.notepadpractice.MainActivity.notesList;
import static com.example.pmorg.notepadpractice.MainActivity.oldFileTitle;

/**
 * This class will be the activity that shows up when you click on a note to read/edit it.
 *
 */
public class ReadNoteActivity extends AppCompatActivity {
    TextView titleTextView;
    TextView bodyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_note);

        //This will generate the Text on the screen
        //This will also allow it to be editable
        titleTextView = (TextView) findViewById(R.id.title_text);
        titleTextView.setText(notesList.get(MainActivity.noteNumber).getTitle());
        bodyTextView = (TextView) findViewById(R.id.body_text);
        bodyTextView.setText(notesList.get(MainActivity.noteNumber).getBody());

        //Initialize the delete button
        FloatingActionButton deleteButton = (FloatingActionButton)findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Makes sure you want to delete this note forever
                new AlertDialog.Builder(ReadNoteActivity.this)
                        .setTitle("Delete")
                        .setMessage("Delete this note?")

                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            //If you do then the file will be deleted forever and if you don't then the dialog box
                            //will disappear and nothing will happen.
                            public void onClick(DialogInterface dialog, int whichButton) {
                                deleteFile(oldFileTitle);
                                Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_LONG).show();
                                Intent myIntent = new Intent(ReadNoteActivity.this, MainActivity.class);
                                startActivity(myIntent);
                            }})
                        .setNegativeButton(android.R.string.no, null).show();


            }
        });

        //Initialize the edit button
        FloatingActionButton editButton = (FloatingActionButton) findViewById(R.id.edit_button);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Saves the input
                if(!titleTextView.getText().toString().equals("") && !titleTextView.getText().toString().equals(null)) {
                    String tempTitleEdit = titleTextView.getText().toString();
                    String tempBodyEdit = bodyTextView.getText().toString();
                    //Removes the Note at this position in notesList
                    MainActivity.notesList.remove(noteNumber);
                    //Removes the Title at this position in titleList
                    MainActivity.titleList.remove(noteNumber);

                    //Saves the file
                    saveEditedFile(oldFileTitle, tempTitleEdit, tempBodyEdit);
                    //Goes back home
                    Intent goBackHome = new Intent(ReadNoteActivity.this, MainActivity.class);
                    startActivity(goBackHome);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Enter a Title", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    //This saves the edited file. The old file will actually be deleted and this will save as a whole new file.
    public void saveEditedFile(String oldTitle, String title, String body)
    {
        deleteFile(oldTitle);
        File file = new File(getBaseContext().getFilesDir(), title);
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(title, Context.MODE_PRIVATE);
            outputStream.write(body.getBytes());
            outputStream.close();
            Toast.makeText(getApplicationContext(),"Note saved", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"There was an error somewhere", Toast.LENGTH_LONG).show();
        }
    }
}
