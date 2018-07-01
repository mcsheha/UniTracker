package com.mikeshehadeh.unitracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MentorDetailActivity extends AppCompatActivity {

    private long mentorId;
    private SQLiteDatabase dB;
    private String mentorName;
    private String mentorEmail;
    private String mentorPhone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor_detail);

        Bundle b = getIntent().getExtras();
        mentorId = b.getLong("mentorTag");

        DBHelper dbHelper = new DBHelper(this);
        dB = dbHelper.getWritableDatabase();


        configureBackButton();
        configureEditMentorButton();
        getAllItems();
        setTextViews();
    }


    private void configureEditMentorButton() {
        FloatingActionButton backButton = (FloatingActionButton) findViewById(R.id.mentor_dtl_btn_edit);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //Show dialog/form for adding a new mentor
            //save user input, check if valid (Toast if not), add to db, swapCursor(getAllItems)
            LayoutInflater factory = LayoutInflater.from(MentorDetailActivity.this);
            final View editMentorDialogView = factory.inflate(R.layout.dialog_create_mentor, null);
            final AlertDialog editMentor = new AlertDialog.Builder(MentorDetailActivity.this).create();
                editMentor.setView(editMentorDialogView);

                EditText nameEditText = (EditText)editMentorDialogView.findViewById(R.id.create_mentor_dialog_edt_txt_name);
                EditText emailEditText = (EditText)editMentorDialogView.findViewById(R.id.create_mentor_dialog_edt_txt_email);
                EditText phoneEditText = (EditText)editMentorDialogView.findViewById(R.id.create_mentor_dialog_edt_txt_phone);
                TextView dialogTitle = (TextView)editMentorDialogView.findViewById(R.id.create_mentor_dialog_title);
                Button saveButton = (Button)editMentorDialogView.findViewById(R.id.create_mentor_btn_add_mentor);
                Button deleteButton = (Button)editMentorDialogView.findViewById(R.id.create_mentor_btn_delete);
                deleteButton.setVisibility(View.VISIBLE);


                nameEditText.setText(mentorName);
                emailEditText.setText(mentorEmail);
                phoneEditText.setText(mentorPhone);
                dialogTitle.setText("Edit Mentor");
                saveButton.setText("Save");


                //save new term button clicked
                editMentorDialogView.findViewById(R.id.create_mentor_btn_add_mentor).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //check if input is valid, add the course to the db
                        EditText nameEditText = (EditText)editMentorDialogView.findViewById(R.id.create_mentor_dialog_edt_txt_name);
                        EditText emailEditText = (EditText)editMentorDialogView.findViewById(R.id.create_mentor_dialog_edt_txt_email);
                        EditText phoneEditText = (EditText)editMentorDialogView.findViewById(R.id.create_mentor_dialog_edt_txt_phone);

                        String name = nameEditText.getText().toString();
                        String email = emailEditText.getText().toString().toLowerCase();
                        String phone = phoneEditText.getText().toString();

                        if(checkInputValid(name, email, phone)) {


                            ContentValues cv = new ContentValues();
                            cv.put(DBTables.mentorTable.COLUMN_MENTOR_NAME, name);
                            cv.put(DBTables.mentorTable.COLUMN_MENTOR_EMAIL, email);
                            cv.put(DBTables.mentorTable.COLUMN_MENTOR_PHONE, phone);


                            String whereClause = DBTables.mentorTable.COLUMN_MENTOR_ID + "=" + mentorId;
                            dB.update(DBTables.mentorTable.TABLE_NAME, cv, whereClause, null);
                            //dB.insert(DBTables.mentorTable.TABLE_NAME, null, cv);

                            mentorName = name;
                            mentorEmail = email;
                            mentorPhone = phone;
                            //repopulate the dialog

                            setTextViews();
                            editMentor.dismiss();
                        }
                    }
                });

                //cancel button clicked
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String whereClause = DBTables.mentorTable.COLUMN_MENTOR_ID + "=" + mentorId;

                        dB.delete(DBTables.mentorTable.TABLE_NAME, whereClause, null);
                        editMentor.dismiss();
                        finish();
                    }
                });

                editMentorDialogView.findViewById(R.id.create_mentor_btn_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editMentor.dismiss();
                    }
                });


                editMentor.show();
            }
        });
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

    private void setTextViews() {
        TextView tvMentorName = (TextView) findViewById(R.id.mentor_dtl_textView_name);
        TextView tvMentorEmail = (TextView) findViewById(R.id.assmnt_dtl_textview_status);
        TextView tvMentorPhone = (TextView) findViewById(R.id.mentor_dtl_textView_phone);

        tvMentorName.setText(mentorName);
        tvMentorEmail.setText(mentorEmail);
        tvMentorPhone.setText(mentorPhone);

    }

    private void getAllItems() {
        String whereClause = DBTables.mentorTable.COLUMN_MENTOR_ID + "= ?";
        String[] whereArgs = new String[]{Long.toString(mentorId)};
        Cursor c = dB.query(DBTables.mentorTable.TABLE_NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        //String startDateStr = c.getString(c.getColumnIndex(DBTables.termTable.COLUMN_TERM_STARTDATE));
        c.moveToFirst();

        mentorName = c.getString(c.getColumnIndex(DBTables.mentorTable.COLUMN_MENTOR_NAME));
        mentorEmail = c.getString(c.getColumnIndex(DBTables.mentorTable.COLUMN_MENTOR_EMAIL));
        mentorPhone = c.getString(c.getColumnIndex(DBTables.mentorTable.COLUMN_MENTOR_PHONE));

    }

    private void configureBackButton() {
        FloatingActionButton backButton = (FloatingActionButton) findViewById(R.id.mentor_dtl_btn_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


}


















