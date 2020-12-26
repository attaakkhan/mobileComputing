package com.atta.mobilecomputingassingment1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;




public class DBHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    static String TAG= "DBHelper";

    // Database Name
    public static final String DATABASE_NAME = "khan";


    public DBHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.i(TAG,"************************************");



        db.execSQL(CovidSymptoms.CREATE_TABLE);
        //db.execSQL("drop table "+CovidSymptoms.TABLE_NAME+";");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + CovidSymptoms.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }
}