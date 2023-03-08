package com.example.androidnotes;

import android.util.JsonWriter;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Notes implements Serializable {

    private  String title;
    private  String text;
    private String lastUpdateTime;
    private final String format="EEE, MMM dd yyyy 'at' HH:mm";
    public static int noteNumber;

    public Notes(String title, String text) {

        this.title = title;
        this.text = text;
        SimpleDateFormat deform= new SimpleDateFormat(format, Locale.US);
        lastUpdateTime= deform.format(new Date());
        noteNumber++;
    }

    public Notes(String title, String text, String lastUpdateTime) {

        this.title = title;
        this.text = text;
        this.lastUpdateTime=lastUpdateTime;
        noteNumber++;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setNewDate(Date d){
        SimpleDateFormat dateFormat= new SimpleDateFormat(format,Locale.US);
        lastUpdateTime= dateFormat.format(new Date());
    }

    @NonNull
    @Override
    public String toString() {
        try {
            StringWriter stringWriter = new StringWriter();
            JsonWriter jsonWriter = new JsonWriter(stringWriter);
            jsonWriter.setIndent("  ");
            jsonWriter.beginObject();
            jsonWriter.name("title").value(getTitle());
            jsonWriter.name("content").value(getText());
            jsonWriter.name("date").value(lastUpdateTime);
            jsonWriter.endObject();
            jsonWriter.close();
            return stringWriter.toString();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
