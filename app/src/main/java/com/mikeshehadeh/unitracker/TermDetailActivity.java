package com.mikeshehadeh.unitracker;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TermDetailActivity extends AppCompatActivity {
    private SQLiteDatabase dB;
    private Cursor mCursor;
    private int term;
    private TextView tvStartDate;
    private TextView tvEndDate;
    private TextView tvTerm;
    private TextView tvTermStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_detail);
        Bundle b = getIntent().getExtras();
        term = b.getInt("term");


        DBHelper dbHelper = new DBHelper(this);

        dB = dbHelper.getWritableDatabase();

        setTextViews();


        mCursor = getAllItems();


        configureBackButton();
    }

    private void setTextViews() {
        tvStartDate = (TextView)findViewById(R.id.textView_startDate);
        tvEndDate = (TextView)findViewById(R.id.textView_endDate);
        tvTerm = (TextView)findViewById(R.id.textView_term);
        tvTermStatus = (TextView)findViewById(R.id.textView_termStatus);

        String sql = "SELECT " + DBTables.termTable.COLUMN_TERM_STARTDATE + " FROM " +
                DBTables.termTable.TABLE_NAME + " WHERE " +
                DBTables.termTable.COLUMN_TERM_ID + "=" + term +";";
        SQLiteStatement statement = dB.compileStatement(sql);
        String startDate = statement.simpleQueryForString();

        sql = "SELECT " + DBTables.termTable.COLUMN_TERM_ENDDATE + " FROM " +
                DBTables.termTable.TABLE_NAME + " WHERE " +
                DBTables.termTable.COLUMN_TERM_ID + "=" + term +";";
        statement = dB.compileStatement(sql);
        String endDate = statement.simpleQueryForString();

        tvTermStatus.setText("Status:  " + getTermStatus());
        tvTerm.setText("Term " + term);
        tvStartDate.setText("Start Date:  " + startDate);
        tvEndDate.setText("End Date:  " + endDate);

    }

    private String getTermStatus() {
        String selection = DBTables.termTable.COLUMN_TERM_ID + "=?";
        String[] termStringArray = {String.valueOf(term)};
        Cursor c = dB.query(DBTables.termTable.TABLE_NAME, null,
                selection, termStringArray, null, null,null);
        c.moveToFirst();
        String startDateStr = c.getString(c.getColumnIndex(DBTables.termTable.COLUMN_TERM_STARTDATE));
        String endDateStr = c.getString(c.getColumnIndex(DBTables.termTable.COLUMN_TERM_ENDDATE));
        Calendar startDateCal = parseStringToCal(startDateStr);
        Calendar endDateCal = parseStringToCal(endDateStr);
        Calendar todayWithTime = Calendar.getInstance();
        String todayWithoutTime = parseCalToString(todayWithTime);
        Calendar today = parseStringToCal(todayWithoutTime);

        if(startDateCal.after(today)) {
            return "Future Term";
        }
        if((startDateCal.before(today) || startDateCal.equals(today)) && (endDateCal.after(today) || endDateCal.equals(today))){
            return "Currently Enrolled";
        }
        else return "Completed";


        //get today, get start date,
        // if term start date is after today, term status is "future term"
        // if term start date is before or equal today, and end date is equal or after today, status is "currently enrolled"
        // else term is completed

    }

    private void configureBackButton() {
        FloatingActionButton backButton = (FloatingActionButton) findViewById(R.id.btn_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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


    public void swapCursor(Cursor newCursor) {
        if (mCursor != null) {
            mCursor.close();
        }
        mCursor = newCursor;
        //if (newCursor != null) {
            //notifyDataSetChanged();
        //}
    }


    private Cursor getAllItems() {
        return dB.query(DBTables.termTable.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                DBTables.termTable.COLUMN_TERM_ID + " ASC"

        );
    }
}
