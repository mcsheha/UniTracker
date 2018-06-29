package com.mikeshehadeh.unitracker;
import android.provider.BaseColumns;


public class DBTables {
    private DBTables() {}

    //One inner class per table
    public static final class termTable implements BaseColumns {
        public static final String TABLE_NAME = "term";
        public static final String COLUMN_TERM_ID = "id";
        public static final String COLUMN_TERM_STARTDATE = "start_date";
        public static final String COLUMN_TERM_ENDDATE = "end_date";

    }

    public static final class mentorTable implements BaseColumns {
        public static final String TABLE_NAME = "mentor";
        public static final String COLUMN_MENTOR_ID = "id";
        public static final String COLUMN_MENTOR_NAME = "name";
        public static final String COLUMN_MENTOR_EMAIL = "email";
        public static final String COLUMN_MENTOR_PHONE = "phone";

    }

    public static final class courseTable implements BaseColumns {
        public static final String TABLE_NAME = "course";
        public static final String COLUMN_COURSE_ID = "id";
        public static final String COLUMN_COURSE_DESIGNATOR = "designator";
        public static final String COLUMN_COURSE_NAME = "name";
        public static final String COLUMN_CU = "cu";
        public static final String COLUMN_TERM_ID = "term_id";
        public static final String COLUMN_STARTDATE = "start_date";
        public static final String COLUMN_ENDDATE_ANTIC = "end_date_antic";

    }

    public static final class courseMentorTable implements BaseColumns {
        public static final String TABLE_NAME = "course_mentor";
        public static final String COLUMN_MENTOR_ID = "mentor_id";
        public static final String COLUMN_COURSE_ID = "course_id";

    }

    public static final class assessmentTable implements BaseColumns {
        public static final String TABLE_NAME = "assessment";
        public static final String COLUMN_ASSESSMENT_ID = "id";
        public static final String COLUMN_ASSESSMENT_TYPE = "type";
        public static final String COLUMN_ASSESSMENT_TITLE = "title";
        public static final String COLUMN_COURSE_ID = "course_id";

    }

    public static final class alertTable implements BaseColumns {
        public static final String TABLE_NAME = "alert";
        public static final String COLUMN_ALERT_ID = "id";
        public static final String COLUMN_ALERT_TYPE = "type";
        public static final String COLUMN_COURSE_ID = "course_id";
        public static final String COLUMN_ASSESSMENT_ID = "assmt_id";
        public static final String COLUMN_ALERT_DATETIME = "date_time";

    }

    public static final class noteTable implements BaseColumns {
        public static final String TABLE_NAME = "note";
        public static final String COLUMN_NOTE_ID = "id";
        public static final String COLUMN_COURSE_ID = "course_id";
        public static final String COLUMN_NOTE_TEXT = "note_text";

    }

}
