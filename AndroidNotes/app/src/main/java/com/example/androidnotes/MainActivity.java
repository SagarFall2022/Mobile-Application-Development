package com.example.androidnotes;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener{
    // Define two static final integer variables.
    private static final int ONCLICK_CODE = 1;
    private static final int NEW_NOTE_CODE = 2;

    // Define a List of Notes, a RecyclerView, a Note object, a NotesAdapter object, and an integer variable.
    private List<Notes> notesList= new ArrayList<>();
    private RecyclerView recyclerView;
    private Notes note;
    private NotesAdapter notesAdapter;
    private int position =-1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the content view for the activity.
        setContentView(R.layout.activity_main);
        // Find the RecyclerView by its ID and set its adapter and layout manager.
        recyclerView= findViewById(R.id.recycler);
        notesAdapter= new NotesAdapter(notesList,this);
        recyclerView.setAdapter(notesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Load data from a file and display it in the RecyclerView.
        notesList.addAll(loadFile());
        notesAdapter.notifyDataSetChanged();

        // Log each note in the notesList.
        for (int i = 0; i < notesList.size(); i++) {
            Notes note = notesList.get(i);
            Log.d("notesList", "Note " + i + ": " + note.toString());
        }
        // Set the title of the action bar to "Android Notes (n)" where n is the number of notes in the list.
        if(notesList!= null){
            getSupportActionBar().setTitle("Android Notes"+"("+notesList.size() +")");
        }

    }

    // Load data from a file and return a List of Notes.
    private List<Notes> loadFile(){
        // Create a new empty list to hold the notes
     List<Notes> listOfNote = new ArrayList<>();
        // Get the name of the file to read from resources
     String fileName= getString(R.string.nameOfFile);


     try{
         // Open the file as an input stream and create a BufferedReader to read from it
         InputStream is = getApplication().getApplicationContext().openFileInput(fileName);
         BufferedReader br = new BufferedReader( new InputStreamReader(is, StandardCharsets.UTF_8));

         // Read the file line by line and append each line to a StringBuilder
         StringBuilder sb = new StringBuilder();
         String line;
         while((line = br.readLine())!=null)
             sb.append(line);
         // Convert the StringBuilder to a JSONArray
         JSONArray jsonArray= new JSONArray(sb.toString());

         // Iterate over the JSONArray and convert each JSONObject to a Notes object and add it to the list
         for(int i=0; i< jsonArray.length();i++){
             JSONObject jsonObject= jsonArray.getJSONObject(i);
             String title = jsonObject.getString("Title");
             String text= jsonObject.getString("Note");
             String date= jsonObject.getString("dateTime");
             Notes note= new Notes(title,text,date);
             notesList.add(note);
         }

     } catch (FileNotFoundException e) {
         e.printStackTrace();
     } catch (IOException e) {
         e.printStackTrace();
     } catch (JSONException e) {
         e.printStackTrace();
     }
        // Return the list of notes
        return listOfNote;
    }

    // Save data to a file when the activity is destroyed or stopped.
    private void saveData() {
        try {
            // open a private file with the specified file name
            FileOutputStream outStream = getApplicationContext()
                    .openFileOutput(getString(R.string.nameOfFile), Context.MODE_PRIVATE);
            // create a PrintWriter object for writing the data to the file
            PrintWriter printWriter = new PrintWriter(outStream);
            // print the notesList to the file
            printWriter.print(notesList);
            // close the print writer and the output stream
            printWriter.close();
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    // Save data to a file when the activity is destroyed.
    @Override
    protected void onDestroy() {
        saveData();
        super.onDestroy();
    }
    // Save data to a file when the activity is stopped.
    @Override
    protected void onStop() {
        saveData();
        super.onStop();
    }


    // Inflate the menu for the activity.
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            // if the user clicked on the "Add" button
            case R.id.add:
                Log.d(TAG, "onOptionsItemSelected: Clicked on the Add button Sager");
                // create an intent to open the EditActivity to create a new note
                Intent intent = new Intent(this, EditActivity.class);
                startActivityForResult(intent,NEW_NOTE_CODE);
                break;
            // if the user clicked on the "Info" button
            case R.id.info:
                // create an intent to open the AboutActivity to show app info
                Intent intent1= new Intent(this,AboutActivity.class);
                startActivity(intent1);
                break;
            // if the user clicked on a different menu item
            default:
                // let the parent handle the event
                return super.onOptionsItemSelected(item);
        }
        // let the parent handle the event
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onClick(View v) {
        // get the position of the clicked item in the RecyclerView
        position= recyclerView.getChildLayoutPosition(v);
        // get the corresponding note from the notesList
        Notes n = notesList.get(position);
        // create an intent to open the EditActivity to edit the note
        Intent intent= new Intent(this, EditActivity.class);
        // pass the note to the intent as an extra
        intent.putExtra("Notes", n);
        // start the activity and wait for a result
        startActivityForResult(intent,ONCLICK_CODE);
    }

    @Override
    public boolean onLongClick(View v) {
        // get the position of the long-clicked item in the RecyclerView
        int pos = recyclerView.getChildLayoutPosition(v);
        // create an AlertDialog to confirm the user wants to delete the note
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("Do you want to Delete this note?");
        // if the user clicked "Yes" in the AlertDialog
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // remove the note from the notesList
                notesList.remove(pos);
                // notify the RecyclerView adapter that the data has changed
                notesAdapter.notifyDataSetChanged();
                // update the title of the activity with the number of remaining notes
                if(notesList!=null){
                    getSupportActionBar().setTitle("Android Notes"+ " ("+ notesList.size() + ")");
                }
            }
        });
        // if the user clicked "No" in the AlertDialog
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        // create the AlertDialog and show it to the user
        AlertDialog dialog= builder.create();
        dialog.show();
        // tell the system that the long-click event is handled
        return true;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        // if the EditActivity returned a note
        if(data!=null){
            note = (Notes) data.getSerializableExtra("Notes");
        }
        // if the EditActivity returned a result and we came from a single-click event
        if(requestCode == ONCLICK_CODE && resultCode == RESULT_OK){
            // if we have a note and the position is valid
            if(note!=null && position> -1){
                // remove the old note and add the new note at the top of the notesList
                notesList.remove(position);
                notesList.add(0,note);
                // notify the RecyclerView adapter that the data has changed
                notesAdapter.notifyDataSetChanged();
            }
        } else if (requestCode== NEW_NOTE_CODE && resultCode==RESULT_OK){
            if(note!=null){
                notesList.add(0,note);
                // notify the RecyclerView adapter that the data has changed
                notesAdapter.notifyDataSetChanged();
            }
        }
        // update the title of the activity's action bar to show the number of notes
        if(notesList!=null){
            getSupportActionBar().setTitle("Android Notes"+ " ("+ notesList.size() + ")");
        }
    }


}