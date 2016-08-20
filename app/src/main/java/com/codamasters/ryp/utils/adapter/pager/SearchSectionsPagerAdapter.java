package com.codamasters.ryp.utils.adapter.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.codamasters.ryp.UI.search.SearchListFragment;

/**
 * Created by Juan on 30/07/2016.
 */
public class SearchSectionsPagerAdapter extends FragmentPagerAdapter {

    public SearchSectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        String reference = null;

        switch (position){
            case 0: reference = "university";
                break;
            case 1: reference = "degree";
                break;
            case 2: reference = "professor";
                break;
        }

        return SearchListFragment.newInstance(position + 1, reference);
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "UNIVERSITY";
            case 1:
                return "DEGREE";
            case 2:
                return "PROFESSOR";
        }
        return null;
    }
}