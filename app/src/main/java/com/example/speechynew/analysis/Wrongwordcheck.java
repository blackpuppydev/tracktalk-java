package com.example.speechynew.analysis;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.speechynew.connectDB.Wrongword;
import java.util.ArrayList;
import java.util.Date;

import static com.example.speechynew.connectDB.Wrongwordinterface.DATE;
import static com.example.speechynew.connectDB.Wrongwordinterface.DAY;
import static com.example.speechynew.connectDB.Wrongwordinterface.HOUR;
import static com.example.speechynew.connectDB.Wrongwordinterface.MINUTE;
import static com.example.speechynew.connectDB.Wrongwordinterface.MONTH;
import static com.example.speechynew.connectDB.Wrongwordinterface.SECOND;
import static com.example.speechynew.connectDB.Wrongwordinterface.TABLE_NAME11;
import static com.example.speechynew.connectDB.Wrongwordinterface.WRONGWORD;
import static com.example.speechynew.connectDB.Wrongwordinterface.YEAR;

public class Wrongwordcheck {

    ArrayList<String> anyword;
    String[] langofword;
    Context context;

    Wrongword wrongword;

    Wrongwordcheck(ArrayList anyword,String[] langofword,Context context){

        this.anyword = new ArrayList<>();
        this.anyword = anyword;
        this.langofword = new String[anyword.size()];
        this.langofword = langofword;
        this.context = context;

        wrongword = new Wrongword(context);

        checkwrong();
    }

    void checkwrong(){

        for(int i=0;i<anyword.size();++i){
            if(langofword[i].equals("en")){
                //Nothing
            }else{
                addwrongindb(anyword.get(i));
            }
        }

    }

    void addwrongindb(String word){

        SQLiteDatabase dbwrong = wrongword.getWritableDatabase();
        ContentValues valueswrong = new ContentValues();
        valueswrong.put(WRONGWORD,word);
        valueswrong.put(DAY,new Date().getDay());
        valueswrong.put(DATE,new Date().getDate());
        valueswrong.put(MONTH,new Date().getMonth()+1);
        valueswrong.put(YEAR,new Date().getYear()+1900);
        valueswrong.put(HOUR,new Date().getHours());
        valueswrong.put(MINUTE,new Date().getMinutes());
        valueswrong.put(SECOND,new Date().getSeconds());
        dbwrong.insertOrThrow(TABLE_NAME11, null, valueswrong);

    }

}
