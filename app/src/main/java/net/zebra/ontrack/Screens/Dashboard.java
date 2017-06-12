package net.zebra.ontrack.Screens;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.zebra.ontrack.R;
import net.zebra.ontrack.tools.Time;
import net.zebra.ontrack.tools.UserManager;


/**
 * Created by Zeb on 4/20/17.
 */

public class Dashboard extends Fragment {
    private TextView currentTime, curTimeHeader;
    private TextView recentDate, recentRecord;
    private Time recentTime;
    private String totTime, currentTimeHeader;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.dashboard,container, false);

        currentTime = (TextView)v.findViewById(R.id.current_logged_time);
        curTimeHeader = (TextView)v.findViewById(R.id.current_time_header);
        recentDate = (TextView)v.findViewById(R.id.most_recent_date);
        recentRecord = (TextView)v.findViewById(R.id.most_recent_time);

        update();

        return v;
    }
    public void update(){
        if (UserManager.getCurrentUser() != null) {
            currentTimeHeader = "Total tracked time for " + UserManager.getCurrentUser().getName();
            totTime = UserManager.getCurrentUser().getTotTime();
        }
        else {
            totTime = "";
            currentTimeHeader = "No User Selected";
        }

        curTimeHeader.setText(currentTimeHeader);
        currentTime.setText(totTime);

        if (UserManager.getCurrentUser() != null && UserManager.getCurrentUser().getTimeArrayListLength() > 0){
            recentTime = UserManager.getCurrentUser().getTimeAtIndex(UserManager.getCurrentUser().getTimeArrayListLength()-1);
            recentDate.setText(recentTime.getDate());
            recentRecord.setText(recentTime.getHours() + " h " + recentTime.getMins() + " m " + recentTime.getSecs() + " s");
        }
        else
            recentRecord.setText("No recent records");
        recentDate.setText("");
    }
}
