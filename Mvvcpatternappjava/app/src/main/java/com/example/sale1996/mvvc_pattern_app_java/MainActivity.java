package com.example.sale1996.mvvc_pattern_app_java;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private NoteViewModel noteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                adapter.setNotes(notes);
            }
        });
    }
}
