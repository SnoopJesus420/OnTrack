package net.zebra.ontrack.Screens;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import net.zebra.ontrack.R;
import net.zebra.ontrack.tools.TimeHandler;
import net.zebra.ontrack.tools.Time;

import java.util.ArrayList;
import java.util.Collections;

public class Log extends Fragment{
    private ListView lv;
    private TextView tv;
    private String header, noTime;
    private final ArrayList<Time> timeArrayList = TimeHandler.getTimeArray();


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.log, container, false);

        lv = (ListView) v.findViewById(R.id.log_list_view);
        tv = (TextView)v.findViewById(R.id.log_header);

        header = "Logged Time";
        noTime = "No Logged Time Available :(";


        tv.setText(header);

        for (int i = 0; i < timeArrayList.size(); i++) {
            if (timeArrayList.get(i).getDate().equals("00/00/00") || timeArrayList.get(i).getTotalTime().equals("0:0:0")) {
                timeArrayList.remove(i);
            }
        }
        Collections.reverse(timeArrayList);

        if (TimeHandler.getTimeArrayListLength() != 0) {
            String[] listItems = new String[timeArrayList.size()];
            lv = (ListView) v.findViewById(R.id.log_list_view);
            if (timeArrayList.size() > 0) {
                if (timeArrayList.get(0) != null) {

                    for (int i = 0; i < timeArrayList.size(); i++) {
                        Time t = timeArrayList.get(i);
                        listItems[i] = t.toString();

                    }
                    ArrayAdapter adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, listItems);
                    lv.setAdapter(adapter);
                }
            }
        }
        else
            tv.setText(noTime);
        Collections.reverse(timeArrayList);
        return v;

    }
    public void update(){
        tv.setText(header);

        for (int i = 0; i < timeArrayList.size(); i++) {
            if (timeArrayList.get(i).getDate().equals("00/00/00") || timeArrayList.get(i).getTotalTime().equals("0:0:0")) {
                timeArrayList.remove(i);
            }
        }
        Collections.reverse(timeArrayList);

        if (TimeHandler.getTimeArrayListLength() != 0) {
            String[] listItems = new String[timeArrayList.size()];
            if (timeArrayList.size() > 0) {
                if (timeArrayList.get(0) != null) {

                    for (int i = 0; i < timeArrayList.size(); i++) {
                        Time t = timeArrayList.get(i);
                        listItems[i] = t.toString();

                    }
                    ArrayAdapter adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, listItems);
                    lv.setAdapter(adapter);
                }
            }
        }
        else
            tv.setText(noTime);
        Collections.reverse(timeArrayList);
    }
}
