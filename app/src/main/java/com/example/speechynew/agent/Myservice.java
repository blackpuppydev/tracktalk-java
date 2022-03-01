package com.example.speechynew.agent;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.speechynew.MainActivity;
import com.example.speechynew.R;
import com.example.speechynew.analysis.Detectword;
import com.example.speechynew.connectDB.Engword;
import com.example.speechynew.connectDB.Rawdata;
import com.example.speechynew.connectDB.Setting;
import com.example.speechynew.connectDB.Timeprocess;
import com.example.speechynew.connectDB.Word;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


import static android.speech.SpeechRecognizer.*;

import static com.example.speechynew.connectDB.Engwordinterface.TABLE_NAME2;
import static com.example.speechynew.connectDB.Rawdatainterface.ALLWORD;
import static com.example.speechynew.connectDB.Rawdatainterface.DATEWORD;
import static com.example.speechynew.connectDB.Rawdatainterface.DAYWORD;
import static com.example.speechynew.connectDB.Rawdatainterface.HOURWORD;
import static com.example.speechynew.connectDB.Rawdatainterface.MINUTEWORD;
import static com.example.speechynew.connectDB.Rawdatainterface.MONTHWORD;
import static com.example.speechynew.connectDB.Rawdatainterface.SECONDWORD;
import static com.example.speechynew.connectDB.Rawdatainterface.TABLE_NAME4;
import static com.example.speechynew.connectDB.Rawdatainterface.YEARWORD;
import static com.example.speechynew.connectDB.Settinginterface.TABLE_NAME0;


public class Myservice extends Service {

    //Database
    Engword eng;
    Word anothereng;
    Rawdata raw;
    Setting setting;
    Timeprocess time;

    //set sound when speech recognition working
    private AudioManager mAudioManager;
    private int mStreamVolume = 0;

    //setting google speech recognition
    private SpeechRecognizer speechRecognizer;
    private Intent speechRecognizerIntent;
    private String keeper = "";

    String nativelangg = " ";
    int challenge = 0;
    int totalchallenge=0;

    int timeprocessstart; //start speak
    int timeprocessstop; //stop speak
    int counttime; //time to speak

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //show popup
        Toast.makeText(this, "Start.", Toast.LENGTH_LONG).show();
        super.onStartCommand(intent, flags, startId);

        //create object database
        eng = new Engword(this);
        anothereng = new Word(this);
        raw = new Rawdata(this);
        time = new Timeprocess(this);
        setting = new Setting(this);

        //set default value
        timeprocessstart=0;
        timeprocessstop=0;
        counttime=0;

        //use for mute sound
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        //get native lang from database setting
        SQLiteDatabase setter = setting.getWritableDatabase();
        Cursor res = setter.rawQuery("select * from " + TABLE_NAME0, null);

        while (res.moveToNext()) {
            nativelangg = res.getString(1);
            challenge = res.getInt(3);
        }

        System.out.println(nativelangg);
        System.out.println(challenge);



