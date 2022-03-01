package com.example.speechynew.connectDB;

import android.provider.BaseColumns;

public interface Schedulerinterface extends BaseColumns {

    String TABLE_NAME9 = "scheduler";
    String DAYSTART = "day";
    String DATESTART = "date";
    String MONTHSTART = "month";
    String YEARSTART = "year";
    String TIMESTARTHOUR = "starthour";
    String TIMESTARTMINUTE = "startminute";
    String TIMESTOPHOUR = "stophour";
    String TIMESTOPMINUTE = "stopminute";
    String STATUS = "status";

}
