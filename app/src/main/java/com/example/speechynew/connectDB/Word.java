package com.example.speechynew.connectDB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import static com.example.speechynew.connectDB.Wordinterface.SECOND;
import static com.example.speechynew.connectDB.Wordinterface.TABLE_NAME3;
import static com.example.speechynew.connectDB.Wordinterface.WORDD;
import static com.example.speechynew.connectDB.Wordinterface.DATE;
import static com.example.speechynew.connectDB.Wordinterface.DAY;
import static com.example.speechynew.connectDB.Wordinterface.MONTH;
import static com.example.speechynew.connectDB.Wordinterface.YEAR;
import static com.example.speechynew.connectDB.Wordinterface.HOUR;
import static com.example.speechynew.connectDB.Wordinterface.MINUTE;


public class Word extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Anyword.db";
    private static final int DATABASE_VERSION = 1;

    public Word(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME3 + " (" + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + WORDD + " TEXT NOT NULL, " + DAY + " TEXT NOT NULL, " + DATE + " TEXT NOT NULL, " +
                 MONTH + " TEXT NOT NULL, " + YEAR + " TEXT NOT NULL, " + HOUR + " TEXT NOT NULL, " + MINUTE + " TEXT NOT NULL, " + SECOND + " TEXT NOT NULL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
