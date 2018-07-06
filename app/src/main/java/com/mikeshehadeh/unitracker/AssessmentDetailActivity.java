package com.mikeshehadeh.unitracker;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AssessmentDetailActivity extends AppCompatActivity {

    private long assmntId;
    private SQLiteDatabase dB;
    private String courseDesignator;
    private String assmntName;
    private String assmntStatus;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private AlertListAdapter mAdapter;
    private FloatingActionButton buttonAddAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_detail);

        Bundle b = getIntent().getExtras();
        assmntId = b.getLong("assmntTag");

        DBHelper dbHelper = new DBHelper(this);
        dB = dbHelper.getWritableDatabase();

        configureBackButton();
        configureEditAsmntButton();
        configureAddAlertButton();
        getAllItems();
        setTextViews();
        buildRecyclerView();

    }

    private void configureAddAlertButton() {
        buttonAddAlert = findViewById(R.id.assmnt_dtl_btn_add_alert);
        buttonAddAlert.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                getDateInput();
            }
        });
    }




    private void getDateInput(){
        final Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar cal = Calendar.getInstance();
                cal.set(year,month,dayOfMonth);
                String alertDateString = parseCalToString(cal);

                ContentValues cv = new ContentValues();
                cv.put(DBTables.alertTable.COLUMN_ALERT_DATETIME, alertDateString);
                cv.put(DBTables.alertTable.COLUMN_ASSESSMENT_ID, assmntId);
                cv.put(DBTables.alertTable.COLUMN_ALERT_TYPE, "Assessment Goal Date");


                dB.insert(DBTables.alertTable.TABLE_NAME, null, cv);
                mAdapter.swapCursor(getAllAlerts());

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
    }

    private String parseCalToString(Calendar lastTermEndCal) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        return dateFormat.format(lastTermEndCal.getTime());

    }

    private Calendar parseStringToCal(String lastTermEndDate) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            cal.setTime(dateFormat.parse(lastTermEndDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return cal;
    }



    private void buildRecyclerView() {
        mRecyclerView = findViewById(R.id.assmnt_dtl_recycler_view_alerts);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new AlertListAdapter(AssessmentDetailActivity.this, getAllAlerts());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        //Slide to delete alerts
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

    private void removeItem(long tag) {
        dB.delete(DBTables.alertTable.TABLE_NAME,
                DBTables.termTable.COLUMN_TERM_ID + "=" + tag, null);
        mAdapter.swapCursor(getAllAlerts());

    }

    private Cursor getAllAlerts() {
        String whereClause = DBTables.alertTable.COLUMN_ASSESSMENT_ID + "= ?";
        String[] whereArgs = new String[]{Long.toString(assmntId)};
        return dB.query(DBTables.alertTable.TABLE_NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
    }


    private void setTextViews() {
        TextView tvCourseDesignator = (TextView) findViewById(R.id.asmnt_dtl_textviewiew_course_designator);
        TextView tvAssmntName = (TextView) findViewById(R.id.asmnt_dtl_textviewiew_name);
        TextView tvStatus = (TextView) findViewById(R.id.assmnt_dtl_textview_status);

        tvCourseDesignator.setText(courseDesignator);
        tvAssmntName.setText(assmntName);
        tvStatus.setText(assmntStatus);



    }

    private void getAllItems() {
        String whereClause = DBTables.assessmentTable.COLUMN_ASSESSMENT_ID + "= ?";
        String[] whereArgs = new String[]{Long.toString(assmntId)};
        Cursor c = dB.query(DBTables.assessmentTable.TABLE_NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        //String startDateStr = c.getString(c.getColumnIndex(DBTables.termTable.COLUMN_TERM_STARTDATE));
        c.moveToFirst();

        assmntName = c.getString(c.getColumnIndex(DBTables.assessmentTable.COLUMN_ASSESSMENT_TITLE));
        String courseID = c.getString(c.getColumnIndex(DBTables.assessmentTable.COLUMN_COURSE_ID));
        assmntStatus = c.getString(c.getColumnIndex(DBTables.assessmentTable.COLUMN_ASSESSMENT_TYPE));

        //Get CourseDesignator from ID
        String whereClause2 = DBTables.courseTable.COLUMN_COURSE_ID + "= ?";
        String[] whereArgs2 = new String[]{courseID};
        Cursor c2 = dB.query(DBTables.courseTable.TABLE_NAME,
                null,
                whereClause2,
                whereArgs2,
                null,
                null,
                null
        );
        c2.moveToFirst();
        courseDesignator = c2.getString(c2.getColumnIndex(DBTables.courseTable.COLUMN_COURSE_DESIGNATOR));
        courseDesignator = courseDesignator + " - ";

    }

    private void configureEditAsmntButton() {

    }

    private void configureBackButton() {
        FloatingActionButton backButton = (FloatingActionButton) findViewById(R.id.assmnt_dtl_btn_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}















