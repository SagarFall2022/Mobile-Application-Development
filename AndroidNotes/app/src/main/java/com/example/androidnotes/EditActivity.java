package com.example.androidnotes;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

public class EditActivity extends AppCompatActivity {
    // Define class fields
    private Notes note;
    private EditText title;
    private EditText text;

    // Called when the activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the content view to the activity_edit layout
        setContentView(R.layout.activity_edit);

        // Assign EditText objects for title and text to fields
        title= findViewById(R.id.TitleTextView);
        text= findViewById(R.id.NoteTextView);
        // Get the intent that started the activity and extract the note if it exists
        Intent intent = getIntent();
        if(intent.hasExtra("Notes")){
            note = (Notes) intent.getSerializableExtra("Notes");
            // If note exists, populate the title and text fields with the note's values
            if(note!=null){
                title.setText(note.getTitle());
                text.setText(note.getText());
            }
        }
    }
    // Inflate the menu and add items to the action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.edit,menu);
        return true;
    }
    // Called when an item in the options menu is selected
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if(item.getItemId()==R.id.save){
            // If the title field is not empty, save the note and return to the MainActivity
            if(!title.getText().toString().trim().isEmpty()){
                Return();
            }// If the title field is empty, display an error dialog
            else {
                newNoteDialog();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    public void newNoteDialog() {
        // Create a new AlertDialog using its Builder
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        // Set the title and negative button text of the dialog
        builder.setTitle("Please enter the title before clicking on save");
        builder.setNegativeButton("Cancel Note", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        // Set the positive button text and the onClickListener to empty method
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        // Create and show the AlertDialog
        AlertDialog dialog=builder.create();
        dialog.show();
    }

    @Override
    public void onBackPressed(){
        // Display a message when the back button is pressed
        Log.d("MyActivity", "onBackPressed() called");
        // Create a new AlertDialog Builder
        AlertDialog.Builder build = new AlertDialog.Builder(this);
        // Check if the title is blank, if true, set the dialog message and buttons
        if(title.getText().toString().trim().isEmpty()){
            build.setTitle("The note will not be saved if the title is left blank");
            build.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            build.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        } else{
            // Set the dialog message and buttons when the title is not blank
            build.setTitle("Your note is not saved! Please Save note "+ title.getText().toString() + "?");
            build.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Return();
                }
            });
            build.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            AlertDialog dialog= build.create();
            dialog.show();
        }
        // Create and show the AlertDialog when the title is blank
        AlertDialog dialog= build.create();
        dialog.show();
    }

    public void Return() {
        // Get the heading and content from the EditText views
        String heading = title.getText().toString();
        String content= text.getText().toString();
        // Check if note exists
        if(note!=null){
            // If the note is changed, set the new date, title and text; otherwise, just finish the activity
            if(!note.getText().equals(content) || !note.getTitle().equals(heading)){
                note.setNewDate(new Date());
                note.setTitle(heading);
                note.setText(content);
            }else finish();
        } else{
            // Create a new note with the entered heading and content and set the new date
            note = new Notes(heading,content);
            note.setNewDate(new Date());
        }
        // Create a new intent, put the note object and send the result back to the caller activity
        Intent intent = new Intent();
        intent.putExtra("Notes", note);
        setResult(RESULT_OK,intent);
        finish();
    }

}