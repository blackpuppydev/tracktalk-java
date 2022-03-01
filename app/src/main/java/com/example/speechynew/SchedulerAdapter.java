package com.example.speechynew;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.speechynew.agent.MyReceiver;
import com.example.speechynew.connectDB.Scheduler;
import com.example.speechynew.connectDB.Status;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.ALARM_SERVICE;

public class SchedulerAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<String> listid; ArrayList<String> listday;
    ArrayList<String> listdate; ArrayList<String> listmonth;
    ArrayList<String> listyear; ArrayList<String> liststarthour;
    ArrayList<String> liststartminute; ArrayList<String> liststophour;
    ArrayList<String> liststopminute; ArrayList<String> liststatus;

    TextView viewdatetime;
    Switch repeatstatus;
    ImageButton btndelete;
    ImageButton btnreport;

    String daycheck = "";
    String monthcheck = "";
    int countall = 0;

    Scheduler scheduler;
    Status status;

    Calendar calendar;
    int getdaycalendar;
    int getdatecalendar;
    int getmonthcalendar;
    int getyearcalendar;


    Calendar calcurrent;
    Calendar calstart;
    Calendar calstop;

    SchedulerAdapter(Context applicationContext,ArrayList<String> listid, ArrayList<String> listday, ArrayList<String> listdate,
                     ArrayList<String> listmonth, ArrayList<String> listyear, ArrayList<String> liststarthour, ArrayList<String> liststartminute,
                     ArrayList<String> liststophour, ArrayList<String> liststopminute,ArrayList<String> liststatus) {

        this.listid = new ArrayList<>(); this.listday = new ArrayList<>();
        this.listdate = new ArrayList<>(); this.listmonth = new ArrayList<>();
        this.listyear = new ArrayList<>(); this.liststarthour = new ArrayList<>();
        this.liststartminute = new ArrayList<>(); this.liststophour = new ArrayList<>();
        this.liststopminute = new ArrayList<>(); this.liststatus = new ArrayList<>();

        mContext = applicationContext;
        this.listid = listid; this.listday = listday;
        this.listdate = listdate; this.listmonth = listmonth;
        this.listyear = listyear; this.liststarthour = liststarthour;
        this.liststartminute = liststartminute; this.liststophour = liststophour;
        this.liststopminute = liststopminute; this.liststatus = liststatus;

        scheduler = new Scheduler(mContext);
        status = new Status(mContext);

        calendar = Calendar.getInstance();
        getdaycalendar = calendar.getTime().getDay();
        getdatecalendar = calendar.getTime().getDate();
        getmonthcalendar = calendar.getTime().getMonth();
        getyearcalendar = calendar.getTime().getYear();

        //for schedule start time and stop time
        calstart = Calendar.getInstance();
        calstart.setTimeInMillis(System.currentTimeMillis());

        calstop = Calendar.getInstance();
        calstop.setTimeInMillis(System.currentTimeMillis());

        calcurrent = Calendar.getInstance();
        calcurrent.setTimeInMillis(System.currentTimeMillis());


    }

    @Override
    public int getCount() {
        return listid.size();
    }

    @Override
    public Object getItem(int position) {
        return listid.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.listview_scheduler, parent, false);
        }

        viewdatetime = convertView.findViewById(R.id.showdatetime);
        repeatstatus = convertView.findViewById(R.id.swicthstatus);
        btndelete = convertView.findViewById(R.id.btndelete);
        btnreport = convertView.findViewById(R.id.btnreport);

        //for schedule start time and stop time
        calstart = Calendar.getInstance();
        calstart.setTimeInMillis(System.currentTimeMillis());

        calstop = Calendar.getInstance();
        calstop.setTimeInMillis(System.currentTimeMillis());

        calcurrent = Calendar.getInstance();
        calcurrent.setTimeInMillis(System.currentTimeMillis());

        String thisday = "";
        //check start time < 10
        if(Integer.parseInt(liststarthour.get(position))<10){
            if(Integer.parseInt(liststartminute.get(position))<10){
                thisday = "0"+liststarthour.get(position)+":0"+liststartminute.get(position);
            }else{
                thisday = "0"+liststarthour.get(position)+":"+liststartminute.get(position);
            }
        }else{
            if(Integer.parseInt(liststartminute.get(position))<10){
                thisday = liststarthour.get(position)+":0"+liststartminute.get(position);
            }else{
                thisday = liststarthour.get(position)+":"+liststartminute.get(position);
            }
        }
        //check stop time < 10
        if(Integer.parseInt(liststophour.get(position))<10){
            if(Integer.parseInt(liststopminute.get(position))<10){
                thisday += "  -  0"+liststophour.get(position)+":0"+liststopminute.get(position);
            }else{
                thisday += "  -  0"+liststophour.get(position)+":"+liststopminute.get(position);
            }
        }else{
            if(Integer.parseInt(liststopminute.get(position))<10){
                thisday += "  -  "+liststophour.get(position)+":0"+liststopminute.get(position);
            }else{
                thisday += "  -  "+liststophour.get(position)+":"+liststopminute.get(position);
            }
        }

        //showtime
        viewdatetime.setText("   "+thisday);

        //change from int to String
        checkday(Integer.parseInt(listday.get(position)));
        checkmonth(Integer.parseInt(listmonth.get(position)));

        //button delete
        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //delete data in database
                scheduler.delete(Integer.parseInt(listid.get(position)));

                //cancel scheduler start time created
                Intent intent = new Intent(mContext, MyReceiver.class);
                intent.putExtra("id",listid.get(position));
                PendingIntent pi = PendingIntent.getBroadcast(mContext,((Integer.parseInt(listid.get(position)))*2)-1,
                        intent,PendingIntent.FLAG_UPDATE_CURRENT); //override
                AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(ALARM_SERVICE);
                alarmManager.cancel(pi); //cancel

                //cancel scheduler stop time created
                Intent intent1 = new Intent(mContext, MyReceiver.class);
                intent1.putExtra("id",listid.get(position));
                PendingIntent pi1 = PendingIntent.getBroadcast(mContext,((Integer.parseInt(listid.get(position)))*2),
                        intent1,PendingIntent.FLAG_UPDATE_CURRENT); //override
                AlarmManager alarmManager1 = (AlarmManager) mContext.getSystemService(ALARM_SERVICE);
                alarmManager1.cancel(pi1); //cancel


                //refresh page
                Intent in = new Intent(mContext,Listscheduler.class);
                in.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                mContext.startActivity(in);


            }
        });

        //button view report
        btnreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent lv = new Intent(mContext,Viewscheduler.class);
                lv.putExtra("id",listid.get(position));
                lv.putExtra("day",listday.get(position));
                lv.putExtra("date",listdate.get(position));
                lv.putExtra("month",listmonth.get(position));
                lv.putExtra("year",listyear.get(position));
                lv.putExtra("starthour",liststarthour.get(position));
                lv.putExtra("startminute",liststartminute.get(position));
                lv.putExtra("stophour",liststophour.get(position));
                lv.putExtra("stopminute",liststopminute.get(position));
                lv.putExtra("status",liststatus.get(position));
                mContext.startActivity(lv);
            }
        });

        //switch status
        repeatstatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){ //switch on

                    //working
                    if(countall>(getCount()*4)){

                        int time = ((Integer.parseInt(liststarthour.get(position))*3600)+
                                    (Integer.parseInt(liststartminute.get(position))*60))-
                                    ((new Date().getHours()*3600)+(new Date().getMinutes()*60)+new Date().getSeconds());

                        if(time<=0){ //now > set

                            time = 86400+(((Integer.parseInt(liststarthour.get(position))*3600)+
                                           (Integer.parseInt(liststartminute.get(position))*60))-
                                           ((new Date().getHours()*3600)+(new Date().getMinutes()*60)+new Date().getSeconds()));

                            /*
                            calendar.add(Calendar.DATE,+1);
                            getdaycalendar = calendar.getTime().getDay();
                            getdatecalendar = calendar.getTime().getDate();
                            getmonthcalendar = calendar.getTime().getMonth();
                            getyearcalendar = calendar.getTime().getYear();

                            //change to active
                            scheduler.update(Integer.parseInt(listid.get(position)),getdaycalendar,getdatecalendar,
                                             getmonthcalendar+1,getyearcalendar+1900,"Active");*/

                            scheduler.updatestatus(Integer.parseInt(listid.get(position)),"Active");

                            checkday(getdaycalendar);
                            checkmonth(getmonthcalendar+1);

                        }else{

                            time = (((Integer.parseInt(liststarthour.get(position))*3600)+
                                     (Integer.parseInt(liststartminute.get(position))*60))-
                                     ((new Date().getHours()*3600)+(new Date().getMinutes()*60)+new Date().getSeconds()));

                            /*
                            //change to active
                            scheduler.update(Integer.parseInt(listid.get(position)),getdaycalendar,getdatecalendar,
                                             getmonthcalendar+1,getyearcalendar+1900,"Active");*/

                            scheduler.updatestatus(Integer.parseInt(listid.get(position)),"Active");

                        }

                        //process time //value (time) use for show time from now
                        int numberOfHours = (time % 86400) / 3600;
                        int numberOfMinutes = ((time % 86400) % 3600) / 60;
                        //show pop up
                        Toast.makeText(mContext,"Tracktalk set for "+numberOfHours+" hour and "+
                                       numberOfMinutes+" minute from now",Toast.LENGTH_LONG).show();

                        //reset calendar (when +1 from now to set now)
                        resetCal();

                        //refresh page
                        Intent intest = new Intent(mContext, Listscheduler.class);
                        intest.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        mContext.startActivity(intest);

                    }



                }else if(!isChecked){ //switch off

                    if(countall>(getCount()*4)){

                        //show pop up
                        Toast.makeText(mContext,"stop this scheduler",Toast.LENGTH_LONG).show();

                        int time = ((Integer.parseInt(liststarthour.get(position))*3600)+
                                    (Integer.parseInt(liststartminute.get(position))*60))-
                                    ((new Date().getHours()*3600)+(new Date().getMinutes()*60)+new Date().getSeconds());

                        if(time<=0){

                            /*
                            calendar.add(Calendar.DATE,+1);
                            getdaycalendar = calendar.getTime().getDay();
                            getdatecalendar = calendar.getTime().getDate();
                            getmonthcalendar = calendar.getTime().getMonth();
                            getyearcalendar = calendar.getTime().getYear();

                            //change to inactive
                            scheduler.update(Integer.parseInt(listid.get(position)),getdaycalendar,getdatecalendar,
                                             getmonthcalendar+1,getyearcalendar+1900,"Inactive");*/

                            scheduler.updatestatus(Integer.parseInt(listid.get(position)),"Inactive");

                            checkday(getdaycalendar);
                            checkmonth(getmonthcalendar+1);

                        }else{

                            /*
                            //change to inactive
                            scheduler.update(Integer.parseInt(listid.get(position)),getdaycalendar,getdatecalendar,
                                             getmonthcalendar+1,getyearcalendar+1900,"Inactive");*/

                            scheduler.updatestatus(Integer.parseInt(listid.get(position)),"Inactive");

                        }

                        //reset calendar (when +1 from now to set now)
                        resetCal();

                        //refresh page
                        Intent intest = new Intent(mContext, Listscheduler.class);
                        intest.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        mContext.startActivity(intest);
                    }
                }
            }
        });

        //check for open this page will check status
        checking(position);

        return convertView;

    }


    void checking(int position){

        countall++;

        if(liststatus.get(position).toString().equals("Active")){
            repeatstatus.setChecked(true);

            if(countall<=getCount()){
                System.out.println(liststatus.get(position)+" : Active");
            }

        }else if(liststatus.get(position).toString().equals("Inactive")){
            repeatstatus.setChecked(false);

            if(countall<=getCount()){
                System.out.println(liststatus.get(position)+" : Inactive");
            }

        }


        //because when open this page app running to check every scheduler
        if(countall==(getCount()*4)){
            countall++;
        }

        //set start time
        alarmerstart(position);

    }

    /**
     * requestCode
     *
     * n -> id
     * n = (n*2)-1,n*2
     *
     * ex ->  id = 1     id = 2      id = 3
     *           = 1,2      = 3,4       = 5,6
     *
     */

    void alarmerstart(int position){

        calstart.set(Calendar.HOUR_OF_DAY,Integer.parseInt(liststarthour.get(position)));
        calstart.set(Calendar.MINUTE,Integer.parseInt(liststartminute.get(position)));
        calstart.set(Calendar.SECOND,0);

        /*calstart.set(Integer.parseInt(listyear.get(position)),
                    Integer.parseInt(listmonth.get(position))-1,
                           Integer.parseInt(listdate.get(position)),
                           Integer.parseInt(liststarthour.get(position)),
                           Integer.parseInt(liststartminute.get(position)),0);*/

        Intent intent = new Intent(mContext, MyReceiver.class);
        intent.putExtra("id",listid.get(position));
        intent.putExtra("timehour",liststarthour.get(position));
        intent.putExtra("timeminute",liststartminute.get(position));

        PendingIntent pi = PendingIntent.getBroadcast(mContext,((Integer.parseInt(listid.get(position)))*2)-1,
                                                      intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(ALARM_SERVICE);

        if(liststatus.get(position).equals("Active")){   //Active

            long curcal = calcurrent.getTimeInMillis();
            long startcal = calstart.getTimeInMillis();

            if(curcal>startcal){
                calstart.add(Calendar.DATE,+1);
            }

            alarmManager.setExact(AlarmManager.RTC_WAKEUP,calstart.getTimeInMillis(),pi);

        }else{ //Inactive
            alarmManager.cancel(pi);
        }

        //set stop time
        alarmerstop(position);

    }

    void alarmerstop(int position){

        calstop.set(Calendar.HOUR_OF_DAY,Integer.parseInt(liststophour.get(position)));
        calstop.set(Calendar.MINUTE,Integer.parseInt(liststopminute.get(position)));
        calstop.set(Calendar.SECOND,0);

        /*calstop.set(Integer.parseInt(listyear.get(position)),
                    Integer.parseInt(listmonth.get(position))-1,
                    Integer.parseInt(listdate.get(position)),
                    Integer.parseInt(liststophour.get(position)),
                    Integer.parseInt(liststopminute.get(position)),0);*/

        Intent intent1 = new Intent(mContext, MyReceiver.class);
        intent1.putExtra("id",listid.get(position));
        intent1.putExtra("timehour",liststophour.get(position));
        intent1.putExtra("timeminute",liststopminute.get(position));
        PendingIntent pi1 = PendingIntent.getBroadcast(mContext,(Integer.parseInt(listid.get(position)))*2,
                                                       intent1,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager1 = (AlarmManager) mContext.getSystemService(ALARM_SERVICE);

        if(liststatus.get(position).equals("Active")){  //Active

            long curcal = calcurrent.getTimeInMillis();
            long stopcal = calstop.getTimeInMillis();

            if(curcal>stopcal){
                calstop.add(Calendar.DATE,+1);
            }

            alarmManager1.setExact(AlarmManager.RTC_WAKEUP,calstop.getTimeInMillis(),pi1);

        }else{ //Inactive
            alarmManager1.cancel(pi1);
        }

    }


    void checkday(int day){
        if(day==0){ daycheck = "Sunday"; }
        else if(day==1){ daycheck = "Monday"; }
        else if(day==2){ daycheck = "Tuesday"; }
        else if(day==3){ daycheck = "Wednesday"; }
        else if(day==4){ daycheck = "Thursday"; }
        else if(day==5){ daycheck = "Friday"; }
        else if(day==6){ daycheck = "Saturday"; }
    }

    void checkmonth(int month){
        if(month==1){ monthcheck = "January"; }
        else if(month==2){ monthcheck = "February"; }
        else if(month==3){ monthcheck = "March"; }
        else if(month==4){ monthcheck = "April"; }
        else if(month==5){ monthcheck = "May"; }
        else if(month==6){ monthcheck = "June"; }
        else if(month==7){ monthcheck = "July"; }
        else if(month==8){ monthcheck = "August"; }
        else if(month==9){ monthcheck = "September"; }
        else if(month==10){ monthcheck = "October"; }
        else if(month==11){ monthcheck = "November"; }
        else if(month==12){ monthcheck = "December"; }
    }

    void resetCal(){
        calendar = Calendar.getInstance();
        getdaycalendar = calendar.getTime().getDay();
        getdatecalendar = calendar.getTime().getDate();
        getmonthcalendar = calendar.getTime().getMonth();
        getyearcalendar = calendar.getTime().getYear();
    }

}


