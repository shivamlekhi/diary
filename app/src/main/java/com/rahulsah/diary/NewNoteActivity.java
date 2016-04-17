package com.rahulsah.diary;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by ShivamLekhi on 3/30/16.
 */
public class NewNoteActivity extends AppCompatActivity {
    TextView titleView;
    TextView entryView;
    Note note;

    public static final String Mode = "Mode";
    public static final String NewMode = "NewMode";
    public static final String EditMode = "EditMode";

    public static final String EntryId = "EntryId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_note_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        titleView = (TextView) findViewById(R.id.note_title);
        entryView = (TextView) findViewById(R.id.note_entry);

        if(getIntent().getStringExtra(Mode).equals(EditMode)) {
            Long noteId = getIntent().getLongExtra(EntryId, Long.valueOf("100"));
            note = Note.findById(Note.class, noteId);

            titleView.setText(note.getTitle());
            entryView.setText(note.getEntry());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        int id = getIntent().getStringExtra(Mode).equals(NewMode) ? R.menu.new_note_menu : R.menu.edit_note_menu;

        getMenuInflater().inflate(id, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.create_note:
                if(titleView.getText().toString().isEmpty() || entryView.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Please fill out both fields", Toast.LENGTH_LONG).show();
                } else {
                    Note note = new Note(titleView.getText().toString(), entryView.getText().toString());
                    note.save();

                    finish();
                    startActivity(new Intent(this, MainActivity.class));
                }
                break;
            case R.id.edit_note:
                note.title = titleView.getText().toString();
                note.entry = entryView.getText().toString();

                note.save();
                finish();
                startActivity(new Intent(this, MainActivity.class));
                break;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
    }
}
