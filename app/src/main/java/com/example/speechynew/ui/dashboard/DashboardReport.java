package com.example.speechynew.ui.dashboard;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.speechynew.R;

public class DashboardReport extends AppCompatActivity {


    TextView date;
    TextView showtotalwordweek;
    TextView showtotaltimeweek;
    TextView showtotalwordminweek;
    TextView showcontinuemaxweek;
    TextView showwordtop;


    String formatedate = "";
    String totalwordweek = "";
    String totaltimeweek = "";
    int continuemaxweek = 0;
    double wordminweek;
    String[] wordtop = new String[3];
    String[] wordtrans = new String[3];
    String totalwordtop = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_dashboard_report);

        date = findViewById(R.id.test);
        showtotalwordweek = findViewById(R.id.totalwordweek);
        showtotaltimeweek = findViewById(R.id.totaltimeweek);
        showtotalwordminweek = findViewById(R.id.totalwordminweek);
        showcontinuemaxweek = findViewById(R.id.totalcontinueweek);
        showwordtop = findViewById(R.id.wordtopweek);

        formatedate = getIntent().getStringExtra("test");
        totalwordweek = getIntent().getStringExtra("totalwordweek");
        totaltimeweek = getIntent().getStringExtra("totaltimeweek");
        wordminweek = getIntent().getDoubleExtra("wordminweek",wordminweek);
        continuemaxweek = getIntent().getIntExtra("continuemaxweek",continuemaxweek);
        wordtop = getIntent().getStringArrayExtra("wordtop");
        wordtrans = getIntent().getStringArrayExtra("wordtrans");


        date.setText(formatedate);
        showtotalwordweek.setText(totalwordweek);
        showtotaltimeweek.setText(totaltimeweek);
        showtotalwordminweek.setText(String.format("%.2f",wordminweek));
        showcontinuemaxweek.setText(String.valueOf(continuemaxweek));

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
