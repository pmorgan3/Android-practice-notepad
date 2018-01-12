package com.example.pmorg.notepadpractice;

import static com.example.pmorg.notepadpractice.MainActivity.notesList;

/**
 * Created by Paul Morgan III on 1/6/2018.
 *
 * This class will just be the setters and getters for the title and body of the note.
 *
 * It will also save the time the note was made. This is to be used later when I update the app.
 *
 */

public class Notes {
    private String title;
    private String body;
    private long creationTime;

    public Notes(String tempTitle, String tempBod)
    {
        title = tempTitle;
        body = tempBod;
    }

    public Notes(String tempTitle, String tempBod, long tempCreationTime)
    {
        title = tempTitle;
        body = tempBod;
        creationTime = tempCreationTime;
    }

    public long getCreationTime()
    {

        return creationTime;
    }

    public void setCreationTime(long newCreationTime)
    {
        creationTime = newCreationTime;
    }

    public String getTitle()
    {
        return title;

    }
    public String getBody()
    {
        return body;
    }
    public void setTitle(String newTitle)
    {
        title = newTitle;
    }
    public void setBody(String newBody)
    {
        body = newBody;
    }

    public String toString()
    {
        return title;
    }
}
