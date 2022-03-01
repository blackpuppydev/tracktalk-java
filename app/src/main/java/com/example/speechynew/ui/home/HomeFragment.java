package com.example.speechynew.ui.home;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.speechynew.R;
import com.example.speechynew.analysis.Translator;
import com.example.speechynew.connectDB.Continuemax;
import com.example.speechynew.connectDB.Engword;
import com.example.speechynew.connectDB.Setting;
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


public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    //view
    TextView day;
    BarChart mChart;
    Button changetype;
    ImageButton graphminus;
    ImageButton graphplus;
    Button nextpage;
    TextView wait5;

    //database
    Engword eng;
    Word anothereng;
    Setting setting;
    Timeprocess time;
    Continuemax continuemax;
    Wrongword wrongword;

    //calendar
    Calendar c ;
    SimpleDateFormat df;
    String formattedDate;
    int getFormattedDay;
    int getFormattedMonth;
    int getFormattedYear;
    int getFormattednameday;

    String datename;
    String monthname;
    boolean resulttype;

    String totalwordday;
    String totaltimeday;
    int totalwordeng = 0;
    int totaltimeeng = 0;
    double wordminday;
    int continuemaxday = 0;
    String[] wordtop = new String[3];
    String[] wordtrans = new String[3];
    String showday;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        datename=" ";
        monthname=" ";

        mChart = (BarChart) root.findViewById(R.id.mp_barchart);
        day = root.findViewById(R.id.showday);
        graphminus = root.findViewById(R.id.graphminus);
        graphplus = root.findViewById(R.id.graphplus);
        changetype = root.findViewById(R.id.changetype);
        nextpage = root.findViewById(R.id.nextpage);
        wait5 = root.findViewById(R.id.wait5);

        //create object database
        eng = new Engword(root.getContext());
        anothereng = new Word(root.getContext());
        setting = new Setting(root.getContext());
        time = new Timeprocess(root.getContext());
        continuemax = new Continuemax(root.getContext());
        wrongword = new Wrongword(root.getContext());

        resulttype = false;

        c = Calendar.getInstance();
        df = new SimpleDateFormat("d-MM-yyyy");
        formattedDate = df.format(c.getTime());
        getFormattednameday = c.getTime().getDay();
        getFormattedDay = c.getTime().getDate();
        getFormattedMonth = c.getTime().getMonth();
        getFormattedYear = c.getTime().getYear();

        wait5.setText(" Please wait 5 second");

        //call method
        nextpageonclick();
        newviewAllday();
        viewtotalday();
        viewtotaltimeday();
        wordmin();
        continuemaxday();
        wrongwordday();

        mChart.getDescription().setEnabled(false);
        mChart.setFitBars(true);

        getnamemonth(getFormattedMonth);
        day.setText("Today, "+getFormattedDay+" "+monthname+" "+(getFormattedYear+1900));
        showday = "Today, "+getFormattedDay+" "+monthname+" "+(getFormattedYear+1900);

        //minus day -
        graphminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                c.add(Calendar.DATE, -1);
                formattedDate = df.format(c.getTime());
                getFormattednameday = c.getTime().getDay();
                getFormattedDay = c.getTime().getDate();
                getFormattedMonth = c.getTime().getMonth();
                getFormattedYear = c.getTime().getYear();

                wait5.setText(" Please wait 5 second");

                nextpageonclick();
                newviewAllday();
                viewtotalday();
                viewtotaltimeday();
                wordmin();
                continuemaxday();
                wrongwordday();

                getnameday(getFormattednameday);
                getnamemonth(getFormattedMonth);

                day.setText(datename+", "+getFormattedDay+" "+monthname+" "+(getFormattedYear+1900));
                showday = datename+", "+getFormattedDay+" "+monthname+" "+(getFormattedYear+1900);


            }
        });

        //plus day +
        graphplus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                c.add(Calendar.DATE, +1);
                formattedDate = df.format(c.getTime());
                getFormattednameday = c.getTime().getDay();
                getFormattedDay = c.getTime().getDate();
                getFormattedMonth = c.getTime().getMonth();
                getFormattedYear = c.getTime().getYear();


                if(c.after(Calendar.getInstance())){ // > current time
                    c.add(Calendar.DATE, -1);
                    formattedDate = df.format(c.getTime());
                    getFormattednameday = c.getTime().getDay();
                    getFormattedDay = c.getTime().getDate();
                    getFormattedMonth = c.getTime().getMonth();
                    getFormattedYear = c.getTime().getYear();

                }else{
                    wait5.setText(" Please wait 5 second");

                    nextpageonclick();
                    newviewAllday();
                    viewtotaltimeday();
                    viewtotalday();
                    wordmin();
                    continuemaxday();
                    wrongwordday();

                    getnameday(getFormattednameday);
                    getnamemonth(getFormattedMonth);

                    day.setText(datename+", "+getFormattedDay+" "+monthname+" "+(getFormattedYear+1900));
                    showday = datename+", "+getFormattedDay+" "+monthname+" "+(getFormattedYear+1900);
                }

            }
        });


        changetype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //change from word count to Percentage
                if(resulttype==false){
                    resulttype=true;
                    newviewAllday();
                    changetype.setText("Word count");

                //change from Percentage to word count
                }else if(resulttype==true){
                    resulttype=false;
                    newviewAllday();
                    changetype.setText("Percentage");
                }

            }
        });


        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);

            }
        });

        return root;

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
                wait5.setText("");

                nextpage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent in = new Intent(getContext(),HomeReport.class);
                        in.putExtra("test",showday);
                        in.putExtra("totalwordday",totalwordday);
                        in.putExtra("totaltimeday",totaltimeday);
                        in.putExtra("wordminday",wordminday);
                        in.putExtra("continuemaxday",continuemaxday);
                        in.putExtra("wordtop",wordtop);
                        in.putExtra("wordtrans",wordtrans);
                        startActivity(in);

                    }
                });
            }

        };handler.postDelayed(runnable,4000);
    }



    public void newviewAllday(){


        if(resulttype==false) { //view word count

            ArrayList<BarEntry> dataVals = new ArrayList<>();

            int manyhour = 24;
            int[] arraytesteng = new int[manyhour];
            int[] arraytestnone = new int[manyhour];

            //eng word
            SQLiteDatabase dbeng = eng.getWritableDatabase();
            Cursor reseng = dbeng.rawQuery("select * from " + TABLE_NAME2 + " where date = " + getFormattedDay + " and month = " +
                                                (getFormattedMonth + 1) + " and year = " + (getFormattedYear + 1900), null);

            if (reseng.getCount() == 0) { //set 0
                for (int i = 0; i < manyhour; ++i) {
                    int values = 0;
                    //yValsEng.add(new BarEntry(i, values));
                    dataVals.add(new BarEntry(i, values));
                }
            }

            while (reseng.moveToNext()) {
                arraytesteng[reseng.getInt(6)]+=reseng.getInt(1);
            }

            //not eng word
            SQLiteDatabase dbnone = anothereng.getWritableDatabase();
            Cursor resnone = dbnone.rawQuery("select * from " + TABLE_NAME3 + " where date = " + getFormattedDay + " and month = " +
                                                  (getFormattedMonth + 1) + " and year = " + (getFormattedYear + 1900), null);

            if (resnone.getCount() == 0) {
                for (int i = 0; i < manyhour; ++i) {
                    int values = 0;
                    dataVals.add(new BarEntry(i, values));
                }
            }

            while (resnone.moveToNext()) {
                arraytestnone[resnone.getInt(6)]+=resnone.getInt(1);
            }


            for(int i=0;i<manyhour;++i){
                int valueseng = arraytesteng[i];
                int valuesno = arraytestnone[i];
                dataVals.add(new BarEntry(i, new float[]{valueseng, valuesno}));
            }

            //setting bar chart

            BarDataSet barDataSet = new BarDataSet(dataVals, " ");
            barDataSet.setColors(Color.parseColor("#FF9933"), Color.parseColor("#8CB9D1"));
            barDataSet.setStackLabels(new String[]{"Number of English words", "Number of non-English words"});

            BarData data = new BarData(barDataSet);
            mChart.setData(data);

            data.setValueFormatter(new MyValueFormatter());

            mChart.getLegend().setXEntrySpace(12);//ระยะห่างระหว่างข้อมูล
            mChart.getLegend().setFormToTextSpace(3);//ระยะห่างระหว่างรูปกับคำอธิบาย

            String[] time = new String[]{"00","01","02","03","04","05","06","07","08","09","10","11",
                                         "12","13","14","15","16","17","18","19","20","21","22","23"};

            XAxis xAxis = mChart.getXAxis();
            xAxis.setValueFormatter(new IndexAxisValueFormatter(time));
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setGranularity(1);
            xAxis.setCenterAxisLabels(false);
            xAxis.setGranularityEnabled(true);
            xAxis.setAxisMaximum(24);
            xAxis.setDrawGridLines(false); //เส้นตาราง

            mChart.setDragEnabled(true);
            mChart.getAxisRight().setAxisMinimum(0);
            mChart.getAxisLeft().setAxisMinimum(0);
            mChart.setVisibleXRangeMaximum(24);
            mChart.invalidate();
            mChart.animateXY(2000, 4000);
            mChart.setDoubleTapToZoomEnabled(true);
            mChart.setPinchZoom(true);
            mChart.fitScreen();

        }else if(resulttype==true){ //view percentage

            ArrayList<BarEntry> dataVals = new ArrayList<>();

            double[] arraytesteng = new double[24];
            double[] arraytestnone = new double[24];
            double[] arrayall = new double[24];
            int manyhour = 24;

            //eng word
            SQLiteDatabase dbeng = eng.getWritableDatabase();
            Cursor reseng = dbeng.rawQuery("select * from " + TABLE_NAME2 + " where date = " + getFormattedDay + " and month = " +
                                                (getFormattedMonth + 1) + " and year = " + (getFormattedYear + 1900), null);

            if (reseng.getCount() == 0) {
                for (int i = 0; i < manyhour; ++i) {
                    int values = 0;
                    dataVals.add(new BarEntry(i, values));
                }
            }

            while (reseng.moveToNext()) {
                arraytesteng[reseng.getInt(6)]+=reseng.getInt(1);
            }


            //not eng word
            SQLiteDatabase dbnone = anothereng.getWritableDatabase();
            Cursor resnone = dbnone.rawQuery("select * from " + TABLE_NAME3 + " where date = " + getFormattedDay + " and month = " +
                                                  (getFormattedMonth + 1) + " and year = " + (getFormattedYear + 1900), null);

            if (resnone.getCount() == 0) {
                for (int i = 0; i < manyhour; ++i) {
                    int values = 0;
                    dataVals.add(new BarEntry(i, values));
                }
            }

            while (resnone.moveToNext()) {
                arraytestnone[resnone.getInt(6)]+=resnone.getInt(1);
            }

            //get all word
            for(int i = 0; i < manyhour; ++i){
                arrayall[i] = arraytesteng[i];
            }
            for(int i = 0; i < manyhour; ++i){
                arrayall[i] +=arraytestnone[i];
            }

            //set data in bar chart
            for (int i = 0; i < manyhour; ++i) {
                float valueseng;
                double testvaleng = arraytesteng[i] / arrayall[i];

                if (Double.isNaN(testvaleng)) {
                    valueseng = (float) 0;
                } else {
                    valueseng = (float) testvaleng * 100;
                }

                float valuesno;
                double testvalno = arraytestnone[i] / arrayall[i];

                if (Double.isNaN(testvalno)) {
                    valuesno = (float) 0;
                } else {
                    valuesno = (float) testvalno * 100;
                }

                dataVals.add(new BarEntry(i, new float[]{valueseng,valuesno}));
            }

            //setting bar chart
            BarDataSet barDataSet = new BarDataSet(dataVals, " ");
            barDataSet.setColors(Color.parseColor("#FF9933"), Color.parseColor("#8CB9D1"));
            barDataSet.setStackLabels(new String[]{"Percentage of English words", "Percentage of non-English words"});
            BarData data = new BarData(barDataSet);
            data.setValueFormatter(new MyValueFormatter());

            mChart.setData(data);//ระยะห่างระหว่างข้อมูล
            mChart.getLegend().setXEntrySpace(12);//ระยะห่างระหว่างรูปกับคำอธิบาย
            mChart.getLegend().setFormToTextSpace(3);

            String[] time = new String[]{"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11"
                    , "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"};

            XAxis xAxis = mChart.getXAxis();
            xAxis.setValueFormatter(new IndexAxisValueFormatter(time));
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setGranularity(1);
            xAxis.setCenterAxisLabels(false);
            xAxis.setGranularityEnabled(true);
            xAxis.setAxisMaximum(24);
            xAxis.setDrawGridLines(false); //เส้นตาราง

            mChart.setDragEnabled(true);
            mChart.getAxisRight().setAxisMinimum(0);
            mChart.getAxisLeft().setAxisMinimum(0);
            mChart.setVisibleXRangeMaximum(24);
            mChart.invalidate();
            mChart.animateXY(2000, 4000);
            mChart.setDoubleTapToZoomEnabled(true);
            mChart.setPinchZoom(true);
            mChart.fitScreen();
        }

    }

    //total word
    public void viewtotalday(){

        int totalall = 0;
        int totaleng = 0;
        int totalanother = 0;

        //eng word
        SQLiteDatabase db2 = eng.getWritableDatabase();
        Cursor res2 = db2.rawQuery("select * from " + TABLE_NAME2 + " where date = " + getFormattedDay +" and month = " +
                                        (getFormattedMonth+1) +" and year = "+(getFormattedYear+1900), null);

        int countlenghtres2 = res2.getCount();
        if(countlenghtres2==0){
            totaleng=0;
        }

        while(res2.moveToNext()){
            totaleng+=res2.getInt(1);
        }

        //not eng word
        SQLiteDatabase db3 = anothereng.getWritableDatabase();
        Cursor res3 = db3.rawQuery("select * from " + TABLE_NAME3 + " where date = " + getFormattedDay +" and month = " +
                                        (getFormattedMonth+1) +" and year = "+(getFormattedYear+1900), null);

        int countlenghtres3 = res3.getCount();
        if(countlenghtres3==0){
            totalanother=0;
        }

        while(res3.moveToNext()){
            totalanother+=res3.getInt(1);
        }

        totalall = totaleng+totalanother;
        totalwordeng = totaleng;
        totalwordday = totaleng+ " / "+totalall;

    }

    //total time
    public void viewtotaltimeday(){

        int totaltime = 0;

        SQLiteDatabase dbtime = time.getWritableDatabase();
        Cursor restime = dbtime.rawQuery("select * from "+ TABLE_NAME5 + " where date = "+getFormattedDay+" and month = "+
                                              (getFormattedMonth+1)+ " and year = "+ (getFormattedYear+1900),null);

        int countrestime = restime.getCount();
        if(countrestime==0){
            totaltime=0;
        }

        while(restime.moveToNext()){
            totaltime += restime.getInt(8);
        }

        totaltimeeng = totaltime;

        int numberOfHours = (totaltime % 86400) / 3600;
        int numberOfMinutes = ((totaltime % 86400) % 3600) / 60;
        int numberOfSeconds = ((totaltime % 86400) % 3600) % 60;

        if(numberOfMinutes<10){
            if(numberOfSeconds<10){
                String text = numberOfHours+" : 0"+numberOfMinutes+" : 0"+numberOfSeconds;
                totaltimeday = text;
            }else{
                String text = numberOfHours+" : 0"+numberOfMinutes+" : "+numberOfSeconds;
                totaltimeday = text;
            }
        }else{
            totaltimeday = numberOfHours + " : " + numberOfMinutes + " : " + numberOfSeconds;
        }
    }


    public void wordmin(){

        double total = (double) totalwordeng * 1.0;
        double total2 = (double) totaltimeeng / 60.0;

        if(total2 == 0.0){
            total2 = 1.0;
        }

        wordminday = total/total2;

        System.out.println("wmddd "+wordminday);

    }

    public void continuemaxday(){

        int checking = 0;
        continuemaxday = 0;
        SQLiteDatabase dbcon = continuemax.getWritableDatabase();
        Cursor resdbcon = dbcon.rawQuery("select * from "+TABLE_NAME10+ " where date = "+getFormattedDay+" and month = "+(getFormattedMonth+1)+
                                              " and year = "+ (getFormattedYear+1900),null);

        while (resdbcon.moveToNext()){
            checking = resdbcon.getInt(1);

            if(continuemaxday < checking){
                continuemaxday = checking;
            }
        }
    }


    public void wrongwordday(){

        ArrayList<String> wordfromdb = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
                wordtop[i] = "";
        }

        for (int i = 0; i < 3; i++) {
            wordtrans[i] = "";
        }

        SQLiteDatabase dbwrong = wrongword.getWritableDatabase();
        Cursor resdbwrong = dbwrong.rawQuery("select * from "+TABLE_NAME11+" where date = "+getFormattedDay+" and month = "+(getFormattedMonth+1)+
                                                  " and year = "+ (getFormattedYear+1900) ,null);

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
            word[i] = ""; //set default
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

                    final Translator t = new Translator(wordtop[i],getContext());
                    t.trans();

                    Handler handler = new Handler();
                    final int finalI = i;
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            wordtrans[finalI] = t.trans();

                        }
                    };handler.postDelayed(runnable,4000);

                }
            }

        }else{
            for (int i = 0; i < 3; i++) {
                if (!word[i].equals("")) {
                    wordtop[i] = word[i];

                    final Translator t = new Translator(wordtop[i],getContext());
                    t.trans();

                    Handler handler = new Handler();
                    final int finalI = i;
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            wordtrans[finalI] = t.trans();
                        }
                    };handler.postDelayed(runnable,4000);
                }
            }
        }

    }

    public void getnamemonth(int month){
        if((month+1)==1){
            monthname="January";
        }else if((month+1)==2){
            monthname="February";
        }else if((month+1)==3){
            monthname="March";
        }else if((month+1)==4){
            monthname="April";
        }else if((month+1)==5){
            monthname="May";
        }else if((month+1)==6){
            monthname="June";
        }else if((month+1)==7){
            monthname="July";
        }else if((month+1)==8){
            monthname="August";
        }else if((month+1)==9){
            monthname="September";
        }else if((month+1)==10){
            monthname="October";
        }else if((month+1)==11){
            monthname="November";
        }else if((month+1)==12){
            monthname="December";
        }
    }

    public void getnameday(int day){
        if(day==0){
            datename="Sunday";
        }else if(day==1){
            datename="Monday";
        }else if(day==2){
            datename="Tuesday";
        }else if(day==3){
            datename="Wednesday";
        }else if(day==4){
            datename="Thursday";
        }else if(day==5){
            datename="Friday";
        }else if(day==6){
            datename="Saturday";
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