package com.mikeshehadeh.unitracker;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class AdvancedActivity extends AppCompatActivity {
    private SQLiteDatabase dB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced);

        DBHelper dbHelper = new DBHelper(this);
        dB = dbHelper.getWritableDatabase();

        configureHomeButton();
        configureClearDataButton();
        configureAddSampleDataButton();
    }

    private void configureAddSampleDataButton() {
        //clear the DB and all sample data
        final Button addSampleDataBtn = (Button) findViewById(R.id.adv_btn_load_sample_data);
        addSampleDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(AdvancedActivity.this);
                builder.setTitle("Replace Current Data With Sample Data?");
                builder.setMessage("You are about to delete all UniTracker data and replace it with Sample Data. Do you really want to proceed?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAllData();
                        addSampleData();
                        showToast("Sample Data Added!");
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "Cancelled.", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });

    }

    private void addSampleData() {

        //termTable
        ContentValues termCV1 = new ContentValues();
        ContentValues termCV2 = new ContentValues();
        ContentValues termCV3 = new ContentValues();
        ContentValues termCV4 = new ContentValues();
        termCV1.put(DBTables.termTable.COLUMN_TERM_ID, "1");
        termCV2.put(DBTables.termTable.COLUMN_TERM_ID, "2");
        termCV3.put(DBTables.termTable.COLUMN_TERM_ID, "3");
        termCV4.put(DBTables.termTable.COLUMN_TERM_ID, "4");
        termCV1.put(DBTables.termTable.COLUMN_TERM_STARTDATE, "01-Dec-2017");
        termCV2.put(DBTables.termTable.COLUMN_TERM_STARTDATE, "01-Jun-2018");
        termCV3.put(DBTables.termTable.COLUMN_TERM_STARTDATE, "01-Dec-2018");
        termCV4.put(DBTables.termTable.COLUMN_TERM_STARTDATE, "01-Jun-2019");
        termCV1.put(DBTables.termTable.COLUMN_TERM_ENDDATE, "31-May-2018");
        termCV2.put(DBTables.termTable.COLUMN_TERM_ENDDATE, "30-Nov-2018");
        termCV3.put(DBTables.termTable.COLUMN_TERM_ENDDATE, "31-May-2019");
        termCV4.put(DBTables.termTable.COLUMN_TERM_ENDDATE, "30-Nov-2019");
        dB.insert(DBTables.termTable.TABLE_NAME, null, termCV1);
        dB.insert(DBTables.termTable.TABLE_NAME, null, termCV2);
        dB.insert(DBTables.termTable.TABLE_NAME, null, termCV3);
        dB.insert(DBTables.termTable.TABLE_NAME, null, termCV4);

        //mentorTable
        ContentValues mentorCV1 = new ContentValues();
        ContentValues mentorCV2 = new ContentValues();
        ContentValues mentorCV3 = new ContentValues();
        ContentValues mentorCV4 = new ContentValues();
        mentorCV1.put(DBTables.mentorTable.COLUMN_MENTOR_ID, "1");
        mentorCV2.put(DBTables.mentorTable.COLUMN_MENTOR_ID, "2");
        mentorCV3.put(DBTables.mentorTable.COLUMN_MENTOR_ID, "3");
        mentorCV4.put(DBTables.mentorTable.COLUMN_MENTOR_ID, "4");
        mentorCV1.put(DBTables.mentorTable.COLUMN_MENTOR_NAME,"Joyce Dahlhauser");
        mentorCV2.put(DBTables.mentorTable.COLUMN_MENTOR_NAME,"Micah Pappas");
        mentorCV3.put(DBTables.mentorTable.COLUMN_MENTOR_NAME,"Amy Antonucci");
        mentorCV4.put(DBTables.mentorTable.COLUMN_MENTOR_NAME,"Alvaro Escobar");
        mentorCV1.put(DBTables.mentorTable.COLUMN_MENTOR_EMAIL,"joyce@wgu.edu");
        mentorCV2.put(DBTables.mentorTable.COLUMN_MENTOR_EMAIL,"micah@wgu.edu");
        mentorCV3.put(DBTables.mentorTable.COLUMN_MENTOR_EMAIL,"amy@wgu.edu");
        mentorCV4.put(DBTables.mentorTable.COLUMN_MENTOR_EMAIL,"alvaro@wgu.edu");
        mentorCV1.put(DBTables.mentorTable.COLUMN_MENTOR_PHONE,"555-555-1111");
        mentorCV2.put(DBTables.mentorTable.COLUMN_MENTOR_PHONE,"555-555-2222");
        mentorCV3.put(DBTables.mentorTable.COLUMN_MENTOR_PHONE,"555-555-3333");
        mentorCV4.put(DBTables.mentorTable.COLUMN_MENTOR_PHONE,"555-555-4444");
        dB.insert(DBTables.mentorTable.TABLE_NAME, null, mentorCV1);
        dB.insert(DBTables.mentorTable.TABLE_NAME, null, mentorCV2);
        dB.insert(DBTables.mentorTable.TABLE_NAME, null, mentorCV3);
        dB.insert(DBTables.mentorTable.TABLE_NAME, null, mentorCV4);

        //courseTable
        ContentValues courseCV1 = new ContentValues();
        ContentValues courseCV2 = new ContentValues();
        ContentValues courseCV3 = new ContentValues();
        ContentValues courseCV4 = new ContentValues();
        ContentValues courseCV5 = new ContentValues();
        ContentValues courseCV6 = new ContentValues();
        ContentValues courseCV7 = new ContentValues();
        ContentValues courseCV8 = new ContentValues();
        ContentValues courseCV9 = new ContentValues();
        courseCV1.put(DBTables.courseTable.COLUMN_COURSE_ID, "1");
        courseCV2.put(DBTables.courseTable.COLUMN_COURSE_ID, "2");
        courseCV3.put(DBTables.courseTable.COLUMN_COURSE_ID, "3");
        courseCV4.put(DBTables.courseTable.COLUMN_COURSE_ID, "4");
        courseCV5.put(DBTables.courseTable.COLUMN_COURSE_ID, "5");
        courseCV6.put(DBTables.courseTable.COLUMN_COURSE_ID, "6");
        courseCV7.put(DBTables.courseTable.COLUMN_COURSE_ID, "7");
        courseCV8.put(DBTables.courseTable.COLUMN_COURSE_ID, "8");
        courseCV9.put(DBTables.courseTable.COLUMN_COURSE_ID, "9");
        courseCV1.put(DBTables.courseTable.COLUMN_COURSE_DESIGNATOR, "C170");
        courseCV2.put(DBTables.courseTable.COLUMN_COURSE_DESIGNATOR, "C483");
        courseCV3.put(DBTables.courseTable.COLUMN_COURSE_DESIGNATOR, "C169");
        courseCV4.put(DBTables.courseTable.COLUMN_COURSE_DESIGNATOR, "C189");
        courseCV5.put(DBTables.courseTable.COLUMN_COURSE_DESIGNATOR, "C192");
        courseCV6.put(DBTables.courseTable.COLUMN_COURSE_DESIGNATOR, "C191");
        courseCV7.put(DBTables.courseTable.COLUMN_COURSE_DESIGNATOR, "C482");
        courseCV8.put(DBTables.courseTable.COLUMN_COURSE_DESIGNATOR, "C176");
        courseCV9.put(DBTables.courseTable.COLUMN_COURSE_DESIGNATOR, "C188");
        courseCV1.put(DBTables.courseTable.COLUMN_COURSE_NAME, "Data Applications");
        courseCV2.put(DBTables.courseTable.COLUMN_COURSE_NAME, "Principles of Management");
        courseCV3.put(DBTables.courseTable.COLUMN_COURSE_NAME, "Scripting and Programming");
        courseCV4.put(DBTables.courseTable.COLUMN_COURSE_NAME, "Data Structures");
        courseCV5.put(DBTables.courseTable.COLUMN_COURSE_NAME, "Data Management III");
        courseCV6.put(DBTables.courseTable.COLUMN_COURSE_NAME, "Operating Systems");
        courseCV7.put(DBTables.courseTable.COLUMN_COURSE_NAME, "Software I");
        courseCV8.put(DBTables.courseTable.COLUMN_COURSE_NAME, "Business of IT");
        courseCV9.put(DBTables.courseTable.COLUMN_COURSE_NAME, "Software Engineering");
        courseCV1.put(DBTables.courseTable.COLUMN_CU, "4");
        courseCV2.put(DBTables.courseTable.COLUMN_CU, "4");
        courseCV3.put(DBTables.courseTable.COLUMN_CU, "4");
        courseCV4.put(DBTables.courseTable.COLUMN_CU, "3");
        courseCV5.put(DBTables.courseTable.COLUMN_CU, "3");
        courseCV6.put(DBTables.courseTable.COLUMN_CU, "6");
        courseCV7.put(DBTables.courseTable.COLUMN_CU, "4");
        courseCV8.put(DBTables.courseTable.COLUMN_CU, "4");
        courseCV9.put(DBTables.courseTable.COLUMN_CU, "4");
        courseCV1.put(DBTables.courseTable.COLUMN_TERM_ID, "1");
        courseCV2.put(DBTables.courseTable.COLUMN_TERM_ID, "1");
        courseCV3.put(DBTables.courseTable.COLUMN_TERM_ID, "1");
        courseCV4.put(DBTables.courseTable.COLUMN_TERM_ID, "2");
        courseCV5.put(DBTables.courseTable.COLUMN_TERM_ID, "2");
        courseCV6.put(DBTables.courseTable.COLUMN_TERM_ID, "2");
        courseCV7.put(DBTables.courseTable.COLUMN_TERM_ID, "3");
        courseCV8.put(DBTables.courseTable.COLUMN_TERM_ID, "3");
        dB.insert(DBTables.courseTable.TABLE_NAME, null, courseCV1);
        dB.insert(DBTables.courseTable.TABLE_NAME, null, courseCV2);
        dB.insert(DBTables.courseTable.TABLE_NAME, null, courseCV3);
        dB.insert(DBTables.courseTable.TABLE_NAME, null, courseCV4);
        dB.insert(DBTables.courseTable.TABLE_NAME, null, courseCV5);
        dB.insert(DBTables.courseTable.TABLE_NAME, null, courseCV6);
        dB.insert(DBTables.courseTable.TABLE_NAME, null, courseCV7);
        dB.insert(DBTables.courseTable.TABLE_NAME, null, courseCV8);
        dB.insert(DBTables.courseTable.TABLE_NAME, null, courseCV9);

        //courseMentorTable
        ContentValues crsMntrCV1 = new ContentValues();
        ContentValues crsMntrCV2 = new ContentValues();
        ContentValues crsMntrCV3 = new ContentValues();
        ContentValues crsMntrCV4 = new ContentValues();
        ContentValues crsMntrCV5 = new ContentValues();
        ContentValues crsMntrCV6 = new ContentValues();
        ContentValues crsMntrCV7 = new ContentValues();
        ContentValues crsMntrCV8 = new ContentValues();
        ContentValues crsMntrCV9 = new ContentValues();
        crsMntrCV1.put(DBTables.courseMentorTable.COLUMN_MENTOR_ID, "4");
        crsMntrCV2.put(DBTables.courseMentorTable.COLUMN_MENTOR_ID, "3");
        crsMntrCV3.put(DBTables.courseMentorTable.COLUMN_MENTOR_ID, "2");
        crsMntrCV4.put(DBTables.courseMentorTable.COLUMN_MENTOR_ID, "1");
        crsMntrCV5.put(DBTables.courseMentorTable.COLUMN_MENTOR_ID, "4");
        crsMntrCV6.put(DBTables.courseMentorTable.COLUMN_MENTOR_ID, "3");
        crsMntrCV7.put(DBTables.courseMentorTable.COLUMN_MENTOR_ID, "2");
        crsMntrCV8.put(DBTables.courseMentorTable.COLUMN_MENTOR_ID, "1");
        crsMntrCV9.put(DBTables.courseMentorTable.COLUMN_MENTOR_ID, "4");
        crsMntrCV1.put(DBTables.courseMentorTable.COLUMN_COURSE_ID, "1");
        crsMntrCV2.put(DBTables.courseMentorTable.COLUMN_COURSE_ID, "2");
        crsMntrCV3.put(DBTables.courseMentorTable.COLUMN_COURSE_ID, "3");
        crsMntrCV4.put(DBTables.courseMentorTable.COLUMN_COURSE_ID, "4");
        crsMntrCV5.put(DBTables.courseMentorTable.COLUMN_COURSE_ID, "5");
        crsMntrCV6.put(DBTables.courseMentorTable.COLUMN_COURSE_ID, "6");
        crsMntrCV7.put(DBTables.courseMentorTable.COLUMN_COURSE_ID, "7");
        crsMntrCV8.put(DBTables.courseMentorTable.COLUMN_COURSE_ID, "8");
        crsMntrCV9.put(DBTables.courseMentorTable.COLUMN_COURSE_ID, "9");
        dB.insert(DBTables.courseMentorTable.TABLE_NAME, null, crsMntrCV1);
        dB.insert(DBTables.courseMentorTable.TABLE_NAME, null, crsMntrCV2);
        dB.insert(DBTables.courseMentorTable.TABLE_NAME, null, crsMntrCV3);
        dB.insert(DBTables.courseMentorTable.TABLE_NAME, null, crsMntrCV4);
        dB.insert(DBTables.courseMentorTable.TABLE_NAME, null, crsMntrCV5);
        dB.insert(DBTables.courseMentorTable.TABLE_NAME, null, crsMntrCV6);
        dB.insert(DBTables.courseMentorTable.TABLE_NAME, null, crsMntrCV7);
        dB.insert(DBTables.courseMentorTable.TABLE_NAME, null, crsMntrCV8);
        dB.insert(DBTables.courseMentorTable.TABLE_NAME, null, crsMntrCV9);

        //assessmentTable
        ContentValues assmntCV1 = new ContentValues();
        ContentValues assmntCV2 = new ContentValues();
        ContentValues assmntCV3 = new ContentValues();
        ContentValues assmntCV4 = new ContentValues();
        ContentValues assmntCV5 = new ContentValues();
        ContentValues assmntCV6 = new ContentValues();
        ContentValues assmntCV7 = new ContentValues();
        ContentValues assmntCV8 = new ContentValues();
        ContentValues assmntCV9 = new ContentValues();
        ContentValues assmntCV10 = new ContentValues();
        ContentValues assmntCV11 = new ContentValues();
        ContentValues assmntCV12 = new ContentValues();
        assmntCV1.put(DBTables.assessmentTable.COLUMN_ASSESSMENT_ID, "1");
        assmntCV2.put(DBTables.assessmentTable.COLUMN_ASSESSMENT_ID, "2");
        assmntCV3.put(DBTables.assessmentTable.COLUMN_ASSESSMENT_ID, "3");
        assmntCV4.put(DBTables.assessmentTable.COLUMN_ASSESSMENT_ID, "4");
        assmntCV5.put(DBTables.assessmentTable.COLUMN_ASSESSMENT_ID, "5");
        assmntCV6.put(DBTables.assessmentTable.COLUMN_ASSESSMENT_ID, "6");
        assmntCV7.put(DBTables.assessmentTable.COLUMN_ASSESSMENT_ID, "7");
        assmntCV8.put(DBTables.assessmentTable.COLUMN_ASSESSMENT_ID, "8");
        assmntCV9.put(DBTables.assessmentTable.COLUMN_ASSESSMENT_ID, "9");
        assmntCV10.put(DBTables.assessmentTable.COLUMN_ASSESSMENT_ID, "10");
        assmntCV11.put(DBTables.assessmentTable.COLUMN_ASSESSMENT_ID, "11");
        assmntCV12.put(DBTables.assessmentTable.COLUMN_ASSESSMENT_ID, "12");
        assmntCV1.put(DBTables.assessmentTable.COLUMN_COURSE_ID, "1");
        assmntCV2.put(DBTables.assessmentTable.COLUMN_COURSE_ID, "2");
        assmntCV3.put(DBTables.assessmentTable.COLUMN_COURSE_ID, "3");
        assmntCV4.put(DBTables.assessmentTable.COLUMN_COURSE_ID, "4");
        assmntCV5.put(DBTables.assessmentTable.COLUMN_COURSE_ID, "5");
        assmntCV6.put(DBTables.assessmentTable.COLUMN_COURSE_ID, "6");
        assmntCV7.put(DBTables.assessmentTable.COLUMN_COURSE_ID, "7");
        assmntCV8.put(DBTables.assessmentTable.COLUMN_COURSE_ID, "8");
        assmntCV9.put(DBTables.assessmentTable.COLUMN_COURSE_ID, "9");
        assmntCV10.put(DBTables.assessmentTable.COLUMN_COURSE_ID, "1");
        assmntCV11.put(DBTables.assessmentTable.COLUMN_COURSE_ID, "2");
        assmntCV12.put(DBTables.assessmentTable.COLUMN_COURSE_ID, "3");
        assmntCV1.put(DBTables.assessmentTable.COLUMN_ASSESSMENT_TITLE, "Assessment 1");
        assmntCV2.put(DBTables.assessmentTable.COLUMN_ASSESSMENT_TITLE, "Assessment 1");
        assmntCV3.put(DBTables.assessmentTable.COLUMN_ASSESSMENT_TITLE, "Assessment 1");
        assmntCV4.put(DBTables.assessmentTable.COLUMN_ASSESSMENT_TITLE, "Assessment 1");
        assmntCV5.put(DBTables.assessmentTable.COLUMN_ASSESSMENT_TITLE, "Assessment 1");
        assmntCV6.put(DBTables.assessmentTable.COLUMN_ASSESSMENT_TITLE, "Assessment 1");
        assmntCV7.put(DBTables.assessmentTable.COLUMN_ASSESSMENT_TITLE, "Assessment 1");
        assmntCV8.put(DBTables.assessmentTable.COLUMN_ASSESSMENT_TITLE, "Assessment 1");
        assmntCV9.put(DBTables.assessmentTable.COLUMN_ASSESSMENT_TITLE, "Assessment 1");
        assmntCV10.put(DBTables.assessmentTable.COLUMN_ASSESSMENT_TITLE, "Assessment 2");
        assmntCV11.put(DBTables.assessmentTable.COLUMN_ASSESSMENT_TITLE, "Assessment 2");
        assmntCV12.put(DBTables.assessmentTable.COLUMN_ASSESSMENT_TITLE, "Assessment 2");
        assmntCV1.put(DBTables.assessmentTable.COLUMN_ASSESSMENT_TYPE, "Objective");
        assmntCV2.put(DBTables.assessmentTable.COLUMN_ASSESSMENT_TYPE, "Performance");
        assmntCV3.put(DBTables.assessmentTable.COLUMN_ASSESSMENT_TYPE, "Objective");
        assmntCV4.put(DBTables.assessmentTable.COLUMN_ASSESSMENT_TYPE, "Performance");
        assmntCV5.put(DBTables.assessmentTable.COLUMN_ASSESSMENT_TYPE, "Objective");
        assmntCV6.put(DBTables.assessmentTable.COLUMN_ASSESSMENT_TYPE, "Performance");
        assmntCV7.put(DBTables.assessmentTable.COLUMN_ASSESSMENT_TYPE, "Objective");
        assmntCV8.put(DBTables.assessmentTable.COLUMN_ASSESSMENT_TYPE, "Performance");
        assmntCV9.put(DBTables.assessmentTable.COLUMN_ASSESSMENT_TYPE, "Objective");
        assmntCV10.put(DBTables.assessmentTable.COLUMN_ASSESSMENT_TYPE, "Performance");
        assmntCV11.put(DBTables.assessmentTable.COLUMN_ASSESSMENT_TYPE, "Objective");
        assmntCV12.put(DBTables.assessmentTable.COLUMN_ASSESSMENT_TYPE, "Performance");
        dB.insert(DBTables.assessmentTable.TABLE_NAME, null, assmntCV1);
        dB.insert(DBTables.assessmentTable.TABLE_NAME, null, assmntCV2);
        dB.insert(DBTables.assessmentTable.TABLE_NAME, null, assmntCV3);
        dB.insert(DBTables.assessmentTable.TABLE_NAME, null, assmntCV4);
        dB.insert(DBTables.assessmentTable.TABLE_NAME, null, assmntCV5);
        dB.insert(DBTables.assessmentTable.TABLE_NAME, null, assmntCV6);
        dB.insert(DBTables.assessmentTable.TABLE_NAME, null, assmntCV7);
        dB.insert(DBTables.assessmentTable.TABLE_NAME, null, assmntCV8);
        dB.insert(DBTables.assessmentTable.TABLE_NAME, null, assmntCV9);
        dB.insert(DBTables.assessmentTable.TABLE_NAME, null, assmntCV10);
        dB.insert(DBTables.assessmentTable.TABLE_NAME, null, assmntCV11);
        dB.insert(DBTables.assessmentTable.TABLE_NAME, null, assmntCV12);

        //alertTable
        ContentValues alertCV1 = new ContentValues();
        ContentValues alertCV2 = new ContentValues();
        ContentValues alertCV3 = new ContentValues();
        ContentValues alertCV4 = new ContentValues();
        ContentValues alertCV5 = new ContentValues();
        alertCV1.put(DBTables.alertTable.COLUMN_ALERT_ID, "1");
        alertCV2.put(DBTables.alertTable.COLUMN_ALERT_ID, "2");
        alertCV3.put(DBTables.alertTable.COLUMN_ALERT_ID, "3");
        alertCV4.put(DBTables.alertTable.COLUMN_ALERT_ID, "4");
        alertCV5.put(DBTables.alertTable.COLUMN_ALERT_ID, "5");
        alertCV1.put(DBTables.alertTable.COLUMN_ALERT_TYPE, "Assessment Goal Date");
        alertCV2.put(DBTables.alertTable.COLUMN_ALERT_TYPE, "Course Start Date");
        alertCV3.put(DBTables.alertTable.COLUMN_ALERT_TYPE, "Course End Date");
        alertCV4.put(DBTables.alertTable.COLUMN_ALERT_TYPE, "Assessment Goal Date");
        alertCV5.put(DBTables.alertTable.COLUMN_ALERT_TYPE, "Course Start Date");
        alertCV1.put(DBTables.alertTable.COLUMN_ASSESSMENT_ID, "1");
        alertCV2.put(DBTables.alertTable.COLUMN_COURSE_ID, "2");
        alertCV3.put(DBTables.alertTable.COLUMN_COURSE_ID, "3");
        alertCV4.put(DBTables.alertTable.COLUMN_ASSESSMENT_ID, "4");
        alertCV5.put(DBTables.alertTable.COLUMN_COURSE_ID, "5");
        alertCV1.put(DBTables.alertTable.COLUMN_ALERT_DATETIME, "06-Jul-2018");
        alertCV2.put(DBTables.alertTable.COLUMN_ALERT_DATETIME, "07-Jul-2018");
        alertCV3.put(DBTables.alertTable.COLUMN_ALERT_DATETIME, "08-Jul-2018");
        alertCV4.put(DBTables.alertTable.COLUMN_ALERT_DATETIME, "09-Jul-2018");
        alertCV5.put(DBTables.alertTable.COLUMN_ALERT_DATETIME, "10-Jul-2018");
        dB.insert(DBTables.alertTable.TABLE_NAME, null, alertCV1);
        dB.insert(DBTables.alertTable.TABLE_NAME, null, alertCV2);
        dB.insert(DBTables.alertTable.TABLE_NAME, null, alertCV3);
        dB.insert(DBTables.alertTable.TABLE_NAME, null, alertCV4);
        dB.insert(DBTables.alertTable.TABLE_NAME, null, alertCV5);

        //noteTable
        ContentValues noteCV1 = new ContentValues();
        ContentValues noteCV2 = new ContentValues();
        ContentValues noteCV3 = new ContentValues();
        ContentValues noteCV4 = new ContentValues();
        ContentValues noteCV5 = new ContentValues();
        noteCV1.put(DBTables.noteTable.COLUMN_NOTE_ID, "1");
        noteCV2.put(DBTables.noteTable.COLUMN_NOTE_ID, "2");
        noteCV3.put(DBTables.noteTable.COLUMN_NOTE_ID, "3");
        noteCV4.put(DBTables.noteTable.COLUMN_NOTE_ID, "4");
        noteCV5.put(DBTables.noteTable.COLUMN_NOTE_ID, "5");
        noteCV1.put(DBTables.noteTable.COLUMN_COURSE_ID, "1");
        noteCV2.put(DBTables.noteTable.COLUMN_COURSE_ID, "2");
        noteCV3.put(DBTables.noteTable.COLUMN_COURSE_ID, "3");
        noteCV4.put(DBTables.noteTable.COLUMN_COURSE_ID, "4");
        noteCV5.put(DBTables.noteTable.COLUMN_COURSE_ID, "5");
        noteCV1.put(DBTables.noteTable.COLUMN_NOTE_TEXT, "This course is easy.");
        noteCV2.put(DBTables.noteTable.COLUMN_NOTE_TEXT, "This course is taking forever.");
        noteCV3.put(DBTables.noteTable.COLUMN_NOTE_TEXT, "This course will be quick.");
        noteCV4.put(DBTables.noteTable.COLUMN_NOTE_TEXT, "This course is fun.");
        noteCV5.put(DBTables.noteTable.COLUMN_NOTE_TEXT, "This course is difficult.");
        dB.insert(DBTables.noteTable.TABLE_NAME, null, noteCV1);
        dB.insert(DBTables.noteTable.TABLE_NAME, null, noteCV2);
        dB.insert(DBTables.noteTable.TABLE_NAME, null, noteCV3);
        dB.insert(DBTables.noteTable.TABLE_NAME, null, noteCV4);
        dB.insert(DBTables.noteTable.TABLE_NAME, null, noteCV5);
    }


    private void configureClearDataButton() {
        //configureOnClickListener first
        Button clearDataBtn = (Button) findViewById(R.id.adv_btn_clear_all_data);
        clearDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(AdvancedActivity.this);
                builder.setTitle("Delete All Data?");
                builder.setMessage("You are about to delete all UniTracker data. Do you really want to proceed?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAllData();
                        showToast("All Data Deleted!");
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "Cancelled.", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });
    }


    private void showToast(String s) {
        //Toast
        Context context = getApplicationContext();
        CharSequence text = s;
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    private void deleteAllData() {
        dB.delete(DBTables.termTable.TABLE_NAME,null,null);
        dB.delete(DBTables.mentorTable.TABLE_NAME,null,null);
        dB.delete(DBTables.courseTable.TABLE_NAME,null,null);
        dB.delete(DBTables.courseMentorTable.TABLE_NAME,null,null);
        dB.delete(DBTables.assessmentTable.TABLE_NAME,null,null);
        dB.delete(DBTables.alertTable.TABLE_NAME,null,null);
        dB.delete(DBTables.noteTable.TABLE_NAME,null,null);

    }


    private void configureHomeButton() {
        FloatingActionButton homeButton = (FloatingActionButton) findViewById(R.id.adv_btn_home);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
