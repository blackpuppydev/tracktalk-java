package com.example.speechynew.connectDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;


import static com.example.speechynew.connectDB.Timeprocessinterface.DATE;
import static com.example.speechynew.connectDB.Timeprocessinterface.DAY;
import static com.example.speechynew.connectDB.Timeprocessinterface.HOUR;
import static com.example.speechynew.connectDB.Timeprocessinterface.MINUTE;
import static com.example.speechynew.connectDB.Timeprocessinterface.MONTH;
import static com.example.speechynew.connectDB.Timeprocessinterface.SECOND;
import static com.example.speechynew.connectDB.Timeprocessinterface.TABLE_NAME5;
import static com.example.speechynew.connectDB.Timeprocessinterface.TOTAL;
import static com.example.speechynew.connectDB.Timeprocessinterface.YEAR;

public class Timeprocess extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Timeprocess.db";
    private static final int DATABASE_VERSION = 1;

    public Timeprocess(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME5 + " (" + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DAY + " INTEGER NOT NULL, " + DATE + " INTEGER NOT NULL, " + MONTH + " INTEGER NOT NULL, " + YEAR + " INTEGER NOT NULL, " + HOUR + " INTEGER NOT NULL, "
                                   + MINUTE + " INTEGER NOT NULL, " + SECOND + " INTEGER NOT NULL, " + TOTAL + " INTEGER NOT NULL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /*
    public boolean update(int day,int date,int month,int year,int totaltime){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put("day",day);
        content.put("date",date);
        content.put("month",month);
        content.put("year",year);
        content.put("totaltime",totaltime);
        db.update(TABLE_NAME5,content,DATE + " = ? AND " + MONTH + " = ? AND " + YEAR + " = ?",new String[]{String.valueOf(date),String.valueOf(month),String.valueOf(year)});
        return true;
    } */

}
