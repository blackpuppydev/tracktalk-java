package com.example.speechynew.analysis;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.speechynew.connectDB.Rawdata;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static com.example.speechynew.connectDB.Rawdatainterface.TABLE_NAME4;


public class Detectword{

    String nativelangg;
    Context context;
    String keeper;
    int countime;

    Rawdata raw;

    public Detectword(String nativelangg,int counttime,Context context){

        this.nativelangg = nativelangg;
        this.countime = counttime;
        this.context = context;

        raw = new Rawdata(context);

        //get data in rawdata now
        SQLiteDatabase dblang = raw.getWritableDatabase();
        Cursor reslang = dblang.rawQuery("select * from "+TABLE_NAME4+" where date = "+new Date().getDate()+" and month = "+
                (new Date().getMonth()+1)+" and year = "+(new Date().getYear()+1900)+" and hour = "+
                new Date().getHours()+" and minute = "+new Date().getMinutes()+" and second = "+
                new Date().getSeconds(),null);

        while (reslang.moveToNext()){
            keeper = reslang.getString(1);
        }
    }


    public void addanyword(){

        String showword=""; //test print
        ArrayList<String> anyy = new ArrayList<>();

        BreakIterator boundary = BreakIterator.getWordInstance(Locale.forLanguageTag(nativelangg));
        boundary.setText(keeper);

        int count=0;
        int start = boundary.first();
        for (int end = boundary.next(); end != BreakIterator.DONE; end = boundary.next()) {
            if(keeper.substring(start, end).equals(" ")){
                //Not counting " "
            }else{
                //counting and substring
                count++;
                anyy.add(keeper.substring(start,end));

                //Test print
                showword+=keeper.substring(start, end)+" ";
            }
            start = end;
        }

        System.out.println(count+ " " + showword);

        Checkalphabet ca = new Checkalphabet(anyy,context,countime);

    }


    public void cutword(){

        //"http://192.168.1.19:5000/cut?text="+keeper+"";
        //"http://172.20.10.8:5000/cut?text="+keeper+""; current
        //"http://192.168.1.40:5000/cut?text="+keeper+"";

        //use library Pythainlp
        if(nativelangg.equals("th-TH")){

            String URL = "http://192.168.1.42:5000/cut?text="+keeper+"";
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            JsonArrayRequest objectRequest = new JsonArrayRequest(
                    Request.Method.GET,
                    URL, null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            ArrayList<String> addword = new ArrayList<>();
                            int counttest = 0;
                            for(int i=0;i<response.length();++i){
                                try {
                                    if(response.get(i).toString().equals(" ")){ }
                                    else{
                                        counttest++;
                                        addword.add(response.get(i).toString());
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            //test print
                            System.out.println(counttest);
                            System.out.print(" ' ");
                            for(int i=0;i<addword.size();++i){
                                System.out.print(addword.get(i)+" ");
                            }
                            System.out.println(" ' ");

                            Checkalphabet ca = new Checkalphabet(addword,context,countime);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println(error.toString());
                        }
                    });

            //how to fix com.android.volley.timeouterror
            objectRequest.setRetryPolicy(new RetryPolicy() {
                @Override
                public int getCurrentTimeout() {
                    return 50000;
                }
                @Override
                public int getCurrentRetryCount() {
                    return 50000;
                }
                @Override
                public void retry(VolleyError error) throws VolleyError { }
            }); //end
            requestQueue.add(objectRequest);
        } else{



            //use BreakIterator first.

            //NLP other lang


        }


    }




}
