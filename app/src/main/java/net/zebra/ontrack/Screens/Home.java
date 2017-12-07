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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import net.zebra.ontrack.R;
import net.zebra.ontrack.Screens.SubScreens.EnterManually;
import net.zebra.ontrack.tools.Time;
import net.zebra.ontrack.tools.TimeManager;
import net.zebra.ontrack.tools.User;
import net.zebra.ontrack.tools.UserManager;

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
    private Button userManage, saveToLog, enterManually;
    private ImageButton settingsBtn;
    private Spinner userSelect;
    private Switch autoSaveSwitch;
    private ArrayAdapter<String> adapter;
    private FloatingActionButton startBtn, stopBtn;
    public static Time getTime;
    private long timeWhenStopped;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.home,container, false);
        final CoordinatorLayout cl = (CoordinatorLayout)v.findViewById(R.id.home_coordinator);
        userSelect = (Spinner)v.findViewById(R.id.select_profile);

        UserManager.setCurrentUser(0);

        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, UserManager.getUserNames());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userSelect.setAdapter(adapter);


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final SharedPreferences.Editor edit = prefs.edit();
        edit.apply();

        ArrayList<User> users = UserManager.getUserList();
        if (users.size() == 0)
            createNewUser(v);


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

        userSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                UserManager.setCurrentUser(adapterView.getItemAtPosition(i).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settings(cl);
            }
        });

        userManage.setText("Manage Users");
        userManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manageUsers(cl);
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
            UserManager.getCurrentUser().addTimeToList(getTime);
            chron.setBase(SystemClock.elapsedRealtime());
            moreThanOnce = true;
            Snackbar.make(cl, "Saved!" , Snackbar.LENGTH_SHORT).show();
            timeWhenStopped = 0;
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
                TimeManager.resetTime(getActivity());
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
        userManage = (Button)popupView.findViewById(R.id.settings_reset_button);


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

        userManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset(v);
            }
        });

        pw.setAnimationStyle(R.style.Fade_Animation);

        pw.setFocusable(true);

        pw.showAtLocation(v, Gravity.CENTER, 0,0);
    }

    public void update(){
        String oldUser = UserManager.getCurrentUser().getName();
        adapter.clear();
        adapter.addAll(UserManager.getUserNames());
        adapter.notifyDataSetChanged();
        UserManager.setCurrentUser(oldUser);
    }

    public void initViews(View v){
        startTimeMessage = (TextView)v.findViewById(R.id.time_clock);

        startBtn = (FloatingActionButton)v.findViewById(R.id.start_button);
        stopBtn = (FloatingActionButton)v.findViewById(R.id.stop_button);

        userManage = (Button)v.findViewById(R.id.manage_users);
        saveToLog = (Button)v.findViewById(R.id.save);
        enterManually = (Button)v.findViewById(R.id.enter_manually);
        settingsBtn = (ImageButton)v.findViewById(R.id.settings_button);

        chron = (Chronometer)v.findViewById(R.id.chron);
    }

    public static boolean getRecordingStatus(){
        return isRec;
    }

    public void createNewUser(final View v){
        final View popupView = getActivity().getLayoutInflater().inflate(R.layout.create_user_layout, null);

        final PopupWindow pw = new PopupWindow(popupView, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);

        final EditText userName = (EditText)popupView.findViewById(R.id.create_user_name);

        final Button createUser = (Button)popupView.findViewById(R.id.create_user_button);
        createUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String u = UserManager.createUser(userName.getText().toString());


                Toast.makeText(getActivity(), u, Toast.LENGTH_SHORT).show();
                if (u.equals("Created!")) {
                    UserManager.setCurrentUser(userName.getText().toString());
                    update();
                    pw.dismiss();
                }
            }
        });

        pw.setAnimationStyle(R.style.Fade_Animation);

        pw.setFocusable(true);

        pw.showAtLocation(v, Gravity.CENTER, 0,0);
    }

    public void manageUsers(final View v){
        final View popupView = getActivity().getLayoutInflater().inflate(R.layout.manage_user_layout, null);

        final PopupWindow pw = new PopupWindow(popupView, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final SharedPreferences.Editor edit = prefs.edit();

        Spinner userManage = (Spinner)popupView.findViewById(R.id.manage_user_spinner);
        Button removeUser = (Button)popupView.findViewById(R.id.remove_user_button);
        Button createNewUser = (Button)popupView.findViewById(R.id.create_user_button);


        if (UserManager.getUserList().size() > 0){
            UserManager.selectUser(UserManager.getUserList().get(0).getName());
        }

        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, UserManager.getUserNames());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userManage.setAdapter(adapter);

        userManage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                UserManager.selectUser(adapterView.getItemAtPosition(i).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        removeUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), UserManager.deleteSelectedUser(), Toast.LENGTH_SHORT).show();
                UserManager.selectUser(0);
                adapter.clear();
                adapter.addAll(UserManager.getUserNames());
                adapter.notifyDataSetChanged();
                if (UserManager.getUserList().size() == 0)
                    createNewUser(v);

            }
        });

        createNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewUser(v);
            }
        });

        pw.setAnimationStyle(R.style.Fade_Animation);

        pw.setFocusable(true);

        pw.showAtLocation(v, Gravity.CENTER, 0,0);

    }
}
