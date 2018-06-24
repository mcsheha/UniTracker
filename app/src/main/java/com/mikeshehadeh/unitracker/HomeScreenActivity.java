package com.mikeshehadeh.unitracker;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeScreenActivity extends AppCompatActivity {

    private SQLiteDatabase mDatabase;
    private TermListAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        DBHelper dbHelper = new DBHelper(this);
        mDatabase = dbHelper.getWritableDatabase();

        configureTermButton();


    }

    private void configureTermButton() {
        Button termButton = (Button) findViewById(R.id.btn_terms);
        termButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreenActivity.this, TermListActivity.class));
            }
        });

    }
}
