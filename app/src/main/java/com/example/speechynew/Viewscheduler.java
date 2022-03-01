package com.example.speechynew;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.speechynew.analysis.Translator;
import com.example.speechynew.connectDB.Continuemax;
import com.example.speechynew.connectDB.Engword;
import com.example.speechynew.connectDB.Timeprocess;
import com.example.speechynew.connectDB.Word;
import com.example.speechynew.connectDB.Wrongword;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static com.example.speechynew.connectDB.Continuemaxinterface.TABLE_NAME10;
import static com.example.speechynew.connectDB.Engwordinterface.TABLE_NAME2;
import static com.example.speechynew.connectDB.Timeprocessinterface.TABLE_NAME5;
import static com.example.speechynew.connectDB.Wordinterface.TABLE_NAME3;
import static com.example.speechynew.connectDB.Wrongwordinterface.TABLE_NAME11;

public class Viewscheduler extends AppCompatActivity {

    TextView date;
    TextView timeshow;
    BarChart mChart;
    Button changetype;
    Button nextpage;
    TextView wait5scheduler;
    ImageButton graphminus;
    ImageButton graphplus;

    Engword eng;
    Word anothereng;
    Timeprocess time;
    Continuemax continuemax;
    Wrongword wrongword;

    String idset;
    String dayset;
    String dateset;
    String monthset;
    String yearset;
    String starthour;
    String startminute;
    String stophour;
    String stopminute;

    int idchange = 0;
    int daychange = 0;
    int datechange = 0;
    int monthchange = 0;
    int yearchange = 0;
    int starthourchange = 0;
    int startminutechange = 0;
    int stophourchange = 0;
    int stopminutechange = 0;

    Calendar c ;
    SimpleDateFormat df;
    String formattedDate;
    int getFormattedDate;
    int getFormattedMonth;
    int getFormattedYear;
    int getFormattednameday;


    int getc_hourminuteHour;
    int getc_hourminuteMinute;

    Calendar currentcal;
    Calendar lastcal;

    Calendar c_hourminute;

    String showdatescheduler;
    String totalwordscheduler;
    String totaltimescheduler;
    int wordscheduler = 0;
    int timescheduler = 0;
    double wordminscheduler;
    int continuemaxscheduler = 0;
    String[] wordtopscheduler = new String[3];
    String[] wordtranscheduler = new String[3];


    boolean resulttype;
    int[] longtime;

    int totalallscheduler = 0;

    String showday = "";
    String showmonth = "";

