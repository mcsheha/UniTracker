package com.mikeshehadeh.unitracker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TermItem {

    private int mImageResource;
    private int termNumber;
    private String termDates;
    private Calendar startDate;

    public Calendar getEndDate() {
        return endDate;
    }

    private Calendar endDate;
    private String termDatesString;


    public TermItem(int termNumber, Calendar startDate) {
        mImageResource = R.drawable.wgu_logo;
        this.termNumber = termNumber;
        this.termDates = termDates;
        this.startDate = startDate;
        setEndDate();
    }


    private void setEndDate() {
        Calendar endDate = (Calendar)startDate.clone();
        endDate.add(Calendar.MONTH,6);
        endDate.add(Calendar.DATE, -1);
        this.endDate = endDate;
        setTermDatesString();

    }

    private void setTermDatesString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        dateFormat.setCalendar(startDate);
        termDatesString = dateFormat.format(startDate.getTime()) + " - " +
                dateFormat.format(endDate.getTime());
    }




    public int getmImageResource() {
        return mImageResource;
    }

    public int getTermNumberInt() {
        return termNumber;
    }

    public String getTermNumber() {
        return "Term " + String.valueOf(termNumber);
    }

    public String getTermDates() {
        return termDatesString;
    }
}


