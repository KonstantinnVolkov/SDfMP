package com.example.notes.DAO;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.notes.activities.MainActivity;
import com.example.notes.models.Note;
import com.example.notes.util.DBConnection;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class FirebaseNoteDAO implements NoteDAO{

    private final DatabaseReference databaseReference;

    public FirebaseNoteDAO() {
        this.databaseReference = DBConnection.getDbReference();
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot,
                                     @Nullable String previousChildName) {
                Map<String, Note> updatedNotesMap = MainActivity.notesLiveData.getValue();
                if (updatedNotesMap == null) {
                    return;
                }
                Note newNote = snapshot.getValue(Note.class);
                updatedNotesMap.put(newNote.getId(), newNote);
                MainActivity.notesLiveData.setValue(updatedNotesMap);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot,
                                       @Nullable String previousChildName) {
                Map<String, Note> updatedNotesMap = MainActivity.notesLiveData.getValue();
                if (updatedNotesMap == null) {
                    return;
                }
                Note newNote = snapshot.getValue(Note.class);
                updatedNotesMap.put(newNote.getId(), newNote);
                MainActivity.notesLiveData.setValue(updatedNotesMap);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Map<String, Note> updatedNotesMap = MainActivity.notesLiveData.getValue();
                if (updatedNotesMap == null) {
                    return;
                }
                Note deletedNote = snapshot.getValue(Note.class);
                updatedNotesMap.remove(deletedNote.getId());
                MainActivity.notesLiveData.setValue(updatedNotesMap);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot,
                                     @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "Failed to read in FirebaseNoteDAO.getAll() " + error.getCode());
            }
        });
    }

    @Override
    public Task<Map<String, Note>> findAll() {
        TaskCompletionSource<Map<String, Note>> taskCompletionSource =
                new TaskCompletionSource<>();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String, Note> notes = new HashMap<>();
                snapshot.getChildren()
                        .forEach((childSnapshot) -> {
                            Note note = childSnapshot.getValue(Note.class);
                            notes.put(note.getId(), note);
                        });
                taskCompletionSource.setResult(notes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "Failed to read in FirebaseNoteDAO.getAll() " + error.getCode());
            }
        });
        return taskCompletionSource.getTask();
    }

    @Override
    public void saveNote(Note note) {
        databaseReference.child(note.getId())
                .setValue(note)
                .addOnCompleteListener((it) -> {
                    if (it.isSuccessful()) {
                        Log.d(TAG, "Note saved successfully");
                    } else {
                        Log.e(TAG, "Error saving note: " + it.getException());
                    }
                });
    }

    @Override
    public void updateNote(Note note) {
        databaseReference.child(note.getId())
                .setValue(note);
    }

    @Override
    public void deleteNote(String taskUUID) {
        databaseReference.child(taskUUID)
                .removeValue()
                .addOnCompleteListener((it) -> {
                    if (it.isSuccessful()) {
                        Log.d(TAG, "Note deleted successfully");
                    } else {
                        Log.e(TAG, "Error deleting note: " + it.getException());
                    }
                });
    }
}
