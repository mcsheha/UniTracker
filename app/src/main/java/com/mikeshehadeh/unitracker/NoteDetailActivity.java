package com.mikeshehadeh.unitracker;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class NoteDetailActivity extends AppCompatActivity {

    private long noteId;
    private SQLiteDatabase dB;
    private String noteText;
    private String courseId;
    private String courseDesignator;
    private boolean isNewNewNote;
    String subject;
    String body;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        Bundle b = getIntent().getExtras();
        noteId = b.getLong("noteTag");
        courseId = Long.toString(b.getLong("courseId"));


        DBHelper dbHelper = new DBHelper(this);
        dB = dbHelper.getWritableDatabase();

        if (noteId < 0){
            isNewNewNote = true;
            noteText = "";
            setCourseDesignator();
            getNewNoteId();
        } else {
            isNewNewNote = false;
            getAllItems();
        }

        configureBackButton();
        configureSaveButton();

        setTextViews();
        configureShareButton();

        //checkIsNewNote();

    }

    private void configureShareButton() {


        FloatingActionButton backButton = (FloatingActionButton) findViewById(R.id.note_dtl_btn_email);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText mNoteText = (EditText)findViewById(R.id.note_detail_note_text);
                body = mNoteText.getText().toString();

                subject = "Note for " + courseDesignator;
                Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                shareIntent.putExtra(Intent.EXTRA_TEXT, body);
                startActivity(Intent.createChooser(shareIntent, "Share Via:"));

            }
        });

    }

    private void getNewNoteId() {
        String selection = "SELECT MAX(" + DBTables.noteTable.COLUMN_NOTE_ID + ") FROM " + DBTables.noteTable.TABLE_NAME + ";";
        SQLiteStatement stmt = dB.compileStatement(selection);
        noteId = (long) stmt.simpleQueryForLong();
        noteId = noteId + 1;


    }

    private void setCourseDesignator() {
        //get the course name from the course ID
       String whereClause = DBTables.courseTable.COLUMN_COURSE_ID + "= ?";
        String[] whereArgs = new String[]{courseId};
        Cursor c2 = dB.query(DBTables.courseTable.TABLE_NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        c2.moveToFirst();

        courseDesignator = c2.getString(c2.getColumnIndex(DBTables.courseTable.COLUMN_COURSE_DESIGNATOR));
    }
/*

    private void checkIsNewNote() {
        String whereClause = DBTables.noteTable.COLUMN_NOTE_ID + "=?";
        String[] whereArgs = new String[]{Long.toString(noteId)};
        Cursor c = dB.query(DBTables.noteTable.TABLE_NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null);
        c.moveToFirst();
        if(c.getCount()>0) {
            isNewNewNote = false;
        }else{isNewNewNote = true;}
    }

*/

    private void getAllItems() {
        String whereClause = DBTables.noteTable.COLUMN_NOTE_ID + "= ?";
        String[] whereArgs = new String[]{Long.toString(noteId)};
        Cursor c = dB.query(DBTables.noteTable.TABLE_NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        c.moveToFirst();

        noteText = c.getString(c.getColumnIndex(DBTables.noteTable.COLUMN_NOTE_TEXT));
        courseId = c.getString(c.getColumnIndex(DBTables.noteTable.COLUMN_COURSE_ID));

        setCourseDesignator();

    }

    private void setTextViews() {
        TextView tvNoteText = (TextView) findViewById(R.id.note_detail_note_text);

        String noteTitle = "Note for " + courseDesignator + ":";

        setTitle(noteTitle);
        tvNoteText.setText(noteText);



    }

    private void configureBackButton() {
        FloatingActionButton backButton = (FloatingActionButton) findViewById(R.id.note_dtl_btn_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void configureSaveButton() {
        FloatingActionButton backButton = (FloatingActionButton) findViewById(R.id.note_dtl_btn_save);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {handleSave();
                finish();
            }
        });
    }

    private void handleSave() {
        //update the DB noteTable, noteText field with new note text.
        //get text from the edit text, save as string and update the notetext column
        EditText mNoteText = (EditText)(findViewById(R.id.note_detail_note_text));

        String noteText = mNoteText.getText().toString();

        if(!isNewNewNote) {
            String whereClause = DBTables.noteTable.COLUMN_NOTE_ID + "= ?";
            String[] whereArgs = new String[]{Long.toString(noteId)};

            ContentValues cv = new ContentValues();
            cv.put(DBTables.noteTable.COLUMN_NOTE_TEXT, noteText);
            cv.put(DBTables.noteTable.COLUMN_COURSE_ID, courseId);

            dB.update(DBTables.noteTable.TABLE_NAME,
                    cv,
                    whereClause,
                    whereArgs);
        }else{
            ContentValues cv = new ContentValues();
            cv.put(DBTables.noteTable.COLUMN_NOTE_ID, noteId);
            cv.put(DBTables.noteTable.COLUMN_COURSE_ID, courseId);
            cv.put(DBTables.noteTable.COLUMN_NOTE_TEXT, noteText);
            dB.insert(DBTables.noteTable.TABLE_NAME, null, cv);
        }

    }


}
