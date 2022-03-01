package com.example.speechynew;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import java.util.Date;

public class SplashscreenActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splashscreen);



        final Handler hand = new Handler();
        Runnable run = new Runnable() {
            @Override
            public void run() {

                startActivity(new Intent(SplashscreenActivity.this,MainActivity.class));
            }
        };
        hand.postDelayed(run,2000);

    }
}
