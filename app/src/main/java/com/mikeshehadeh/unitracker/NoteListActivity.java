package com.mikeshehadeh.unitracker;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

public class NoteListActivity extends AppCompatActivity {
    private NoteListAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private SQLiteDatabase dB;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton buttonAddNote;
    private long courseID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);

        DBHelper dbHelper = new DBHelper(this);
        dB = dbHelper.getWritableDatabase();

        Bundle b = getIntent().getExtras();
        courseID = b.getLong("courseID");

        buildRecyclerView();
        configureHomeButton();

        buttonAddNote = findViewById(R.id.button_note_list_add);
        buttonAddNote.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                addNewNote();
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                removeItem((long) viewHolder.itemView.getTag());

            }
        }).attachToRecyclerView(mRecyclerView);




    }


    @Override
    public void onResume(){
        super.onResume();
        mAdapter.swapCursor(getAllItems());

    }

    private Cursor getAllItems() {
        String whereClause = DBTables.noteTable.COLUMN_COURSE_ID + "= ?";
        String[] whereArgs = new String[]{Long.toString(courseID)};
        return dB.query(DBTables.noteTable.TABLE_NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                DBTables.noteTable.COLUMN_NOTE_ID + " DESC"
        );

    }

    private void removeItem(long tag) {
        String whereClause = DBTables.noteTable.COLUMN_NOTE_ID + "=?";
        String [] whereArgs = new String[]{Long.toString(tag)};
        dB.delete(DBTables.noteTable.TABLE_NAME, whereClause, whereArgs);
        mAdapter.swapCursor(getAllItems());


    }

    private void addNewNote() {
        Intent i = new Intent(this, NoteDetailActivity.class);
        long noteLong = -1;
        i.putExtra("noteTag", noteLong);
        i.putExtra("courseId", courseID);
        startActivity(i);
    }

    private void configureHomeButton() {
        FloatingActionButton backButton = (FloatingActionButton) findViewById(R.id.btn_note_list_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void buildRecyclerView() {
        mRecyclerView = findViewById(R.id.recycler_view_note_list);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new NoteListAdapter(this, getAllItems());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new NoteListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                RecyclerView.ViewHolder viewHolder = mRecyclerView.findViewHolderForAdapterPosition(position);
                //Put code here for what happens when an item is clicked
                long tag = (long) viewHolder.itemView.getTag();
                showNoteDetails(tag);
            }
        });
    }

    private void showNoteDetails(long tag) {
        Intent i = new Intent(this, NoteDetailActivity.class);
        i.putExtra("noteTag", tag);
        startActivity(i);
    }





}
