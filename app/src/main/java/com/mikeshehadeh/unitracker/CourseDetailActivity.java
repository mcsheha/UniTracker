package com.mikeshehadeh.unitracker;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CourseDetailActivity extends AppCompatActivity {

    private long courseID;
    private SQLiteDatabase dB;
    private String courseName;
    private String courseDesignator;
    private String courseStartDate;
    private String courseEndDate;
    private String courseCus;
    private EditText etStartDate;
    private EditText etEndDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        Bundle b = getIntent().getExtras();
        courseID = b.getLong("courseTag");

        DBHelper dbHelper = new DBHelper(this);
        dB = dbHelper.getWritableDatabase();

        configureBackButton();
        configureAssessmentsButton();
        configureNotesButton();
        configureAlertButton();
        configureMentorsButton();
        configureEditButton();
        getAllItems();
        setTextViews();

    }

    private void configureEditButton() {
        FloatingActionButton assmtButton = (FloatingActionButton) findViewById(R.id.crse_dtl_btn_edit_crse);
        assmtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchEditCourseDialog();
            }
        });
    }

    private void launchEditCourseDialog() {
        LayoutInflater factory = LayoutInflater.from(this);
        final View editCourseDialogView = factory.inflate(R.layout.dialog_edit_course, null);
        final AlertDialog editCourse = new AlertDialog.Builder(this).create();
        editCourse.setView(editCourseDialogView);
        etStartDate = (EditText)editCourseDialogView.findViewById(R.id.edit_course_dialog_et_start_date);
        etEndDate = (EditText)editCourseDialogView.findViewById(R.id.edit_course_dialog_et_end_date);
        //setStartAndEndDates();
        if (courseStartDate != null && !courseStartDate.isEmpty() && !courseStartDate.equals("null")){
            etStartDate.setText(courseStartDate);
        } else {
            Calendar cal = Calendar.getInstance();
            String str = parseCalToString(cal);
            etStartDate.setText(str);
        }
        if (courseEndDate != null && !courseEndDate.isEmpty() && !courseEndDate.equals("null")){
            etEndDate.setText(courseEndDate);
        } else {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, 1);
            String str = parseCalToString(cal);
            etEndDate.setText(str);
        }



        //save  button clicked
        editCourseDialogView.findViewById(R.id.edit_course_dialog_btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Update the db with the new dates where the course ID = courseID
                String newStartDate = etStartDate.getText().toString();
                String newEndDate = etEndDate.getText().toString();

                boolean datesValid = checkDatesValid(newStartDate, newEndDate);
                if(datesValid){
                    ContentValues cv = new ContentValues();
                    cv.put(DBTables.courseTable.COLUMN_STARTDATE, newStartDate);
                    cv.put(DBTables.courseTable.COLUMN_ENDDATE_ANTIC, newEndDate);

                    String whereClause = DBTables.courseTable.COLUMN_COURSE_ID + "= ?";
                    String[] whereArgs = new String[]{Long.toString(courseID)};
                    dB.update(DBTables.courseTable.TABLE_NAME, cv, whereClause, whereArgs);
                    //mAdapter.swapCursor(getAllItems());
                    //set text for the new dates
                    editCourse.dismiss();
                    getAllItems();
                    setTextViews();
                }



            }
        });
        //cancel button clicked
        editCourseDialogView.findViewById(R.id.edit_course_dialog_btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editCourse.dismiss();
            }
        });
        //Start Date edit text clicked
        editCourseDialogView.findViewById(R.id.edit_course_dialog_et_start_date).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                getStartDateInput();
                //EditText et = (EditText) addCourseAlertDialogView.findViewById(R.id.create_course_alert_dialog_edt_txt_date);
                etStartDate.setText(courseStartDate);
            }
        });
        //End Date edit text clicked
        editCourseDialogView.findViewById(R.id.edit_course_dialog_et_end_date).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                getEndDateInput();
                //EditText et = (EditText) addCourseAlertDialogView.findViewById(R.id.create_course_alert_dialog_edt_txt_date);
                etEndDate.setText(courseEndDate);
            }
        });
        editCourse.show();
    }

    private boolean checkDatesValid(String newStartDate, String newEndDate) {
        boolean bool = false;
        Calendar newStarDateCal = parseStringToCal(newStartDate);
        Calendar newEndDateCal = parseStringToCal(newEndDate);
        if(newStarDateCal.after(newEndDateCal) || newStarDateCal.equals(newEndDateCal)){
            //toast End Date must be after Start Date
            Context context = getApplicationContext();
            CharSequence text = "End Date must be after Start Date";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else {bool = true;}

        return bool;
    }

    private void getEndDateInput() {
        final Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar cal = Calendar.getInstance();
                cal.set(year,month,dayOfMonth);
                courseEndDate = parseCalToString(cal);
                etEndDate.setText(courseEndDate);
            }
        };

        DatePickerDialog dialog = new DatePickerDialog(this,
                android.R.style.Theme_DeviceDefault, mDateSetListener, year, month, day);
        dialog.show();
        Context context = getApplicationContext();
        CharSequence text = "Select End Date";
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        //dialogAlertDate.setText(alertDateTime);

    }

    private void getStartDateInput() {
        final Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            Calendar cal = Calendar.getInstance();
            cal.set(year,month,dayOfMonth);
            courseStartDate = parseCalToString(cal);
            etStartDate.setText(courseStartDate);
            }
        };

        DatePickerDialog dialog = new DatePickerDialog(this,
                android.R.style.Theme_DeviceDefault, mDateSetListener, year, month, day);
        dialog.show();
        Context context = getApplicationContext();
        CharSequence text = "Select Start Date";
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        //dialogAlertDate.setText(alertDateTime);
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

    private void configureMentorsButton() {
        Button assmtButton = (Button) findViewById(R.id.crse_dtl_mentors_button);
        assmtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CourseDetailActivity.this, CourseMentorListActivity.class);
                i.putExtra("courseID", courseID);
                startActivity(i);

            }
        });
    }

    private void configureAlertButton() {
        Button alertButton = (Button) findViewById(R.id.crse_dtl_alerts_button);
        alertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CourseDetailActivity.this, CourseAlertListActivity.class);
                i.putExtra("courseID", courseID);
                i.putExtra("courseDesignator", courseDesignator);
                startActivity(i);

            }
        });
    }

    private void configureNotesButton() {
        Button assmtButton = (Button) findViewById(R.id.crse_dtl_notes_button);
        assmtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CourseDetailActivity.this, NoteListActivity.class);
                i.putExtra("courseID", courseID);
                startActivity(i);

            }
        });
    }

    private void configureAssessmentsButton() {
        Button assmtButton = (Button) findViewById(R.id.crse_dtl_assmt_button);
        assmtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CourseDetailActivity.this, AssessmentListActivity.class);
                i.putExtra("courseID", courseID);
                startActivity(i);

            }
        });
    }

    private void setTextViews() {
        //TextView tvCourseName = (TextView) findViewById(R.id.crse_dtl_name);
        //TextView tvCourseDesignator = (TextView) findViewById(R.id.crse_dtl_textView_designator);
        TextView tvCourseStartDate = (TextView) findViewById(R.id.crse_dtl_textView_start_date);
        TextView tvCourseEndDate = (TextView) findViewById(R.id.crse_dtl_textView_endDate);
        TextView tvCus = (TextView) findViewById(R.id.crse_dtl_textView_cus);

        if (courseStartDate == null) {
            courseStartDate = "";
        }

        if (courseEndDate == null) {
            courseEndDate = "";
        }

        //tvCourseName.setText(courseName);
        //tvCourseDesignator.setText(courseDesignator);
        tvCourseStartDate.setText("Start Date:  " + courseStartDate);
        tvCourseEndDate.setText("End Date:  " + courseEndDate);
        tvCus.setText(courseCus + " CUs");

        String title = courseDesignator + courseName;
        setTitle(title);


    }

    private void configureBackButton() {
        FloatingActionButton backButton = (FloatingActionButton) findViewById(R.id.crse_dtl_btn_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getAllItems() {
        String whereClause = DBTables.courseTable.COLUMN_COURSE_ID + "= ?";
        String[] whereArgs = new String[]{Long.toString(courseID)};

        Cursor c = dB.query(DBTables.courseTable.TABLE_NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        //String startDateStr = c.getString(c.getColumnIndex(DBTables.termTable.COLUMN_TERM_STARTDATE));
        c.moveToFirst();


        courseName = c.getString(c.getColumnIndex(DBTables.courseTable.COLUMN_COURSE_NAME));
        String str = c.getString(c.getColumnIndex(DBTables.courseTable.COLUMN_COURSE_DESIGNATOR));
        courseDesignator = str + " - ";
        courseStartDate = c.getString(c.getColumnIndex(DBTables.courseTable.COLUMN_STARTDATE));

        courseEndDate = c.getString(c.getColumnIndex(DBTables.courseTable.COLUMN_ENDDATE_ANTIC));
        courseCus = c.getString(c.getColumnIndex(DBTables.courseTable.COLUMN_CU));


    }
}
