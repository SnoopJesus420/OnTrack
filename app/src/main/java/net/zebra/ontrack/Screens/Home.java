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
import android.support.design.widget.Snackbar;
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
    public static boolean isRec, emptyChron, moreThanOnce;
    Chronometer chron;
    private Button resetTime, saveToLog, enterManually;
    private FloatingActionButton startBtn, stopBtn;
    public static String getTime;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.home,container, false);
        isRec = false;
        emptyChron = true;

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
                moreThanOnce = false;

                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor edit = sp.edit();
                edit.putBoolean("isRecording", true);



                chron.setBase(SystemClock.elapsedRealtime());
                chron.start();

                startTimeMessage.setVisibility(View.INVISIBLE);

                startBtn.hide();
                stopBtn.show();

            }
        });

        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isRec = false;
                emptyChron = false;
                startTimeMessage.setVisibility(View.VISIBLE);

                long elapsedMillis = SystemClock.elapsedRealtime() - chron.getBase();
                long seconds = TimeUnit.MILLISECONDS.toSeconds(elapsedMillis);

                getTime = "0:0:" + String.valueOf(seconds);
                chron.stop();

                stopBtn.hide();
                startBtn.show();


            }
        });

        resetTime.setText("Reset Time");
        resetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset(view);
            }
        });

        saveToLog.setText("Save");
        saveToLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!emptyChron && !moreThanOnce) {
                    RecordedTime.addTime(getTime);
                    chron.setBase(SystemClock.elapsedRealtime());
                    moreThanOnce = true;
                    Snackbar.make(v, "Saved!" , Snackbar.LENGTH_SHORT).show();
                }
                else
                    Snackbar.make(v, "No time has been recorded!", Snackbar.LENGTH_SHORT).show();
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
    }
    public void reset(final View v){
        final View popupView = getActivity().getLayoutInflater().inflate(R.layout.reset_time_confirmation, null);

        final PopupWindow pw = new PopupWindow(popupView, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        Button yes = (Button)popupView.findViewById(R.id.reset_yes),
                no = (Button)popupView.findViewById(R.id.reset_no);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecordedTime.resetTime(getActivity());
                pw.dismiss();
                Snackbar.make(v, "Time has been reset!", Snackbar.LENGTH_SHORT).show();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pw.dismiss();
            }
        });

        pw.setAnimationStyle(R.style.Fade_Animation);

        pw.setFocusable(true);

        pw.showAtLocation(v, Gravity.CENTER, 0,0);
    }
    public static boolean areWeRec(){
        return isRec;
    }
}
