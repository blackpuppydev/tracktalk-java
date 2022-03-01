package com.example.speechynew.ui.notifications;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.speechynew.R;

public class NotificationsReport extends AppCompatActivity {

    TextView date;
    TextView showtotalwordmonth;
    TextView showtotaltimemonth;
    TextView showtotalwordminmonth;
    TextView showcontinuemaxmonth;
    TextView showwordtop;

    String formatedate = "";
    String totalwordmonth = "";
    String totaltimemonth = "";
    double wordminmonth;
    int continuemaxmonth;

    String[] wordtop = new String[3];
    String[] wordtrans = new String[3];
    String totalwordtop="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_notifications_report);

        date = findViewById(R.id.test);
        showtotalwordmonth = findViewById(R.id.totalwordmonth);
        showtotaltimemonth = findViewById(R.id.totaltimemonth);
        showtotalwordminmonth = findViewById(R.id.totalwordminmonth);
        showcontinuemaxmonth = findViewById(R.id.totalcontinuemonth);
        showwordtop = findViewById(R.id.wordtopmonth);

        formatedate = getIntent().getStringExtra("datemonth");
        totalwordmonth = getIntent().getStringExtra("showtotalwordmonth");
        totaltimemonth = getIntent().getStringExtra("showtotaltimemonth");
        wordminmonth = getIntent().getDoubleExtra("wordminmonth",wordminmonth);
        continuemaxmonth = getIntent().getIntExtra("continuemaxmonth",continuemaxmonth);
        wordtop = getIntent().getStringArrayExtra("wordtopmonth");
        wordtrans = getIntent().getStringArrayExtra("wordtrans");

        date.setText(formatedate);
        showtotalwordmonth.setText(totalwordmonth);
        showtotaltimemonth.setText(totaltimemonth);
        showtotalwordminmonth.setText(String.format("%.2f",wordminmonth));
        showcontinuemaxmonth.setText(String.valueOf(continuemaxmonth));

        for(int i=0;i<wordtop.length;++i){

            if(i == wordtop.length-1){
                if(wordtop[i].equals("")){}
                else{
                    totalwordtop += wordtop[i] +" = "+ wordtrans[i]+"  ";
                }
            }else{
                if(wordtop[i].equals("")){}
                else{
                    totalwordtop += wordtop[i] +" = "+ wordtrans[i]+", ";
                }
            }

        }

        if(totalwordtop.equals("")){
            showwordtop.setText("-");
        }else{
            showwordtop.setText(totalwordtop);
        }

    }

}
