package com.example.androidnotes;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NotesViewHolder extends RecyclerView.ViewHolder {
    TextView Title;

    TextView Note;

    TextView dateTime;

    NotesViewHolder(@NonNull View view){
        super(view);
        Title=view.findViewById(R.id.Title);
        Note= view.findViewById(R.id.note);
        dateTime=view.findViewById(R.id.DateTime);
    }
}
