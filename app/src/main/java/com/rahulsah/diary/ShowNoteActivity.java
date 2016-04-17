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

/**
 * Created by ShivamLekhi on 3/30/16.
 */
public class ShowNoteActivity extends AppCompatActivity {
    TextView title, entry;

    public static final String EntryId = "EntryId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_note_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar3);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        title = (TextView) findViewById(R.id.entry_title_textview);
        entry = (TextView) findViewById(R.id.entry_body_textview);

        Note note = Note.findById(Note.class, getIntent().getExtras().getLong(EntryId));

        title.setText(note.getTitle());
        entry.setText(note.getEntry());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.show_note_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.edit_note_button:
                Intent intent = new Intent(this, NewNoteActivity.class);

                intent.putExtra(NewNoteActivity.Mode, NewNoteActivity.EditMode);
                intent.putExtra(NewNoteActivity.EntryId, getIntent().getExtras().getLong(EntryId));

                this.finish();
                startActivity(intent);
                break;
        }
        return true;
    }
}
