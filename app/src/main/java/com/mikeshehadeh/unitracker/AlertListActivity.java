/*
package com.mikeshehadeh.unitracker;

import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AlertListActivity extends Fragment {
    private AlertListAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private SQLiteDatabase dB;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton buttonAddAlert;
    private long assessmentId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_alert_list, container, false);
        return view;


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_alert_list);

        DBHelper dbHelper = new DBHelper(this);
        dB = dbHelper.getWritableDatabase();

        Bundle b = getIntent().getExtras();
        assessmentId = b.getLong("assmntID");

        buildRecyclerView();

        //buttonAddAlert = findViewById(R.id.assmnt_dtl_btn_add_alert);
*/
/*

        buttonAddAlert.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                addNewAlert();
            }
        });
*//*


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

    }

    private void addNewAlert() {

    }

    private void buildRecyclerView() {
        mRecyclerView = findViewById(R.id.assmnt_dtl_recycler_view_alerts);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new AlertListAdapter(this, getAllItems());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);


    }

    private Cursor getAllItems() {
        String whereClause = DBTables.alertTable.COLUMN_ASSESSMENT_ID + "= ?";
        String[] whereArgs = new String[]{Long.toString(assessmentId)};
        return dB.query(DBTables.alertTable.TABLE_NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                DBTables.alertTable.COLUMN_ALERT_ID + " ASC"

        );
    }

}






















*/
