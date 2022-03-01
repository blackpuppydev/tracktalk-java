package com.example.speechynew;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class ViewsearchreportActivity extends AppCompatActivity {

    TextView showdatesearch;
    TextView showtotalwordsearch;
    TextView showtotaltimesearch;
    TextView showwordminsearch;
    TextView showcontinuesearch;
    TextView showwordtopsearch;

    String formatedatesearch = "";
    String totalwordsearch = "";
    String totaltimesearch = "";
    double wordminsearch;
    int continuemaxsearch;

    String[] wordtop = new String[3];
    String[] wordtrans = new String[3];
    String totalwordtop="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_viewsearchreport);

        showdatesearch = findViewById(R.id.datesearch);
        showtotalwordsearch = findViewById(R.id.totalwordsearch);
        showtotaltimesearch = findViewById(R.id.totaltimesearch);
        showwordminsearch = findViewById(R.id.totalwordminsearch);
        showcontinuesearch = findViewById(R.id.totalcontinuesearch);
        showwordtopsearch = findViewById(R.id.wordtopsearch);

        formatedatesearch = getIntent().getStringExtra("showdatesearch");
        totalwordsearch = getIntent().getStringExtra("totalwordsearch");
        totaltimesearch = getIntent().getStringExtra("totaltimesearch");
        wordminsearch = getIntent().getDoubleExtra("wordminsearch",wordminsearch);
        continuemaxsearch = getIntent().getIntExtra("continuemaxsearch",continuemaxsearch);
        wordtop = getIntent().getStringArrayExtra("wordtop");
        wordtrans = getIntent().getStringArrayExtra("wordtrans");


        showdatesearch.setText(formatedatesearch);
        showtotalwordsearch.setText(totalwordsearch);
        showtotaltimesearch.setText(totaltimesearch);
        showwordminsearch.setText(String.format("%.2f",wordminsearch));
        showcontinuesearch.setText(String.valueOf(continuemaxsearch));

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
            showwordtopsearch.setText("-");
        }else{
            showwordtopsearch.setText(totalwordtop);
        }


    }


}
