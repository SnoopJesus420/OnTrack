package net.zebra.ontrack;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.icu.text.AlphabeticIndex;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.app.FragmentTransaction;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import net.zebra.ontrack.Screens.Dashboard;
import net.zebra.ontrack.Screens.Home;
import net.zebra.ontrack.Screens.Log;
import net.zebra.ontrack.tools.RecordedTime;
import net.zebra.ontrack.tools.Time;

public class MainActivity extends AppCompatActivity {

    public String tbl;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            CoordinatorLayout cl = (CoordinatorLayout) findViewById(R.id.home_coordinator);

            switch (item.getItemId()) {
                case R.id.navigation_home:

                    if (Home.areWeRec()) {
                        Snackbar.make(cl, "You are recording!", Snackbar.LENGTH_SHORT).show();
                        return false;
                    } else {
                        Fragment fragment = new Home();
                        replaceFragment(fragment);
                    }
                    return true;
                case R.id.navigation_dashboard:
                    if (Home.areWeRec()) {
                        Snackbar.make(cl, "You are recording!", Snackbar.LENGTH_SHORT).show();
                        return false;
                    } else {

                        Fragment frag = new Dashboard();
                        replaceFragment(frag);
                    }
                    return true;
                case R.id.navigation_log:
                    if (Home.areWeRec()) {
                        Snackbar.make(cl, "You are recording!", Snackbar.LENGTH_SHORT).show();
                        return false;
                    } else {
                        Fragment frag = new Log();
                        replaceFragment(frag);
                    }
                    return true;
            }
            return false;
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Time t = new Time(0,0,0, "00/00/00");
        RecordedTime.addTimeToList(t);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor edit = prefs.edit();
        edit.putBoolean("previously_started", Boolean.FALSE);
        edit.apply();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        Fragment fragment = new Home();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.content, fragment).addToBackStack(null).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor edit = prefs.edit();
        boolean previouslyStarted = prefs.getBoolean("previously_started", false);

        tbl = prefs.getString("timeBeforeLeave", "00:00:00");
        if (!tbl.contains("null")) {
            if (prefs.getString("timeBeforeLeave", "00:00:00").equals("00:00:00")) {

                //RecordedTime.addTime(tbl);
                RecordedTime.restoreTimeArrayList(this);

            }
        } else
            RecordedTime.addTime("00:00:00");

        if (!previouslyStarted)

        {
            edit.putBoolean("previously_started", Boolean.TRUE);
            //RecordedTime.addTime(tbl);
            edit.apply();
            RecordedTime.restoreTimeArrayList(this);

        }
    }



    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor e = prefs.edit();
        //e.putString("timeBeforeLeave", RecordedTime.getTotalTime());
        e.apply();

        RecordedTime.storeTimeArrayList(this);
    }
    private void replaceFragment (Fragment fragment){
        String backStateName =  fragment.getClass().getName();

        FragmentManager manager = getFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);

        if (!fragmentPopped && manager.findFragmentByTag(backStateName) == null){ //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.content, fragment, backStateName);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }
}
