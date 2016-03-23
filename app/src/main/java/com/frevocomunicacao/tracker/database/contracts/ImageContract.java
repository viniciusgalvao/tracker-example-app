package com.frevocomunicacao.tracker.database.contracts;

import android.provider.BaseColumns;

public final class ImageContract {

    public ImageContract() {}

    public static abstract class ImageEntry implements BaseColumns {
        public static final String TABLE_NAME               = "images";
        public static final String COLUMN_FIELD_ID          = "_id";
        public static final String COLUMN_FIELD_VISIT_ID    = "visit_id";
        public static final String COLUMN_FIELD_NAME        = "name";
        public static final String COLUMN_FIELD_SOURCE      = "source";
    }

    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE  = " INTEGER";
    private static final String COMMA_SEP = ",";

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ImageEntry.TABLE_NAME + " (" +
                    ImageEntry.COLUMN_FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    ImageEntry.COLUMN_FIELD_VISIT_ID + INT_TYPE + COMMA_SEP +
                    ImageEntry.COLUMN_FIELD_NAME + TEXT_TYPE + COMMA_SEP +
                    ImageEntry.COLUMN_FIELD_SOURCE + TEXT_TYPE +
            " )";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ImageEntry.TABLE_NAME;
}
