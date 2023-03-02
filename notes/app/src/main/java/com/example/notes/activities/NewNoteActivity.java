package com.example.notes.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.notes.DAO.FirebaseNoteDAO;
import com.example.notes.R;
import com.example.notes.models.Note;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class NewNoteActivity extends AppCompatActivity {

    private TextInputEditText mTitleInput;
    private TextInputEditText mDescriptionInput;
    private final FirebaseNoteDAO noteDAO = new FirebaseNoteDAO();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        mTitleInput = findViewById(R.id.title_input);
        mDescriptionInput = findViewById(R.id.description_input);

        String id = "";
        String title;
        String description;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getString("noteId");
            title = extras.getString("noteTitle");
            description = extras.getString("noteDescription");

            mTitleInput.setText(title);
            mDescriptionInput.setText(description);
        }

        Button saveNoteButton = findViewById(R.id.save_button);
        //effectively final var for lambda (kakoy-to bullshit)
        String finalId = id;
        saveNoteButton.setOnClickListener((v) -> {
            final Note noteToSave = buildNote(finalId);
            if (noteToSave == null) {
                return;
            }
            noteDAO.saveNote(noteToSave);
            setResult(Activity.RESULT_OK);
            finish();
        });
    }

    private Note buildNote(String finalId){
        if (finalId.equals("")) {
            finalId = UUID.randomUUID().toString();
        }
        final String title =
                Objects.requireNonNull(mTitleInput.getText())
                        .toString()
                        .trim();
        final String description =
                Objects.requireNonNull(mDescriptionInput.getText())
                        .toString()
                        .trim();
        final Date createdAt = new Date();

        if (title.isEmpty() || description.isEmpty()) {
            Toast.makeText(NewNoteActivity.this,
                    "Title and description cannot be empty!",
                    Toast.LENGTH_SHORT
            ).show();
            return null;
        }
        return new Note(finalId, title, description, createdAt);
    }
}