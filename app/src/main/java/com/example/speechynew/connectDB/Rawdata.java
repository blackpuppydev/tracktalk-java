package com.example.speechynew.connectDB;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;


import static com.example.speechynew.connectDB.Rawdatainterface.DATEWORD;
import static com.example.speechynew.connectDB.Rawdatainterface.DAYWORD;
import static com.example.speechynew.connectDB.Rawdatainterface.HOURWORD;
import static com.example.speechynew.connectDB.Rawdatainterface.MINUTEWORD;
import static com.example.speechynew.connectDB.Rawdatainterface.MONTHWORD;
import static com.example.speechynew.connectDB.Rawdatainterface.SECONDWORD;
import static com.example.speechynew.connectDB.Rawdatainterface.TABLE_NAME4;
import static com.example.speechynew.connectDB.Rawdatainterface.ALLWORD;
import static com.example.speechynew.connectDB.Rawdatainterface.YEARWORD;

public class Rawdata extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Rawdata.db";
    private static final int DATABASE_VERSION = 1;

    public Rawdata(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME4 + " (" + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ALLWORD + " TEXT NOT NULL, " + DAYWORD + " INTEGER NOT NULL, " + DATEWORD + " INTEGER NOT NULL, " +
                MONTHWORD + " INTEGER NOT NULL, " + YEARWORD + " INTEGER NOT NULL, " + HOURWORD + " INTEGER NOT NULL, " + MINUTEWORD + " INTEGER NOT NULL, "+ SECONDWORD + " INTEGER NOT NULL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
