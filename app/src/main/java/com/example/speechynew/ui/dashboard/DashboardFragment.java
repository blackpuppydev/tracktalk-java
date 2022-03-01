package com.example.speechynew.ui.dashboard;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import com.example.speechynew.connectDB.Timeprocess;
import com.example.speechynew.connectDB.Word;
import com.example.speechynew.connectDB.Wrongword;
import com.example.speechynew.ui.notifications.NotificationsFragment;
import com.example.speechynew.ui.notifications.NotificationsReport;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.sql.Time;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.example.speechynew.connectDB.Continuemaxinterface.TABLE_NAME10;
import static com.example.speechynew.connectDB.Engwordinterface.TABLE_NAME2;
import static com.example.speechynew.connectDB.Timeprocessinterface.TABLE_NAME5;
import static com.example.speechynew.connectDB.Wordinterface.TABLE_NAME3;
import static com.example.speechynew.connectDB.Wrongwordinterface.TABLE_NAME11;
import static java.lang.StrictMath.abs;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    BarChart mChart;
    TextView week;
    ImageButton graphminus;
    ImageButton graphplus;
    TextView wait5week;

    Button changetype;
    boolean resulttype;

    Engword eng;
    Word anothereng;
    Timeprocess time;
    Continuemax continuemax;
    Wrongword wrongword;

    Calendar c ;
    SimpleDateFormat df;
    String formattedDate;

    Calendar c2 ;
    SimpleDateFormat df2;
    String formattedDate2;

    int getFormattedDay;
    int getFormattedMonth;
    int getFormattedYear;

    Button nextpage;

    String dateweek;
    String showtotalword;
    String showtotaltime;

    int totalwordeng;
    int totaltimeeng;
    double wordminweek;

    int continuemaxweek = 0;
    String[] wordtop = new String[3];
    String[] wordtrans = new String[3];


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        //final TextView textView = root.findViewById(R.id.viewweek);

        week = root.findViewById(R.id.showweek);
        mChart = root.findViewById(R.id.mp_barchartweek);
        graphminus = root.findViewById(R.id.graphminus);
        graphplus = root.findViewById(R.id.graphplus);
        changetype = root.findViewById(R.id.changetype);
        nextpage = root.findViewById(R.id.nextpage);
        wait5week = root.findViewById(R.id.wait5week);

        eng = new Engword(root.getContext());
        anothereng = new Word(root.getContext());
        time = new Timeprocess(root.getContext());
        continuemax = new Continuemax(root.getContext());
        wrongword = new Wrongword(root.getContext());

        resulttype = false;

        c = Calendar.getInstance();
        df = new SimpleDateFormat("d-MM-yyyy");
        formattedDate = df.format(c.getTime());

        getFormattedDay = c.getTime().getDate();
        getFormattedMonth = c.getTime().getMonth();
        getFormattedYear = c.getTime().getYear();

        c2 = Calendar.getInstance();
        c2.add(Calendar.DATE,-6);
        df2 = new SimpleDateFormat("d-MM-yyyy");
        formattedDate2 = df2.format(c2.getTime());

        week.setText(formattedDate2 + " - "+ formattedDate);
        dateweek = formattedDate2 + " - "+ formattedDate;

        mChart.getDescription().setEnabled(false);
        mChart.setFitBars(true);

        wait5week.setText(" Please wait 5 second");

        nextpageonclick();
        viewallweek();
        wordmin();

        //minus week -
        graphminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                c.add(Calendar.DATE, -7);
                formattedDate = df.format(c.getTime());
                getFormattedDay = c.getTime().getDate();
                getFormattedMonth = c.getTime().getMonth();
                getFormattedYear = c.getTime().getYear();

                c2.add(Calendar.DATE,-7);
                formattedDate2 = df2.format(c2.getTime());

                week.setText(formattedDate2 + " - "+ formattedDate);
                dateweek = formattedDate2 + " - "+ formattedDate;

                continuemaxweek = 0;

                wait5week.setText(" Please wait 5 second");

                for (int i = 0; i < 3; i++) {
                    wordtop[i] = "";
                }

                for (int i = 0; i < 3; i++) {
                    wordtrans[i] = "";
                }

                nextpageonclick();
                viewallweek();
                wordmin();

            }
        });

        //plus week +
        graphplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                c.add(Calendar.DATE, +7);
                formattedDate = df.format(c.getTime());
                getFormattedDay = c.getTime().getDate();
                getFormattedMonth = c.getTime().getMonth();
                getFormattedYear = c.getTime().getYear();

                c2.add(Calendar.DATE,+7);
                formattedDate2 = df2.format(c2.getTime());


                if(c.after(Calendar.getInstance())){

                    c.add(Calendar.DATE, -7);
                    formattedDate = df.format(c.getTime());
                    getFormattedDay = c.getTime().getDate();
                    getFormattedMonth = c.getTime().getMonth();
                    getFormattedYear = c.getTime().getYear();

                    c2.add(Calendar.DATE,-7);
                    formattedDate2 = df2.format(c2.getTime());

                }else{

                    week.setText(formattedDate2 + " - "+ formattedDate);
                    dateweek = formattedDate2 + " - "+ formattedDate;

                    continuemaxweek = 0;

                    for (int i = 0; i < 3; i++) {
                        wordtop[i] = "";
                    }

                    for (int i = 0; i < 3; i++) {
                        wordtrans[i] = "";
                    }

                    wait5week.setText(" Please wait 5 second");

                    nextpageonclick();
                    viewallweek();
                    wordmin();
                }

            }
        });


        changetype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //change from word count to Percentage
                if(resulttype==false){
                    resulttype=true;
                    viewallweek();
                    changetype.setText("Word count");

                    //change from Percentage to word count
                }else if(resulttype==true){
                    resulttype=false;
                    viewallweek();
                    changetype.setText("Percentage");
                }

            }
        });

        dashboardViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //   textView.setText(s);
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

                wait5week.setText("");

                nextpage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent report = new Intent(getContext(),DashboardReport.class);
                        report.putExtra("test",dateweek);
                        report.putExtra("totalwordweek",showtotalword);
                        report.putExtra("totaltimeweek",showtotaltime);
                        report.putExtra("wordminweek",wordminweek);
                        report.putExtra("continuemaxweek",continuemaxweek);
                        report.putExtra("wordtop",wordtop);
                        report.putExtra("wordtrans",wordtrans);
                        startActivity(report);

                    }
                });
            }

        };handler.postDelayed(runnable,4000);

    }


    public void viewallweek(){

        int counteng=0;
        int countnone =0;
        int totaltime=0;
        int checkingcon = 0;
        ArrayList<String> wordfromdb;

        if(resulttype==false){

            ArrayList<BarEntry> dataVals = new ArrayList<>();
            wordfromdb = new ArrayList<>();

            String timeingraph[] = new String[7];

            int test = 7;
            int[] arraytesteng = new int[test];
            int[] arraytestnone = new int[test];


            for(int i=0;i<test;++i) {

                //for time
                SQLiteDatabase dbtime = time.getWritableDatabase();
                Cursor restime = dbtime.rawQuery("select * from " + TABLE_NAME5 +" where date = "+ getFormattedDay + " and month = " + (getFormattedMonth+1) + " and year = " + (getFormattedYear+1900),null);

                while(restime.moveToNext()){
                    totaltime += restime.getInt(8);
                }

                //for engword
                SQLiteDatabase db2 = eng.getWritableDatabase();
                Cursor res = db2.rawQuery("select * from " + TABLE_NAME2 +" where date = "+ getFormattedDay + " and month = " + (getFormattedMonth+1) + " and year = " + (getFormattedYear+1900), null);

                if (res.getCount() == 0) {
                    arraytesteng[i]=0;
                }

                while (res.moveToNext()) {
                    counteng+=res.getInt(1);
                    arraytesteng[i]+=res.getInt(1);
                }

                //for word
                SQLiteDatabase db3 = anothereng.getWritableDatabase();
                Cursor res3 = db3.rawQuery("select * from " + TABLE_NAME3 +" where date = "+ getFormattedDay + " and month = " + (getFormattedMonth+1) + " and year = " + (getFormattedYear+1900), null);

                if (res3.getCount() == 0) {
                    arraytestnone[i]=0;
                }

                while (res3.moveToNext()) {
                    countnone+=res3.getInt(1);
                    arraytestnone[i]+=res3.getInt(1);
                }

                //for continue
                SQLiteDatabase dbcon = continuemax.getWritableDatabase();
                Cursor resdbcon = dbcon.rawQuery("select * from "+TABLE_NAME10+ " where date = "+getFormattedDay+" and month = "+(getFormattedMonth+1)+
                        " and year = "+ (getFormattedYear+1900),null);

                while (resdbcon.moveToNext()){
                    checkingcon = resdbcon.getInt(1);

                    if(continuemaxweek < checkingcon){
                        continuemaxweek = checkingcon;
                    }
                }

                //for wrongword
                SQLiteDatabase dbwrong = wrongword.getWritableDatabase();
                Cursor resdbwrong = dbwrong.rawQuery("select * from "+TABLE_NAME11+" where date = "+getFormattedDay+" and month = "+(getFormattedMonth+1)+
                        " and year = "+ (getFormattedYear+1900) ,null);

                int countresdbwrong = resdbwrong.getCount();

                if(countresdbwrong==0){
                    //return;
                }

                while (resdbwrong.moveToNext()){
                    wordfromdb.add(resdbwrong.getString(1));
                }

                timeingraph[abs(i-6)] = getFormattedDay+"/"+(getFormattedMonth+1);

                c.add(Calendar.DATE, -1);
                formattedDate = df.format(c.getTime());

                getFormattedDay = c.getTime().getDate();
                getFormattedMonth = c.getTime().getMonth();
                getFormattedYear = c.getTime().getYear();

            }

            //top3
            if(wordfromdb.size()==0){
                //nothing
            }else{
                wrongwordtop3(wordfromdb);
            }

            for(int i=0;i<test;++i){
                int valueseng = arraytesteng[abs(i-6)];
                int valuesno = arraytestnone[abs(i-6)];

                dataVals.add(new BarEntry(i, new float[]{valueseng, valuesno}));
            }


            BarDataSet barDataSet = new BarDataSet(dataVals, " ");
            barDataSet.setColors(Color.parseColor("#FF9933"), Color.parseColor("#8CB9D1"));
            barDataSet.setStackLabels(new String[]{"Number of English words", "Number of non-English words"});
            BarData data = new BarData(barDataSet);

            data.setValueFormatter(new DashboardFragment.MyValueFormatter());
            mChart.setData(data);//ระยะห่างระหว่างข้อมูล
            mChart.getLegend().setXEntrySpace(12);//ระยะห่างระหว่างรูปกับคำอธิบาย
            mChart.getLegend().setFormToTextSpace(3);

            XAxis xAxis = mChart.getXAxis();
            xAxis.setValueFormatter(new IndexAxisValueFormatter(timeingraph));
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setGranularity(1);
            xAxis.setCenterAxisLabels(false);
            xAxis.setGranularityEnabled(true);
            xAxis.setAxisMaximum(7);
            xAxis.setDrawGridLines(false); //เส้นตาราง

            mChart.setDragEnabled(true);
            mChart.getAxisRight().setAxisMinimum(0);
            mChart.getAxisLeft().setAxisMinimum(0);
            mChart.setVisibleXRangeMaximum(7);
            mChart.invalidate();
            mChart.animateXY(2000, 4000);
            mChart.setDoubleTapToZoomEnabled(true);
            mChart.setPinchZoom(true);
            mChart.fitScreen();

            totaltimeeng = totaltime;

            int numberOfDays = totaltime / 86400;
            int numberOfHours = (totaltime % 86400) / 3600;
            int numberOfMinutes = ((totaltime % 86400) % 3600) / 60;
            int numberOfSeconds = ((totaltime % 86400) % 3600) % 60;


            if(numberOfHours<10) {
                if (numberOfMinutes < 10) {
                    if (numberOfSeconds < 10) {
                        String text = numberOfDays + " Day  \n0" + numberOfHours + " : 0" + numberOfMinutes + " : 0" + numberOfSeconds;
                        showtotaltime = text;
                    } else {
                        String text = numberOfDays + " Day  \n0" + numberOfHours + " : 0" + numberOfMinutes + " : " + numberOfSeconds;
                        showtotaltime = text;
                    }
                }else{
                    String text = numberOfDays + " Day  \n0" + numberOfHours + " : " + numberOfMinutes + " : " + numberOfSeconds;
                    showtotaltime = text;
                }
            }else{
                showtotaltime = numberOfDays+" Day  \n"+numberOfHours + " : " + numberOfMinutes + " : " + numberOfSeconds;
            }


        }else if(resulttype==true){ //view percentage

            wordfromdb = new ArrayList<>();
            ArrayList<BarEntry> dataVals = new ArrayList<>();

            String timeingraph[] = new String[7];

            int test = 7;
            double[] arraytesteng = new double[test];
            double[] arraytestnone = new double[test];
            double[] arrayall = new double[test];


            for(int i=0;i<test;++i) {

                //for time
                SQLiteDatabase dbtime = time.getWritableDatabase();
                Cursor restime = dbtime.rawQuery("select * from " + TABLE_NAME5 +" where date = "+ getFormattedDay + " and month = " + (getFormattedMonth+1) + " and year = " + (getFormattedYear+1900),null);

                while(restime.moveToNext()){
                    totaltime += restime.getInt(8);
                }

                //for engword
                SQLiteDatabase db2 = eng.getWritableDatabase();
                Cursor res = db2.rawQuery("select * from " + TABLE_NAME2 +" where date = "+ getFormattedDay + " and month = " + (getFormattedMonth+1) + " and year = " + (getFormattedYear+1900), null);

                if (res.getCount() == 0) {
                    arraytesteng[i]=0;
                }

                while (res.moveToNext()) {
                    counteng+=res.getInt(1);
                    arraytesteng[i]+=res.getInt(1);
                }

                //for notengword
                SQLiteDatabase db3 = anothereng.getWritableDatabase();
                Cursor res3 = db3.rawQuery("select * from " + TABLE_NAME3 +" where date = "+ getFormattedDay + " and month = " + (getFormattedMonth+1) + " and year = " + (getFormattedYear+1900), null);

                if (res3.getCount() == 0) {
                    //show message
                    arraytestnone[i]=0;

                }

                while (res3.moveToNext()) {
                    countnone+=res3.getInt(1);
                    arraytestnone[i]+=res3.getInt(1);
                }


                //for continue
                SQLiteDatabase dbcon = continuemax.getWritableDatabase();
                Cursor resdbcon = dbcon.rawQuery("select * from "+TABLE_NAME10+ " where date = "+getFormattedDay+" and month = "+(getFormattedMonth+1)+
                        " and year = "+ (getFormattedYear+1900),null);

                while (resdbcon.moveToNext()){
                    checkingcon = resdbcon.getInt(1);

                    if(continuemaxweek < checkingcon){
                        continuemaxweek = checkingcon;
                    }
                }

                //for wrongword
                SQLiteDatabase dbwrong = wrongword.getWritableDatabase();
                Cursor resdbwrong = dbwrong.rawQuery("select * from "+TABLE_NAME11+" where date = "+getFormattedDay+" and month = "+(getFormattedMonth+1)+
                        " and year = "+ (getFormattedYear+1900) ,null);

                int countresdbwrong = resdbwrong.getCount();

                if(countresdbwrong==0){
                    //return;
                }

                while (resdbwrong.moveToNext()){
                    wordfromdb.add(resdbwrong.getString(1));
                }

                timeingraph[abs(i-6)] = getFormattedDay+"/"+(getFormattedMonth+1);

                c.add(Calendar.DATE, -1);
                formattedDate = df.format(c.getTime());
                getFormattedDay = c.getTime().getDate();
                getFormattedMonth = c.getTime().getMonth();
                getFormattedYear = c.getTime().getYear();

            }

            //top3
            if(wordfromdb.size()==0){
                //nothing
            }else{
                wrongwordtop3(wordfromdb);
            }

            for(int i = 0; i < test; ++i){
                arrayall[i] = arraytesteng[i];
            }

            for(int i = 0; i < test; ++i){
                arrayall[i] +=arraytestnone[i];
            }

            for (int i = 0; i < test; ++i) {
                float valueseng;
                double testvaleng = arraytesteng[abs(i-6)] / arrayall[abs(i-6)];

                if (Double.isNaN(testvaleng)) {
                    valueseng = (float) 0;
                } else {
                    valueseng = (float) testvaleng * 100;
                }

                float valuesno;
                double testvalno = arraytestnone[abs(i-6)] / arrayall[abs(i-6)];

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

            data.setValueFormatter(new DashboardFragment.MyValueFormatter());
            mChart.setData(data);//ระยะห่างระหว่างข้อมูล
            mChart.getLegend().setXEntrySpace(12);//ระยะห่างระหว่างรูปกับคำอธิบาย
            mChart.getLegend().setFormToTextSpace(3);
            XAxis xAxis = mChart.getXAxis();
            xAxis.setValueFormatter(new IndexAxisValueFormatter(timeingraph));
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setGranularity(1);
            xAxis.setCenterAxisLabels(false);
            xAxis.setGranularityEnabled(true);
            xAxis.setAxisMaximum(7);
            xAxis.setDrawGridLines(false); //เส้นตาราง

            mChart.setDragEnabled(true);
            mChart.getAxisRight().setAxisMinimum(0);
            mChart.getAxisLeft().setAxisMinimum(0);
            mChart.setVisibleXRangeMaximum(7);
            mChart.invalidate();
            mChart.animateXY(2000, 4000);
            mChart.setDoubleTapToZoomEnabled(true);
            mChart.setPinchZoom(true);
            mChart.fitScreen();

            totaltimeeng = totaltime;

            int numberOfDays = totaltime / 86400;
            int numberOfHours = (totaltime % 86400) / 3600;
            int numberOfMinutes = ((totaltime % 86400) % 3600) / 60;
            int numberOfSeconds = ((totaltime % 86400) % 3600) % 60;


            if(numberOfHours<10) {
                if (numberOfMinutes < 10) {
                    if (numberOfSeconds < 10) {
                        String text = numberOfDays + " Day \n0" + numberOfHours + " : 0" + numberOfMinutes + " : 0" + numberOfSeconds;
                        showtotaltime = text;
                    } else {
                        String text = numberOfDays + " Day  \n0" + numberOfHours + " : 0" + numberOfMinutes + " : " + numberOfSeconds;
                        showtotaltime = text;
                    }
                }else{
                    String text = numberOfDays + " Day  \n0" + numberOfHours + " : " + numberOfMinutes + " : " + numberOfSeconds;
                    showtotaltime = text;
                }
            }else{
                showtotaltime = numberOfDays+" Day  \n"+numberOfHours + " : " + numberOfMinutes + " : " + numberOfSeconds;
            }

        }

        c.add(Calendar.DATE, +7);
        formattedDate = df.format(c.getTime());
        getFormattedDay = c.getTime().getDate();
        getFormattedMonth = c.getTime().getMonth();
        getFormattedYear = c.getTime().getYear();

        totalwordeng = counteng;

        showtotalword = counteng+ " / "+(counteng+countnone);

    }


    void wordmin(){

        double total = (double) totalwordeng * 1.0;
        double total2 = (double) totaltimeeng/60.0;

        if(total2==0){
            total2 = 1;
        }

        wordminweek = total/total2;

    }




    public void wrongwordtop3(ArrayList<String> wordfromdb){


        for (int i = 0; i < 3; i++) {
            wordtop[i] = "";
        }

        for (int i = 0; i < 3; i++) {
            wordtrans[i] = "";
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