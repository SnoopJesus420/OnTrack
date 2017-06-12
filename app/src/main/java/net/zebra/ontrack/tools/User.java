package net.zebra.ontrack.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Zeb on 6/6/2017.
 */

public class User {

    public User(String n){
        name = n;
    }

    public String getName(){
        return name;
    }

    public void addEntireArray(ArrayList<Time> t){
        if (t.size() > 0) {
            for (int i = 0; i < t.size(); i++) {
                timeArrayList.add(t.get(i));
            }
        }
    }

    public void addTimeToList(Time t){
        timeArrayList.add(t);
    }

    public void setIsUpdated(boolean b){
        isUpdated = b;
    }

    public Time getTimeAtIndex(int idx){
        DateFormat df = new SimpleDateFormat("MM/dd/yy");
        Date day = new Date();
        Time ifFails = new Time(0,0,0,df.format(day));
        if (timeArrayList.size() > 0)
            return timeArrayList.get(idx);
        else
            return ifFails;
    }

    public ArrayList<Time> getTimeArray(){
        if (timeArrayList.size() > 0)
            return timeArrayList;
        else
            return null;
    }

    public int getTimeArrayListLength(){
        return timeArrayList.size();
    }

    public String getTotTime() {

        hh = 0;
        mm = 0;
        ss = 0;

        for (int i = 0; i < timeArrayList.size(); i++) {
            hh += timeArrayList.get(i).getHours();
            mm += timeArrayList.get(i).getMins();
            ss += timeArrayList.get(i).getSecs();
        }

        while (ss > 59){
            ss -= 60;
            mm += 1;
        }

        while (mm > 59){
            mm -= 60;
            hh += 1;
        }

        if (ss <= 9) {
            fSeconds = String.format("%02d", ss);
        } else fSeconds = Integer.toString(ss);

        if (mm <= 9) {
            fMinutes = String.format("%02d", mm);
        } else fMinutes = Integer.toString(mm);

        if (hh <= 9) {
            fHours = String.format("%02d", hh);
        } else fHours = Integer.toString(hh);

        return fHours + ":" + fMinutes + ":" + fSeconds;
    }

    public boolean getIsUpdated(){
        return isUpdated;
    }

    public void resetTime(Context c){
        time = "00:00:00";
        hours = 0;
        minutes = 0;
        seconds = 0;
        timeArrayList.clear();

        isReset = true;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor e = prefs.edit();
        e.putString("timeBeforeLeave", time);
        e.apply();
    }

    private String name, fHours, fMinutes, fSeconds, time;
    private int hours, minutes, seconds, hh, mm, ss;
    private boolean isUpdated, isReset;
    private ArrayList<Time> timeArrayList = new ArrayList<>();
}
