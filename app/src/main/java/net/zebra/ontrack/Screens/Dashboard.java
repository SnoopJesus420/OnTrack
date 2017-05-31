package net.zebra.ontrack.Screens;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.zebra.ontrack.R;
import net.zebra.ontrack.tools.RecordedTime;
import net.zebra.ontrack.tools.Time;

/**
 * Created by Zeb on 4/20/17.
 */

public class Dashboard extends Fragment {
    private TextView currenttime;
    private ProgressBar progressBar;
    private TextView recentDate, recentRecord;
    private Time recentTime;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.dashboard,container, false);

        currenttime = (TextView)v.findViewById(R.id.current_logged_time);
        String clt = "Current Logged Time: ";
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        String t = clt + RecordedTime.getTotalTime() ;
        currenttime.setText(t);

        recentDate = (TextView)v.findViewById(R.id.most_recent_date);
        recentRecord = (TextView)v.findViewById(R.id.most_recent_time);
        recentTime = RecordedTime.getTimeAtIndex(RecordedTime.getTimeArrayListLength()-1);
        recentDate.setText(recentTime.getDate());
        recentRecord.setText(recentTime.getHours() + " H, " + recentTime.getMins() + " M, " + recentTime.getSecs() + " S");




        return v;
    }
}
