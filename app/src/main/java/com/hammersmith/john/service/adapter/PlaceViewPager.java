package com.hammersmith.john.service.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hammersmith.john.service.app.PlaceDetailActivity;

/**
 * Created by Khmer on 10/2/2015.
 */
public class PlaceViewPager extends FragmentPagerAdapter {

    public PlaceViewPager(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        PlaceDetailActivity.MyFragment fragment = PlaceDetailActivity.MyFragment.newInstance(position);
        return fragment;
    }

    @Override
    public int getCount() {
        return 1;
    }
}
