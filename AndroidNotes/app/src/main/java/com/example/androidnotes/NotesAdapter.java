package com.example.androidnotes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesViewHolder> {

    private final List<Notes> notesList;
    private final MainActivity mainAct;

    public NotesAdapter(List<Notes> notesList, MainActivity ma){
        this.notesList= notesList;
        this.mainAct=ma;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.note_entry,parent,false);

        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);
        return new NotesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {

        Notes notes = notesList.get(position);

        holder.Title.setText(notes.getTitle());
        if(notes.getText().length()> 80){
            String concatText = notes.getText().substring(0,80);

            holder.Note.setText(concatText);
            holder.dateTime.setText(notes.getLastUpdateTime());
        }

        else holder.Note.setText(notes.getText());
            holder.dateTime.setText(notes.getLastUpdateTime());

    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }
}
