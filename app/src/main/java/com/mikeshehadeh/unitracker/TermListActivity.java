package com.mikeshehadeh.unitracker;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

/*        buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = Integer.parseInt(editTextRemove.getText().toString());
                removeItem(position);
            }
        });*/
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

    public void addNewTerm() {
        int termNumber = getNextTermNumber();
        Calendar newTermStartDate = getNewTermStartDate(termNumber - 1);
        mTermList.add(new TermItem(termNumber,newTermStartDate));
        mAdapter.notifyItemInserted(mTermList.size());
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

    public void removeItem (int position) {
        mTermList.remove(position);
        mAdapter.notifyItemRemoved(position);

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
        mAdapter = new TermListAdapter(mTermList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new TermListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //Put code here for what happens when an item is clicked
            }
        });

    }

    private Cursor getAllItems() {
        return dB.query(DBTables.termTable.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                DBTables.termTable.COLUMN_TERM_ID + " DESC"

        );
    }
}