    String timestartset = "";
    String timestopset = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_viewscheduler);

        date = findViewById(R.id.date);
        timeshow = findViewById(R.id.timeshow);
        mChart = findViewById(R.id.mp_barchart);
        changetype = findViewById(R.id.changetype);
        nextpage = findViewById(R.id.nextpage);
        wait5scheduler = findViewById(R.id.wait5scheduler);
        graphminus = findViewById(R.id.graphminus);
        graphplus = findViewById(R.id.graphplus);

        currentcal = Calendar.getInstance();
        lastcal = Calendar.getInstance();



        //Create object database class
        eng = new Engword(this);
        anothereng = new Word(this);
        time = new Timeprocess(this);
        continuemax = new Continuemax(this);
        wrongword = new Wrongword(this);


        mChart.getDescription().setEnabled(false);
        mChart.setFitBars(true);

        //Data from before page
        idset = getIntent().getStringExtra("id");
        dayset = getIntent().getStringExtra("day");
        dateset = getIntent().getStringExtra("date");
        monthset = getIntent().getStringExtra("month");
        yearset = getIntent().getStringExtra("year");
        starthour = getIntent().getStringExtra("starthour");
        startminute = getIntent().getStringExtra("startminute");
        stophour = getIntent().getStringExtra("stophour");
        stopminute = getIntent().getStringExtra("stopminute");

        //Change data type
        idchange = Integer.parseInt(idset);
        daychange = Integer.parseInt(dayset);
        datechange = Integer.parseInt(dateset);
        monthchange = Integer.parseInt(monthset);
        yearchange = Integer.parseInt(yearset);
        starthourchange = Integer.parseInt(starthour);
        startminutechange = Integer.parseInt(startminute);
        stophourchange = Integer.parseInt(stophour);
        stopminutechange = Integer.parseInt(stopminute);

        c_hourminute = Calendar.getInstance();
        c_hourminute.set(Calendar.HOUR_OF_DAY,starthourchange);
        c_hourminute.set(Calendar.MINUTE,startminutechange);
        getc_hourminuteHour = c_hourminute.getTime().getHours();
        getc_hourminuteMinute = c_hourminute.getTime().getMinutes();

        lastcal.set(Calendar.DATE,datechange);
        lastcal.set(yearchange,monthchange-1,datechange);

        resulttype = false;

        checktimestartstop();

        timeshow.setText(timestartset+" - "+timestopset);


        //start current time
        c = Calendar.getInstance();
        df = new SimpleDateFormat("d-MM-yyyy");
        formattedDate = df.format(c.getTime());
        getFormattednameday = c.getTime().getDay();
        getFormattedDate = c.getTime().getDate();
        getFormattedMonth = c.getTime().getMonth();
        getFormattedYear = c.getTime().getYear();

        checkday();
        checkmonth();

        //check button plus and minus
        long checkcurrentcal = currentcal.getTimeInMillis();
        long checklastcal = lastcal.getTimeInMillis();

        if(checkcurrentcal<checklastcal){
            graphplus.setEnabled(false);
            graphminus.setEnabled(false);
        }

        date.setText(showday+" "+getFormattedDate+" "+showmonth+" "+(getFormattedYear+1900));
        showdatescheduler = showday+" "+getFormattedDate+" "+showmonth+" "+(getFormattedYear+1900)+" "+timestartset+" - "+timestopset;

        //between time
        int longtiming = ((stophourchange*60)+stopminutechange)-((starthourchange*60)+startminutechange);
        longtime = new int[longtiming+1];

        wait5scheduler.setText("Please wait 5 second");

        //call method
        viewscheculer();
        viewtotalwordscheduler();

        //no data
        if(totalallscheduler==0){
            changetype.setVisibility(View.GONE);
            nextpage.setVisibility(View.GONE);
            wait5scheduler.setVisibility(View.GONE);
        }

        nextpageonclick();
        viewtimescheduler();
        wordminschedulerset();
        continuouslyscheduler();
        wrongwordscheduler();

        //button change between word count or percentage
        changetype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //change from word count to Percentage
                if(resulttype==false){
                    resulttype=true;
                    viewscheculer();
                    changetype.setText("Word count");

                    //change from Percentage to word count
                }else if(resulttype==true){
                    resulttype=false;
                    viewscheculer();
                    changetype.setText("Percentage");
                }

            }
        });

        //minus day
        graphminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                c.add(Calendar.DATE, -1);
                formattedDate = df.format(c.getTime());
                getFormattednameday = c.getTime().getDay();
                getFormattedDate = c.getTime().getDate();
                getFormattedMonth = c.getTime().getMonth();
                getFormattedYear = c.getTime().getYear();

                //check first day when add this schedule
                long c_timecal = c.getTimeInMillis();
                long last_timecal = lastcal.getTimeInMillis();

                if (c_timecal<last_timecal){

                    c.add(Calendar.DATE, +1);
                    formattedDate = df.format(c.getTime());
                    getFormattednameday = c.getTime().getDay();
                    getFormattedDate = c.getTime().getDate();
                    getFormattedMonth = c.getTime().getMonth();
                    getFormattedYear = c.getTime().getYear();

                    graphminus.setEnabled(false);
                    graphplus.setEnabled(true);

                }else{

                    checkday();
                    checkmonth();
                    date.setText(showday+" "+getFormattedDate+" "+showmonth+" "+(getFormattedYear+1900));
                    checktimestartstop();
                    showdatescheduler = getFormattedDate+"/"+getFormattedMonth+"/"+getFormattedYear+"  "+timestartset+" - "+timestopset;

                    viewscheculer();
                    viewtotalwordscheduler();

                    if(totalallscheduler==0){ //no data
                        mChart.clear();
                        mChart.invalidate();
                        changetype.setVisibility(View.GONE);
                        nextpage.setVisibility(View.GONE);
                        wait5scheduler.setVisibility(View.GONE);
                    }else{
                        changetype.setVisibility(View.VISIBLE);
                        nextpage.setVisibility(View.VISIBLE);
                        wait5scheduler.setVisibility(View.VISIBLE);
                    }

                    wait5scheduler.setText("Please wait 5 second");

                    nextpageonclick();
                    viewtimescheduler();
                    wordminschedulerset();
                    continuouslyscheduler();
                    wrongwordscheduler();

                    graphplus.setEnabled(true);
                    graphminus.setEnabled(true);
                }

            }
        });

        //plus day
        graphplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                c.add(Calendar.DATE, +1);
                formattedDate = df.format(c.getTime());
                getFormattednameday = c.getTime().getDay();
                getFormattedDate = c.getTime().getDate();
                getFormattedMonth = c.getTime().getMonth();
                getFormattedYear = c.getTime().getYear();

                //check calendar > current time
                long currenttimecal = currentcal.getTimeInMillis();
                long c_timecal = c.getTimeInMillis();

                if(c_timecal>(currenttimecal+10)){

                    c.add(Calendar.DATE, -1);
                    formattedDate = df.format(c.getTime());
                    getFormattednameday = c.getTime().getDay();
                    getFormattedDate = c.getTime().getDate();
                    getFormattedMonth = c.getTime().getMonth();
                    getFormattedYear = c.getTime().getYear();

                    graphplus.setEnabled(false);
                    graphminus.setEnabled(true);

                }else{

                    graphplus.setEnabled(true);
                    graphminus.setEnabled(true);

                    checkday();
                    checkmonth();
                    date.setText(showday+" "+getFormattedDate+" "+showmonth+" "+(getFormattedYear+1900));
                    checktimestartstop();
                    showdatescheduler = getFormattedDate+"/"+getFormattedMonth+"/"+getFormattedYear+"  "+timestartset+" - "+timestopset;

                    viewscheculer();
                    viewtotalwordscheduler();

                    if(totalallscheduler==0){
                        mChart.clear();
                        mChart.invalidate();
                        changetype.setVisibility(View.GONE);
                        nextpage.setVisibility(View.GONE);
                        wait5scheduler.setVisibility(View.GONE);
                    }else{
                        changetype.setVisibility(View.VISIBLE);
                        nextpage.setVisibility(View.VISIBLE);
                        wait5scheduler.setVisibility(View.VISIBLE);
                    }

                    wait5scheduler.setText("Please wait 5 second");

                    nextpageonclick();
                    viewtimescheduler();
                    wordminschedulerset();
                    continuouslyscheduler();
                    wrongwordscheduler();
                }

            }
        });

    }

    void nextpageonclick(){

        nextpage.setEnabled(false);
        nextpage.setBackgroundResource(R.drawable.buttonshape11);

        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                nextpage.setEnabled(true);
                nextpage.setBackgroundResource(R.drawable.buttonshape6);

                wait5scheduler.setText("");

                nextpage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent in = new Intent(Viewscheduler.this, Viewschedulerreport.class);
                        in.putExtra("showdatescheduler",showdatescheduler);
                        in.putExtra("totalwordscheduler",totalwordscheduler);
                        in.putExtra("totaltimescheduler",totaltimescheduler);
                        in.putExtra("wordminscheduler",wordminscheduler);
                        in.putExtra("continuemaxscheduler",continuemaxscheduler);
                        in.putExtra("wordtopscheduler",wordtopscheduler);
                        in.putExtra("wordtranscheduler",wordtranscheduler);
                        startActivity(in);

                    }
                });

            }

        };handler.postDelayed(runnable,4000);

    }



    public void viewscheculer(){

        //when no data
        mChart.setNoDataText("There's no information at this moment.");
        mChart.setNoDataTextColor(Color.RED);


        int longtime = ((stophourchange*60)+stopminutechange)-((starthourchange*60)+startminutechange);
        ArrayList<BarEntry> dataVals;

        if(resulttype==false){

            dataVals = new ArrayList<>();
            int[] arraydataeng = new int[longtime+1];
            int[] arraydatanone = new int[longtime+1];

            SQLiteDatabase dbeng = eng.getWritableDatabase();
            SQLiteDatabase dbnone = anothereng.getWritableDatabase();

            Cursor reseng = dbeng.rawQuery("select * from " + TABLE_NAME2 + " where date = " + getFormattedDate +" and month = " +
                    (getFormattedMonth+1) +" and year = " + (getFormattedYear+1900) + " and (hour*60)+minute >= "+((starthourchange*60)+startminutechange)
                    + " and (hour*60)+minute < " +((stophourchange*60)+stopminutechange), null);

            Cursor resnone = dbnone.rawQuery("select * from " + TABLE_NAME3 + " where date = " + getFormattedDate +" and month = " +
                    (getFormattedMonth+1) +" and year = " + (getFormattedYear+1900) + " and (hour*60)+minute >= "+((starthourchange*60)+startminutechange)
                    + " and (hour*60)+minute < " +((stophourchange*60)+stopminutechange), null);

            int countreseng = reseng.getCount();
            int countresnone = resnone.getCount();

            if(countreseng==0){
                return;
            }

            if(countresnone==0){
                return;
            }

            while (reseng.moveToNext()) {
                arraydataeng[((reseng.getInt(6)*60)+reseng.getInt(7))-((starthourchange*60)+startminutechange)]+=reseng.getInt(1);
            }

            while (resnone.moveToNext()) {
                arraydatanone[((resnone.getInt(6)*60)+resnone.getInt(7))-((starthourchange*60)+startminutechange)]+=resnone.getInt(1);
            }

            for(int i=0;i<longtime;++i){

                int valueseng = arraydataeng[i];
                int valuesno = arraydatanone[i];
                dataVals.add(new BarEntry(i, new float[]{valueseng, valuesno}));
            }

            BarDataSet barDataSet = new BarDataSet(dataVals, " ");
            barDataSet.setColors(Color.parseColor("#FF9933"), Color.parseColor("#8CB9D1"));
            barDataSet.setStackLabels(new String[]{"Number of English words", "Number of non-English words"});

            BarData data = new BarData(barDataSet);
            mChart.setData(data);

            data.setValueFormatter(new Viewscheduler.MyValueFormatter());

            mChart.getLegend().setXEntrySpace(12);//ระยะห่างระหว่างข้อมูล
            mChart.getLegend().setFormToTextSpace(3);//ระยะห่างระหว่างรูปกับคำอธิบาย

            //edit text under graph
            String[] time = new String[longtime];

            for(int i=0;i<longtime;++i){

                String anstime = "";

                if(getc_hourminuteHour<10){
                    if(getc_hourminuteMinute<10){
                        anstime = "0"+getc_hourminuteHour+":0"+getc_hourminuteMinute;
                    }else{
                        anstime = "0"+getc_hourminuteHour+":"+getc_hourminuteMinute;
                    }
                }else{
                    if(getc_hourminuteMinute<10){
                        anstime = getc_hourminuteHour+":0"+getc_hourminuteMinute;
                    }else{
                        anstime = getc_hourminuteHour+":"+getc_hourminuteMinute;
                    }
                }

                time[i] = anstime;
                c_hourminute.add(Calendar.MINUTE, +1);
                getc_hourminuteHour = c_hourminute.getTime().getHours();
                getc_hourminuteMinute = c_hourminute.getTime().getMinutes();

            }

            //reset time
            c_hourminute.add(Calendar.MINUTE, -longtime);
            getc_hourminuteHour = c_hourminute.getTime().getHours();
            getc_hourminuteMinute = c_hourminute.getTime().getMinutes();

            XAxis xAxis = mChart.getXAxis();
            xAxis.setValueFormatter(new IndexAxisValueFormatter(time));
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setGranularity(1);

            xAxis.setCenterAxisLabels(false);
            xAxis.setGranularityEnabled(true);
            xAxis.setAxisMaximum(longtime);
            xAxis.setDrawGridLines(false); //เส้นตาราง

            mChart.setDragEnabled(true);
            mChart.getAxisRight().setAxisMinimum(0);
            mChart.getAxisLeft().setAxisMinimum(0);
            mChart.setVisibleXRangeMaximum(longtime);
            mChart.invalidate();
            mChart.animateXY(2000, 4000);
            mChart.setDoubleTapToZoomEnabled(true);
            mChart.setPinchZoom(true);
            mChart.fitScreen();

        }else if(resulttype==true){

            dataVals = new ArrayList<>();

            double[] arraydataeng = new double[longtime+1];
            double[] arraydatanone = new double[longtime+1];
            double[] arrayall = new double[longtime+1];

            SQLiteDatabase dbeng = eng.getWritableDatabase();
            SQLiteDatabase dbnone = anothereng.getWritableDatabase();

            Cursor reseng = dbeng.rawQuery("select * from " + TABLE_NAME2 + " where date = " + getFormattedDate +" and month = " +
                    (getFormattedMonth+1) +" and year = " + (getFormattedYear+1900) + " and (hour*60)+minute >= "+((starthourchange*60)+startminutechange)
                    + " and (hour*60)+minute < " +((stophourchange*60)+stopminutechange), null);

            Cursor resnone = dbnone.rawQuery("select * from " + TABLE_NAME3 + " where date = " + getFormattedDate +" and month = " +
                    (getFormattedMonth+1) +" and year = " + (getFormattedYear+1900) + " and (hour*60)+minute >= "+((starthourchange*60)+startminutechange)
                    + " and (hour*60)+minute < " +((stophourchange*60)+stopminutechange), null);

            int countreseng = reseng.getCount();
            int countresnone = resnone.getCount();

            if(countreseng==0){
                return;
            }

            if(countresnone==0){
                return;
            }

            while (reseng.moveToNext()) {
                arraydataeng[((reseng.getInt(6)*60)+reseng.getInt(7))-((starthourchange*60)+startminutechange)]+=reseng.getInt(1);
            }

            while (resnone.moveToNext()) {
                arraydatanone[((resnone.getInt(6)*60)+resnone.getInt(7))-((starthourchange*60)+startminutechange)]+=resnone.getInt(1);
            }

            for(int i = 0; i < longtime; ++i){
                arrayall[i] = arraydataeng[i];
            }

            for(int i = 0; i < longtime; ++i){
                arrayall[i] +=arraydatanone[i];
            }

            for(int i=0;i<=longtime-1;++i){
                float valueseng;
                double testvaleng = arraydataeng[i] / arrayall[i];

                if (Double.isNaN(testvaleng)) {
                    valueseng = (float) 0;
                } else {
                    valueseng = (float) testvaleng * 100;
                }

                float valuesno;
                double testvalno = arraydatanone[i] / arrayall[i];

                if (Double.isNaN(testvalno)) {
                    valuesno = (float) 0;
                } else {
                    valuesno = (float) testvalno * 100;
                }

                dataVals.add(new BarEntry(i, new float[]{valueseng,valuesno}));
            }

            BarDataSet barDataSet = new BarDataSet(dataVals, " ");
            barDataSet.setColors(Color.parseColor("#FF9933"), Color.parseColor("#8CB9D1"));
            barDataSet.setStackLabels(new String[]{"Percentage of English words", "Percentage of non-English words"});

            BarData data = new BarData(barDataSet);
            mChart.setData(data);
            data.setValueFormatter(new Viewscheduler.MyValueFormatter());


            mChart.getLegend().setXEntrySpace(12);//ระยะห่างระหว่างข้อมูล
            mChart.getLegend().setFormToTextSpace(3);//ระยะห่างระหว่างรูปกับคำอธิบาย
            String[] time = new String[longtime];


            for(int i=0;i<longtime;++i){

                String anstime = "";

                if(getc_hourminuteHour<10){
                    if(getc_hourminuteMinute<10){
                        anstime = "0"+getc_hourminuteHour+":0"+getc_hourminuteMinute;
                    }else{
                        anstime = "0"+getc_hourminuteHour+":"+getc_hourminuteMinute;
                    }
                }else{
                    if(getc_hourminuteMinute<10){
                        anstime = getc_hourminuteHour+":0"+getc_hourminuteMinute;
                    }else{
                        anstime = getc_hourminuteHour+":"+getc_hourminuteMinute;
                    }
                }

                time[i] = anstime;
                c_hourminute.add(Calendar.MINUTE, +1);
                getc_hourminuteHour = c_hourminute.getTime().getHours();
                getc_hourminuteMinute = c_hourminute.getTime().getMinutes();

            }

            //reset time
            c_hourminute.add(Calendar.MINUTE, -longtime);
            getc_hourminuteHour = c_hourminute.getTime().getHours();
            getc_hourminuteMinute = c_hourminute.getTime().getMinutes();

            XAxis xAxis = mChart.getXAxis();
            xAxis.setValueFormatter(new IndexAxisValueFormatter(time));
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setGranularity(1);
            xAxis.setCenterAxisLabels(false);
            xAxis.setGranularityEnabled(true);
            xAxis.setAxisMaximum(longtime);
            xAxis.setDrawGridLines(false); //เส้นตาราง

            mChart.setDragEnabled(true);
            mChart.getAxisRight().setAxisMinimum(0);
            mChart.getAxisLeft().setAxisMinimum(0);
            mChart.setVisibleXRangeMaximum(longtime);
            mChart.invalidate();
            mChart.animateXY(2000, 4000);
            mChart.setDoubleTapToZoomEnabled(true);
            mChart.setPinchZoom(true);
            mChart.fitScreen();

        }
    }



    public void viewtotalwordscheduler(){

        int totalall = 0;
        int totaleng = 0;
        int totalanother = 0;

        SQLiteDatabase db2 = eng.getWritableDatabase();
        Cursor res2 = db2.rawQuery("select * from " + TABLE_NAME2 + " where date = " + getFormattedDate + " and month = "+ (getFormattedMonth+1) +
                " and year = "+(getFormattedYear+1900)+" and (hour*60)+minute >= " + ((starthourchange*60)+startminutechange) +
                " and (hour*60)+minute < " + (((stophourchange*60)+stopminutechange)), null);

        int countlenghtres2 = res2.getCount();

        if(countlenghtres2==0){ }

        while(res2.moveToNext()){ totaleng+=res2.getInt(1); }

        SQLiteDatabase db3 = anothereng.getWritableDatabase();
        Cursor res3 = db3.rawQuery("select * from " + TABLE_NAME3 + " where date = " + getFormattedDate + " and month = "+ (getFormattedMonth+1) +
                " and year = "+(getFormattedYear+1900)+" and (hour*60)+minute >= " + ((starthourchange*60)+startminutechange) +
                " and (hour*60)+minute < " + (((stophourchange*60)+stopminutechange)), null);

        int countlenghtres3 = res3.getCount();

        if(countlenghtres3==0){ }

        while(res3.moveToNext()){ totalanother+=res3.getInt(1);}

        totalall = totaleng+totalanother;

        totalallscheduler = totalall;

        wordscheduler = totaleng;

        totalwordscheduler = totaleng+ " / "+totalall;

    }


    public void viewtimescheduler(){

        int totaltime = 0;

        SQLiteDatabase dbtime = time.getWritableDatabase();
        Cursor restime = dbtime.rawQuery("select * from " + TABLE_NAME5 + " where date = " + getFormattedDate + " and month = "+ (getFormattedMonth+1) +
                " and year = "+ (getFormattedYear+1900) +" and (hour*60)+minute >= " + ((starthourchange*60)+startminutechange) +
                " and (hour*60)+minute < " + (((stophourchange*60)+stopminutechange)),null);

        int countrestime = restime.getCount();

        if(countrestime==0){
            totaltime=0;
        }

        while(restime.moveToNext()){
            totaltime += restime.getInt(8);
        }

        timescheduler = totaltime;

        int numberOfHours = (totaltime % 86400) / 3600;
        int numberOfMinutes = ((totaltime % 86400) % 3600) / 60;
        int numberOfSeconds = ((totaltime % 86400) % 3600) % 60;

        if(numberOfMinutes<10){
            if(numberOfSeconds<10){
                String text = numberOfHours+" : 0"+numberOfMinutes+" : 0"+numberOfSeconds;
                totaltimescheduler = text;
            }else{
                String text = numberOfHours+" : 0"+numberOfMinutes+" : "+numberOfSeconds;
                totaltimescheduler = text;
            }
        }else{
            totaltimescheduler = numberOfHours + " : " + numberOfMinutes + " : " + numberOfSeconds;
        }

    }


    public void wordminschedulerset(){

        double total = (double) wordscheduler * 1.0;
        double total2 = (double) timescheduler / 60.0;

        if(total2 == 0.0){
            total2 = 1.0;
        }
        wordminscheduler = total/total2;

    }

    public void continuouslyscheduler(){

        int checking = 0;
        continuemaxscheduler = 0;
        SQLiteDatabase dbcon = continuemax.getWritableDatabase();
        Cursor resdbcon = dbcon.rawQuery("select * from "+TABLE_NAME10+ " where date = " + getFormattedDate + " and month = "+ (getFormattedMonth+1) +
                " and year = "+(getFormattedYear+1900)+" and (hour*60)+minute >= " + ((starthourchange*60)+startminutechange) +
                " and (hour*60)+minute < " + (((stophourchange*60)+stopminutechange)),null);

        while (resdbcon.moveToNext()){
            checking = resdbcon.getInt(1);

            if(continuemaxscheduler < checking){
                continuemaxscheduler = checking;
            }

        }

    }


    public void wrongwordscheduler(){

        ArrayList<String> wordfromdb = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            wordtopscheduler[i] = "";
        }

        for(int i=0;i<3;++i){
            wordtranscheduler[i] = "";
        }

        SQLiteDatabase dbwrong = wrongword.getWritableDatabase();
        Cursor resdbwrong = dbwrong.rawQuery("select * from "+TABLE_NAME11+ " where date = " + getFormattedDate + " and month = "+ (getFormattedMonth+1) +
                " and year = "+(getFormattedYear+1900)+" and (hour*60)+minute >= " + ((starthourchange*60)+startminutechange) +
                " and (hour*60)+minute < " + (((stophourchange*60)+stopminutechange)),null);

        int countresdbwrong = resdbwrong.getCount();

        if(countresdbwrong==0){ return; }

        while (resdbwrong.moveToNext()){
            wordfromdb.add(resdbwrong.getString(1));
        }

        //wordcount
        int N = wordfromdb.size();
        String word[] = new String[N];
        int count[] = new int[N];

        for (int i = 0; i < word.length; i++) {
            word[i] = "";
        }

        for (int i = 0; i < N; i++) {
            String text = wordfromdb.get(i);
            for (int j = 0; j < word.length; j++) {
                if (word[j].equals("")) {
                    word[j] = text;
                    count[j] = 1;
                    break;
                } else if (word[j].equals(text)) {
                    count[j]++;
                    break;
                }
            }
        }

        for (int i = 0; i < N; i++) {
            for (int j = i+1; j < N-1; j++) {
                if(count[i] < count[j] && !word[i].equals("") && !word[j].equals("")){
                    int temp = count[i];
                    count[i] = count[j];
                    count[j] = temp;

                    String tempText = word[i];
                    word[i] = word[j];
                    word[j] = tempText;
                }
            }
        }


        for (int i = 0; i < word.length; i++) {
            if (!word[i].equals("")) {
                //System.out.println(word[i] + " " + count[i]);
            }
        }

        System.out.println("Top3");

        if(word.length<3){
            for(int i=0;i<word.length;++i){
                if (!word[i].equals("")) {
                    wordtopscheduler[i] = word[i];
                    System.out.println(word[i]+"   "+count[i]);
                    final Translator t = new Translator(wordtopscheduler[i],Viewscheduler.this);

                    t.trans();

                    Handler handler = new Handler();
                    final int finalI = i;
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            wordtranscheduler[finalI] = t.trans();

                        }
                    };handler.postDelayed(runnable,3000);
                }
            }
        }else{
            for (int i = 0; i < 3; i++) {
                if (!word[i].equals("")) {
                    wordtopscheduler[i] = word[i];
                    System.out.println(word[i]+"   "+count[i]);
                    final Translator t = new Translator(wordtopscheduler[i],Viewscheduler.this);

                    t.trans();

                    Handler handler = new Handler();
                    final int finalI = i;
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            wordtranscheduler[finalI] = t.trans();

                        }
                    };handler.postDelayed(runnable,3000);                }
            }
        }
    }

    private class MyValueFormatter implements IValueFormatter {
        private DecimalFormat mFormat;
        public MyValueFormatter() {
            mFormat = new DecimalFormat("###,###,##0");
        }
        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            if(value > 0) {
                return mFormat.format(value);
            } else {
                return "";
            }
        }
    }


    public void checkday(){
        if(getFormattednameday==0){
            showday = "Sunday";
        }else if(getFormattednameday==1){
            showday = "Monday";
        }else if(getFormattednameday==2){
            showday = "Tuesday";
        }else if(getFormattednameday==3){
            showday = "Wednesday";
        }else if(getFormattednameday==4){
            showday = "Thursday";
        }else if(getFormattednameday==5){
            showday = "Friday";
        }else if(getFormattednameday==6){
            showday = "Saturday";
        }
    }

    public void checkmonth(){
        if(getFormattedMonth==0){
            showmonth = "January";
        }else if(getFormattedMonth==1){
            showmonth = "February";
        }else if(getFormattedMonth==2){
            showmonth = "March";
        }else if(getFormattedMonth==3){
            showmonth = "April";
        }else if(getFormattedMonth==4){
            showmonth = "May";
        }else if(getFormattedMonth==5){
            showmonth = "June";
        }else if(getFormattedMonth==6){
            showmonth = "July";
        }else if(getFormattedMonth==7){
            showmonth = "August";
        }else if(getFormattedMonth==8){
            showmonth = "September";
        }else if(getFormattedMonth==9){
            showmonth = "October";
        }else if(getFormattedMonth==10){
            showmonth = "November";
        }else if(getFormattedMonth==11){
            showmonth = "December";
        }
    }

    void checktimestartstop(){
        if(starthourchange<10){
            if(startminutechange<10){
                timestartset = "0"+starthourchange+":0"+startminutechange;
            }else{
                timestartset = "0"+starthourchange+":"+startminutechange;
            }
        }else{
            if(startminutechange<10){
                timestartset = starthourchange+":0"+startminutechange;
            }else{
                timestartset = starthourchange+":"+startminutechange;
            }
        }

        if(stophourchange<10){
            if(stopminutechange<10){
                timestopset = "0"+stophourchange+":0"+stopminutechange;
            }else{
                timestopset = "0"+stophourchange+":"+stopminutechange;
            }
        }else{
            if(stopminutechange<10){
                timestopset = stophourchange+":0"+stopminutechange;
            }else{
                timestopset = stophourchange+":"+stopminutechange;
            }
        }
    }

}
