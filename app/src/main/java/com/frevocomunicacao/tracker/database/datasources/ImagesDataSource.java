package com.frevocomunicacao.tracker.database.datasources;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.frevocomunicacao.tracker.database.DbHelper;
import com.frevocomunicacao.tracker.database.contracts.CheckinContract;
import com.frevocomunicacao.tracker.database.contracts.ImageContract;
import com.frevocomunicacao.tracker.database.contracts.VisitContract;
import com.frevocomunicacao.tracker.database.models.VisitImage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vinicius on 21/03/16.
 */
public class ImagesDataSource {

    private SQLiteDatabase db;
    private DbHelper dbHelper;
    private String[] allColumns = {
            ImageContract.ImageEntry.COLUMN_FIELD_ID,
            ImageContract.ImageEntry.COLUMN_FIELD_VISIT_ID,
            ImageContract.ImageEntry.COLUMN_FIELD_NAME,
            ImageContract.ImageEntry.COLUMN_FIELD_SOURCE
    };

    public ImagesDataSource(Context context) {
        dbHelper = new DbHelper(context);
    }

    public void open() throws SQLException {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public boolean create(VisitImage object) {
        this.open();

        ContentValues values = new ContentValues();
        values.put(ImageContract.ImageEntry.COLUMN_FIELD_VISIT_ID, object.getVisitId());
        values.put(ImageContract.ImageEntry.COLUMN_FIELD_NAME, object.getName());
        values.put(ImageContract.ImageEntry.COLUMN_FIELD_SOURCE, object.getSource());

        long insertId = db.insert(
                ImageContract.ImageEntry.TABLE_NAME,
                null,
                values);

        this.close();

        return insertId != 0 ? true : false;
    }

    public int update(VisitImage object) {
        this.open();

        ContentValues values = new ContentValues();
        values.put(ImageContract.ImageEntry.COLUMN_FIELD_VISIT_ID, object.getVisitId());
        values.put(ImageContract.ImageEntry.COLUMN_FIELD_NAME, object.getName());
        values.put(ImageContract.ImageEntry.COLUMN_FIELD_SOURCE, object.getSource());

        // query
        String selection = ImageContract.ImageEntry.COLUMN_FIELD_ID + " = ?";
        String[] selectionArgs = { String.valueOf(object.getId()) };

        int count = db.update(
                ImageContract.ImageEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);

        this.close();

        return count;
    }

    public void delete(VisitImage object) {
        this.open();

        db.delete(ImageContract.ImageEntry.TABLE_NAME, ImageContract.ImageEntry.COLUMN_FIELD_ID
                + " = " + object.getId(), null);

        this.close();
    }

    public boolean exist(VisitImage object) {
        this.open();

        String q = VisitContract.VisitEntry.COLUMN_FIELD_ID + " = ?";

        if (this.find(q, new String[]{String.valueOf(object.getId())}) != null) {
            this.close();
            return true;
        }

        this.close();

        return false;
    }

    public VisitImage find(String query, String[] args) {
        this.open();

        VisitImage object = null;

        Cursor cursor = db.query(
                ImageContract.ImageEntry.TABLE_NAME,   // The table to query
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

    public List<VisitImage> findAll(String query, String[] args, String sortOrder) {
        this.open();

        List<VisitImage> images = new ArrayList<VisitImage>();

        Cursor cursor = db.query(
                ImageContract.ImageEntry.TABLE_NAME,   // The table to query
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
                VisitImage object = cursorToObject(cursor);
                images.add(object);
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
            this.close();
        }

        return images;
    }

    private VisitImage cursorToObject(Cursor cursor) {
        // object instance
        VisitImage visitImage = new VisitImage();

        // fill data
        visitImage.setId(cursor.getInt(cursor.getColumnIndex(ImageContract.ImageEntry.COLUMN_FIELD_ID)));
        visitImage.setName(cursor.getString(cursor.getColumnIndex(ImageContract.ImageEntry.COLUMN_FIELD_NAME)));
        visitImage.setSource(cursor.getString(cursor.getColumnIndex(ImageContract.ImageEntry.COLUMN_FIELD_SOURCE)));

        // return object
        return visitImage;
    }

    public void truncateTable() {
        this.open();

        // truncate sql
        db.execSQL("DELETE FROM " + CheckinContract.CheckinEntry.TABLE_NAME);

        this.close();
    }
}
