package com.example.speechynew.connectDB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import static com.example.speechynew.connectDB.Wrongwordinterface.DATE;
import static com.example.speechynew.connectDB.Wrongwordinterface.DAY;
import static com.example.speechynew.connectDB.Wrongwordinterface.HOUR;
import static com.example.speechynew.connectDB.Wrongwordinterface.MINUTE;
import static com.example.speechynew.connectDB.Wrongwordinterface.MONTH;
import static com.example.speechynew.connectDB.Wrongwordinterface.SECOND;
import static com.example.speechynew.connectDB.Wrongwordinterface.TABLE_NAME11;
import static com.example.speechynew.connectDB.Wrongwordinterface.WRONGWORD;
import static com.example.speechynew.connectDB.Wrongwordinterface.YEAR;


public class Wrongword extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Wrongword.db";
    private static final int DATABASE_VERSION = 1;

    public Wrongword(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME11 + " (" + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + WRONGWORD + " TEXT NOT NULL, " + DAY + " TEXT NOT NULL, " + DATE + " TEXT NOT NULL, " +
                MONTH + " TEXT NOT NULL, " + YEAR + " TEXT NOT NULL, " + HOUR + " TEXT NOT NULL, " + MINUTE + " TEXT NOT NULL, " + SECOND + " TEXT NOT NULL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
