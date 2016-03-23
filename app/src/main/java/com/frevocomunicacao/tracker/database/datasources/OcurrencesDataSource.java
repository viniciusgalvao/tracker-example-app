package com.frevocomunicacao.tracker.database.datasources;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.frevocomunicacao.tracker.database.DbHelper;
import com.frevocomunicacao.tracker.database.contracts.CheckinContract;
import com.frevocomunicacao.tracker.database.contracts.OcurrenceContract;
import com.frevocomunicacao.tracker.database.models.Ocurrence;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vinicius on 21/03/16.
 */
public class OcurrencesDataSource {

    // database fields
    private SQLiteDatabase db;
    private DbHelper dbHelper;
    private String[] allColumns = {
            OcurrenceContract.OcurrenceEntry._ID,
            OcurrenceContract.OcurrenceEntry.COLUMN_FIELD_NAME
    };

    public OcurrencesDataSource(Context context) {
        dbHelper = new DbHelper(context);
        this.open();
    }

    public void open() throws SQLException {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public boolean create(Ocurrence object) {
        ContentValues values = new ContentValues();
        values.put(OcurrenceContract.OcurrenceEntry.COLUMN_FIELD_ID, object.getId());
        values.put(OcurrenceContract.OcurrenceEntry.COLUMN_FIELD_NAME, object.getName());

        long insertId = db.insert(
                OcurrenceContract.OcurrenceEntry.TABLE_NAME,
                null,
                values);

        return insertId != 0 ? true : false;
    }

    public void update(Ocurrence object) {
        ContentValues values = new ContentValues();
        values.put(OcurrenceContract.OcurrenceEntry.COLUMN_FIELD_ID, object.getId());
        values.put(OcurrenceContract.OcurrenceEntry.COLUMN_FIELD_NAME, object.getName());

        // query
        String selection = OcurrenceContract.OcurrenceEntry.COLUMN_FIELD_ID + " = ?";
        String[] selectionArgs = { String.valueOf(object.getId()) };

        int count = db.update(
                OcurrenceContract.OcurrenceEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    public void delete(Ocurrence object) {
        db.delete(OcurrenceContract.OcurrenceEntry.TABLE_NAME, OcurrenceContract.OcurrenceEntry.COLUMN_FIELD_ID
                + " = " + object.getId(), null);
    }

    public boolean exist(Ocurrence object) {
        String q = OcurrenceContract.OcurrenceEntry.COLUMN_FIELD_ID + " = ?";

        if (this.find(q, new String[]{String.valueOf(object.getId())}) != null) {
            return true;
        }

        return false;
    }

    public Ocurrence find(String query, String[] args) {
        Ocurrence object = null;

        Cursor cursor = db.query(
                OcurrenceContract.OcurrenceEntry.TABLE_NAME,    // The table to query
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

    public List<Ocurrence> findAll(String query, String[] args, String sortOrder) {
        List<Ocurrence> ocurrences = new ArrayList<Ocurrence>();

        Cursor cursor = db.query(
                OcurrenceContract.OcurrenceEntry.TABLE_NAME,   // The table to query
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
                Ocurrence object = cursorToObject(cursor);
                ocurrences.add(object);
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return ocurrences;
    }

    private Ocurrence cursorToObject(Cursor cursor) {
        // object instance
        Ocurrence object = new Ocurrence();

        // fill data
        object.setId(cursor.getInt(0));
        object.setName(cursor.getString(1));

        // return object
        return object;
    }
}
