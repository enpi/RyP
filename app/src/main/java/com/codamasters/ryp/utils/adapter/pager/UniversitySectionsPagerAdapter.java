package com.codamasters.ryp.utils.adapter.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.codamasters.ryp.UI.ranking.RankingListFragment;

/**
 * Created by Juan on 30/07/2016.
 */
public class UniversitySectionsPagerAdapter extends FragmentPagerAdapter {

    private String university_key;

    public UniversitySectionsPagerAdapter(FragmentManager fm, String university_key) {
        super(fm);
        this.university_key = university_key;
    }

    @Override
    public Fragment getItem(int position) {

        String reference = null;

        switch (position){
            case 0: reference = "university_degree";
                break;
            case 1: reference = "university_professor";
                break;
        }

        return RankingListFragment.newInstance(position + 1, reference, university_key);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "DEGREE";
            case 1:
                return "PROFESSOR";
        }
        return null;
    }
}