        //setting speech recognition
        speechRecognizer = createSpeechRecognizer(this);
        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH); //start for speaking
        //set language model
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        //set language for use (if not set then use dafault language on phone)
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, nativelangg);
        //show confidence score
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CONFIDENCE_SCORES,true);

        //start Speechrecognizer
        speechRecognizer.startListening(speechRecognizerIntent);

        //Method recognition
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {
                System.out.println("onReadyForSpeech");
            }

            @Override
            public void onBeginningOfSpeech() {
                //The user has started to speak.

                //Start time for speaking
                timeprocessstart = (new Date().getHours()*3600)+(new Date().getMinutes()*60)+new Date().getSeconds();

                System.out.println("onBeginningOfSpeech");
            }

            @Override
            public void onRmsChanged(float rmsdB) {
                //The sound level in the audio stream has changed.
            }

            @Override
            public void onBufferReceived(byte[] buffer) {
                //More sound has been received.
            }

            @Override
            public void onEndOfSpeech() {

                //End time for speaking (second)
                timeprocessstop = (new Date().getHours()*3600)+(new Date().getMinutes()*60)+new Date().getSeconds();

                //Stop time - Start time ex. 00:00:50 - 00:00:55 = 5 sec
                counttime = timeprocessstop-timeprocessstart;

                System.out.println("onEndOfSpeech");
            }

            @Override
            public void onError(int error) {
                //A network or recognition error occurred.

                switch (error) {
                    case ERROR_SPEECH_TIMEOUT: //6
                        System.out.println("Timeout");
                        speechRecognizer.cancel();
                        speechRecognizer.startListening(speechRecognizerIntent);
                        break;

                    case ERROR_AUDIO: //3
                        System.out.println("Error audio");
                        speechRecognizer.cancel();
                        speechRecognizer.startListening(speechRecognizerIntent);
                        break;

                    case ERROR_CLIENT: //5
                        System.out.println("ERROR_CLIENT");
                        speechRecognizer.cancel();
                        speechRecognizer.startListening(speechRecognizerIntent);
                        break;

                    case ERROR_NETWORK_TIMEOUT: //1
                        System.out.println("ERROR_NETWORK_TIMEOUT");
                        speechRecognizer.cancel();
                        speechRecognizer.startListening(speechRecognizerIntent);
                        break;

                    case ERROR_NETWORK:
                        System.out.println("ERROR_NETWORK");
                        speechRecognizer.cancel();
                        speechRecognizer.startListening(speechRecognizerIntent);
                        break;

                    case ERROR_RECOGNIZER_BUSY: //8 *****
                        System.out.println("ERROR_RECOGNIZER_BUSY");
                        speechRecognizer.cancel();
                        speechRecognizer.startListening(speechRecognizerIntent);
                        break;
                }

                speechRecognizer.cancel();
                speechRecognizer.startListening(speechRecognizerIntent);

            }

            @Override
            public void onResults(Bundle results) {
                //Called when recognition results are ready.

                //Word from speech recognition and confidence score
                ArrayList<String> matchesfound = results.getStringArrayList(RESULTS_RECOGNITION);
                float[] checkconfi = results.getFloatArray(CONFIDENCE_SCORES);

                //Not null
                if (matchesfound != null) {

                    keeper = matchesfound.get(0);

                    for(int i=0;i<matchesfound.size();++i){
                        System.out.println("Word "+(i+1)+" : "+matchesfound.get(i));
                    }

                    for(int i=0;i<checkconfi.length;++i){
                        System.out.println("Confidence : " + checkconfi[i]);
                    }


                    //Show word to test
                    //Toast.makeText(Myservice.this, "Word = " + keeper, Toast.LENGTH_SHORT).show();

                    //save to database
                    SQLiteDatabase dball = raw.getWritableDatabase();
                    ContentValues valuesall = new ContentValues();
                    valuesall.put(ALLWORD, keeper);
                    valuesall.put(DAYWORD, new Date().getDay());//start at 0 is Sunday
                    valuesall.put(DATEWORD, new Date().getDate());
                    valuesall.put(MONTHWORD, new Date().getMonth() + 1); //start at 0
                    valuesall.put(YEARWORD, new Date().getYear() + 1900); //format is This year - 1900
                    valuesall.put(HOURWORD, new Date().getHours());
                    valuesall.put(MINUTEWORD, new Date().getMinutes());
                    valuesall.put(SECONDWORD, new Date().getSeconds());
                    dball.insertOrThrow(TABLE_NAME4, null, valuesall);


                    //sent to analysis (Detecword is first)
                    Detectword d = new Detectword(nativelangg , counttime ,Myservice.this);

                    if(nativelangg.equals("th-TH")){
                        d.cutword(); //use pythai
                    }else{
                        d.addanyword(); //use breakiterator
                    }

                    //check challenge
                    gotchallenge();

                    //getting system volume into var for later un-muting
                    mStreamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    //setting system volume to zero, muting
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);

                    keeper = "";

                    speechRecognizer.cancel();
                    speechRecognizer.startListening(speechRecognizerIntent);
                }

                System.out.println("onResults");
            }

            @Override
            public void onPartialResults(Bundle partialResults) {
                //Called when partial recognition results are available.
                speechRecognizer.cancel();
                speechRecognizer.startListening(speechRecognizerIntent);
                //System.out.println("onPartialResults");
            }

            @Override
            public void onEvent(int eventType, Bundle params) {
                //Reserved for adding future events.
                //System.out.println("onEvent");
            }
        });


        //we have some options for service
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        speechRecognizer.cancel();
        timeprocessstart=0;
        timeprocessstop=0;

        Toast.makeText(this, "Stop.", Toast.LENGTH_LONG).show();

    }



    void gotchallenge(){

        SQLiteDatabase dbeng = eng.getWritableDatabase();
        Cursor reseng = dbeng.rawQuery("select * from "+TABLE_NAME2+" where date = "+new Date().getDate()+" and month = "+
                (new Date().getMonth()+1)+" and year = "+(new Date().getYear()+1900),null);

        while(reseng.moveToNext()){
            totalchallenge+=reseng.getInt(1);
        }

        if(totalchallenge>=challenge){
            if(challenge==0){
                //App is start first time
            }else{
                notificationcall();
            }

        }

        //check when open MainActivity
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("chaActive", true)) {//open MainActivity
            Intent intent1 = new Intent(this,MainActivity.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent1);
            System.out.println("Open MainActivity Service");
        }else{//not open
            System.out.println("Not open MainActivity Service");
        }

    }


    private void notificationcall() {

        if(challenge==0){
            //App is start first time
        }else {

            // Builds your notification
            NotificationCompat.Builder builder = new NotificationCompat.Builder(Myservice.this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Complete!")
                    .setContentText("Your challenge is successful! You can check challenge now.");

            // Creates the intent needed to show the notification
            Intent notificationIntent = new Intent(this, MainActivity.class);                           //PendingIntent.FLAG_ONE_SHOT
            PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_ONE_SHOT);
            builder.setContentIntent(contentIntent);
            builder.setAutoCancel(true);


            // Add as notification
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(0, builder.build());
        }
    }
}


