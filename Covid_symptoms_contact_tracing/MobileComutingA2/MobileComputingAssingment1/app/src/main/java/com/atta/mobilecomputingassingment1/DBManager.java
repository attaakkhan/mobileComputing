package com.atta.mobilecomputingassingment1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DBManager {


    DBHelper dbHelper;
    SQLiteDatabase database;
    public DBManager open(Context context) throws SQLException {
         dbHelper = new DBHelper(context);
         database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(CovidSymptoms covidSymptoms) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(CovidSymptoms.COLUMN_HEART_RATE, covidSymptoms.getHeartRate());
        contentValue.put(CovidSymptoms.COLUMN_RESPIRATORY_RATE, covidSymptoms.getRespiratoryRate());
        contentValue.put(CovidSymptoms.COLUMN_ITEM_1, covidSymptoms.getNausea());
        contentValue.put(CovidSymptoms.COLUMN_ITEM_2, covidSymptoms.getHeadAch());
        contentValue.put(CovidSymptoms.COLUMN_ITEM_3, covidSymptoms.getDiarhea());
        contentValue.put(CovidSymptoms.COLUMN_ITEM_4, covidSymptoms.getSoarThroat());
        contentValue.put(CovidSymptoms.COLUMN_ITEM_5, covidSymptoms.getFever());
        contentValue.put(CovidSymptoms.COLUMN_ITEM_6, covidSymptoms.getMuscleAche());
        contentValue.put(CovidSymptoms.COLUMN_ITEM_7, covidSymptoms.getLossSmellTaste());
        contentValue.put(CovidSymptoms.COLUMN_ITEM_8, covidSymptoms.getCough());
        contentValue.put(CovidSymptoms.COLUMN_ITEM_9, covidSymptoms.getShortnessBreath());
        contentValue.put(CovidSymptoms.COLUMN_ITEM_10, covidSymptoms.getTired());
        contentValue.put(CovidSymptoms.COLUMN_ITEM_time, getDateTime());
        contentValue.put(CovidSymptoms.COLUMN_ITEM_log,covidSymptoms.getLongitude() );
        contentValue.put(CovidSymptoms.COLUMN_ITEM_lat,covidSymptoms.getLatitude() );

        database.insert(CovidSymptoms.TABLE_NAME, null, contentValue);

    }


    public ArrayList<CovidSymptoms> readAllItems() {
        ArrayList<CovidSymptoms> items = new ArrayList<>();

        Cursor cursor = database.query(CovidSymptoms.TABLE_NAME
                , null// columns - null will give all
                , null// selection
                , null// selection arguments
                , null// groupBy
                , null// having
                , null// no need or order by for now;)
        );
        if (cursor != null) {
            while (cursor.moveToNext()) {
                // move the cursor to next row if there is any to read it's data
                CovidSymptoms item = readItem(cursor);
                items.add(item);
            }
        }
        return items;
    }

    private CovidSymptoms readItem(Cursor cursor) {
        CovidSymptoms symptom = new CovidSymptoms();
        symptom.setHeartRate(cursor.getFloat(cursor.getColumnIndex(CovidSymptoms.COLUMN_HEART_RATE)));
        symptom.setRespiratoryRate(cursor.getFloat(cursor.getColumnIndex(CovidSymptoms.COLUMN_RESPIRATORY_RATE)));
        symptom.setNausea(cursor.getFloat(cursor.getColumnIndex(CovidSymptoms.COLUMN_ITEM_1)));
        symptom.setHeadAch(cursor.getFloat(cursor.getColumnIndex(CovidSymptoms.COLUMN_ITEM_2)));
        symptom.setDiarhea(cursor.getFloat(cursor.getColumnIndex(CovidSymptoms.COLUMN_ITEM_3)));
        symptom.setSoarThroat(cursor.getFloat(cursor.getColumnIndex(CovidSymptoms.COLUMN_ITEM_4)));
        symptom.setFever(cursor.getFloat(cursor.getColumnIndex(CovidSymptoms.COLUMN_ITEM_5)));
        symptom.setMuscleAche(cursor.getFloat(cursor.getColumnIndex(CovidSymptoms.COLUMN_ITEM_6)));
        symptom.setLossSmellTaste(cursor.getFloat(cursor.getColumnIndex(CovidSymptoms.COLUMN_ITEM_7)));
        symptom.setCough(cursor.getFloat(cursor.getColumnIndex(CovidSymptoms.COLUMN_ITEM_8)));
        symptom.setShortnessBreath(cursor.getFloat(cursor.getColumnIndex(CovidSymptoms.COLUMN_ITEM_9)));
        symptom.setTired(cursor.getFloat(cursor.getColumnIndex(CovidSymptoms.COLUMN_ITEM_10)));
        symptom.setTimestamp(cursor.getLong(cursor.getColumnIndex(CovidSymptoms.COLUMN_ITEM_time)));
        symptom.setLatitude(cursor.getDouble(cursor.getColumnIndex(CovidSymptoms.COLUMN_ITEM_lat)));
        symptom.setLongitude(cursor.getDouble(cursor.getColumnIndex(CovidSymptoms.COLUMN_ITEM_log)));





        return symptom;
    }
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
    public int getCount() {


        String countQuery = "SELECT  * FROM " + CovidSymptoms.TABLE_NAME;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }


}
