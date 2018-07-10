package com.mikeshehadeh.unitracker;

import android.content.ContentUris;
import android.content.ContentValues;
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
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

public class CourseMentorListActivity extends AppCompatActivity {
    private CourseMentorListAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private SQLiteDatabase dB;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton buttonAddMentor;
    private long courseID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_mentor_list);

        Bundle b = getIntent().getExtras();
        courseID = b.getLong("courseID");

        DBHelper dbHelper = new DBHelper(this);
        dB = dbHelper.getWritableDatabase();

        buildRecyclerView();
        configureBackButton();
        configureAddButton();

        String title = getDesignator() + " Mentors";
        setTitle(title);

        buttonAddMentor = findViewById(R.id.button_course_mentor_list_add);
        buttonAddMentor.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                assignMentorToCourse();
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
                removeMentorFromCourse((long) viewHolder.itemView.getTag());

            }
        }).attachToRecyclerView(mRecyclerView);
    }

    private String getDesignator() {
        String whereClause = DBTables.courseTable.COLUMN_COURSE_ID + "=?";
        String[] whereArgs = new String[]{Long.toString(courseID)};

        Cursor c = dB.query(DBTables.courseTable.TABLE_NAME, null, whereClause, whereArgs, null, null, null);
        c.moveToFirst();
        return c.getString(c.getColumnIndex(DBTables.courseTable.COLUMN_COURSE_DESIGNATOR));

    }

    private void configureAddButton() {
        FloatingActionButton addButton = (FloatingActionButton) findViewById(R.id.button_course_mentor_list_add);
        addButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                assignMentorToCourse();
            }
        });
    }


    @Override
    public void onResume(){
        super.onResume();
        mAdapter.swapCursor(getAllItems());

    }

    private String[] getAssignedMentorIDs(){
        String whereClause = DBTables.courseMentorTable.COLUMN_COURSE_ID + "=?";
        String[] whereArgs = new String[]{Long.toString(courseID)};
        ArrayList<String> arrayListIDs = new ArrayList<>();
        Cursor c = dB.query(DBTables.courseMentorTable.TABLE_NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                DBTables.courseMentorTable.COLUMN_MENTOR_ID + " ASC");
        //iterate cursor and add to mentor IDs to the arraylist
        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            arrayListIDs.add(c.getString(c.getColumnIndex(DBTables.courseMentorTable.COLUMN_MENTOR_ID)));
        }

        return arrayListIDs.toArray(new String[arrayListIDs.size()]);
    }


    private Cursor getAllItems() {
        String sql = "SELECT mentor.id, mentor.name, mentor.email, mentor.phone " +
        "FROM mentor " +
        "JOIN course_mentor ON mentor.id=course_mentor.mentor_id " +
        "WHERE course_mentor.course_id = " + courseID;

        String whereClause = DBTables.courseMentorTable.COLUMN_COURSE_ID + "=?";
        String[] whereArgs = new String[]{Long.toString(courseID)};
        return dB.rawQuery(sql,null);
    }



    private void removeMentorFromCourse(long tag) {
        String whereClause = DBTables.courseMentorTable.COLUMN_MENTOR_ID + "=?";
        String [] whereArgs = new String[]{Long.toString(tag)};
        dB.delete(DBTables.courseMentorTable.TABLE_NAME, whereClause, whereArgs);
        mAdapter.swapCursor(getAllItems());
    }


    private void assignMentorToCourse() {
        //Show a drop down with the list of mentors, save input to DB, swap cursor
        LayoutInflater factory = LayoutInflater.from(this);
        final View addMentorToCourseDialogView = factory.inflate(R.layout.dialog_add_mentor_to_course, null);
        final AlertDialog addMentorToCourse = new AlertDialog.Builder(this).create();
        addMentorToCourse.setView(addMentorToCourseDialogView);

        final Spinner spinner = (Spinner) addMentorToCourseDialogView.findViewById(R.id.dialog_add_mentor_to_course_spinner);
        String[] spinnerItems = getAllMentorNames();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //save new term button clicked
        addMentorToCourseDialogView.findViewById(R.id.dialog_add_mentor_to_course_btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedMentorName = spinner.getSelectedItem().toString();
                long mentorId = getMentorIdFromName(selectedMentorName);
                if(!checkIfAlreadyInTable(mentorId)){
                    ContentValues cv = new ContentValues();
                    cv.put(DBTables.courseMentorTable.COLUMN_COURSE_ID, courseID);
                    cv.put(DBTables.courseMentorTable.COLUMN_MENTOR_ID, mentorId);
                    dB.insert(DBTables.courseMentorTable.TABLE_NAME, null, cv);

                }


                mAdapter.swapCursor(getAllItems());

                addMentorToCourse.dismiss();

            }
        });

        //cancel button clicked
        addMentorToCourseDialogView.findViewById(R.id.dialog_add_mentor_to_course_btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMentorToCourse.dismiss();
            }
        });


        addMentorToCourse.show();

    }

    private String[] getAllMentorNames() {
        ArrayList<String> mentorsArrayList = new ArrayList<>();
        Cursor c = dB.query(DBTables.mentorTable.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            mentorsArrayList.add(c.getString(c.getColumnIndex(DBTables.mentorTable.COLUMN_MENTOR_NAME)));
        }
        return mentorsArrayList.toArray(new String[mentorsArrayList.size()]);
    }

    private boolean checkIfAlreadyInTable(long mentorId) {
        String whereClause = DBTables.courseMentorTable.COLUMN_COURSE_ID + "= ? AND " +
                DBTables.courseMentorTable.COLUMN_MENTOR_ID + "= ?";
        String[] whereArgs = new String[]{Long.toString(courseID), Long.toString(mentorId)};
        Cursor c = dB.query(DBTables.courseMentorTable.TABLE_NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null);
        if (c.getCount()>0) {return true;}
        else return false;
    }

    private long getMentorIdFromName(String selectedMentorName) {
        String whereClause = DBTables.mentorTable.COLUMN_MENTOR_NAME + "= ?";
        String[] whereArgs = new String[]{selectedMentorName};
        Cursor c = dB.query(DBTables.mentorTable.TABLE_NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null);
        c.moveToFirst();
        return c.getLong(c.getColumnIndex(DBTables.mentorTable.COLUMN_MENTOR_ID));
    }

    private void buildRecyclerView() {
        mRecyclerView = findViewById(R.id.recycler_view_course_mentor_list);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new CourseMentorListAdapter(this, getAllItems());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);


    }

    private void configureBackButton() {
        FloatingActionButton homeButton = (FloatingActionButton) findViewById(R.id.btn_course_mentor_list_back);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}





















