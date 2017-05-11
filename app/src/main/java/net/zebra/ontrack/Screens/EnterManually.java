package net.zebra.ontrack.Screens;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import net.zebra.ontrack.R;

/**
 * Created by Zeb on 5/6/17.
 */

public class EnterManually extends Activity {
    private TextView timeView, dateView, weatherView;
    private Button enterTime, enterDate, enterWeather;

    public void initViews(){
        timeView = (TextView)findViewById(R.id.time_text_view);
        dateView = (TextView)findViewById(R.id.date_text_view);
        weatherView = (TextView)findViewById(R.id.weather_text_view);

        enterTime = (Button)findViewById(R.id.enter_time_manual);
        enterDate= (Button)findViewById(R.id.enter_date_manual);
        enterWeather = (Button)findViewById(R.id.enter_weather_manual);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_manually);

        initViews();

        timeView.setText("Time: ");
        dateView.setText("Date: ");
        weatherView.setText("Weather: ");

        enterTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View popupView = getLayoutInflater().inflate(R.layout.time_date_popup, null);

                final PopupWindow pw = new PopupWindow(popupView, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);




                pw.setAnimationStyle(R.style.Fade_Animation);

                pw.setFocusable(true);

                pw.showAtLocation(v, Gravity.CENTER, 0,0);
            }
        });



    }


}
