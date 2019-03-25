package com.study.study1;

import android.util.Log;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Util {
    public static final String ZHIHU_BASE_URL = "https://news-at.zhihu.com/api/4/news/";

    public static  int getDayOfYear(){
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        int totalDay=day;
        for(int i=1;i<month;i++){
            totalDay += getMonthDay(year,month);
        }
        return totalDay;
    }

    public static boolean isRunYear(int year){
        if((year % 4== 0 && year % 100 != 0) ||(year %400 ==0))
            return true;
        else
            return false;
    }

    public static int getMonthDay(int y, int m){
        if(m==2){
            if(isRunYear(y)){
                return 29;
            } else {
                return 28;
            }
        } else if(m==1||m==3||m==5||m==7||m==8||m==10||m==12){
            return 31;
        } else {
            return 30;
        }
    }

    public static String getRefreshTime(int i){
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE,-i);
        String time = new SimpleDateFormat( "yyyyMMdd").format(c.getTime());
        Log.i(MyLog.TAG, "getRefreshTime: "+time);
        return time;
    }
}
