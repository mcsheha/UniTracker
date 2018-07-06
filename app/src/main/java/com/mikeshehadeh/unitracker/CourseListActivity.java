package com.mikeshehadeh.unitracker;

import android.content.ContentValues;
import android.content.Context;
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
import android.widget.Toast;

public class CourseListActivity extends AppCompatActivity {
    private CourseListAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private SQLiteDatabase dB;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton buttonAddCourse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        DBHelper dbHelper = new DBHelper(this);
        dB = dbHelper.getWritableDatabase();

        buildRecyclerView();
        configureHomeButton();

        buttonAddCourse = findViewById(R.id.button_crs_list_add);
        buttonAddCourse.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                addNewCourse();
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
    private void removeItem(long courseID) {
        dB.delete(DBTables.courseTable.TABLE_NAME,
                DBTables.courseTable.COLUMN_COURSE_ID + "=" + courseID, null);
        mAdapter.swapCursor(getAllItems());
    }

    private void addNewCourse() {
        LayoutInflater factory = LayoutInflater.from(this);
        final View addCourseDialogView = factory.inflate(R.layout.dialog_create_course, null);
        final AlertDialog createCourse = new AlertDialog.Builder(this).create();
        createCourse.setView(addCourseDialogView);

        //save new term button clicked
        addCourseDialogView.findViewById(R.id.create_course_btn_add_course).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check if input is valid, add the course to the db
                EditText designatorEditText = (EditText)addCourseDialogView.findViewById(R.id.create_mentor_dialog_edt_txt_name);
                EditText nameEditText = (EditText)addCourseDialogView.findViewById(R.id.create_mentor_dialog_edt_txt_email);
                EditText cusEditText = (EditText)addCourseDialogView.findViewById(R.id.create_mentor_dialog_edt_txt_date);

                String designator = designatorEditText.getText().toString();
                String courseName = nameEditText.getText().toString();


                String cusString = cusEditText.getText().toString();
                int cus = 0;
                if (!cusString.isEmpty()) {
                    cus = Integer.valueOf(cusString);
                }

                if(checkInputValid(designator, courseName, cus)) {
                    ContentValues cv = new ContentValues();
                    cv.put(DBTables.courseTable.COLUMN_COURSE_DESIGNATOR, designator);
                    cv.put(DBTables.courseTable.COLUMN_COURSE_NAME, courseName);
                    cv.put(DBTables.courseTable.COLUMN_CU, cus);

                    dB.insert(DBTables.courseTable.TABLE_NAME, null, cv);
                    mAdapter.swapCursor(getAllItems());
                    createCourse.dismiss();
                }
            }
        });

        //cancel button clicked
        addCourseDialogView.findViewById(R.id.create_mentor_btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createCourse.dismiss();
            }
        });


        createCourse.show();

    }


    private boolean checkInputValid(String designator, String courseName, int cus) {
        boolean inputValid = true;
        if(designator == null || designator.trim().isEmpty()) {
            inputValid = false;
        }
        if (courseName == null || courseName.trim().isEmpty()) {
            inputValid = false;
        }
        if (cus <= 0) {
            inputValid = false;
        }
        if(!inputValid) {
            //toast invalid inputs
            Context context = getApplicationContext();
            CharSequence text = "Inputs not valid!";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

        }

        return inputValid;
    }

    private void configureHomeButton() {
        FloatingActionButton homeButton = (FloatingActionButton) findViewById(R.id.btn_crs_list_home);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void buildRecyclerView() {
        mRecyclerView = findViewById(R.id.recycler_view_crs_list);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new CourseListAdapter(this, getAllItems());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new CourseListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                RecyclerView.ViewHolder viewHolder = mRecyclerView.findViewHolderForAdapterPosition(position);
                //Put code here for what happens when an item is clicked
                long tag = (long) viewHolder.itemView.getTag();

                showCourseDetails(tag);

            }
        });



    }

    private void showCourseDetails(long tag) {
        Intent i = new Intent(this, CourseDetailActivity.class);
        i.putExtra("courseTag", tag);
        startActivity(i);
    }

    private Cursor getAllItems() {
        return dB.query(DBTables.courseTable.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                DBTables.termTable.COLUMN_TERM_ID + " ASC"

        );
    }
}
