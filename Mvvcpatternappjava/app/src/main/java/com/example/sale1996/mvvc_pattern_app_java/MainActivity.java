package com.example.sale1996.mvvc_pattern_app_java;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int ADD_NOTE_REQUEST = 1;
    public static final int EDIT_NOTE_REQUEST = 2;

    private NoteViewModel noteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton buttonAddNote = findViewById(R.id.button_add_note);
        buttonAddNote.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
                startActivityForResult(intent, ADD_NOTE_REQUEST);
            }
        });

        //instanciramo recycle view
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        //postavljamo layout da se itemi unutar njega instaciraju jedan ispod drugog
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final NoteAdapter adapter = new NoteAdapter();
        recyclerView.setAdapter(adapter);

        //sada prvo instanciramo viewmodel pa se onda subskrajbujemo na livedata "allNotes"
        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                // u ovoj funkciji updejtamo sta nas zanima kada se baza updejta
                //adapter.setNotes(notes);
                //nakon dodavanja animacija dobijamo ovo...
                adapter.submitList(notes);
            }
        });

        /*
        * Sada ce da se implementira SWIPE elementa na levo ili desno...
        *
        * Prvi parametar za ItemTouchHelper.SimpleCallback kaze da li radimo drag&drop..
        * dok drugi govori za swipe i tu smo naveli da hocemo i swipe u levo i swipe u desno
        * */
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                //sa direction propertijem mozemo da razlikujemo u kom smeru je swipe uradjen
                //pa da radimo drugacije operacije...
                noteViewModel.delete(adapter.getNoteAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Note deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView); //ukoliko ne uradimo attach, dzaba smo krecili...

        //OVDE POSTAVLJAMO LISTENER ZA EDITOVANJE!
        adapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Note note) {
                Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
                intent.putExtra(AddNoteActivity.EXTRA_ID, note.getId());
                intent.putExtra(AddNoteActivity.EXTRA_TITLE, note.getTitle());
                intent.putExtra(AddNoteActivity.EXTRA_DESCRIPTION, note.getDescription());
                intent.putExtra(AddNoteActivity.EXTRA_PRIORITY, note.getPriority());

                startActivityForResult(intent, EDIT_NOTE_REQUEST);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK){
            String title = data.getStringExtra(AddNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddNoteActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddNoteActivity.EXTRA_PRIORITY, 1);

            Note note = new Note(title,description, priority);
            noteViewModel.insert(note);

            Toast.makeText(this, "Note saved!", Toast.LENGTH_SHORT).show();
        }
        else if(requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK){
            int id = data.getIntExtra(AddNoteActivity.EXTRA_ID, -1);

            if(id == -1){
                Toast.makeText(this, "Note can not be updated!", Toast.LENGTH_SHORT).show();
            }

            String title = data.getStringExtra(AddNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddNoteActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddNoteActivity.EXTRA_PRIORITY, 1);

            Note note = new Note(title,description, priority);
            note.setId(id);
            noteViewModel.update(note);

            Toast.makeText(this, "Note updated!", Toast.LENGTH_SHORT).show();

        }
        else{
            Toast.makeText(this, "Note not saved!", Toast.LENGTH_SHORT).show();
        }
    }

    //dodavanje menija
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater  = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_all_notes:
                noteViewModel.deleteAllNotes();
                Toast.makeText(this, "All notes deleted! Congrats!", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
