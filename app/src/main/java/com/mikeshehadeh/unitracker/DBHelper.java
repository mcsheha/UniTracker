package com.mikeshehadeh.unitracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.mikeshehadeh.unitracker.DBTables.*;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "unitracker.db";
    public static final int DATABASE_VERSION = 2;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_TERM_TABLE = "CREATE TABLE " + termTable.TABLE_NAME + " (" +
                termTable.COLUMN_TERM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                termTable.COLUMN_TERM_STARTDATE + " TEXT, " +
                termTable.COLUMN_TERM_ENDDATE + " TEXT);"
                ;


        final String SQL_CREATE_MENTOR_TABLE = "CREATE TABLE " + mentorTable.TABLE_NAME + " (" +
                mentorTable.COLUMN_MENTOR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                mentorTable.COLUMN_MENTOR_NAME + " TEXT NOT NULL, " +
                mentorTable.COLUMN_MENTOR_EMAIL + " TEXT NOT NULL, " +
                mentorTable.COLUMN_MENTOR_PHONE + " TEXT NOT NULL);"
                ;


        final String SQL_CREATE_COURSE_TABLE = "CREATE TABLE " + courseTable.TABLE_NAME + " (" +
                courseTable.COLUMN_COURSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                courseTable.COLUMN_COURSE_DESIGNATOR + " TEXT NOT NULL, " +
                courseTable.COLUMN_COURSE_NAME + " TEXT NOT NULL, " +
                courseTable.COLUMN_CU + " INT NOT NULL, " +
                courseTable.COLUMN_TERM_ID + " INT, " +
                courseTable.COLUMN_STARTDATE + " TEXT, " +
                courseTable.COLUMN_ENDDATE_ANTIC + " TEXT, " +
                "FOREIGN KEY (" + courseTable.COLUMN_TERM_ID +
                ") REFERENCES " + termTable.TABLE_NAME +
                "(" + termTable.COLUMN_TERM_ID + "));"
                ;


        final String SQL_CREATE_COURSE_MENTOR_TABLE = "CREATE TABLE " + courseMentorTable.TABLE_NAME + " (" +
                courseMentorTable.COLUMN_COURSE_ID + " INTEGER, " +
                courseMentorTable.COLUMN_MENTOR_ID + " INTEGER, PRIMARY KEY(" +
                courseMentorTable.COLUMN_MENTOR_ID + ", " + courseMentorTable.COLUMN_COURSE_ID + "), " +
                "FOREIGN KEY (" + courseMentorTable.COLUMN_COURSE_ID +
                ") REFERENCES " + courseTable.TABLE_NAME +
                "(" + courseTable.COLUMN_COURSE_ID + "), " +
                "FOREIGN KEY (" + courseMentorTable.COLUMN_MENTOR_ID +
                ") REFERENCES " + mentorTable.TABLE_NAME +
                "(" + mentorTable.COLUMN_MENTOR_ID + ")); "
                ;


        final String SQL_CREATE_ASSESSMENT_TABLE = "CREATE TABLE " + assessmentTable.TABLE_NAME + " (" +
                assessmentTable.COLUMN_ASSESSMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                assessmentTable.COLUMN_ASSESSMENT_TYPE + " TEXT NOT NULL, " +
                assessmentTable.COLUMN_ASSESSMENT_TITLE + " TEXT NOT NULL, " +
                assessmentTable.COLUMN_COURSE_ID + " INTEGER NOT NULL, " +
                "FOREIGN KEY (" + assessmentTable.COLUMN_COURSE_ID +
                ") REFERENCES " + courseTable.TABLE_NAME +
                "(" + courseTable.COLUMN_COURSE_ID + "));"
                ;


        final String SQL_CREATE_ALERT_TABLE = "CREATE TABLE " + alertTable.TABLE_NAME + " (" +
                alertTable.COLUMN_ALERT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                alertTable.COLUMN_ALERT_TYPE + " TEXT NOT NULL, " +
                alertTable.COLUMN_COURSE_ID + " INTEGER, " +
                alertTable.COLUMN_ASSESSMENT_ID + " INTEGER, " +
                alertTable.COLUMN_ALERT_DATETIME + " TEXT NOT NULL, " +
                "FOREIGN KEY (" + alertTable.COLUMN_COURSE_ID +
                ") REFERENCES " + courseTable.TABLE_NAME +
                "(" + courseTable.COLUMN_COURSE_ID + "), " +
                "FOREIGN KEY (" + alertTable.COLUMN_ASSESSMENT_ID +
                ") REFERENCES " + assessmentTable.TABLE_NAME +
                "(" + assessmentTable.COLUMN_ASSESSMENT_ID + ")); "
                ;


        final String SQL_CREATE_NOTE_TABLE = "CREATE TABLE " + noteTable.TABLE_NAME + " (" +
                noteTable.COLUMN_NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                noteTable.COLUMN_COURSE_ID + " INTEGER, " +
                noteTable.COLUMN_NOTE_TEXT + " TEXT, " +
                "FOREIGN KEY (" + noteTable.COLUMN_COURSE_ID +
                ") REFERENCES " + courseTable.TABLE_NAME +
                "(" + courseTable.COLUMN_COURSE_ID + "));"
                ;


        db.execSQL(SQL_CREATE_TERM_TABLE);
        db.execSQL(SQL_CREATE_MENTOR_TABLE);
        db.execSQL(SQL_CREATE_COURSE_TABLE);
        db.execSQL(SQL_CREATE_COURSE_MENTOR_TABLE);
        db.execSQL(SQL_CREATE_ASSESSMENT_TABLE);
        db.execSQL(SQL_CREATE_ALERT_TABLE);
        db.execSQL(SQL_CREATE_NOTE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
