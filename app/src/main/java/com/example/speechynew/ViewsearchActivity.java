package com.example.speechynew;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.speechynew.analysis.Translator;
import com.example.speechynew.connectDB.Continuemax;
import com.example.speechynew.connectDB.Engword;
import com.example.speechynew.connectDB.Timeprocess;
import com.example.speechynew.connectDB.Word;
import com.example.speechynew.connectDB.Wrongword;
import com.example.speechynew.ui.home.HomeFragment;
import com.example.speechynew.ui.home.HomeReport;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.android.gms.common.data.DataHolder;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static com.example.speechynew.connectDB.Continuemaxinterface.TABLE_NAME10;
import static com.example.speechynew.connectDB.Engwordinterface.TABLE_NAME2;
import static com.example.speechynew.connectDB.Timeprocessinterface.TABLE_NAME5;
import static com.example.speechynew.connectDB.Wordinterface.TABLE_NAME3;
import static com.example.speechynew.connectDB.Wrongwordinterface.TABLE_NAME11;

public class ViewsearchActivity extends AppCompatActivity {

    String dateset;
    String monthset;
    String yearset;
    String starthour;
    String startminute;
    String stophour;
    String stopminute;

    int datechange =0;
    int monthchange = 0;
    int yearchange = 0;
    int starthourchange=0;
    int startminutechange =0;
    int stophourchange =0;
    int stopminutechange=0;

    int[] testlongtime;

    TextView date;
    BarChart mChart;
    Button changetype;
    Button nextpage;
    TextView wait5search;

    Engword eng;
    Word anothereng;
    Timeprocess time;
    Continuemax continuemax;
    Wrongword wrongword;

    String showdatesearch;
    String totalwordsearch;
    String totaltimesearch;
    int wordsearch = 0;
    int timesearch = 0;
    double wordminsearch;
    int continuemaxsearch = 0;
    String[] wordtop = new String[3];
    String[] wordtrans = new String[3];

    Calendar c ;
    SimpleDateFormat df;
    String formattedDate;
    int getFormattedDay;
    int getFormattedMonth;
    int getFormattedYear;
    int getFormattednameday;
    int getFormattedHour;
    int getFormattedMinute;

    boolean resulttype;

