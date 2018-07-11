package com.mikeshehadeh.unitracker;



public class CourseItem {

    private int mImageResource;
    private int courseNumber;
    private String courseTitle;
    private String courseDatesString;
    private String courseDesignator;


    public CourseItem(int courseNumber) {
        mImageResource = R.drawable.wgu_logo;
        this.courseNumber = courseNumber;
    }




    private void setCourseTitleString() {


    }


    public int getmImageResource() {
        return mImageResource;
    }

    public int getCourseNumberInt() {
        return courseNumber;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

}