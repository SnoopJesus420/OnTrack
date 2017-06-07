package net.zebra.ontrack.Screens;

import android.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import net.zebra.ontrack.R;
import net.zebra.ontrack.Screens.SubScreens.EnterManually;
import net.zebra.ontrack.tools.Time;
import net.zebra.ontrack.tools.TimeHandler;
import net.zebra.ontrack.tools.User;
import net.zebra.ontrack.tools.UserHandler;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Zeb on 4/19/17.
 */

public class Home extends Fragment {

    private TextView startTimeMessage;
    public static boolean isRec, emptyChron, moreThanOnce, autoSave;
    Chronometer chron;
    private Button resetTime, saveToLog, enterManually;
    private ImageButton settingsBtn;
    private Switch autoSaveSwitch;
    private FloatingActionButton startBtn, stopBtn;
    public static Time getTime;
    private long timeWhenStopped;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.home,container, false);
        final CoordinatorLayout cl = (CoordinatorLayout)v.findViewById(R.id.home_coordinator);
        Spinner userSelect = (Spinner)v.findViewById(R.id.select_profile);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, UserHandler.getUserNames());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userSelect.setAdapter(adapter);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final SharedPreferences.Editor edit = prefs.edit();


        isRec = false;
        emptyChron = true;
        timeWhenStopped = 0;
        autoSave = prefs.getBoolean("auto_save", Boolean.FALSE);

        initViews(v);

        startTimeMessage.setText("Tap start to begin recording");

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRec = true;
                moreThanOnce = false;

                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor edit = sp.edit();
                edit.putBoolean("isRecording", true);
                edit.apply();

                chron.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
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
                timeWhenStopped = chron.getBase() - SystemClock.elapsedRealtime();

                getTime = new Time((int) seconds , new SimpleDateFormat("MM/dd/yy").format(new Date()));
                if (autoSave){
                    chron.stop();
                    save(cl);
                }
                else
                    chron.stop();

                stopBtn.hide();
                startBtn.show();


            }
        });

        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settings(cl);
            }
        });

        resetTime.setText("Reset");
        resetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset(cl);
            }
        });

        saveToLog.setText("Save");
        saveToLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save(cl);
            }
        });

        enterManually.setText("Add Manually");
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

    public void save(final CoordinatorLayout cl){
        if (!emptyChron && !moreThanOnce) {
            TimeHandler.addTimeToList(getTime);
            chron.setBase(SystemClock.elapsedRealtime());
            moreThanOnce = true;
            Snackbar.make(cl, "Saved!" , Snackbar.LENGTH_SHORT).show();
            timeWhenStopped = 0;
            TimeHandler.setIsUpdated(true);
        }
        else
            Snackbar.make(cl, "No time has been recorded!", Snackbar.LENGTH_SHORT).show();
    }

    public void reset(final View v){
        final View popupView = getActivity().getLayoutInflater().inflate(R.layout.reset_time_confirmation, null);

        final PopupWindow pw = new PopupWindow(popupView, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        Button yes = (Button)popupView.findViewById(R.id.reset_yes),
                no = (Button)popupView.findViewById(R.id.reset_no);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimeHandler.resetTime(getActivity());
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

    public void settings(final View v){
        final View popupView = getActivity().getLayoutInflater().inflate(R.layout.settings_menu_layout, null);

        final PopupWindow pw = new PopupWindow(popupView, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final SharedPreferences.Editor edit = prefs.edit();

        autoSaveSwitch = (Switch)popupView.findViewById(R.id.settings_auto_save);

        autoSaveSwitch.setChecked(prefs.getBoolean("auto_save", Boolean.FALSE));

        autoSaveSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    autoSave = true;
                    edit.putBoolean("auto_save", autoSave);
                    edit.apply();
                }
                else {
                    autoSave = false;
                    edit.putBoolean("auto_save", autoSave);
                    edit.apply();
                }
            }
        });

        pw.setAnimationStyle(R.style.Fade_Animation);

        pw.setFocusable(true);

        pw.showAtLocation(v, Gravity.CENTER, 0,0);
    }

    public void initViews(View v){
        startTimeMessage = (TextView)v.findViewById(R.id.time_clock);

        startBtn = (FloatingActionButton)v.findViewById(R.id.start_button);
        stopBtn = (FloatingActionButton)v.findViewById(R.id.stop_button);

        resetTime = (Button)v.findViewById(R.id.reset_time);
        saveToLog = (Button)v.findViewById(R.id.save);
        enterManually = (Button)v.findViewById(R.id.enter_manually);
        settingsBtn = (ImageButton)v.findViewById(R.id.settings_button);

        chron = (Chronometer)v.findViewById(R.id.chron);
    }

    public static boolean getRecordingStatus(){
        return isRec;
    }
}
