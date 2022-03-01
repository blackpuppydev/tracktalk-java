package com.example.speechynew.analysis;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Translator {

    String word;
    Context context;
    String transword = "";

    public Translator(String word, Context context){
        this.word = word;
        this.context = context;

    }


    public String trans(){


        //"http://172.20.10.8:5000/trans?text="+word+"";
        //"http://192.168.1.40:5000/trans?text="+word+"";
        String URL = "http://192.168.1.42:5000/trans?text="+word+""; //from this mac
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            transword = response.getString("TRANS");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //test print
                        System.out.println(word+" translator : "+transword);
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
            public void retry(VolleyError error) throws VolleyError {

            }
        }); //end

        requestQueue.add(objectRequest);

        return transword;

    }

}
