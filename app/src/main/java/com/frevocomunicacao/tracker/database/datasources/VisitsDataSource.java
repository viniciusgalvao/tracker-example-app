package com.frevocomunicacao.tracker.database.datasources;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.frevocomunicacao.tracker.database.DbHelper;
import com.frevocomunicacao.tracker.database.contracts.VisitContract;
import com.frevocomunicacao.tracker.database.models.Visit;

import java.util.ArrayList;
import java.util.List;


public class VisitsDataSource {

    private SQLiteDatabase db;
    private DbHelper dbHelper;
    private String[] allColumns = {
            VisitContract.VisitEntry._ID,
            VisitContract.VisitEntry.COLUMN_FIELD_ID,
            VisitContract.VisitEntry.COLUMN_FIELD_VISIT_TYPE_ID,
            VisitContract.VisitEntry.COLUMN_FIELD_EMPLOYEE_ID,
            VisitContract.VisitEntry.COLUMN_FIELD_MOTIVE,
            VisitContract.VisitEntry.COLUMN_FIELD_CEP,
            VisitContract.VisitEntry.COLUMN_FIELD_STATE,
            VisitContract.VisitEntry.COLUMN_FIELD_CITY,
            VisitContract.VisitEntry.COLUMN_FIELD_ADDRESS,
            VisitContract.VisitEntry.COLUMN_FIELD_NEIGHBORHOOD,
            VisitContract.VisitEntry.COLUMN_FIELD_COMPLEMENT,
            VisitContract.VisitEntry.COLUMN_FIELD_NUMBER,
            VisitContract.VisitEntry.COLUMN_FIELD_REF_POINT,
            VisitContract.VisitEntry.COLUMN_FIELD_PHONE,
            VisitContract.VisitEntry.COLUMN_FIELD_VISIT_STATUS_ID,
            VisitContract.VisitEntry.COLUMN_FIELD_DATE_FINISH,
    };

    public VisitsDataSource(Context context) {
        dbHelper = new DbHelper(context);
    }

