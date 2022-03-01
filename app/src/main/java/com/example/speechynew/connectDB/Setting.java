package com.example.speechynew.connectDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;


import static com.example.speechynew.connectDB.Settinginterface.CHADAY;
import static com.example.speechynew.connectDB.Settinginterface.NATIVELANG;
import static com.example.speechynew.connectDB.Settinginterface.PERCENTAGENONE;
import static com.example.speechynew.connectDB.Settinginterface.TABLE_NAME0;


public class Setting extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Setting.db";
    private static final int DATABASE_VERSION = 1;

    public Setting(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME0 + " (" + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NATIVELANG + " TEXT NOT NULL, " + PERCENTAGENONE + " TEXT NOT NULL, " + CHADAY + " INTEGER NOT NULL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean update(String nativelang,double perc,String chaday){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put("nativelang",nativelang);
        content.put("percentagenone",perc);
        content.put("chaday",chaday);
        db.update(TABLE_NAME0,content,"_id=?",new String[]{String.valueOf(1)});
        return true;
    }
}
