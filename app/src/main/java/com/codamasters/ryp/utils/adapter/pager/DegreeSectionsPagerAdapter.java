package com.codamasters.ryp.utils.adapter.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.codamasters.ryp.UI.search.SearchListFragment;

/**
 * Created by Juan on 30/07/2016.
 */
public class DegreeSectionsPagerAdapter extends FragmentPagerAdapter {

    private String university_key;

    public DegreeSectionsPagerAdapter(FragmentManager fm, String university_key) {
        super(fm);
        this.university_key = university_key;
    }

    @Override
    public Fragment getItem(int position) {

        String reference = null;

        switch (position){
            case 0: reference = "professor";
                break;
        }

        return SearchListFragment.newInstance(position + 1, reference);
    }

    @Override
    public int getCount() {
        // Show 1 total pages.
        return 1;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "PROFESSOR";
        }
        return null;
    }
}