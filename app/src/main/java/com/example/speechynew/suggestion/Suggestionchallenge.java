package com.example.speechynew.suggestion;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.speechynew.R;

public class Suggestionchallenge extends AppCompatActivity {

    TextView suggestcha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_suggestionchallenge);

        suggestcha = findViewById(R.id.suggestcha);



    }


}
