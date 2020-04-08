package com.example.sale1996.mvvc_pattern_app_java;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

public class AddNoteActivity extends AppCompatActivity {
    public static final String EXTRA_ID =
            "com.example.sale1996.mvvc_pattern_app_java.EXTRA_ID";
    public static final String EXTRA_TITLE =
            "com.example.sale1996.mvvc_pattern_app_java.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION =
            "com.example.sale1996.mvvc_pattern_app_java.EXTRA_DESCRIPTION";
    public static final String EXTRA_PRIORITY =
            "com.example.sale1996.mvvc_pattern_app_java.EXTRA_PRIORITY";

    private EditText editTextTitle;
    private EditText editTextDescription;
    private NumberPicker numberPickerPriority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        numberPickerPriority = findViewById(R.id.number_picker_priority);

        //min i max value mora preko jave da se uradi za number picker
        numberPickerPriority.setMinValue(1);
        numberPickerPriority.setMaxValue(10);

        //ovako postavljamo back dugme da ima ikonicu ic_close
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        //Posto na ovom prozoru imamo i mogucnost editovanja, onda cemo da proverimo
        //nas intent da li u sebi sadrzi ID, ako sadrzi.. onda moramo da namestimo
        //prozor da bude za EDIT!
        Intent intent = getIntent();

        if(intent.hasExtra(EXTRA_ID)){
            setTitle("Edit Note");
            editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            editTextDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            numberPickerPriority.setValue(intent.getIntExtra(EXTRA_PRIORITY,1));

        }
        else{
            setTitle("Add Note");
        }
    }

    private void saveNote(){
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        int priority = numberPickerPriority.getValue();

        if(title.trim().isEmpty() || description.trim().isEmpty()){
            Toast.makeText(this, "Please insert title and description BOTH!", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DESCRIPTION, description);
        data.putExtra(EXTRA_PRIORITY, priority);

        //check if we have id :D
        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if(id != -1){
            data.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();
    }

    //postavljanje meni-bar-a
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        //kaze postavi nas meni na ovoj aktivnosti da bude kao na prosledjenom layoutu
        menuInflater.inflate(R.menu.add_note_menu, menu);
        //true znaci prikazi menu, ako je false onda nemoj prikazati
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_note:
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
