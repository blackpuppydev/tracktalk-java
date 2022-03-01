package com.example.speechynew.agent;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;

import androidx.core.app.NotificationCompat;

import com.example.speechynew.Listscheduler;
import com.example.speechynew.MainActivity;
import com.example.speechynew.R;
import com.example.speechynew.connectDB.Scheduler;
import com.example.speechynew.connectDB.Status;

import java.util.Calendar;
import java.util.Date;

import static android.content.Context.ALARM_SERVICE;
import static com.example.speechynew.connectDB.Schedulerinterface.TABLE_NAME9;
import static com.example.speechynew.connectDB.Statusinterface.TABLE_NAME7;

public class MyReceiver extends BroadcastReceiver {

    //Database
    Status status;
    Scheduler scheduler;

    //check status
    String checkst = "";
    boolean c = false;

    String id;
    String timehour;
    String timeminute;

    Calendar calstart;
    Calendar calstop;


    @Override
    public void onReceive(Context context, Intent intent) {


        id = intent.getStringExtra("id");
        timehour = intent.getStringExtra("timehour");
        timeminute = intent.getStringExtra("timeminute");

        System.out.println("hour : "+timehour+" minute : "+timeminute);

        calstart = Calendar.getInstance();
        calstart.setTimeInMillis(System.currentTimeMillis());
        calstop = Calendar.getInstance();
        calstop.setTimeInMillis(System.currentTimeMillis());

        //create object db
        status = new Status(context);
        scheduler = new Scheduler(context);

        //check scheduler for repeat
        SQLiteDatabase dbschedulercheck = scheduler.getWritableDatabase();
        Cursor resschedulercheck = dbschedulercheck.rawQuery("select * from "+TABLE_NAME9+" where _id = "+id,null);

        int starthour = 0;
        int startminute = 0;
        int stophour = 0;
        int stopminute = 0;

        while (resschedulercheck.moveToNext()){
            starthour = resschedulercheck.getInt(5);
            startminute = resschedulercheck.getInt(6);
            stophour = resschedulercheck.getInt(7);
            stopminute = resschedulercheck.getInt(8);
        }

        if(Integer.parseInt(timehour)==starthour && Integer.parseInt(timeminute)==startminute){ //start time
            //Override
            calstart.set(Calendar.HOUR_OF_DAY,Integer.parseInt(timehour));
            calstart.set(Calendar.MINUTE,Integer.parseInt(timeminute));
            calstart.set(Calendar.SECOND,0);
            calstart.add(Calendar.DATE,+1);
            Intent intentstart = new Intent(context, MyReceiver.class);
            intentstart.putExtra("id",id);
            intentstart.putExtra("timehour",timehour);
            intentstart.putExtra("timeminute",timeminute);
            PendingIntent pi = PendingIntent.getBroadcast(context,((Integer.parseInt(id))*2)-1,
                    intentstart,PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,calstart.getTimeInMillis(),pi);

        }else if(Integer.parseInt(timehour)==stophour && Integer.parseInt(timeminute)==stopminute){ //stop time
            //Override
            calstop.set(Calendar.HOUR_OF_DAY,Integer.parseInt(timehour));
            calstop.set(Calendar.MINUTE,Integer.parseInt(timeminute));
            calstop.set(Calendar.SECOND,0);
            calstop.add(Calendar.DATE,+1);
            Intent intentstop = new Intent(context, MyReceiver.class);
            intentstop.putExtra("id",id);
            intentstop.putExtra("timehour",timehour);
            intentstop.putExtra("timeminute",timeminute);
            PendingIntent pi1 = PendingIntent.getBroadcast(context,(Integer.parseInt(id))*2,
                    intentstop,PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager1 = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            alarmManager1.setExact(AlarmManager.RTC_WAKEUP,calstop.getTimeInMillis(),pi1);

        }


        //check status database
        SQLiteDatabase dbstatus = status.getWritableDatabase();
        Cursor resdbstatus = dbstatus.rawQuery("select * from "+TABLE_NAME7,null);

        while (resdbstatus.moveToNext()){
            checkst = resdbstatus.getString(1);
        }

        if(checkst.equals("Inactive")){ //when speech recognition stop

            int stopho = -1;
            int stopmi = -1;

            SQLiteDatabase dbscheduler = scheduler.getWritableDatabase();
            Cursor resdbscheduler = dbscheduler.rawQuery("select * from "+TABLE_NAME9+" where stophour = "+new Date().getHours()+" and stopminute = "+new Date().getMinutes() ,null);

            while(resdbscheduler.moveToNext()){
                stopho = resdbscheduler.getInt(7);
                stopmi = resdbscheduler.getInt(8);
            }

            if(stopho != -1 && stopmi != -1){ //if stop time scheduler
                //change to inactive
                //scheduler.update(Integer.parseInt(id),new Date().getDay(),new Date().getDate(),new Date().getMonth()+1,new Date().getYear()+1900,"Inactive");
                scheduler.updatestatus(Integer.parseInt(id),"Active");
            }else{
                //start scheduler
                status.update("Active");
                c = true;
                context.startService(new Intent(context,Myservice.class));
                notificationcall(context);
            }

        }else if(checkst.equals("Active")){ //when speech recognition running

            int startho = -1;
            int startmi = -1;

            SQLiteDatabase dbscheduler = scheduler.getWritableDatabase();
            Cursor resdbscheduler = dbscheduler.rawQuery("select * from "+TABLE_NAME9+" where starthour = "+new Date().getHours()+" and startminute = "+new Date().getMinutes() ,null);

            while (resdbscheduler.moveToNext()){
                startho = resdbscheduler.getInt(5);
                startmi = resdbscheduler.getInt(6);
            }

            if(startho != -1 && startmi != -1){ //if start time scheduler
                //nothing

            }else{
                //change to Inactive all
                status.update("Inactive");
                //scheduler.update(Integer.parseInt(id),new Date().getDay(),new Date().getDate(),new Date().getMonth()+1,new Date().getYear()+1900,"Inactive");
                scheduler.updatestatus(Integer.parseInt(id),"Active");
                c = false;
                context.stopService(new Intent(context,Myservice.class));
                notificationcall(context);

                if(PreferenceManager.getDefaultSharedPreferences(context).getBoolean("ListActive", true)){
                    Intent intentlist = new Intent(context, Listscheduler.class);
                    intentlist.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    context.startActivity(intentlist);

                    System.out.println("Open Listscheduler");
                }
            }
        }

        //check when open MainActivity
        if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean("isActive", true)) {//open MainActivity
            Intent intent1 = new Intent(context,MainActivity.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            context.startActivity(intent1);
            System.out.println("Open MainActivity");
        }else{//not open
            System.out.println("Not open MainActivity");
        }


    }

    public void notificationcall(Context con) {

        String contenttitle = "";
        String contenttext = "";

        if(c ==true){ //when speech recognition start
            contenttitle = "Tracktalk started!";
            contenttext = "You can speak english now. Tracktalk is detecting";
        }else if(c == false){ //when speech recognition stop
            contenttitle = "Tracktalk stopped!";
            contenttext = "You can check your activity";
        }

        // Builds notification
        Context context = con.getApplicationContext();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(contenttitle)
                .setContentText(contenttext);

        // Creates the intent needed to show the notification
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 00, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);                        //request code : 00
        builder.setAutoCancel(true);

        //set sound
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(uri);

        //vibrate
        long[] vibrate = {0,1000};
        builder.setVibrate(vibrate);

        // Add as notification
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(00, builder.build());

    }

}
