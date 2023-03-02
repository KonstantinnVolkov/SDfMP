package com.example.notes.adadpters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.DAO.FirebaseNoteDAO;
import com.example.notes.R;
import com.example.notes.activities.NewNoteActivity;
import com.example.notes.models.Note;

import java.text.DateFormat;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private Context context;
    private List<Note> notes;
    private final String POPUP_MENU_ITEM_DELETE = "DELETE";
    private final String POPUP_MENU_ITEM_EDIT = "EDIT";
    private final FirebaseNoteDAO noteDAO = new FirebaseNoteDAO();

    public NoteAdapter(Context context, List<Note> notes) {
        this.context = context;
        this.notes = notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(
                LayoutInflater.from(context)
                        .inflate(
                                R.layout.note_item_view,
                                parent,
                                false
                        )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = notes.get(position);
        String formattedDate = DateFormat.getDateInstance().format(note.getCreatedAt());

        holder.noteIdOutput.setText(note.getId());
        holder.titleOutput.setText(note.getTitle());
        holder.descriptionOutput.setText(note.getDescription());
        holder.createdAtDateOutput.setText(formattedDate);

        holder.itemView.setOnClickListener(v -> {
            openNewNoteActivity(note);
        });

        holder.itemView.setOnLongClickListener((v) -> {
            PopupMenu popupMenu = new PopupMenu(context, v);
            popupMenu.getMenu().add(POPUP_MENU_ITEM_EDIT);
            popupMenu.getMenu().add(POPUP_MENU_ITEM_DELETE);
            popupMenu.setOnMenuItemClickListener((item) -> {
                switch (item.getTitle().toString()) {
                    case POPUP_MENU_ITEM_DELETE ->
                            noteDAO.deleteNote(note.getId());
                    case POPUP_MENU_ITEM_EDIT -> {
                        openNewNoteActivity(note);
                    }
                    default -> {
                        return false;
                    }
                }
                return true;
            });
            popupMenu.show();
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {

        TextView noteIdOutput;
        TextView titleOutput;
        TextView descriptionOutput;
        TextView createdAtDateOutput;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            this.noteIdOutput = itemView.findViewById(R.id.noteId_output);
            this.titleOutput = itemView.findViewById(R.id.title_output);
            this.descriptionOutput = itemView.findViewById(R.id.description_output);
            this.createdAtDateOutput = itemView.findViewById(R.id.created_at_date_output);
        }
    }

    private void openNewNoteActivity(Note note){
        Intent intent = new Intent(context, NewNoteActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("noteId", note.getId());
        intent.putExtra("noteTitle", note.getTitle());
        intent.putExtra("noteDescription", note.getDescription());
        context.startActivity(intent);
    }
}
