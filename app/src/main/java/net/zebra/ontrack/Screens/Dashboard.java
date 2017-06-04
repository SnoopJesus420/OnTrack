package net.zebra.ontrack.Screens;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.zebra.ontrack.R;
import net.zebra.ontrack.tools.TimeHandler;
import net.zebra.ontrack.tools.Time;


/**
 * Created by Zeb on 4/20/17.
 */

public class Dashboard extends Fragment {
    private TextView currenttime;
    private TextView recentDate, recentRecord;
    private Time recentTime;

    private String t;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.dashboard,container, false);

        currenttime = (TextView)v.findViewById(R.id.current_logged_time);
        currenttime.setText(TimeHandler.getTotTime());

        recentDate = (TextView)v.findViewById(R.id.most_recent_date);
        recentRecord = (TextView)v.findViewById(R.id.most_recent_time);


        if (TimeHandler.getTimeArrayListLength() > 0){

            recentTime = TimeHandler.getTimeAtIndex(TimeHandler.getTimeArrayListLength()-1);
            recentDate.setText(recentTime.getDate());
            recentRecord.setText(recentTime.getHours() + " h " + recentTime.getMins() + " m " + recentTime.getSecs() + " s");
        }
        else
            recentRecord.setText("No recent records");
            recentDate.setText("");

        return v;
    }

}
