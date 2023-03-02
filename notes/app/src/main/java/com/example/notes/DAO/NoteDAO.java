package com.example.notes.DAO;

import com.example.notes.models.Note;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface NoteDAO {

    void saveNote(Note note);
    void deleteNote(String taskUUID);
    Task<Map<String, Note>> findAll();
    void updateNote(Note note);


}
