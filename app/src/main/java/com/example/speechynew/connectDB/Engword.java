package com.example.speechynew.connectDB;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import static com.example.speechynew.connectDB.Engwordinterface.TABLE_NAME2;
import static com.example.speechynew.connectDB.Engwordinterface.WORDENG;
import static com.example.speechynew.connectDB.Engwordinterface.DATEENG;
import static com.example.speechynew.connectDB.Engwordinterface.DAYENG;
import static com.example.speechynew.connectDB.Engwordinterface.MONTHENG;
import static com.example.speechynew.connectDB.Engwordinterface.YEARENG;
import static com.example.speechynew.connectDB.Engwordinterface.HOURENG;
import static com.example.speechynew.connectDB.Engwordinterface.MINUTEENG;
import static com.example.speechynew.connectDB.Engwordinterface.SECONDENG;


public class Engword extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Engword.db";
    private static final int DATABASE_VERSION = 1;

    public Engword(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME2 + " (" + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + WORDENG + " TEXT NOT NULL, " + DAYENG + " INTEGER NOT NULL, " + DATEENG + " INTEGER NOT NULL, " +
                MONTHENG + " INTEGER NOT NULL, " + YEARENG + " INTEGER NOT NULL, " + HOURENG + " INTEGER NOT NULL, " + MINUTEENG + " INTEGER NOT NULL, "+ SECONDENG + " INTEGER NOT NULL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
