package com.mikeshehadeh.unitracker;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.Toast;

public class MentorListActivity extends AppCompatActivity {
    private MentorListAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private SQLiteDatabase dB;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton buttonAddMentor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor_list);

        DBHelper dbHelper = new DBHelper(this);
        dB = dbHelper.getWritableDatabase();

        buildRecyclerView();
        configureHomeButton();

        buttonAddMentor = findViewById(R.id.button_mentor_list_add);
        buttonAddMentor.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                addNewMentor();
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

    @Override
    public void onResume(){
        super.onResume();
        mAdapter.swapCursor(getAllItems());

    }



    private void removeItem(long tag) {
        String whereClause = DBTables.mentorTable.COLUMN_MENTOR_ID + "=?";
        String[] whereArgs = new String[]{Long.toString(tag)};
        dB.delete(DBTables.mentorTable.TABLE_NAME, whereClause, whereArgs);
        mAdapter.swapCursor(getAllItems());
    }

    private void buildRecyclerView() {
        mRecyclerView = findViewById(R.id.recycler_view_mentor_list);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new MentorListAdapter(this, getAllItems());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new MentorListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                RecyclerView.ViewHolder viewHolder = mRecyclerView.findViewHolderForAdapterPosition(position);
                //Put code here for what happens when an item is clicked
                long tag = (long) viewHolder.itemView.getTag();
                showMentorDetails(tag);
            }
        });
    }

    private void showMentorDetails(long tag) {
        Intent i = new Intent(this, MentorDetailActivity.class);
        i.putExtra("mentorTag", tag);
        startActivity(i);
    }

    private void configureHomeButton() {
        FloatingActionButton homeButton = (FloatingActionButton) findViewById(R.id.btn_mentor_list_home);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addNewMentor() {
        //Show dialog/form for adding a new mentor
        //save user input, check if valid (Toast if not), add to db, swapCursor(getAllItems)
        LayoutInflater factory = LayoutInflater.from(this);
        final View addMentorDialogView = factory.inflate(R.layout.dialog_create_mentor, null);
        final AlertDialog createMentor = new AlertDialog.Builder(this).create();
        createMentor.setView(addMentorDialogView);



        //save new term button clicked
        addMentorDialogView.findViewById(R.id.create_mentor_btn_add_mentor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check if input is valid, add the course to the db
                EditText nameEditText = (EditText)addMentorDialogView.findViewById(R.id.create_mentor_dialog_edt_txt_name);
                EditText emailEditText = (EditText)addMentorDialogView.findViewById(R.id.create_mentor_dialog_edt_txt_email);
                EditText phoneEditText = (EditText)addMentorDialogView.findViewById(R.id.create_mentor_dialog_edt_txt_date);

                String name = nameEditText.getText().toString();
                String email = emailEditText.getText().toString().toLowerCase();
                String phone = phoneEditText.getText().toString();

                if(checkInputValid(name, email, phone)) {
                    //Toast input valid
                    Context context = getApplicationContext();
                    CharSequence text = "Inputs valid!";
                    int duration = Toast.LENGTH_LONG;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                    ContentValues cv = new ContentValues();
                    cv.put(DBTables.mentorTable.COLUMN_MENTOR_NAME, name);
                    cv.put(DBTables.mentorTable.COLUMN_MENTOR_EMAIL, email);
                    cv.put(DBTables.mentorTable.COLUMN_MENTOR_PHONE, phone);


                    dB.insert(DBTables.mentorTable.TABLE_NAME, null, cv);
                    mAdapter.swapCursor(getAllItems());
                    createMentor.dismiss();
                }
            }
        });


        //cancel button clicked
        addMentorDialogView.findViewById(R.id.create_mentor_btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createMentor.dismiss();
            }
        });


        createMentor.show();


    }



    private boolean checkInputValid(String name, String email, String phone) {
        boolean inputValid = true;
        if(!email.matches("^[a-zA-Z0-9]+@[a-zA-Z0-9]+(.[a-zA-Z]{2,})$")){
            inputValid = false;
        }
        if (name == null || name.trim().isEmpty()) {
            inputValid = false;
        }
        if (phone == null || phone.trim().isEmpty()) {
            inputValid = false;
        }
        if(!inputValid) {
            //toast invalid inputs
            Context context = getApplicationContext();
            CharSequence text = "Inputs not valid!";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }

        return inputValid;

    }


    private Cursor getAllItems() {
        return dB.query(DBTables.mentorTable.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                DBTables.mentorTable.COLUMN_MENTOR_ID + " ASC"

        );
    }
}
