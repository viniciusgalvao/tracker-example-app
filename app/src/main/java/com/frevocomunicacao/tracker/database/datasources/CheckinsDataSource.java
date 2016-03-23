package com.frevocomunicacao.tracker.database.datasources;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.frevocomunicacao.tracker.database.DbHelper;
import com.frevocomunicacao.tracker.database.contracts.CheckinContract;
import com.frevocomunicacao.tracker.database.contracts.ImageContract;
import com.frevocomunicacao.tracker.database.contracts.OcurrenceContract;
import com.frevocomunicacao.tracker.database.models.Checkin;
import com.frevocomunicacao.tracker.database.models.Ocurrence;
import com.frevocomunicacao.tracker.database.models.VisitImage;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Vinicius on 21/03/16.
 */
public class CheckinsDataSource {

    private SQLiteDatabase db;
    private DbHelper dbHelper;
    private String[] allColumns = {
            CheckinContract.CheckinEntry.COLUMN_FIELD_ID,
            CheckinContract.CheckinEntry.COLUMN_FIELD_VISIT_ID,
            CheckinContract.CheckinEntry.COLUMN_FIELD_VISIT_STATUS_ID,
            CheckinContract.CheckinEntry.COLUMN_FIELD_EMPLOYEE_ID,
            CheckinContract.CheckinEntry.COLUMN_FIELD_OBSERVATION,
            CheckinContract.CheckinEntry.COLUMN_FIELD_DATE,
            CheckinContract.CheckinEntry.COLUMN_FIELD_HOUR,
            CheckinContract.CheckinEntry.COLUMN_FIELD_LATITUDE,
            CheckinContract.CheckinEntry.COLUMN_FIELD_LONGITUDE
    };

    public CheckinsDataSource(Context context) {
        dbHelper = new DbHelper(context);
        this.open();
    }

    public void open() throws SQLException {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public boolean create(Checkin object) {
        ContentValues values = new ContentValues();
        values.put(CheckinContract.CheckinEntry.COLUMN_FIELD_VISIT_ID, object.getVisitId());
        values.put(CheckinContract.CheckinEntry.COLUMN_FIELD_VISIT_STATUS_ID, object.getVisitStatusId());
        values.put(CheckinContract.CheckinEntry.COLUMN_FIELD_EMPLOYEE_ID, object.getEmployeeId());
        values.put(CheckinContract.CheckinEntry.COLUMN_FIELD_OBSERVATION, object.getObservation());
        values.put(CheckinContract.CheckinEntry.COLUMN_FIELD_DATE, object.getDate());
        values.put(CheckinContract.CheckinEntry.COLUMN_FIELD_HOUR, object.getHour());
        values.put(CheckinContract.CheckinEntry.COLUMN_FIELD_LATITUDE, object.getLatitude());
        values.put(CheckinContract.CheckinEntry.COLUMN_FIELD_LONGITUDE, object.getLongitude());

        long insertId = db.insert(
                CheckinContract.CheckinEntry.TABLE_NAME,
                null,
                values);

        return insertId != 0 ? true : false;
    }

    public void update(Checkin object) {
        ContentValues values = new ContentValues();
        values.put(CheckinContract.CheckinEntry.COLUMN_FIELD_VISIT_ID, object.getVisitId());
        values.put(CheckinContract.CheckinEntry.COLUMN_FIELD_VISIT_STATUS_ID, object.getVisitStatusId());
        values.put(CheckinContract.CheckinEntry.COLUMN_FIELD_EMPLOYEE_ID, object.getEmployeeId());
        values.put(CheckinContract.CheckinEntry.COLUMN_FIELD_OBSERVATION, object.getObservation());
        values.put(CheckinContract.CheckinEntry.COLUMN_FIELD_DATE, object.getDate());
        values.put(CheckinContract.CheckinEntry.COLUMN_FIELD_HOUR, object.getHour());
        values.put(CheckinContract.CheckinEntry.COLUMN_FIELD_LATITUDE, object.getLatitude());
        values.put(CheckinContract.CheckinEntry.COLUMN_FIELD_LONGITUDE, object.getLongitude());

        // query
        String selection = CheckinContract.CheckinEntry.COLUMN_FIELD_ID + " = ?";
        String[] selectionArgs = { String.valueOf(object.getLocalId()) };

        int count = db.update(
                CheckinContract.CheckinEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    public void delete(Checkin object) {
        db.delete(CheckinContract.CheckinEntry.TABLE_NAME, CheckinContract.CheckinEntry.COLUMN_FIELD_ID
                + " = " + object.getId(), null);
    }

    public boolean exist(Checkin object) {
        String q = CheckinContract.CheckinEntry.COLUMN_FIELD_ID + " = ?";

        if (this.find(q, new String[]{String.valueOf(object.getId())}) != null) {
            return true;
        }

        return false;
    }

    public Checkin find(String query, String[] args) {
        Checkin object = null;

        Cursor cursor = db.query(
                CheckinContract.CheckinEntry.TABLE_NAME,        // The table to query
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
        }

        return object;
    }

    public List<Checkin> findAll(String query, String[] args, String sortOrder) {
        List<Checkin> checkins = new ArrayList<Checkin>();

        Cursor cursor = db.query(
                CheckinContract.CheckinEntry.TABLE_NAME,        // The table to query
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
                Checkin object = cursorToObject(cursor);
                checkins.add(object);
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return checkins;
    }

    private Checkin cursorToObject(Cursor cursor) {
        // object instance
        Checkin checkin = new Checkin();

        // fill data
        checkin.setId(cursor.getInt(0));
        checkin.setVisitId(cursor.getInt(1));

        // return object
        return checkin;
    }
}
