package com.frevocomunicacao.tracker.database.contracts;

import android.provider.BaseColumns;

public final class OcurrenceContract {

    public OcurrenceContract() {}

    public static abstract class OcurrenceEntry implements BaseColumns {
        public static final String TABLE_NAME               = "ocurrences";
        public static final String COLUMN_FIELD_ID          = "id";
        public static final String COLUMN_FIELD_NAME        = "name";
    }

    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE  = " INTEGER";
    private static final String COMMA_SEP = ",";

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + OcurrenceEntry.TABLE_NAME + " (" +
                    OcurrenceEntry._ID + " INTEGER PRIMARY KEY," +
                    OcurrenceEntry.COLUMN_FIELD_ID + INT_TYPE + COMMA_SEP +
                    OcurrenceEntry.COLUMN_FIELD_NAME + TEXT_TYPE  +
            " )";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + OcurrenceEntry.TABLE_NAME;
}
