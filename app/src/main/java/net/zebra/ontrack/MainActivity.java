package net.zebra.ontrack;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.UserHandle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.zebra.ontrack.Screens.Dashboard;
import net.zebra.ontrack.Screens.Home;
import net.zebra.ontrack.Screens.Log;
import net.zebra.ontrack.tools.FragmentPageAdapter;
import net.zebra.ontrack.tools.TimeManager;
import net.zebra.ontrack.tools.Time;
import net.zebra.ontrack.tools.User;
import net.zebra.ontrack.tools.UserManager;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public String tbl;
    private ViewPager viewPager;
    private MenuItem prevMenuItem;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_dashboard:
                    viewPager.setCurrentItem(1);

                    return true;
                case R.id.navigation_log:
                    viewPager.setCurrentItem(2);
                    return true;
            }
            return false;
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UserManager.setCurrentUser(0);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor edit = prefs.edit();
        edit.putBoolean("previously_started", Boolean.FALSE);
        edit.apply();

        final BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        final List<android.support.v4.app.Fragment> listFragments = new ArrayList<>();
        listFragments.add(new Home());
        listFragments.add(new Dashboard());
        listFragments.add(new Log());

        final FragmentPageAdapter fpa = new FragmentPageAdapter(
                getSupportFragmentManager(), listFragments);

        viewPager = (ViewPager)findViewById(R.id.view_pager);
        if(viewPager != null){
            viewPager.setAdapter(fpa);
        }

        viewPager.setOffscreenPageLimit(3);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                }
                else {
                    navigation.getMenu().getItem(0).setChecked(false);
                }
                if (position == 0){
                    ((Home)fpa.getItem(position)).update();
                }
                if (position == 1){
                    ((Dashboard)fpa.getItem(position)).update();
                }
                if (position == 2){
                    ((Log)fpa.getItem(position)).update();
                }

                navigation.getMenu().getItem(position).setChecked(true);

                prevMenuItem = navigation.getMenu().getItem(position);
            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                }
                else {
                    navigation.getMenu().getItem(0).setChecked(false);
                }
                if (position == 0){
                    ((Home)fpa.getItem(position)).update();
                }
                if (position == 1){
                    ((Dashboard)fpa.getItem(position)).update();
                }
                if (position == 2){
                    ((Log)fpa.getItem(position)).update();
                }

                navigation.getMenu().getItem(position).setChecked(true);

                prevMenuItem = navigation.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        Gson g = new Gson();
        String userListBeforeLeave = g.toJson(UserManager.getUserMap());
        System.out.println(userListBeforeLeave);


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor e = prefs.edit();
        e.putString("timeBeforeLeave", userListBeforeLeave);
        e.apply();

    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor edit = prefs.edit();
        boolean previouslyStarted = prefs.getBoolean("previously_started", false);
        Gson g = new Gson();
        tbl = prefs.getString("timeBeforeLeave", "{}");
        System.out.println(tbl);
        UserManager.setCurrentUser(0);
        if (!tbl.contains("null") && !tbl.contains("{}")) {
            /*if (tbl.equals("00:00:00")) {
                edit.putBoolean("previously_started", Boolean.TRUE);
                Type type = new TypeToken<HashMap<String, User>>(){}.getType();
                HashMap<String, User> restoredUserMap = g.fromJson(tbl, type);
                UserManager.addUserMap(restoredUserMap);
                edit.apply();
            }*/
            if (!previouslyStarted) {
                edit.putBoolean("previously_started", Boolean.TRUE);
                Type type = new TypeToken<HashMap<String, User>>(){}.getType();
                HashMap<String, User> restoredUserMap = g.fromJson(tbl, type);
                UserManager.addUserMap(restoredUserMap);
                edit.apply();
            }
        }
    }
}
