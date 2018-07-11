package com.mikeshehadeh.unitracker;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CourseAlertListActivity extends AppCompatActivity {
    private CourseAlertListAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private SQLiteDatabase dB;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton buttonAddCourseAlert;
    private long courseID;
    private String courseDesignator;
    private long alertID;
    private String alertDateTime;
    private EditText dialogAlertDate;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_alerts);

        DBHelper dbHelper = new DBHelper(this);
        dB = dbHelper.getWritableDatabase();

        Bundle b = getIntent().getExtras();
        courseID = b.getLong("courseID");
        courseDesignator = b.getString("courseDesignator");

        buildRecyclerView();
        configureBackButton();
        String titleString = courseDesignator + "Alerts";
        setTitle(titleString);

        buttonAddCourseAlert = findViewById(R.id.course_alerts_btn_add_alert);
        buttonAddCourseAlert.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                addNewCourseAlert();
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
        String whereClause = DBTables.alertTable.COLUMN_COURSE_ID + "= ?";
        String[] whereArgs = new String[]{Long.toString(courseID)};
        return dB.query(DBTables.alertTable.TABLE_NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                DBTables.alertTable.COLUMN_ALERT_ID + " DESC"
        );
    }


    private void removeItem(long tag) {
        String whereClause = DBTables.alertTable.COLUMN_ALERT_ID + "=?";
        String [] whereArgs = new String[]{Long.toString(tag)};
        dB.delete(DBTables.alertTable.TABLE_NAME, whereClause, whereArgs);
        mAdapter.swapCursor(getAllItems());
    }

    private void addNewCourseAlert() {
        LayoutInflater factory = LayoutInflater.from(this);
        final View addCourseAlertDialogView = factory.inflate(R.layout.dialog_create_course_alert, null);
        final AlertDialog createCourseAlert = new AlertDialog.Builder(this).create();
        createCourseAlert.setView(addCourseAlertDialogView);
        dialogAlertDate = (EditText)addCourseAlertDialogView.findViewById(R.id.create_course_alert_dialog_edt_txt_date);
        Calendar cal = Calendar.getInstance();
        String todayInString = parseCalToString(cal);
        alertDateTime = todayInString;
        dialogAlertDate.setText(todayInString);


        //save new alert button clicked
        addCourseAlertDialogView.findViewById(R.id.create_course_alert_btn_add_alert).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                RadioGroup radioGroup = (RadioGroup) addCourseAlertDialogView.findViewById(R.id.create_course_alert_radio_group);
                int selectedId = radioGroup.getCheckedRadioButtonId();
                RadioButton selectedButton = (RadioButton) addCourseAlertDialogView.findViewById(selectedId);

                String alertType = "Course " + selectedButton.getText().toString();


                ContentValues cv = new ContentValues();
                cv.put(DBTables.alertTable.COLUMN_ALERT_TYPE, alertType);
                cv.put(DBTables.alertTable.COLUMN_ALERT_DATETIME, alertDateTime);
                cv.put(DBTables.alertTable.COLUMN_COURSE_ID, courseID);


                dB.insert(DBTables.alertTable.TABLE_NAME, null, cv);
                mAdapter.swapCursor(getAllItems());
                createCourseAlert.dismiss();

            }
        });


        //cancel button clicked
        addCourseAlertDialogView.findViewById(R.id.create_course_alert_btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createCourseAlert.dismiss();
            }
        });

        //Date edit text clicked
        addCourseAlertDialogView.findViewById(R.id.create_course_alert_dialog_edt_txt_date).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                getDateInput();
                //EditText et = (EditText) addCourseAlertDialogView.findViewById(R.id.create_course_alert_dialog_edt_txt_date);
                dialogAlertDate.setText(alertDateTime);
            }
        });


        createCourseAlert.show();


    }

    private void getDateInput() {
        final Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar cal = Calendar.getInstance();
                cal.set(year,month,dayOfMonth);
                alertDateTime = parseCalToString(cal);
                dialogAlertDate.setText(alertDateTime);



            }
        };

        DatePickerDialog dialog = new DatePickerDialog(this,
                android.R.style.Theme_DeviceDefault, mDateSetListener, year, month, day);
        dialog.show();
        Context context = getApplicationContext();
        CharSequence text = "Select Alert Date.";
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        //dialogAlertDate.setText(alertDateTime);
    }

    private String parseCalToString(Calendar lastTermEndCal) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        return dateFormat.format(lastTermEndCal.getTime());

    }


    private void configureBackButton() {
        FloatingActionButton backButton = (FloatingActionButton) findViewById(R.id.course_alerts_btn_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void buildRecyclerView() {
        mRecyclerView = findViewById(R.id.course_alerts_recycler_view_alerts);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new CourseAlertListAdapter(this, getAllItems());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new CourseAlertListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                RecyclerView.ViewHolder viewHolder = mRecyclerView.findViewHolderForAdapterPosition(position);
                //Put code here for what happens when an item is clicked
                long tag = (long) viewHolder.itemView.getTag();
                showAlertDetails(tag);
            }
        });

    }

    private void showAlertDetails(long tag) {

    }


}
