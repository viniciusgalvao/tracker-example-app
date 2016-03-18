package com.frevocomunicacao.tracker.database;

import android.provider.BaseColumns;

public final class OcurrencesContract {

    public OcurrencesContract() {}

    public static abstract class OcurrenceEntry implements BaseColumns {
        public static final String TABLE_NAME = "ocurrences";
        public static final String COLUMN_FIELD_ID          = "id";
        public static final String COLUMN_FIELD_NAME        = "name";
    }

    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE  = " INTEGER";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + OcurrenceEntry.TABLE_NAME + " (" +
                    OcurrenceEntry._ID + " INTEGER PRIMARY KEY," +
                    OcurrenceEntry.COLUMN_FIELD_ID + INT_TYPE + COMMA_SEP +
                    OcurrenceEntry.COLUMN_FIELD_NAME + TEXT_TYPE + COMMA_SEP +
            " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + OcurrenceEntry.TABLE_NAME;
}
