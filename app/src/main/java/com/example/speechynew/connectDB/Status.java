package com.example.speechynew.connectDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import static com.example.speechynew.connectDB.Statusinterface.STATUS;
import static com.example.speechynew.connectDB.Statusinterface.TABLE_NAME7;


public class Status extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Status.db";
    private static final int DATABASE_VERSION = 1;

    public Status(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME7 + " (" + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + STATUS + " TEXT NOT NULL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public boolean update(String status){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put("status",status);
        db.update(TABLE_NAME7,content,"_id=?",new String[]{String.valueOf(1)});
        return true;
    }
}
