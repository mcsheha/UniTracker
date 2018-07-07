package com.mikeshehadeh.unitracker;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

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
        String whereClause = DBTables.assessmentTable.COLUMN_ASSESSMENT_ID + "= ?";
        String[] whereArgs = new String[]{Long.toString(tag)};
        dB.delete(DBTables.assessmentTable.TABLE_NAME, whereClause, whereArgs);
        mAdapter.swapCursor(getAllItems());


    }

    private void addNewAssessment() {
        //show dialog, get values when save is pressed and write values to db
        LayoutInflater factory = LayoutInflater.from(this);
        final View addAssmtDialogView = factory.inflate(R.layout.dialog_add_assessment, null);
        final AlertDialog createAssmt = new AlertDialog.Builder(this).create();
        createAssmt.setView(addAssmtDialogView);

        addAssmtDialogView.findViewById(R.id.dialog_add_assmt_btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check if input is valid, add the course to the db
                EditText assmntNameEditText = (EditText) addAssmtDialogView.findViewById(R.id.dialog_add_assmt_edit_text_name);
                RadioGroup radioGroup = (RadioGroup) addAssmtDialogView.findViewById(R.id.dialog_add_assmt_radio_btn_group);
                int selectedId = radioGroup.getCheckedRadioButtonId();
                RadioButton selectedButton = (RadioButton) addAssmtDialogView.findViewById(selectedId);

                String assmtType = selectedButton.getText().toString();
                String assmtName = assmntNameEditText.getText().toString();

                ContentValues cv = new ContentValues();
                cv.put(DBTables.assessmentTable.COLUMN_COURSE_ID, courseID);
                cv.put(DBTables.assessmentTable.COLUMN_ASSESSMENT_TYPE, assmtType);
                cv.put(DBTables.assessmentTable.COLUMN_ASSESSMENT_TITLE, assmtName);

                dB.insert(DBTables.assessmentTable.TABLE_NAME, null, cv);
                mAdapter.swapCursor(getAllItems());
                createAssmt.dismiss();

            }

    });

        addAssmtDialogView.findViewById(R.id.dialog_add_assmt_btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAssmt.dismiss();
            }
        });
        createAssmt.show();
    }


    private void buildRecyclerView() {
        mRecyclerView = findViewById(R.id.recycler_view_assmt_list);
        mLayoutManager = new LinearLayoutManager(AssessmentListActivity.this);
        mAdapter = new AssessmentListAdapter(AssessmentListActivity.this, getAllItems());

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