    int testall = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_viewsearch);

        date = findViewById(R.id.date);
        mChart = (BarChart) findViewById(R.id.mp_barchart);
        changetype = findViewById(R.id.changetype);
        nextpage = findViewById(R.id.nextpage);
        wait5search = findViewById(R.id.wait5search);

        resulttype = false;

        //Create object database class
        eng = new Engword(this);
        anothereng = new Word(this);
        time = new Timeprocess(this);
        continuemax = new Continuemax(this);
        wrongword = new Wrongword(this);

        mChart.getDescription().setEnabled(false);
        mChart.setFitBars(true);

        //Data from before page
        dateset = getIntent().getStringExtra("date");
        monthset = getIntent().getStringExtra("month");
        yearset = getIntent().getStringExtra("year");
        starthour = getIntent().getStringExtra("starthour");
        startminute = getIntent().getStringExtra("startminute");
        stophour = getIntent().getStringExtra("stophour");
        stopminute = getIntent().getStringExtra("stopminute");

        //Change data type
        datechange = Integer.parseInt(dateset);
        monthchange = Integer.parseInt(monthset);
        yearchange = Integer.parseInt(yearset);
        starthourchange = Integer.parseInt(starthour);
        startminutechange = Integer.parseInt(startminute);
        stophourchange = Integer.parseInt(stophour);
        stopminutechange = Integer.parseInt(stopminute);

        String timestartset = "";
        String timestopset = "";

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

        date.setText(dateset+"/"+monthset+"/"+yearset+"\n"+timestartset+" - "+timestopset);
        showdatesearch = dateset+"/"+monthset+"/"+yearset+"   "+timestartset+" - "+timestopset;

        //set start
        c = Calendar.getInstance();
        c.set(Calendar.DATE,datechange);
        c.set(Calendar.MONTH,monthchange);
        c.set(Calendar.YEAR,yearchange);
        c.set(Calendar.HOUR_OF_DAY,starthourchange);
        c.set(Calendar.MINUTE,startminutechange);


        df = new SimpleDateFormat("d-MM-yyyy");
        formattedDate = df.format(c.getTime());
        getFormattednameday = c.getTime().getDay();
        getFormattedDay = c.getTime().getDate();
        getFormattedMonth = c.getTime().getMonth();
        getFormattedYear = c.getTime().getYear();
        getFormattedHour = c.getTime().getHours();
        getFormattedMinute = c.getTime().getMinutes();


        int longtime = ((stophourchange*60)+stopminutechange)-((starthourchange*60)+startminutechange);
        testlongtime = new int[longtime+1];

        wait5search.setText("Please wait 5 second");

        viewinterest();
        viewtotalword();

        if(testall==0){
            changetype.setVisibility(View.GONE);
            nextpage.setVisibility(View.GONE);
            wait5search.setVisibility(View.GONE);
        }

        viewtimeinterest();
        wordmin();
        continuously();
        wrongwordsearch();

        nextpage.setEnabled(false);
        nextpage.setBackgroundResource(R.drawable.buttonshape11);

        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                nextpage.setEnabled(true);
                nextpage.setBackgroundResource(R.drawable.buttonshape6);

                wait5search.setText("");

                nextpage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent in = new Intent(ViewsearchActivity.this, ViewsearchreportActivity.class);
                        in.putExtra("showdatesearch",showdatesearch);
                        in.putExtra("totalwordsearch",totalwordsearch);
                        in.putExtra("totaltimesearch",totaltimesearch);
                        in.putExtra("wordminsearch",wordminsearch);
                        in.putExtra("continuemaxsearch",continuemaxsearch);
                        in.putExtra("wordtop",wordtop);
                        in.putExtra("wordtrans",wordtrans);
                        startActivity(in);

                    }
                });
            }

        };handler.postDelayed(runnable,4000);


        changetype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //change from word count to Percentage
                if(resulttype==false){
                    resulttype=true;
                    viewinterest();
                    changetype.setText("Word count");

                    //change from Percentage to word count
                }else if(resulttype==true){
                    resulttype=false;
                    viewinterest();
                    changetype.setText("Percentage");
                }

            }
        });

    }


    public void viewinterest(){

        //no data
        mChart.setNoDataText("There's no information at this moment.");
        mChart.setNoDataTextColor(Color.RED);

        int longtime = ((stophourchange*60)+stopminutechange)-((starthourchange*60)+startminutechange);

        if(resulttype==false){

            int[] arraydataeng = new int[longtime+1];
            int[] arraydatanone = new int[longtime+1];

            SQLiteDatabase dbeng = eng.getWritableDatabase();
            SQLiteDatabase dbnone = anothereng.getWritableDatabase();

            ArrayList<BarEntry> dataVals = new ArrayList<>();

            Cursor reseng = dbeng.rawQuery("select * from " + TABLE_NAME2 + " where date = " + datechange +" and month = " +
                    monthchange +" and year = " + yearchange + " and (hour*60)+minute >= "+((starthourchange*60)+startminutechange)
                    + " and (hour*60)+minute < " +((stophourchange*60)+stopminutechange), null);

            Cursor resnone = dbnone.rawQuery("select * from " + TABLE_NAME3 + " where date = " + datechange +" and month = " +
                    monthchange +" and year = " + yearchange + " and (hour*60)+minute >= "+((starthourchange*60)+startminutechange)
                    + " and (hour*60)+minute < " +((stophourchange*60)+stopminutechange), null);

            int countreseng = reseng.getCount();
            int countresnone = resnone.getCount();

            if(countreseng==0){ return; }

            if(countresnone==0){ return;}

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

            data.setValueFormatter(new ViewsearchActivity.MyValueFormatter());

            mChart.getLegend().setXEntrySpace(12);//ระยะห่างระหว่างข้อมูล
            mChart.getLegend().setFormToTextSpace(3);//ระยะห่างระหว่างรูปกับคำอธิบาย

            //edit text under graph
            String[] time = new String[longtime];

            for(int i=0;i<longtime;++i){

                String anstime = "";

                if(getFormattedHour<10){
                    if(getFormattedMinute<10){
                        anstime = "0"+getFormattedHour+":0"+getFormattedMinute;
                    }else{
                        anstime = "0"+getFormattedHour+":"+getFormattedMinute;
                    }
                }else{
                    if(getFormattedMinute<10){
                        anstime = getFormattedHour+":0"+getFormattedMinute;
                    }else{
                        anstime = getFormattedHour+":"+getFormattedMinute;
                    }
                }

                time[i] = anstime;
                c.add(Calendar.MINUTE, +1);
                getFormattedHour = c.getTime().getHours();
                getFormattedMinute = c.getTime().getMinutes();

            }

            //reset date
            c.add(Calendar.MINUTE, -longtime);
            getFormattedHour = c.getTime().getHours();
            getFormattedMinute = c.getTime().getMinutes();

            XAxis xAxis = mChart.getXAxis();
            xAxis.setValueFormatter(new IndexAxisValueFormatter(time));
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setGranularity(1);
            xAxis.setCenterAxisLabels(false);
            xAxis.setGranularityEnabled(true);
            xAxis.setAxisMaximum(longtime);
            xAxis.setDrawGridLines(false);

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

            double[] arraydataeng = new double[longtime+1];
            double[] arraydatanone = new double[longtime+1];
            double[] arrayall = new double[longtime+1];

            SQLiteDatabase dbeng = eng.getWritableDatabase();
            SQLiteDatabase dbnone = anothereng.getWritableDatabase();

            ArrayList<BarEntry> dataVals = new ArrayList<>();

            Cursor reseng = dbeng.rawQuery("select * from " + TABLE_NAME2 + " where date = " + datechange +" and month = " +
                    monthchange +" and year = " + yearchange + " and (hour*60)+minute >= "+((starthourchange*60)+startminutechange)
                    + " and (hour*60)+minute < " +((stophourchange*60)+stopminutechange), null);

            Cursor resnone = dbnone.rawQuery("select * from " + TABLE_NAME3 + " where date = " + datechange +" and month = " +
                    monthchange +" and year = " + yearchange + " and (hour*60)+minute >= "+((starthourchange*60)+startminutechange)
                    + " and (hour*60)+minute < " +((stophourchange*60)+stopminutechange), null);

            int countreseng = reseng.getCount();
            int countresnone = resnone.getCount();

            if(countreseng==0){ return;}

            if(countresnone==0){ return;}

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
            data.setValueFormatter(new ViewsearchActivity.MyValueFormatter());

            mChart.getLegend().setXEntrySpace(12);//ระยะห่างระหว่างข้อมูล
            mChart.getLegend().setFormToTextSpace(3);//ระยะห่างระหว่างรูปกับคำอธิบาย
            String[] time = new String[longtime];


            for(int i=0;i<longtime;++i){

                String anstime = "";

                if(getFormattedHour<10){
                    if(getFormattedMinute<10){
                        anstime = "0"+getFormattedHour+":0"+getFormattedMinute;
                    }else{
                        anstime = "0"+getFormattedHour+":"+getFormattedMinute;
                    }
                }else{
                    if(getFormattedMinute<10){
                        anstime = getFormattedHour+":0"+getFormattedMinute;
                    }else{
                        anstime = getFormattedHour+":"+getFormattedMinute;
                    }
                }

                time[i] = anstime;
                c.add(Calendar.MINUTE, +1);
                getFormattedHour = c.getTime().getHours();
                getFormattedMinute = c.getTime().getMinutes();

            }

            //reset date
            c.add(Calendar.MINUTE, -longtime);
            getFormattedHour = c.getTime().getHours();
            getFormattedMinute = c.getTime().getMinutes();

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


    public void viewtotalword(){

        int totalall = 0;
        int totaleng = 0;
        int totalanother = 0;

        SQLiteDatabase db2 = eng.getWritableDatabase();
        Cursor res2 = db2.rawQuery("select * from " + TABLE_NAME2 + " where date = " + datechange + " and month = "+ monthchange +
                " and year = "+yearchange+" and (hour*60)+minute >= " + ((starthourchange*60)+startminutechange) +
                " and (hour*60)+minute < " + (((stophourchange*60)+stopminutechange)), null);

        int countlenghtres2 = res2.getCount();

        if(countlenghtres2==0){ return; }

        while(res2.moveToNext()){
            totaleng+=res2.getInt(1);
        }

        SQLiteDatabase db3 = anothereng.getWritableDatabase();
        Cursor res3 = db3.rawQuery("select * from " + TABLE_NAME3 + " where date = " + datechange + " and month = "+ monthchange +
                " and year = "+yearchange+" and (hour*60)+minute >= " + ((starthourchange*60)+startminutechange) +
                " and (hour*60)+minute < " + (((stophourchange*60)+stopminutechange)), null);

        int countlenghtres3 = res3.getCount();

        if(countlenghtres3==0){ return; }

        while(res3.moveToNext()){
            totalanother+=res3.getInt(1);
        }

        totalall = totaleng+totalanother;

        testall = totalall;

        wordsearch = totaleng;

        totalwordsearch = totaleng+ " / "+totalall;

    }

    public void viewtimeinterest(){

        int totaltime = 0;

        SQLiteDatabase dbtime = time.getWritableDatabase();
        Cursor restime = dbtime.rawQuery("select * from " + TABLE_NAME5 + " where date = " + datechange + " and month = "+ monthchange +
                " and year = "+yearchange+" and (hour*60)+minute >= " + ((starthourchange*60)+startminutechange) +
                " and (hour*60)+minute < " + (((stophourchange*60)+stopminutechange)),null);

        int countrestime = restime.getCount();

        if(countrestime==0){
            totaltime=0;
        }

        while(restime.moveToNext()){
            totaltime += restime.getInt(8);
        }

        timesearch = totaltime;

        int numberOfHours = (totaltime % 86400) / 3600;
        int numberOfMinutes = ((totaltime % 86400) % 3600) / 60;
        int numberOfSeconds = ((totaltime % 86400) % 3600) % 60;

        if(numberOfMinutes<10){
            if(numberOfSeconds<10){
                String text = numberOfHours+" : 0"+numberOfMinutes+" : 0"+numberOfSeconds;
                totaltimesearch = text;
            }else{
                String text = numberOfHours+" : 0"+numberOfMinutes+" : "+numberOfSeconds;
                totaltimesearch = text;
            }
        }else{
            totaltimesearch = numberOfHours + " : " + numberOfMinutes + " : " + numberOfSeconds;
        }

    }

    public void wordmin(){

        double total = (double) wordsearch * 1.0;
        double total2 = (double) timesearch / 60.0;

        if(total2 == 0.0){
            total2 = 1.0;
        }

        wordminsearch = total/total2;

    }


    public void continuously(){

        int checking = 0;
        continuemaxsearch = 0;
        SQLiteDatabase dbcon = continuemax.getWritableDatabase();
        Cursor resdbcon = dbcon.rawQuery("select * from "+TABLE_NAME10+ " where date = " + datechange + " and month = "+ monthchange +
                " and year = "+yearchange+" and (hour*60)+minute >= " + ((starthourchange*60)+startminutechange) +
                " and (hour*60)+minute < " + (((stophourchange*60)+stopminutechange)),null);

        while (resdbcon.moveToNext()){
            checking = resdbcon.getInt(1);

            if(continuemaxsearch < checking){
                continuemaxsearch = checking;
            }

        }

    }


    public void wrongwordsearch(){

        ArrayList<String> wordfromdb = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            wordtop[i] = "";
        }

        for (int i=0;i<3;++i){
            wordtrans[i] = "";
        }

        SQLiteDatabase dbwrong = wrongword.getWritableDatabase();
        Cursor resdbwrong = dbwrong.rawQuery("select * from "+TABLE_NAME11+ " where date = " + datechange + " and month = "+ monthchange +
                " and year = "+yearchange+" and (hour*60)+minute >= " + ((starthourchange*60)+startminutechange) +
                " and (hour*60)+minute < " + (((stophourchange*60)+stopminutechange)),null);

        int countresdbwrong = resdbwrong.getCount();

        if(countresdbwrong==0){
            return;
        }


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
                    wordtop[i] = word[i];
                    System.out.println(word[i]+"   "+count[i]);
                    final Translator t = new Translator(wordtop[i],ViewsearchActivity.this);
                    t.trans();

                    Handler handler = new Handler();
                    final int finalI = i;
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            wordtrans[finalI] = t.trans();

                        }
                    };handler.postDelayed(runnable,3000);
                }
            }
        }else{
            for (int i = 0; i < 3; i++) {
                if (!word[i].equals("")) {
                    wordtop[i] = word[i];
                    System.out.println(word[i]+"   "+count[i]);
                    final Translator t = new Translator(wordtop[i],ViewsearchActivity.this);
                    t.trans();

                    Handler handler = new Handler();
                    final int finalI = i;
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            wordtrans[finalI] = t.trans();

                        }
                    };handler.postDelayed(runnable,3000);
                }
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

}