    public void open() throws SQLException {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long create(Visit object) {
        this.open();

        ContentValues values = new ContentValues();
        values.put(VisitContract.VisitEntry.COLUMN_FIELD_ID             , object.getId());
        values.put(VisitContract.VisitEntry.COLUMN_FIELD_VISIT_TYPE_ID  , object.getVisitTypeId());
        values.put(VisitContract.VisitEntry.COLUMN_FIELD_EMPLOYEE_ID    , object.getEmpolyeeId());
        values.put(VisitContract.VisitEntry.COLUMN_FIELD_MOTIVE         , object.getMotive());
        values.put(VisitContract.VisitEntry.COLUMN_FIELD_CEP            , object.getCep());
        values.put(VisitContract.VisitEntry.COLUMN_FIELD_STATE          , object.getState());
        values.put(VisitContract.VisitEntry.COLUMN_FIELD_CITY           , object.getCity());
        values.put(VisitContract.VisitEntry.COLUMN_FIELD_ADDRESS        , object.getAddress());
        values.put(VisitContract.VisitEntry.COLUMN_FIELD_NEIGHBORHOOD   , object.getNeighborhood());
        values.put(VisitContract.VisitEntry.COLUMN_FIELD_COMPLEMENT     , object.getComplement());
        values.put(VisitContract.VisitEntry.COLUMN_FIELD_NUMBER         , object.getNumber());
        values.put(VisitContract.VisitEntry.COLUMN_FIELD_REF_POINT      , object.getReferencePoint());
        values.put(VisitContract.VisitEntry.COLUMN_FIELD_VISIT_STATUS_ID, object.getVisitStatusId());

        long insertId = db.insert(
                VisitContract.VisitEntry.TABLE_NAME,
                null,
                values);

        this.close();

        return insertId;
    }

    public int update(Visit object) {
        this.open();

        ContentValues values = new ContentValues();
        values.put(VisitContract.VisitEntry.COLUMN_FIELD_ID             , object.getId());
        values.put(VisitContract.VisitEntry.COLUMN_FIELD_VISIT_TYPE_ID  , object.getVisitTypeId());
        values.put(VisitContract.VisitEntry.COLUMN_FIELD_EMPLOYEE_ID    , object.getEmpolyeeId());
        values.put(VisitContract.VisitEntry.COLUMN_FIELD_MOTIVE         , object.getMotive());
        values.put(VisitContract.VisitEntry.COLUMN_FIELD_CEP            , object.getCep());
        values.put(VisitContract.VisitEntry.COLUMN_FIELD_STATE          , object.getState());
        values.put(VisitContract.VisitEntry.COLUMN_FIELD_CITY           , object.getCity());
        values.put(VisitContract.VisitEntry.COLUMN_FIELD_ADDRESS        , object.getAddress());
        values.put(VisitContract.VisitEntry.COLUMN_FIELD_NEIGHBORHOOD   , object.getNeighborhood());
        values.put(VisitContract.VisitEntry.COLUMN_FIELD_COMPLEMENT     , object.getComplement());
        values.put(VisitContract.VisitEntry.COLUMN_FIELD_NUMBER         , object.getNumber());
        values.put(VisitContract.VisitEntry.COLUMN_FIELD_REF_POINT      , object.getReferencePoint());
        values.put(VisitContract.VisitEntry.COLUMN_FIELD_VISIT_STATUS_ID, object.getVisitStatusId());

        // query
        String selection = VisitContract.VisitEntry.COLUMN_FIELD_ID + " = ?";
        String[] selectionArgs = { String.valueOf(object.getId()) };

        // exec
        int count = db.update(
                VisitContract.VisitEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);

        this.close();

        return count;
    }

    public void delete(Visit object) {
        this.open();

        db.delete(VisitContract.VisitEntry.TABLE_NAME, VisitContract.VisitEntry.COLUMN_FIELD_ID
                + " = " + object.getId(), null);

        this.close();
    }

    public boolean exist(Visit object) {
        this.open();

        String q = VisitContract.VisitEntry.COLUMN_FIELD_ID + " = ?";

        if (this.find(q, new String[]{String.valueOf(object.getId())}) != null) {
            this.close();

            return true;
        }

        this.close();

        return false;
    }

    public Visit find(String query, String[] args) {
        this.open();

        Visit object = null;

        Cursor cursor = db.query(
                VisitContract.VisitEntry.TABLE_NAME,        // The table to query
                allColumns,                                     // The columns to return
                query,                                          // The columns for the WHERE clause
                args,                                           // The values for the WHERE clause
                null,                                           // don't group the rows
                null,                                           // don't filter by row groups
                null                                            // The sort order
        );

        try {
            while (cursor.moveToNext()) {
                object = cursorToObject(cursor);
            }
        } finally {
            cursor.close();
            this.close();
        }

        return object;
    }

    public List<Visit> findAll(String query, String[] args, String sortOrder) {
        this.open();

        List<Visit> visits = new ArrayList<Visit>();

        Cursor cursor = db.query(
                VisitContract.VisitEntry.TABLE_NAME,        // The table to query
                allColumns,                                     // The columns to return
                query,                                          // The columns for the WHERE clause
                args,                                           // The values for the WHERE clause
                null,                                           // don't group the rows
                null,                                           // don't filter by row groups
                sortOrder                                       // The sort order
        );

        try {
            // move cursor to first record
            cursor.moveToFirst();

            // get data
            while (!cursor.isAfterLast()) {
                Visit object = cursorToObject(cursor);
                visits.add(object);
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
            this.close();
        }

        return visits;
    }

    private Visit cursorToObject(Cursor cursor) {
        // object instance
        Visit visit = new Visit();

        // fill data
        visit.setLocalId(cursor.getLong(cursor.getColumnIndex(VisitContract.VisitEntry._ID)));
        visit.setId(cursor.getInt(cursor.getColumnIndex(VisitContract.VisitEntry.COLUMN_FIELD_ID)));
        visit.setVisitTypeId(cursor.getInt(cursor.getColumnIndex(VisitContract.VisitEntry.COLUMN_FIELD_VISIT_TYPE_ID)));
        visit.setEmployeeId(cursor.getInt(cursor.getColumnIndex(VisitContract.VisitEntry.COLUMN_FIELD_EMPLOYEE_ID)));
        visit.setMotive(cursor.getString(cursor.getColumnIndex(VisitContract.VisitEntry.COLUMN_FIELD_MOTIVE)));
        visit.setCep(cursor.getString(cursor.getColumnIndex(VisitContract.VisitEntry.COLUMN_FIELD_CEP)));
        visit.setState(cursor.getString(cursor.getColumnIndex(VisitContract.VisitEntry.COLUMN_FIELD_STATE)));
        visit.setCity(cursor.getString(cursor.getColumnIndex(VisitContract.VisitEntry.COLUMN_FIELD_CITY)));
        visit.setAddress(cursor.getString(cursor.getColumnIndex(VisitContract.VisitEntry.COLUMN_FIELD_ADDRESS)));
        visit.setNeighborhood(cursor.getString(cursor.getColumnIndex(VisitContract.VisitEntry.COLUMN_FIELD_NEIGHBORHOOD)));
        visit.setComplement(cursor.getString(cursor.getColumnIndex(VisitContract.VisitEntry.COLUMN_FIELD_COMPLEMENT)));
        visit.setNumber(cursor.getString(cursor.getColumnIndex(VisitContract.VisitEntry.COLUMN_FIELD_NUMBER)));
        visit.setReferencePoint(cursor.getString(cursor.getColumnIndex(VisitContract.VisitEntry.COLUMN_FIELD_REF_POINT)));
        visit.setPhone(cursor.getString(cursor.getColumnIndex(VisitContract.VisitEntry.COLUMN_FIELD_PHONE)));
        visit.setVisitStatusId(cursor.getInt(cursor.getColumnIndex(VisitContract.VisitEntry.COLUMN_FIELD_VISIT_STATUS_ID)));

        // return object
        return visit;
    }

    public void truncateTable() {
        this.open();

        db.execSQL("DELETE FROM " + VisitContract.VisitEntry.TABLE_NAME);

        this.close();
    }
}
