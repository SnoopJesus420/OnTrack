package net.zebra.ontrack.tools;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;

import net.zebra.ontrack.Screens.Dashboard;

import java.util.List;

/**
 * Created by Zeb on 6/4/2017.
 */

public class FragmentPageAdapter extends android.support.v4.app.FragmentPagerAdapter {
    List<Fragment> listFragments;

    public Dashboard d;

    public FragmentPageAdapter(FragmentManager fm, List<Fragment> listFragments) {
        super(fm);
        this.listFragments = listFragments;
    }


    @Override
    public Fragment getItem(int position) {
        return listFragments.get(position);
    }

    @Override
    public int getCount() {
        return listFragments.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }


}

