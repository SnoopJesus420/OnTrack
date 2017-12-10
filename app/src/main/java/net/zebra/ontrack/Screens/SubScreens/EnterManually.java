package net.zebra.ontrack.Screens.SubScreens;

import android.app.ActionBar;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import net.zebra.ontrack.R;
import net.zebra.ontrack.tools.TimeManager;
import net.zebra.ontrack.tools.Time;
import net.zebra.ontrack.tools.UserManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Zeb on 5/6/17.
 */

public class EnterManually extends Activity {
    private TextView timeView, dateView, headerView;
    private Button enterTime, enterDate;
    public Button setButton, saveButton, cancelButton, todayButton;
    public int hours, mins, secs;
    public String totalTime, totalDate;
    private int size = 2;
    private boolean alreadyClearedHours;
    private boolean alreadyClearedMins;
    private boolean alreadyClearedSecs;

    public void initViews(){
        headerView = (TextView)findViewById(R.id.header);
        timeView = (TextView)findViewById(R.id.time_text_view);
        dateView = (TextView)findViewById(R.id.date_text_view);

        enterTime = (Button)findViewById(R.id.enter_time_manual);
        enterDate= (Button)findViewById(R.id.enter_date_manual);
        saveButton = (Button)findViewById(R.id.enter_manually_save);
        cancelButton = (Button)findViewById(R.id.cancel_manually);

        enterTime.setText("Enter Time");
        enterDate.setText("Enter Date");
        saveButton.setText("Save");
        cancelButton.setText("Cancel");


    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_manually);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor e = prefs.edit();
        e.putBoolean("previously_started", Boolean.TRUE);
        e.apply();

        initViews();

        final CoordinatorLayout cl = (CoordinatorLayout) findViewById(R.id.manual_coordinator);

        headerView.setText("Add Manually");
        timeView.setText("Time: --/--/--");
        dateView.setText("Date: --/--/--");

        totalDate = "";
        totalTime = "";


        enterTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEnterTime(v);
            }
        });
        enterDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEnterDate(v);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalTime.equals("") && totalDate.equals("")) {
                    Snackbar.make(cl, "Please enter some values", Snackbar.LENGTH_SHORT).show();
                }
                else if (totalDate.equals("")){
                    Snackbar.make(cl, "Please enter a Date", Snackbar.LENGTH_SHORT).show();
                }
                else if (totalTime.equals("")){
                    Snackbar.make(cl, "Please enter a Time", Snackbar.LENGTH_SHORT).show();
                }
                else {
                    Time manual = new Time(hours, mins, secs, totalDate);
                    UserManager.getCurrentUser().addTimeToList(manual);
                    finish();
                }

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void setEnterTime(View v){
        final View popupView = getLayoutInflater().inflate(R.layout.manual_time_popup, null);

        final PopupWindow pw = new PopupWindow(popupView, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);

        pw.setFocusable(true);

        pw.setAnimationStyle(R.style.Fade_Animation);

        pw.showAtLocation(v, Gravity.CENTER, 0,0);

        final EditText hh = (EditText) popupView.findViewById(R.id.enter_hours);
        final EditText mm = (EditText) popupView.findViewById(R.id.enter_minutes);
        final EditText ss = (EditText) popupView.findViewById(R.id.enter_seconds);

        hh.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (hh.getText().toString().length()==size) {
                    mm.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (mm.getText().toString().length()==size) {
                    ss.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        setButton = (Button)popupView.findViewById(R.id.manual_time_set_button);
        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(hh.getText().toString().isEmpty()){
                    hh.setText("00");
                }
                if(mm.getText().toString().isEmpty()){
                    mm.setText("00");
                }
                if(ss.getText().toString().isEmpty()){
                    ss.setText("00");
                }
                hours = Integer.parseInt(hh.getText().toString());
                mins = Integer.parseInt(mm.getText().toString());
                secs = Integer.parseInt(ss.getText().toString());

                totalTime = hours + ":" + mins + ":" + secs;

                if (!hh.getText().toString().isEmpty() && !mm.getText().toString().isEmpty() && !ss.getText().toString().isEmpty()){
                    String tot = "Time: " + totalTime;
                    timeView.setText(tot);
                    pw.dismiss();
                }
                else
                    Snackbar.make(popupView, "Please enter a valid time", Snackbar.LENGTH_SHORT).show();

            }
        });


    }

    public void setEnterDate(View v){

        final View popupView = getLayoutInflater().inflate(R.layout.manual_date_popup, null);

        final PopupWindow pw = new PopupWindow(popupView, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);

        pw.setFocusable(true);

        pw.setAnimationStyle(R.style.Fade_Animation);

        pw.showAtLocation(v, Gravity.CENTER, 0,0);

        final DateFormat df = new SimpleDateFormat("MM/dd/yy");
        final Date day = new Date();

        final EditText MM = (EditText)popupView.findViewById(R.id.enter_month);
        final EditText dd = (EditText)popupView.findViewById(R.id.enter_day);
        final EditText yy = (EditText)popupView.findViewById(R.id.enter_year);

        MM.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (MM.getText().toString().length()==size) {
                    dd.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        dd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (dd.getText().toString().length()==size) {
                    yy.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        setButton = (Button)popupView.findViewById(R.id.manual_date_set_button);
        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                totalDate = MM.getText().toString() + "/" + dd.getText().toString() + "/" + yy.getText().toString();
                if (!totalDate.equals("//")) {
                    String tot = "Date: " + totalDate;
                    dateView.setText(tot);
                    pw.dismiss();
                }
                else
                    Snackbar.make(popupView, "Please enter a valid date", Snackbar.LENGTH_SHORT).show();
            }
        });

        todayButton = (Button)popupView.findViewById(R.id.manual_today_button);
        todayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                totalDate = df.format(day);
                String tot = "Date: " + totalDate;
                dateView.setText(tot);
                pw.dismiss();
            }
        });





    }
}
