package com.example.speechynew;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;

import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.speechynew.agent.Myservice;
import com.example.speechynew.connectDB.Engword;
import com.example.speechynew.connectDB.Setting;
import com.example.speechynew.connectDB.Status;

import java.util.Date;

import static com.example.speechynew.connectDB.Engwordinterface.TABLE_NAME2;
import static com.example.speechynew.connectDB.Settinginterface.TABLE_NAME0;
import static com.example.speechynew.connectDB.Statusinterface.TABLE_NAME7;

public class MainActivity extends AppCompatActivity {

    int totalchallenge;
    int totaleng;

    Button settime;
    Button checkactivity;
    Button addschedule;

    Switch switchactive;


    ImageButton editer;
    ImageView voice;

    ImageButton notichallenge;
    TextView slideonoff;

    Setting setting;
    Status statusdb;
    Engword eng;

    AnimationDrawable speak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        System.out.println("onCreateMain");
    }

    public void click(View v){
        int id = v.getId();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    public void onResume(){
        super.onResume();
        //for check stay MainActivity
        PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("isActive", true).commit();
        PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("chaActive", true).commit();


        settime = findViewById(R.id.settime);
        checkactivity = findViewById(R.id.checkactivity);
        switchactive = findViewById(R.id.swicthactive);
        editer = findViewById(R.id.setting);
        addschedule = findViewById(R.id.addschedule);
        notichallenge = findViewById(R.id.notichallenge);
        slideonoff = findViewById(R.id.slideonoff);


        voice = findViewById(R.id.voice);
        voice.setBackgroundResource(R.drawable.animation);
        speak = (AnimationDrawable) voice.getBackground();

        setting = new Setting(this);
        statusdb = new Status(this);
        eng = new Engword(this);

        //get nativelang and challenge
        totaleng = 0;
        totalchallenge = 0;

        SQLiteDatabase db = setting.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME0, null);
        int countres = res.getCount();

        if(countres==0){
            //check for setting is null
            Activitysetlang();
            return;
        }
        while (res.moveToNext()){
            totalchallenge = res.getInt(3);
        }

        //use runnable check when open Mainactivity first
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                getchallenge();

                if(totaleng >= totalchallenge && totalchallenge != 0){

                    notichallenge.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_tracktalkchallenge));

                    notichallenge.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            System.out.println("You got challenge");

                            final Dialog dialog = new Dialog(MainActivity.this);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(R.layout.challengedialog);
                            dialog.setCancelable(true);

                            Button btngotchallenge = dialog.findViewById(R.id.btngotchallenge);
                            TextView showcha = dialog.findViewById(R.id.showgotchallenge);


                            btngotchallenge.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.cancel();

                                }
                            });

                            showcha.setText(totaleng+" / "+totalchallenge);

                            dialog.show();

                        }
                    });

                }

            }
        };handler.postDelayed(runnable,1000);

        //Check status
        SQLiteDatabase dbstatus = statusdb.getWritableDatabase();
        Cursor resstatus = dbstatus.rawQuery("select * from " + TABLE_NAME7, null);

        if(resstatus.getCount()==0){
            SQLiteDatabase db3 = statusdb.getWritableDatabase();
            ContentValues value3 = new ContentValues();
            value3.put("status","Inactive");
            db3.insertOrThrow(TABLE_NAME7,null,value3);

        }else{

            String sta =" ";

            while(resstatus.moveToNext()){
                sta = resstatus.getString(1);
            }

            if(sta.equals("Inactive")){
                switchactive.setChecked(false);
                speak.stop();
                voice.setBackground(Drawable.createFromPath("@drawable/voicestop"));
                slideonoff.setText("Slide to open tracking mode");

            }else if(sta.equals("Active")){
                switchactive.setChecked(true);
                voice.setBackgroundResource(R.drawable.animation);
                speak = (AnimationDrawable) voice.getBackground();
                speak.start();
                slideonoff.setText("Slide to close tracking mode");
            }

        }

        switchactive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    statusdb.update("Active");
                    editer.setEnabled(false);
                    slideonoff.setText("Slide to close tracking mode");

                    startService(new Intent(MainActivity.this, Myservice.class));
                    onWindowFocusChangedon();

                } else {
                    // The toggle is disabled
                    statusdb.update("Inactive");
                    editer.setEnabled(true);
                    slideonoff.setText("Slide to open tracking mode");

                    stopService(new Intent(MainActivity.this, Myservice.class));
                    onWindowFocusChangedoff();

                }
            }

            private void onWindowFocusChangedon() {
                voice.setBackgroundResource(R.drawable.animation);
                speak = (AnimationDrawable) voice.getBackground();
                speak.start();
            }

            private void onWindowFocusChangedoff() {
                speak.stop();
                voice.setBackground(Drawable.createFromPath("@drawable/voicestop"));
            }

        });


        editer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activitysetlang();
            }
        });

        addschedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MainActivity.this,Listscheduler.class);
                startActivity(in);
            }
        });


        settime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent see = new Intent(MainActivity.this,SearchActivity.class);
                startActivity(see);
            }
        });

        checkactivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityViewReport();
            }
        });

        System.out.println("onResumeMain");



    }


    @Override
    public void onPause(){
        super.onPause();
        PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("isActive", false).commit();
        PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("chaActive", false).commit();
        System.out.println("onPauseMain");
    }



    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }


    public void onStop(){
        super.onStop();
        //PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("isActive", false).commit();
        System.out.println("onStopMain");
    }


    public void onRestart(){
        super.onRestart();

        System.out.println("onRestartMain");

    }

    public void onDestroy(){
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("isActive", false).commit();
        PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("chaActive", false).commit();
        System.out.println("onDestroyMain");
    }

    public void onBackPressed() {
        //super.onBackPressed();
        System.out.println("onBackPressedMain");
        moveTaskToBack(true);
    }


    void getchallenge(){

        //get engword for compare between engword and challenge
        SQLiteDatabase dbeng = eng.getWritableDatabase();
        Cursor reseng = dbeng.rawQuery("select * from "+TABLE_NAME2+" where date = "+new Date().getDate()+" and month = "+
                (new Date().getMonth()+1)+" and year = "+(new Date().getYear()+1900),null);

        int countreseng = reseng.getCount();

        if(countreseng==0){
            return;
        }

        while (reseng.moveToNext()){
            totaleng += reseng.getInt(1);
        }

        System.out.println("Cha : "+totalchallenge);
        System.out.println("Eng : "+totaleng);

    }


    //Go to setting lang page
    public void Activitysetlang(){
        Intent setlang = new Intent(MainActivity.this,SettinglangActivity.class);
        startActivity(setlang);

    }

    //Go to report page
    public void ActivityViewReport(){
        Intent viewreport = new Intent(MainActivity.this,ViewReportActivity.class);
        startActivity(viewreport);

    }


}
