package com.mikeshehadeh.unitracker;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AssessmentDetailActivity extends AppCompatActivity {

    private long assmntId;
    private SQLiteDatabase dB;
    private String courseDesignator;
    private String assmntName;
    private String assmntStatus;

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
        getAllItems();
        setTextViews();
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















