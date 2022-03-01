package com.example.speechynew.analysis;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.speechynew.connectDB.Continuemax;

import java.util.ArrayList;
import java.util.Date;

import static com.example.speechynew.connectDB.Continuemaxinterface.DATE;
import static com.example.speechynew.connectDB.Continuemaxinterface.DAY;
import static com.example.speechynew.connectDB.Continuemaxinterface.HOUR;
import static com.example.speechynew.connectDB.Continuemaxinterface.MAXCON;
import static com.example.speechynew.connectDB.Continuemaxinterface.MINUTE;
import static com.example.speechynew.connectDB.Continuemaxinterface.MONTH;
import static com.example.speechynew.connectDB.Continuemaxinterface.SECOND;
import static com.example.speechynew.connectDB.Continuemaxinterface.TABLE_NAME10;
import static com.example.speechynew.connectDB.Continuemaxinterface.YEAR;

public class Continueword {

    ArrayList<String> anyword;
    String[] langofword;
    Context context;

    int maxcount = 0;
    int count=0;

    //database continue
    Continuemax continueword;

    Continueword(ArrayList anyword, String[] langofword, Context context){

        this.anyword = new ArrayList<>();
        this.anyword = anyword;
        this.langofword = new String[anyword.size()];
        this.langofword = langofword;
        this.context = context;

        //create object database
        continueword = new Continuemax(context);

        checkcon();
        savemaxcontinue();

    }


    void checkcon(){

        for(int i=0;i<langofword.length;++i){
            if(langofword[i].equals("en")){
                count++;
                if(count>maxcount){
                    maxcount = count;
                }
            }else{
                if(count>maxcount){
                    maxcount = count;
                    count=0;
                }else{
                    count=0;
                }
            }
        }
    }

    void savemaxcontinue(){

        if(maxcount==0){ //check if 0 word count
            //don't save
        }else{
            SQLiteDatabase dbmaxcon = continueword.getWritableDatabase();
            ContentValues valuescon = new ContentValues();
            valuescon.put(MAXCON,maxcount);
            valuescon.put(DAY,new Date().getDay());
            valuescon.put(DATE,new Date().getDate());
            valuescon.put(MONTH,new Date().getMonth()+1);
            valuescon.put(YEAR,new Date().getYear()+1900);
            valuescon.put(HOUR,new Date().getHours());
            valuescon.put(MINUTE,new Date().getMinutes());
            valuescon.put(SECOND,new Date().getSeconds());
            dbmaxcon.insertOrThrow(TABLE_NAME10, null, valuescon);
        }
    }

}
