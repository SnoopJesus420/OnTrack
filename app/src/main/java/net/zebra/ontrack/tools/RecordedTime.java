package net.zebra.ontrack.tools;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Zeb on 4/23/17.
 */

public class RecordedTime{

    public static String getTotalTime(){
        return time;
    }

    public static void addTime(String t){
        if (t.contains(":")) {

            DateFormat df = new SimpleDateFormat("MM/dd/yy");
            Date day = new Date();

            splitTime = t.split(":");

            newHours = Integer.parseInt(splitTime[0]);
            newMinutes = Integer.parseInt(splitTime[1]);
            newSeconds = Integer.parseInt(splitTime[2]);

            hours += newHours;
            minutes += newMinutes;
            seconds += newSeconds;


                if (seconds >= 60) {
                    do {
                        seconds -= 60;
                        minutes += 1;
                    } while (seconds >= 60);

                }

                if (minutes >= 60) {
                    do{
                        minutes -= 60;
                        hours += 1;
                    } while (minutes >= 60);
                }

            if (seconds <= 9) {
                fSeconds = String.format("%02d", seconds);
            }
            else fSeconds = Integer.toString(seconds);

            if (minutes <= 9) {
                fMinutes = String.format("%02d", minutes);
            }
            else fMinutes = Integer.toString(minutes);

            fHours = Integer.toString(hours);
            Time timeObj = new Time(hours, minutes, seconds);
            addTimeToList(timeObj);
            time = fHours + ":" + fMinutes + ":" + fSeconds;

            isReset = false;
        }
        else {
            time = t;
            isReset = false;
        }
    }

    public static void addTimeWithDate(String t, Date day){
        if (t.contains(":")) {

            splitTime = t.split(":");

            newHours = Integer.parseInt(splitTime[0]);
            newMinutes = Integer.parseInt(splitTime[1]);
            newSeconds = Integer.parseInt(splitTime[2]);

            hours += newHours;
            minutes += newMinutes;
            seconds += newSeconds;


            if (seconds >= 60) {
                do {
                    seconds -= 60;
                    minutes += 1;
                } while (seconds >= 60);

            }

            if (minutes >= 60) {
                do{
                    minutes -= 60;
                    hours += 1;
                } while (minutes >= 60);
            }

            if (seconds <= 9) {
                fSeconds = String.format("%02d", seconds);
            }
            else fSeconds = Integer.toString(seconds);

            if (minutes <= 9) {
                fMinutes = String.format("%02d", minutes);
            }
            else fMinutes = Integer.toString(minutes);

            fHours = Integer.toString(hours);
            Time timeObj = new Time(hours, minutes, seconds);
            addTimeToList(timeObj);
            time = fHours + ":" + fMinutes + ":" + fSeconds;

            isReset = false;
        }
        else {
            time = t;
            isReset = false;
        }
    }

    public static void addTimeToList(Time t){
        timeArrayList.add(t);
    }


    public static void resetTime(Context c){
        time = "00:00:00";
        hours = 0;
        minutes = 0;
        seconds = 0;

        isReset = true;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor e = prefs.edit();
        e.putString("timeBeforeLeave", time);
        e.commit();
    }

    public static Time getTimeAtIndex(int idx){
        return timeArrayList.get(idx);
    }


    public static boolean checkReset(){
        return isReset;
    }

    public static String time, fSeconds, fMinutes, fHours;
    public static String splitTime[];
    public static int newHours, newMinutes, newSeconds;
    public static int hours, minutes, seconds;
    public static boolean isReset;
    private static ArrayList<Time> timeArrayList = new ArrayList<Time>();

}
