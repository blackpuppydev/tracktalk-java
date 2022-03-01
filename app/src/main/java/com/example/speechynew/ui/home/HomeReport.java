package com.example.speechynew.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.speechynew.R;

public class HomeReport extends AppCompatActivity {


    TextView showday;
    TextView showtotalwordday;
    TextView showtotaltimeday;
    TextView showwordminday;
    TextView showcontinuemaxday;
    TextView showwordtopday;


    String formatedate = "";
    String totalwordday = "";
    String totaltimeday = "";
    double wordminday;
    int continuemaxday;

    String[] wordtop = new String[3];
    String[] wordtrans = new String[3];
    String totalwordtop="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_home_report);

        showday = findViewById(R.id.test);
        showtotalwordday = findViewById(R.id.totalwordday);
        showtotaltimeday = findViewById(R.id.totaltimeday);
        showwordminday = findViewById(R.id.totalwordminday);
        showcontinuemaxday = findViewById(R.id.totalcontinueday);
        showwordtopday = findViewById(R.id.wordtopday);

        formatedate = getIntent().getStringExtra("test");
        totalwordday = getIntent().getStringExtra("totalwordday");
        totaltimeday = getIntent().getStringExtra("totaltimeday");
        wordminday = getIntent().getDoubleExtra("wordminday",wordminday);
        continuemaxday = getIntent().getIntExtra("continuemaxday",continuemaxday);
        wordtop = getIntent().getStringArrayExtra("wordtop");
        wordtrans = getIntent().getStringArrayExtra("wordtrans");


        showday.setText(formatedate);
        showtotalwordday.setText(totalwordday);
        showtotaltimeday.setText(totaltimeday);
        showwordminday.setText(String.format("%.2f",wordminday));
        showcontinuemaxday.setText(String.valueOf(continuemaxday));

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
            showwordtopday.setText("-");
        }else{
            showwordtopday.setText(totalwordtop);
        }




    }

}
