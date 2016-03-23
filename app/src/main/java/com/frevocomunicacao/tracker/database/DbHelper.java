package com.frevocomunicacao.tracker.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.frevocomunicacao.tracker.database.contracts.CheckinContract;
import com.frevocomunicacao.tracker.database.contracts.ImageContract;
import com.frevocomunicacao.tracker.database.contracts.OcurrenceContract;
import com.frevocomunicacao.tracker.database.contracts.VisitContract;

/**
 * Created by Vinicius on 21/03/16.
 */
public class DbHelper extends SQLiteOpenHelper {

    // if you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "tracker.db";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create tables
        db.execSQL(VisitContract.SQL_CREATE_ENTRIES);
        db.execSQL(CheckinContract.SQL_CREATE_ENTRIES);
        db.execSQL(OcurrenceContract.SQL_CREATE_ENTRIES);
        db.execSQL(ImageContract.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // just discard ocurrences table
        db.execSQL(VisitContract.SQL_DELETE_ENTRIES);
        db.execSQL(CheckinContract.SQL_DELETE_ENTRIES);
        db.execSQL(OcurrenceContract.SQL_DELETE_ENTRIES);
        db.execSQL(ImageContract.SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
