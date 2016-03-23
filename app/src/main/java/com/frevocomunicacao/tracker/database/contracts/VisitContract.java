package com.frevocomunicacao.tracker.database.contracts;

import android.provider.BaseColumns;

public final class VisitContract {

    public VisitContract() {}

    public static abstract class VisitEntry implements BaseColumns {
        public static final String TABLE_NAME               = "visits";
        public static final String COLUMN_FIELD_ID          = "id";
        public static final String COLUMN_FIELD_EMPLOYEE_ID = "employee_id";
        public static final String COLUMN_FIELD_MOTIVE      = "motive";
        public static final String COLUMN_FIELD_CEP         = "cep";
        public static final String COLUMN_FIELD_STATE       = "state";
        public static final String COLUMN_FIELD_CITY        = "city";
        public static final String COLUMN_FIELD_ADDRESS     = "address";
        public static final String COLUMN_FIELD_NEIGHBORHOOD= "neighborhood";
        public static final String COLUMN_FIELD_COMPLEMENT  = "complement";
        public static final String COLUMN_FIELD_NUMBER      = "number";
        public static final String COLUMN_FIELD_REF_POINT   = "reference_point";
        public static final String COLUMN_FIELD_PHONE       = "phone";
        public static final String COLUMN_FIELD_VISIT_STATUS_ID = "visit_status_id";
        public static final String COLUMN_FIELD_DATE_FINISH = "date_finish";
    }

    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE  = " INTEGER";
    private static final String COMMA_SEP = ",";

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + VisitEntry.TABLE_NAME + " (" +
                    VisitEntry._ID                          + " INTEGER PRIMARY KEY," +
                    VisitEntry.COLUMN_FIELD_ID              + INT_TYPE  + COMMA_SEP +
                    VisitEntry.COLUMN_FIELD_EMPLOYEE_ID     + INT_TYPE  + COMMA_SEP +
                    VisitEntry.COLUMN_FIELD_MOTIVE          + TEXT_TYPE + COMMA_SEP +
                    VisitEntry.COLUMN_FIELD_CEP             + TEXT_TYPE + COMMA_SEP +
                    VisitEntry.COLUMN_FIELD_STATE           + TEXT_TYPE + COMMA_SEP +
                    VisitEntry.COLUMN_FIELD_CITY            + TEXT_TYPE + COMMA_SEP +
                    VisitEntry.COLUMN_FIELD_ADDRESS         + TEXT_TYPE + COMMA_SEP +
                    VisitEntry.COLUMN_FIELD_NEIGHBORHOOD    + TEXT_TYPE + COMMA_SEP +
                    VisitEntry.COLUMN_FIELD_COMPLEMENT      + TEXT_TYPE + COMMA_SEP +
                    VisitEntry.COLUMN_FIELD_NUMBER          + TEXT_TYPE + COMMA_SEP +
                    VisitEntry.COLUMN_FIELD_REF_POINT       + TEXT_TYPE + COMMA_SEP +
                    VisitEntry.COLUMN_FIELD_PHONE           + TEXT_TYPE + COMMA_SEP +
                    VisitEntry.COLUMN_FIELD_VISIT_STATUS_ID + INT_TYPE  + COMMA_SEP +
                    VisitEntry.COLUMN_FIELD_DATE_FINISH     + TEXT_TYPE + " NULL " +
            " )";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + VisitEntry.TABLE_NAME;
}
