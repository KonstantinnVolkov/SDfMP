package com.example.notes.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.notes.DAO.FirebaseNoteDAO;
import com.example.notes.R;
import com.example.notes.adadpters.NoteAdapter;
import com.example.notes.models.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private NoteAdapter noteAdapter;
    private RecyclerView mRecyclerView;
    private FloatingActionButton createNewNoteBtn;

    private final FirebaseNoteDAO noteDAO = new FirebaseNoteDAO();
    public static final MutableLiveData<Map<String, Note>> notesLiveData = new MutableLiveData<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notesLiveData.observe(this, (notes) -> {
            List<Note> notesToSortList = new ArrayList<>(notes.values());
            if (notesToSortList.size() != 1) {
                Collections.sort(notesToSortList, new Note());
            }
            updateUI(notesToSortList);
        });

        noteDAO.findAll().addOnCompleteListener((task) -> {
            if (task.isSuccessful()) {
                Map<String, Note> taskResultNotesMap = task.getResult();
                notesLiveData.setValue(taskResultNotesMap);
            }
        });
        List<Note> notesToRender = new ArrayList<>();
        setupMainActivity(notesToRender);
    }

    private void setupMainActivity(List<Note> notes) {
        noteAdapter = new NoteAdapter(getApplicationContext(), notes);

        mRecyclerView = findViewById(R.id.notes_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        mRecyclerView.setAdapter(noteAdapter);

        createNewNoteBtn = findViewById(R.id.create_new_note_button);
        createNewNoteBtn.setOnClickListener((v) ->
                openNewNoteActivity()
        );
    }

    private void updateUI(List<Note> notesToRender){
        if (noteAdapter == null) {
            noteAdapter = new NoteAdapter(getApplicationContext(), notesToRender);
            mRecyclerView.setAdapter(noteAdapter);
        } else {
            noteAdapter.setNotes(notesToRender);
            noteAdapter.notifyDataSetChanged();
        }
    }

    private void openNewNoteActivity() {
        Intent intent = new Intent(this, NewNoteActivity.class);
        startActivity(intent);
    }

}