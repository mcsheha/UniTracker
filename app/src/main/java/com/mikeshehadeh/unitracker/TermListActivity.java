package com.mikeshehadeh.unitracker;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.sql.PreparedStatement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TermListActivity extends AppCompatActivity {
    private ArrayList<TermItem> mTermList;
    private RecyclerView mRecyclerView;
    private TermListAdapter mAdapter;
    private SQLiteDatabase dB;

    private RecyclerView.LayoutManager mLayoutManager;

    private FloatingActionButton buttonAddTerm;
    private Button buttonRemove;
    private EditText editTextInsert;
    private EditText editTextRemove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_list);
        DBHelper dbHelper = new DBHelper(this);
        dB = dbHelper.getWritableDatabase();

        createTermList();
        buildRecyclerView();
        configureHomeButton();

       buttonAddTerm = findViewById(R.id.button_add_term);
        //buttonRemove = findViewById(R.id.button_remove);
        //editTextInsert = findViewById(R.id.edittext_insert);
       // editTextRemove = findViewById(R.id.edittext_remove);

        buttonAddTerm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                addNewTerm();
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

    private void configureHomeButton() {
        FloatingActionButton homeButton = (FloatingActionButton) findViewById(R.id.btn_home);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }




    private Calendar getNewTermStartDate(int termNumber) {
        Calendar newStartDate = Calendar.getInstance();
        for(TermItem t : mTermList) {
            if (t.getTermNumberInt() == termNumber) {
                newStartDate = t.getEndDate();
                newStartDate.add(Calendar.DATE, 1);
                return newStartDate;
            }

        }
        return newStartDate;

    }


/*

    private String getNextTermDates() {
    }
*/

    private int getNextTermNumber() {
        //Get highest term already in mTermList and then add 1
        int highestTerm = 0;
        for(TermItem t: mTermList) {
            int i = t.getTermNumberInt();
            if(i > highestTerm){
                highestTerm = i;
            }
            highestTerm++;
        }
        return highestTerm;
    }



    public void pullTermsFromDB() {

    }

    public void createTermList(){

        mTermList = new ArrayList<>();

        mTermList.add(new TermItem(1, getCalendarToday()));
        //mTermList.add(new TermItem(2, "12/01/2018 - 05/31/2019"));
        //mTermList.add(new TermItem(3, "06/01/2018 - 11/30/2018"));

    }

    private Calendar getCalendarToday() {
        Calendar calendar;
        calendar = new GregorianCalendar(2018,0,1);
        return calendar;
    }


    public void buildRecyclerView(){
        mRecyclerView = findViewById(R.id.recycler_view_term_list);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new TermListAdapter(this, getAllItems());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new TermListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //Put code here for what happens when an item is clicked
            }
        });

    }

    public void addNewTerm() {
        int newTermNumber = getNextTermNumberFromDB();
        String newTermStr;
        String newTermStartDate;
        String newTermEndDate;
        //the term table is not empty
        if (newTermNumber > 1 ){
            newTermStr = Integer.toString(newTermNumber);
            newTermStartDate = getNextTermStartDate(newTermNumber);
            newTermEndDate = getNextTermEndDate(newTermStartDate);

        }
        //the term table is empty
        else{
            newTermStr = "1";
            newTermStartDate = "01-Mar-2017";
            newTermEndDate = getNextTermEndDate(newTermStartDate);
        }

        ContentValues cv = new ContentValues();
        cv.put(DBTables.termTable.COLUMN_TERM_ID, newTermStr);
        cv.put(DBTables.termTable.COLUMN_TERM_STARTDATE, newTermStartDate);
        cv.put(DBTables.termTable.COLUMN_TERM_ENDDATE, newTermEndDate);

        dB.insert(DBTables.termTable.TABLE_NAME, null, cv);
        mAdapter.swapCursor(getAllItems());

    }

    public void removeItem (long termID) {
        dB.delete(DBTables.termTable.TABLE_NAME,
                DBTables.termTable.COLUMN_TERM_ID + "=" + termID, null);
        mAdapter.swapCursor(getAllItems());
    }
/*

    public void removeItem (int position) {
        mTermList.remove(position);
        mAdapter.notifyItemRemoved(position);


    }
*/

    private String getNextTermEndDate(String newTermStartDate) {
        Calendar endDate = parseStringToCal(newTermStartDate);
        endDate.add(Calendar.MONTH,6);
        endDate.add(Calendar.DATE, -1);
        return parseCalToString(endDate);

    }

    private String getNextTermStartDate(int newTermNumber) {
        int lastTermNumber = newTermNumber - 1;
        //Query DB for previous term end date, add one day, convert it string, return string
        String sql = "SELECT " + DBTables.termTable.COLUMN_TERM_ENDDATE +" FROM " + 
                DBTables.termTable.TABLE_NAME + " WHERE " + DBTables.termTable.COLUMN_TERM_ID +
                "=" + lastTermNumber + ";";
        SQLiteStatement statement = dB.compileStatement(sql);
        String lastTermEndDate = statement.simpleQueryForString();
        //Parse lastTermEndDate to Date
        Calendar lastTermEndCal = parseStringToCal(lastTermEndDate);
        //Add one day
        lastTermEndCal.add(Calendar.DATE, 1);
        //Convert to String and return
        return parseCalToString(lastTermEndCal);

        
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

    private int getNextTermNumberFromDB() {
        String sql = "SELECT MAX(" + DBTables.termTable.COLUMN_TERM_ID + ") FROM " +
                DBTables.termTable.TABLE_NAME + ";";
        //Cursor c = dB.rawQuery(sql, null);
        SQLiteStatement statement = dB.compileStatement(sql);
        String maxValue = statement.simpleQueryForString();
        if(maxValue != null && !maxValue.isEmpty()) {
            return Integer.parseInt(maxValue) + 1;
        }
        else return 1;
    }
/*

    public void addNewTerm() {
        int termNumber = getNextTermNumber();
        Calendar newTermStartDate = getNewTermStartDate(termNumber - 1);
        mTermList.add(new TermItem(termNumber,newTermStartDate));
        mAdapter.notifyItemInserted(mTermList.size());
    }
*/

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
