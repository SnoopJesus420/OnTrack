package net.zebra.ontrack.tools;

import java.util.Date;

/**
 * Created by Zeb on 5/30/2017.
 */

public class Time {
    //Precondition: date must be in the following format: mm/dd/yy
    public Time(int h, int m, int s, String d){
        hours = h;
        mins = m;
        secs = s;
        date = d;
    }
    public int getHours(){
        return hours;
    }
    public int getMins(){
        return mins;
    }
    public int getSecs(){
        return secs;
    }
    public String getDate(){return date;}
    public String toString(){
        return hours + " hours " + mins + " minutes " + secs + " seconds on " + date;
    }
    public String getTotalTime(){
        return hours + ":" + mins + ":" + secs;
    }

    private int hours, mins, secs;
    private String date;
}
