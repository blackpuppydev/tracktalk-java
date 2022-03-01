package com.example.speechynew;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class Viewschedulerreport extends AppCompatActivity {

    TextView showdatescheduler;
    TextView showtotalwordscheduler;
    TextView showtotaltimescheduler;
    TextView showwordminscheduler;
    TextView showcontinuescheduler;
    TextView showwordtopscheduler;

    String formatedatescheduler = "";
    String totalwordscheduler = "";
    String totaltimescheduler = "";
    double wordminscheduler;
    int continuemaxscheduler;

    String[] wordtopscheduler = new String[3];
    String[] wordtranscheduler = new String[3];
    String totalwordtop="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_viewschedulerreport);

        showdatescheduler = findViewById(R.id.datescheduler);
        showtotalwordscheduler = findViewById(R.id.totalwordscheduler);
        showtotaltimescheduler = findViewById(R.id.totaltimescheduler);
        showwordminscheduler = findViewById(R.id.totalwordminscheduler);
        showcontinuescheduler = findViewById(R.id.totalcontinuescheduler);
        showwordtopscheduler = findViewById(R.id.wordtopscheduler);

        formatedatescheduler = getIntent().getStringExtra("showdatescheduler");
        totalwordscheduler = getIntent().getStringExtra("totalwordscheduler");
        totaltimescheduler = getIntent().getStringExtra("totaltimescheduler");
        wordminscheduler = getIntent().getDoubleExtra("wordminscheduler",wordminscheduler);
        continuemaxscheduler = getIntent().getIntExtra("continuemaxscheduler",continuemaxscheduler);
        wordtopscheduler = getIntent().getStringArrayExtra("wordtopscheduler");
        wordtranscheduler = getIntent().getStringArrayExtra("wordtranscheduler");

        showdatescheduler.setText(formatedatescheduler);
        showtotalwordscheduler.setText(totalwordscheduler);
        showtotaltimescheduler.setText(totaltimescheduler);
        showwordminscheduler.setText(String.format("%.2f",wordminscheduler));
        showcontinuescheduler.setText(String.valueOf(continuemaxscheduler));

        for(int i=0;i<wordtopscheduler.length;++i){

            if(i == wordtopscheduler.length-1){
                if(wordtopscheduler[i].equals("")){}
                else{
                    totalwordtop += wordtopscheduler[i] +" = "+ wordtranscheduler[i]+"  ";
                }
            }else{
                if(wordtopscheduler[i].equals("")){}
                else{
                    totalwordtop += wordtopscheduler[i] +" = "+ wordtranscheduler[i]+", ";
                }
            }

        }

        if(totalwordtop.equals("")){
            showwordtopscheduler.setText("-");
        }else{
            showwordtopscheduler.setText(totalwordtop);
        }
    }



}
