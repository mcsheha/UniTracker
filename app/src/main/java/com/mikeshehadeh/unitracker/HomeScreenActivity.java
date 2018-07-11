package com.mikeshehadeh.unitracker;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class HomeScreenActivity extends AppCompatActivity {

    private SQLiteDatabase dB;
    private TermListAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        DBHelper dbHelper = new DBHelper(this);
        dB = dbHelper.getWritableDatabase();

        configureTermsButton();
        configureCoursesButton();
        configureAdvancedButton();
        configureMentorsButton();
        checkForAlerts();

    }

    private void checkForAlerts() {
        //get cursor for all alerts with date equal to today
        Calendar todayCal = Calendar.getInstance();
        String todayString = parseCalToString(todayCal);
        String whereClause = DBTables.alertTable.COLUMN_ALERT_DATETIME + "=?";
        String[] whereArgs = new String[]{todayString};
        Cursor c = dB.query(DBTables.alertTable.TABLE_NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null);

        //cursor not empty
        //Iterate alert table cursor, check if courseID or assmentID is not null
            //For assmnt ID not null, get the course designator from assmnt ID
                //concat with alert name, append to stringbuilder
            //For courseID not null, get course designator from course ID
                //concat with alert name, append to stringbuilder
        if(c.moveToFirst()) {
            ArrayList<String> resultsAL = new ArrayList<>();
            for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                String assmntID = c.getString(c.getColumnIndex(DBTables.alertTable.COLUMN_ASSESSMENT_ID));
                if(assmntID != null) {
                    String designator = getCourseDesignatorFromAssessmentID(assmntID);
                    String alertName = c.getString(c.getColumnIndex(DBTables.alertTable.COLUMN_ALERT_TYPE));
                    resultsAL.add(designator + " - " + alertName);
                }else{
                    String courseID = c.getString(c.getColumnIndex(DBTables.alertTable.COLUMN_COURSE_ID));
                    String designator = getCourseDesignatorFromCourseID(courseID);
                    String alertName = c.getString(c.getColumnIndex(DBTables.alertTable.COLUMN_ALERT_TYPE));
                    resultsAL.add(designator + " - " + alertName);
                }
            }
            //Convert the arraylist to a single string, Show popup with the alerts names
            StringBuilder sb = new StringBuilder();
            for (String s : resultsAL) {
                sb.append(s);
                sb.append("\n");
            }
            String alertText = sb.toString();

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Today's Alerts");
            builder.setMessage(alertText);
            builder.setCancelable(false);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                }
            });
            builder.show();
        }
    }



    private String getCourseDesignatorFromCourseID(String courseID) {
        String whereClause = DBTables.courseTable.COLUMN_COURSE_ID + "=?";
        String[] whereArgs = new String[]{courseID};
        Cursor c = dB.query(DBTables.courseTable.TABLE_NAME, null, whereClause, whereArgs, null, null, null);
        c.moveToFirst();
        return c.getString(c.getColumnIndex(DBTables.courseTable.COLUMN_COURSE_DESIGNATOR));
    }


    private String getCourseDesignatorFromAssessmentID(String assmntID) {
        String whereClause = DBTables.assessmentTable.COLUMN_ASSESSMENT_ID + "=?";
        String[] whereArgs = new String[]{assmntID};
        Cursor c = dB.query(DBTables.assessmentTable.TABLE_NAME, null,
                whereClause, whereArgs, null, null, null);
        c.moveToFirst();
        String courseID = c.getString(c.getColumnIndex(DBTables.assessmentTable.COLUMN_COURSE_ID));

        return getCourseDesignatorFromCourseID(courseID);

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

    private void configureAdvancedButton() {
        Button advancedButton = (Button) findViewById(R.id.home_btn_advanced);
        advancedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreenActivity.this, AdvancedActivity.class));
            }
        });
    }

    private void configureCoursesButton() {
        Button courseButton = (Button) findViewById(R.id.btn_courses);
        courseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreenActivity.this, CourseListActivity.class));
            }
        });
    }

    private void configureTermsButton() {
        Button termButton = (Button) findViewById(R.id.btn_terms);
        termButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreenActivity.this, TermListActivity.class));
            }
        });

    }

    private void configureMentorsButton() {
        Button termButton = (Button) findViewById(R.id.btn_mentors);
        termButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreenActivity.this, MentorListActivity.class));
            }
        });

    }
}
