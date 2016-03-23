package com.frevocomunicacao.tracker.database.contracts;

import android.provider.BaseColumns;

public final class CheckinContract {

    public CheckinContract() {}

    public static abstract class CheckinEntry implements BaseColumns {
        public static final String TABLE_NAME                   = "checkins";
        public static final String COLUMN_FIELD_ID              = "_id";
        public static final String COLUMN_FIELD_VISIT_ID        = "visit_id";
        public static final String COLUMN_FIELD_VISIT_STATUS_ID = "visit_status_id";
        public static final String COLUMN_FIELD_EMPLOYEE_ID     = "employee_id";
        public static final String COLUMN_FIELD_OBSERVATION     = "observation";

        public static final String COLUMN_FIELD_DATE        = "date";
        public static final String COLUMN_FIELD_HOUR        = "hour";
        public static final String COLUMN_FIELD_LATITUDE    = "latitude";
        public static final String COLUMN_FIELD_LONGITUDE   = "longitude";
    }

    private static final String TEXT_TYPE   = " TEXT";
    private static final String INT_TYPE    = " INTEGER";
    private static final String COMMA_SEP   = ",";

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + CheckinEntry.TABLE_NAME + " (" +
                    CheckinEntry.COLUMN_FIELD_ID              + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    CheckinEntry.COLUMN_FIELD_VISIT_ID        + INT_TYPE + COMMA_SEP +
                    CheckinEntry.COLUMN_FIELD_VISIT_STATUS_ID + INT_TYPE + COMMA_SEP +
                    CheckinEntry.COLUMN_FIELD_EMPLOYEE_ID     + INT_TYPE + COMMA_SEP +
                    CheckinEntry.COLUMN_FIELD_OBSERVATION     + TEXT_TYPE + COMMA_SEP +
                    CheckinEntry.COLUMN_FIELD_DATE            + TEXT_TYPE + COMMA_SEP +
                    CheckinEntry.COLUMN_FIELD_HOUR            + TEXT_TYPE + COMMA_SEP +
                    CheckinEntry.COLUMN_FIELD_LATITUDE        + " DECIMAL(10,8)" + COMMA_SEP +
                    CheckinEntry.COLUMN_FIELD_LONGITUDE       + " DECIMAL(11,8)" +
            " )";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + CheckinEntry.TABLE_NAME;
}
