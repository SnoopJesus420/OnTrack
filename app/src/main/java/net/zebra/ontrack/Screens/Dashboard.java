package net.zebra.ontrack.Screens;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.zebra.ontrack.R;
import net.zebra.ontrack.tools.RecordedTime;

/**
 * Created by Zeb on 4/20/17.
 */

public class Dashboard extends Fragment {
    private TextView currenttime;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.dashboard,container, false);

        currenttime = (TextView)v.findViewById(R.id.current_logged_time);
        String clt = "Current Logged Time: ";

        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);


        //time = RecordedTime.getTime();

        /*String splitTime[] = newTime.split(":");

        newMinutes = Integer.parseInt(splitTime[0]);
        newSeconds = Integer.parseInt(splitTime[1]);

        hours += newHours;
        minutes += newMinutes;
        seconds += newSeconds;

        time = hours + ":" + minutes + ":" + seconds;*/

        String t = clt + RecordedTime.getTime() ;
        currenttime.setText(t);

        return v;
    }
}
