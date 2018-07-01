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

public class AssessmentListActivity extends AppCompatActivity {
    private AssessmentListAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private SQLiteDatabase dB;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton buttonAddAssessment;
    private long courseID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_list);

        DBHelper dbHelper = new DBHelper(this);
        dB = dbHelper.getWritableDatabase();

        Bundle b = getIntent().getExtras();
        courseID = b.getLong("courseID");

        buildRecyclerView();
        configureHomeButton();

        buttonAddAssessment = findViewById(R.id.button_assmt_list_add);
        buttonAddAssessment.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                addNewAssessment();
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

    private void addNewAssessment() {
    }


    private void buildRecyclerView() {
        mRecyclerView = findViewById(R.id.recycler_view_assmt_list);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new AssessmentListAdapter(this, getAllItems());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new AssessmentListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                RecyclerView.ViewHolder viewHolder = mRecyclerView.findViewHolderForAdapterPosition(position);
                //Put code here for what happens when an item is clicked
                long tag = (long) viewHolder.itemView.getTag();
                showAssessmentDetails(tag);
            }
        });
    }


    private void showAssessmentDetails(long tag) {
        Intent i = new Intent(this, AssessmentDetailActivity.class);
        i.putExtra("assmntTag", tag);
        startActivity(i);
    }

    private void configureHomeButton() {
        FloatingActionButton backButton = (FloatingActionButton) findViewById(R.id.btn_assmt_list_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }



    private Cursor getAllItems() {
        String whereClause = DBTables.assessmentTable.COLUMN_COURSE_ID + "= ?";
        String[] whereArgs = new String[]{Long.toString(courseID)};
        return dB.query(DBTables.assessmentTable.TABLE_NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                DBTables.assessmentTable.COLUMN_ASSESSMENT_ID + " ASC"

        );
    }
}
