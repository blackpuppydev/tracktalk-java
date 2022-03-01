package com.example.speechynew.analysis;

import android.content.Context;

import java.util.ArrayList;

public class Checkalphabet {

    ArrayList<String> anyy;
    String[] lang;
    Context context;
    int counttime;


    Checkalphabet(ArrayList anyy,Context context,int counttime){

        this.anyy = new ArrayList<>();
        this.lang = new String[anyy.size()];
        this.anyy = anyy;
        this.context = context;
        this.counttime = counttime;

        checkAlfabet();
    }

    void checkAlfabet(){

        //alphabet in english
        char[] Alphabet = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
                'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};

        for(int i=0;i<anyy.size();++i){

            //first alphabet of word
            char detect = anyy.get(i).charAt(0);

            //check first alphabet equal Alphabet array
            for(int j=0;j<Alphabet.length;++j){
                if(detect == Alphabet[j]){
                    lang[i] = "en";
                }
            }
            //lang is null
            if(lang[i]==null){
                lang[i] = "unknow";
            }
        }


        //for print
        String wordding = " ";
        String langofword = " ";

        for(int i=0;i<anyy.size();++i){
            wordding += anyy.get(i)+" ";
        }

        for(int i=0;i<lang.length;++i){
            langofword += lang[i]+" ";
        }

        System.out.println(wordding);
        System.out.println(langofword);

        //for check environment of eng
        CheckEnvi ce = new CheckEnvi(anyy,lang,counttime,context);

    }
}
