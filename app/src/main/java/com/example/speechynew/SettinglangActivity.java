package com.example.speechynew;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.speechynew.connectDB.Setting;
import com.example.speechynew.suggestion.Suggestionchallenge;
import com.example.speechynew.suggestion.Suggestionpercent;
import com.webianks.library.scroll_choice.ScrollChoice;

import java.util.ArrayList;
import java.util.List;

import static com.example.speechynew.connectDB.Settinginterface.CHADAY;
import static com.example.speechynew.connectDB.Settinginterface.NATIVELANG;
import static com.example.speechynew.connectDB.Settinginterface.PERCENTAGENONE;
import static com.example.speechynew.connectDB.Settinginterface.TABLE_NAME0;

public class SettinglangActivity extends AppCompatActivity {

    int MIC_PERMISSION_CODE = 1;

    List<String> datas = new ArrayList<>();

    TextView textView;
    TextView errorpercent;
    TextView errorchallenge;
    ScrollChoice scroll;
    Button ok;
    EditText edchaday;
    EditText percent;
    ImageButton suggestpercent;
    ImageButton suggestchallenge;

    String secondLang;
    Setting setting;
    String chaday;
    double perc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_settinglang);

        setting = new Setting(this);

        ok = (Button)findViewById(R.id.button);
        textView = (TextView)findViewById(R.id.result);
        scroll = (ScrollChoice) findViewById(R.id.scroll_choice);
        edchaday = findViewById(R.id.editchaday);
        percent = findViewById(R.id.editpercent);
        errorpercent = findViewById(R.id.errorpercent);
        errorchallenge = findViewById(R.id.errorchallenge);
        suggestpercent = findViewById(R.id.suggestpercent);
        suggestchallenge = findViewById(R.id.suggestchallenge);


        //set text from database
        SQLiteDatabase db = setting.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME0, null);
        int countres = res.getCount();
        if(countres==0){
            //set default
            edchaday.setText("1000");
            percent.setText("33.33");
            chaday = "1000";
            perc = 33.33;
        }else{
            while (res.moveToNext()){
                perc = res.getDouble(2);
                chaday = res.getString(3);
            }
            edchaday.setText(chaday);
            percent.setText(String.valueOf(perc));
        }

        secondLang = "th-TH"; //start not set
        textView.setText("Native language : Thai");

        if(ContextCompat.checkSelfPermission(SettinglangActivity.this,
                Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED){
                //already permission
        }else{
            requestMicpermission();
        }

        suggestpercent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), Suggestionpercent.class);
                startActivity(in);
            }
        });

        suggestchallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), Suggestionchallenge.class);
                startActivity(in);
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if( percent.getText().toString().equals("") || Double.parseDouble(percent.getText().toString())>100 ||
                    Double.parseDouble(percent.getText().toString())<0 ) {
                    errorpercent.setText("Please set should be set at least 0 and not more than 100.\nFor speaking other languages in speaking English");
                }else{
                    errorpercent.setText("");
                }

                if(edchaday.getText().toString().equals("")){
                    errorchallenge.setText("Please set your challenge");
                }else{
                    errorchallenge.setText("");
                }

                if(!edchaday.getText().toString().equals("")){
                    if( !percent.getText().toString().equals("") && Double.parseDouble(percent.getText().toString())<=100 &&
                            Double.parseDouble(percent.getText().toString())>=0){

                        perc = Double.parseDouble(percent.getText().toString());
                        chaday = edchaday.getText().toString();

                        //Set database
                        SQLiteDatabase db = setting.getWritableDatabase();
                        Cursor res = db.rawQuery("select * from " + TABLE_NAME0, null);
                        int countres = res.getCount();

                        if(countres==0){
                            ContentValues value = new ContentValues();
                            value.put(NATIVELANG,secondLang);
                            value.put(PERCENTAGENONE,perc);
                            value.put(CHADAY,chaday);
                            db.insertOrThrow(TABLE_NAME0,null,value);
                        }else{
                            setting.update(secondLang,perc,chaday);
                        }

                        //Go to next page
                        openActivity2();
                    }
                }

            }
        });

        //add language in scroll
        loadData();

        scroll.addItems(datas,2);
        scroll.setOnItemSelectedListener(new ScrollChoice.OnItemSelectedListener() {
            @Override
            public void onItemSelected(ScrollChoice scrollChoice, int position, String name) {

                textView.setText("Native language : "+name);

                if(name.equals("Thai")){
                    secondLang="th-TH";
                }else if(name.equals("Japan")){
                    secondLang="ja-JP";
                }else if(name.equals("Chinese")){
                    secondLang="zh";
                }else if(name.equals("Combodia")){
                    secondLang="km-KH";
                }else if(name.equals("Laos")){
                    secondLang="lo-LA";
                }else if(name.equals("Korean")){
                    secondLang="ko-KR";
                }else if(name.equals("India")){
                    secondLang="hi-IN";
                }else if(name.equals("Arabic (United Arab Emirates)")){
                    secondLang="ar-AE";
                }else if(name.equals("Nepal")){
                    secondLang="ne-NP";
                }else if(name.equals("Myanmar")){
                    secondLang="my-MM";
                }

            }
        });
    }



    void openActivity2(){
        Intent intent = new Intent(SettinglangActivity.this,MainActivity.class);
        startActivity(intent);
    }


    private void loadData(){
        datas.add("Chinese");
        datas.add("Japan");
        datas.add("Thai");
        datas.add("Laos");
        datas.add("Korean");
        datas.add("India");
        datas.add("Myanmar");
        datas.add("Arabic (United Arab Emirates)");
        datas.add("Combodia");
        datas.add("Nepal");
    }

    private void requestMicpermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.RECORD_AUDIO)){
            //already
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(SettinglangActivity.this,new String[]{Manifest.permission.RECORD_AUDIO},MIC_PERMISSION_CODE);

                        }
                    }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create().show();

        }else{
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},MIC_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == MIC_PERMISSION_CODE) {
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){ }else{ }
        }
    }
}
