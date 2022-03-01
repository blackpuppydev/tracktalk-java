package com.example.speechynew;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.toolbox.StringRequest;

import java.util.Calendar;

public class SearchActivity extends AppCompatActivity {

    Button setdate;
    Button setstarttime;
    Button setstoptime;
    Button btnok;
    TextView setagain;

    int dateset;
    int monthset;
    int yearset;
    int starthourset;
    int startminuteset;
    int stophourset;
    int stopminuteset;

    String starttext = "";
    String stoptext = "";

    Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

    Calendar mcurrentTimeStart = Calendar.getInstance();
    int hourStart = mcurrentTimeStart.get(Calendar.HOUR_OF_DAY);
    int minuteStart = mcurrentTimeStart.get(Calendar.MINUTE);

    Calendar mcurrentTimeStop = Calendar.getInstance();
    int hourStop = mcurrentTimeStop.get(Calendar.HOUR_OF_DAY);
    int minuteStop = mcurrentTimeStop.get(Calendar.MINUTE);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        setdate = findViewById(R.id.setdate);
        setstarttime = findViewById(R.id.settimeStart);
        setstoptime = findViewById(R.id.settimeStop);
        setagain = findViewById(R.id.setagain);
        btnok = findViewById(R.id.btnok);

        dateset = dayOfMonth;
        monthset = month+1;
        yearset = year;
        starthourset = hourStart;
        startminuteset = minuteStart;
        stophourset = hourStop;
        stopminuteset = minuteStop;

        setdate.setText(dayOfMonth+"/"+(month+1)+"/"+year);

        //check addstart<10
        if(hourStart<10){
            if(minuteStart<10){
                starttext = "0"+hourStart+":0"+minuteStart;
            }else{
                starttext = "0"+hourStart+":"+minuteStart;
            }
        }else{
            if(minuteStart<10){
                starttext = hourStart+":0"+minuteStart;
            }else{
                starttext = hourStart+":"+minuteStart;
            }
        }
        setstarttime.setText(starttext);

        //check addstop<10
        if(hourStop<10){
            if(minuteStop<10) {
                stoptext = "0" + hourStop + ":0" + minuteStop;
            }else{
                stoptext = "0" + hourStop + ":" + minuteStop;
            }
        }else{
            if(minuteStop<10) {
                stoptext = hourStop + ":0" + minuteStop;
            }else{
                stoptext = hourStop + ":" + minuteStop;
            }
        }
        setstoptime.setText(stoptext);



        setdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(SearchActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                                month+=1;
                                dateset = day;
                                monthset = month;
                                yearset = year;
                                setdate.setText(day + "/" + month + "/" + year);
                            }
                        }, year, month, dayOfMonth);

                datePickerDialog.show();

            }
        });

        setstarttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(SearchActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        starthourset = selectedHour;
                        startminuteset = selectedMinute;

                        if(selectedHour<10){
                            if(selectedMinute<10){
                                starttext = "0"+selectedHour+":0"+selectedMinute;
                            }else{
                                starttext = "0"+selectedHour+":"+selectedMinute;
                            }
                        }else{
                            if(selectedMinute<10){
                                starttext = selectedHour+":0"+selectedMinute;
                            }else{
                                starttext = selectedHour+":"+selectedMinute;
                            }
                        }

                        setstarttime.setText(starttext);
                    }
                }, hourStart, minuteStart, true);//Yes 24 hour time

                mTimePicker.show();
            }
        });

        setstoptime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(SearchActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        stophourset = selectedHour;
                        stopminuteset = selectedMinute;

                        if(selectedHour<10){
                            if(selectedMinute<10){
                                stoptext = "0"+selectedHour+":0"+selectedMinute;
                            }else{
                                stoptext = "0"+selectedHour+":"+selectedMinute;
                            }
                        }else{
                            if(selectedMinute<10){
                                stoptext = selectedHour+":0"+selectedMinute;
                            }else{
                                stoptext = selectedHour+":"+selectedMinute;
                            }
                        }

                        setstoptime.setText(stoptext);
                    }
                }, hourStop, minuteStop, true);//Yes 24 hour time

                mTimePicker.show();
            }
        });


        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int start = (starthourset*3600)+(startminuteset*60);
                int stop = (stophourset*3600)+(stopminuteset*60);

                if(stop <= start){
                    setagain.setText("Please set time again.");
                    //nothing
                }else{
                    Intent intent = new Intent(SearchActivity.this,ViewsearchActivity.class);
                    intent.putExtra("date", String.valueOf(dateset));
                    intent.putExtra("month",String.valueOf(monthset));
                    intent.putExtra("year",String.valueOf(yearset));
                    intent.putExtra("starthour",String.valueOf(starthourset));
                    intent.putExtra("startminute",String.valueOf(startminuteset));
                    intent.putExtra("stophour",String.valueOf(stophourset));
                    intent.putExtra("stopminute",String.valueOf(stopminuteset));
                    startActivity(intent);
                }
            }
        });

    }




}
