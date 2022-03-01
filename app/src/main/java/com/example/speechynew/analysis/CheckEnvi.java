package com.example.speechynew.analysis;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.speechynew.connectDB.Engword;
import com.example.speechynew.connectDB.Setting;
import com.example.speechynew.connectDB.Timeprocess;
import com.example.speechynew.connectDB.Timeprocessinterface;
import com.example.speechynew.connectDB.Word;
import com.example.speechynew.connectDB.Wrongword;

import java.util.ArrayList;
import java.util.Date;

import static com.example.speechynew.connectDB.Engwordinterface.DATEENG;
import static com.example.speechynew.connectDB.Engwordinterface.DAYENG;
import static com.example.speechynew.connectDB.Engwordinterface.HOURENG;
import static com.example.speechynew.connectDB.Engwordinterface.MINUTEENG;
import static com.example.speechynew.connectDB.Engwordinterface.MONTHENG;
import static com.example.speechynew.connectDB.Engwordinterface.SECONDENG;
import static com.example.speechynew.connectDB.Engwordinterface.TABLE_NAME2;
import static com.example.speechynew.connectDB.Engwordinterface.WORDENG;
import static com.example.speechynew.connectDB.Engwordinterface.YEARENG;
import static com.example.speechynew.connectDB.Settinginterface.TABLE_NAME0;
import static com.example.speechynew.connectDB.Timeprocessinterface.TABLE_NAME5;
import static com.example.speechynew.connectDB.Timeprocessinterface.TOTAL;
import static com.example.speechynew.connectDB.Wordinterface.DATE;
import static com.example.speechynew.connectDB.Wordinterface.DAY;
import static com.example.speechynew.connectDB.Wordinterface.HOUR;
import static com.example.speechynew.connectDB.Wordinterface.MINUTE;
import static com.example.speechynew.connectDB.Wordinterface.MONTH;
import static com.example.speechynew.connectDB.Wordinterface.SECOND;
import static com.example.speechynew.connectDB.Wordinterface.TABLE_NAME3;
import static com.example.speechynew.connectDB.Wordinterface.WORDD;
import static com.example.speechynew.connectDB.Wordinterface.YEAR;

public class CheckEnvi {

    ArrayList<String> anyword;
    String[] langofword;
    int counttime;
    Context context;

    int engcount;
    int notengcount;

    //database
    Engword eng;
    Word anothereng;
    Timeprocess time;
    Setting setting;


    CheckEnvi(ArrayList anyword, String[] langofword, int counttime, Context context){

        this.anyword = new ArrayList<>();
        this.anyword = anyword;
        this.langofword = new String[anyword.size()];
        this.langofword = langofword;
        this.counttime = counttime;
        this.context = context;

        eng = new Engword(context);
        anothereng = new Word(context);
        time = new Timeprocess(context);
        setting = new Setting(context);

        for(int i=0;i<langofword.length;++i){
            if(langofword[i].equals("en")){
                engcount++; //for eng word
            }else{
                notengcount++; //for other word
            }
        }

        //call method
        checking();

    }


    void checking(){

        double percentageset = 0;
        //double engpercentage = ((double) engcount/ (double) langofword.length)*100;
        double nonepercentage = ((double) notengcount/(double) langofword.length)*100;

        //percentage from setting
        SQLiteDatabase dbsetting = setting.getWritableDatabase();
        Cursor ressetting = dbsetting.rawQuery("select * from "+TABLE_NAME0,null);

        if(ressetting.getCount()==0){ return; }

        while(ressetting.moveToNext()){
            percentageset = ressetting.getDouble(2);
        }

        //check for environment eng
        if(nonepercentage <= percentageset){
            //Toast.makeText(context ,"You are in English speaking." , Toast.LENGTH_SHORT).show();
            System.out.println("You are in English speaking.");
            savedataofeng();
        }else if(nonepercentage > percentageset) {
            //Toast.makeText(context ,"You are 'Not' in English speaking." , Toast.LENGTH_SHORT).show();
            System.out.println("You are 'Not' in English speaking.");
            savedataofnoteng();
        }

        //continue and wrong word
        Continueword con = new Continueword(anyword,langofword,context);
        Wrongwordcheck wrong = new Wrongwordcheck(anyword,langofword,context);

    }


    //save any word to eng word database (word count)
    public void savedataofeng(){

        SQLiteDatabase db = eng.getWritableDatabase();
        ContentValues valueseng = new ContentValues();
        valueseng.put(WORDENG, anyword.size());
        valueseng.put(DAYENG, new Date().getDay());//start at 0 is Sunday
        valueseng.put(DATEENG, new Date().getDate());
        valueseng.put(MONTHENG, new Date().getMonth() + 1); //start at 0
        valueseng.put(YEARENG, new Date().getYear() + 1900); //format is This year - 1900
        valueseng.put(HOURENG, new Date().getHours());
        valueseng.put(MINUTEENG, new Date().getMinutes());
        valueseng.put(SECONDENG, new Date().getSeconds());
        db.insertOrThrow(TABLE_NAME2, null, valueseng);


        //save counttime in db
        SQLiteDatabase dbtime = time.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Timeprocessinterface.DAY, new Date().getDay());//start at 0 is Sunday
        values.put(Timeprocessinterface.DATE, new Date().getDate());
        values.put(Timeprocessinterface.MONTH, new Date().getMonth() + 1); //start at 0
        values.put(Timeprocessinterface.YEAR, new Date().getYear() + 1900); //format is This year - 1900
        values.put(Timeprocessinterface.HOUR, new Date().getHours());
        values.put(Timeprocessinterface.MINUTE, new Date().getMinutes());
        values.put(Timeprocessinterface.SECOND, new Date().getSeconds());
        values.put(TOTAL,counttime);
        dbtime.insertOrThrow(TABLE_NAME5, null, values);

        System.out.println("Time is : "+counttime);

    }

    //save any word to Word database
    public void savedataofnoteng(){

        SQLiteDatabase db = anothereng.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(WORDD, anyword.size());
        values.put(DAY, new Date().getDay());//start at 0 is Sunday
        values.put(DATE, new Date().getDate());
        values.put(MONTH, new Date().getMonth() + 1); //start at 0
        values.put(YEAR, new Date().getYear() + 1900); //format is This year - 1900
        values.put(HOUR, new Date().getHours());
        values.put(MINUTE, new Date().getMinutes());
        values.put(SECOND, new Date().getSeconds());
        db.insertOrThrow(TABLE_NAME3, null, values);

    }

}
