package net.zebra.ontrack.Screens.SubScreens;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import net.zebra.ontrack.R;
import net.zebra.ontrack.tools.RecordedTime;
import net.zebra.ontrack.tools.Time;

/**
 * Created by Zeb on 5/6/17.
 */

public class EnterManually extends Activity {
    private TextView timeView, dateView, weatherView, headerView;
    private Button enterTime, enterDate;
    public Button setButton,saveButton, cancelButton;
    public String hours, mins, secs;
    public String totalTime, totalDate;

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

        initViews();

        final CoordinatorLayout cl = (CoordinatorLayout) findViewById(R.id.manual_coordinator);

        headerView.setText("Enter Manually");
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
                if (!totalTime.equals("") && !totalDate.equals("")) {
                    Time manual = new Time(Integer.parseInt(hours), Integer.parseInt(mins), Integer.parseInt(secs), totalDate);
                    RecordedTime.addTimeToList(manual);
                    finish();
                }
                else if (totalDate.equals("")){
                    Snackbar.make(cl, "Please enter a Date", Snackbar.LENGTH_SHORT).show();
                }
                else if (totalTime.equals("")){
                    Snackbar.make(cl, "Please enter a Time", Snackbar.LENGTH_SHORT).show();
                }
                else Snackbar.make(cl, "Please enter some values", Snackbar.LENGTH_SHORT).show();
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

        pw.setAnimationStyle(R.style.Fade_Animation);

        pw.setFocusable(true);

        pw.showAtLocation(v, Gravity.CENTER, 0,0);

        final EditText hh = (EditText) popupView.findViewById(R.id.enter_hours);
        final EditText mm = (EditText) popupView.findViewById(R.id.enter_minutes);
        final EditText ss = (EditText) popupView.findViewById(R.id.enter_seconds);

        setButton = (Button)popupView.findViewById(R.id.manual_time_set_button);
        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                totalTime = hh.getText().toString() + ":" + mm.getText().toString() + ":" + ss.getText().toString();
                hours = hh.getText().toString();
                mins = mm.getText().toString();
                secs = ss.getText().toString();

                if (!hours.isEmpty() && !mins.isEmpty() && !secs.isEmpty()){
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

        pw.setAnimationStyle(R.style.Fade_Animation);

        pw.setFocusable(true);

        pw.showAtLocation(v, Gravity.CENTER, 0,0);

        final EditText MM = (EditText)popupView.findViewById(R.id.enter_month);
        final EditText dd = (EditText)popupView.findViewById(R.id.enter_day);
        final EditText yy = (EditText)popupView.findViewById(R.id.enter_year);

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


    }
}
