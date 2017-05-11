package net.zebra.ontrack.Screens;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.PopupWindow;
import android.widget.TextView;

import net.zebra.ontrack.R;
import net.zebra.ontrack.tools.RecordedTime;

import java.util.concurrent.TimeUnit;

/**
 * Created by Zeb on 4/19/17.
 */

public class Home extends Fragment {

    private TextView startTimeMessage;
    public static boolean isRec;
    Chronometer chron;
    private Button resetTime, saveToLog, enterManually;
    private FloatingActionButton startBtn, stopBtn;
    public static String getTime;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.home,container, false);
        isRec = false;

        startTimeMessage = (TextView)v.findViewById(R.id.time_clock);

        startBtn = (FloatingActionButton)v.findViewById(R.id.start_button);
        stopBtn = (FloatingActionButton)v.findViewById(R.id.stop_button);

        resetTime = (Button)v.findViewById(R.id.reset_time);
        saveToLog = (Button)v.findViewById(R.id.save);
        enterManually = (Button)v.findViewById(R.id.enter_manually);

        chron = (Chronometer)v.findViewById(R.id.chron);
        startTimeMessage.setText("Tap start to begin recording");

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRec = true;

                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor edit = sp.edit();
                edit.putBoolean("isRecording", true);



                chron.setBase(SystemClock.elapsedRealtime());
                chron.start();

                startTimeMessage.setVisibility(View.INVISIBLE);
                stopBtn.show();
                startBtn.hide();
            }
        });

        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRec = false;
                startTimeMessage.setVisibility(View.VISIBLE);
                chron.stop();


                /*SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor edit = sp.edit();

                edit.putString("stoptime", stoppedTime);
                edit.putString("hours", hours);
                edit.putString("minutes", minutes);
                edit.apply(); */

                long elapsedMillis = SystemClock.elapsedRealtime() - chron.getBase();

                //long hours = TimeUnit.MILLISECONDS.toHours(elapsedMillis);
                //long minutes = TimeUnit.MILLISECONDS.toMinutes(elapsedMillis);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(elapsedMillis);

                getTime = "0:0:" + String.valueOf(seconds);


                chron.stop();

                startBtn.show();
                stopBtn.hide();

            }
        });

        resetTime.setText("Reset Time");
        resetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecordedTime.resetTime(getActivity());
            }
        });

        saveToLog.setText("Save");
        saveToLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecordedTime.setTime(getTime);
            }
        });

        enterManually.setText("Enter Manually");
        enterManually.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterManually();
            }
        });




        return v;
    }



    public void enterManually(){

        startActivity(new Intent(getActivity(), EnterManually.class));
        /*final View popupView = getActivity().getLayoutInflater().inflate(R.layout.enter_manually, null);

        final PopupWindow pw = new PopupWindow(popupView, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);

        TextView manualHeader = (TextView)popupView.findViewById(R.id.enter_manually_header);
        TextView timeTv = (TextView)popupView.findViewById(R.id.time_text_view);

        manualHeader.setText("Enter Time Manually");
        timeTv.setText("--:--:--");



        pw.setAnimationStyle(R.style.Fade_Animation);

        pw.showAtLocation(v, Gravity.CENTER, 0,0);*/
    }
    public static boolean areWeRec(){
        return isRec;
    }
}
