package com.example.speechynew.connectDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import static com.example.speechynew.connectDB.Schedulerinterface.DATESTART;
import static com.example.speechynew.connectDB.Schedulerinterface.DAYSTART;
import static com.example.speechynew.connectDB.Schedulerinterface.MONTHSTART;
import static com.example.speechynew.connectDB.Schedulerinterface.STATUS;
import static com.example.speechynew.connectDB.Schedulerinterface.TABLE_NAME9;
import static com.example.speechynew.connectDB.Schedulerinterface.TIMESTARTHOUR;
import static com.example.speechynew.connectDB.Schedulerinterface.TIMESTARTMINUTE;
import static com.example.speechynew.connectDB.Schedulerinterface.TIMESTOPHOUR;
import static com.example.speechynew.connectDB.Schedulerinterface.TIMESTOPMINUTE;
import static com.example.speechynew.connectDB.Schedulerinterface.YEARSTART;


public class Scheduler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "scheduler.db";
    private static final int DATABASE_VERSION = 1;

    public Scheduler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME9 + " (" + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DAYSTART + " TEXT NOT NULL, " + DATESTART + " TEXT NOT NULL, " + MONTHSTART + " TEXT NOT NULL, " +
                YEARSTART + " TEXT NOT NULL, " + TIMESTARTHOUR + " TEXT NOT NULL, " + TIMESTARTMINUTE + " TEXT NOT NULL, " + TIMESTOPHOUR + " TEXT NOT NULL, " + TIMESTOPMINUTE + " TEXT NOT NULL, " + STATUS + " TEXT NOT NULL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //when change switch
    public boolean update(int id,int day,int date,int month,int year,String stus){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put(DAYSTART,day);
        content.put(DATESTART,date);
        content.put(MONTHSTART,month);
        content.put(YEARSTART,year);
        content.put(STATUS,stus);
        db.update(TABLE_NAME9,content,"_id=?",new String[]{String.valueOf(id)});
        return true;
    }


    //when change switch
    public boolean updatestatus(int id,String stus){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put(STATUS,stus);
        db.update(TABLE_NAME9,content,"_id=?",new String[]{String.valueOf(id)});
        return true;
    }

    //when push button bin
    public boolean delete(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME9, "_id=?", new String[]{String.valueOf(id)});
        return true;

    }

}
