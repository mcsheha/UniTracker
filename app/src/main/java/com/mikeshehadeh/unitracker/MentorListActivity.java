package com.mikeshehadeh.unitracker;

import android.content.Context;
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
import android.widget.Toast;

public class MentorListActivity extends AppCompatActivity {
    private MentorListAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private SQLiteDatabase dB;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton buttonAddMentor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor_list);

        DBHelper dbHelper = new DBHelper(this);
        dB = dbHelper.getWritableDatabase();

        buildRecyclerView();
        configureHomeButton();

        buttonAddMentor = findViewById(R.id.button_mentor_list_add);
        buttonAddMentor.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                addNewMentor();
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



    private void removeItem(long tag) {
    }

    private void buildRecyclerView() {
        mRecyclerView = findViewById(R.id.recycler_view_mentor_list);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new MentorListAdapter(this, getAllItems());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new MentorListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                RecyclerView.ViewHolder viewHolder = mRecyclerView.findViewHolderForAdapterPosition(position);
                //Put code here for what happens when an item is clicked
                long tag = (long) viewHolder.itemView.getTag();
                Context context = getApplicationContext();
                CharSequence text = "Position " + tag + " Clicked";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                showMentorDetails(tag);

            }
        });
    }

    private void showMentorDetails(long tag) {
        //Uncomment after creating MentorDetail screen
        //Intent i = new Intent(this, MentorDetailActivity.class);
        //i.putExtra("courseTag", tag);
        //startActivity(i);
    }

    private void configureHomeButton() {
        FloatingActionButton homeButton = (FloatingActionButton) findViewById(R.id.btn_mentor_list_home);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addNewMentor() {
    }


    private Cursor getAllItems() {
        return dB.query(DBTables.mentorTable.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                DBTables.mentorTable.COLUMN_MENTOR_ID + " ASC"

        );
    }
}
