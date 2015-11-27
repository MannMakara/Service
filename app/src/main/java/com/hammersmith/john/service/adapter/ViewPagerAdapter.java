package com.hammersmith.john.service.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.hammersmith.john.service.R;
import com.hammersmith.john.service.app.Tab1;
import com.hammersmith.john.service.app.Tab2;
import com.hammersmith.john.service.app.Tab3;
import com.hammersmith.john.service.app.Tab4;
import com.hammersmith.john.service.slidingtab.SlidingTabLayout;

/**
 * Created by John on 8/24/2015.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter implements SlidingTabLayout.TabIconProvider {

    int iconRes[] = {
            R.drawable.icon_home,
            R.drawable.icon_room,
            R.drawable.icon_favor,
            R.drawable.icon_setting
    };
    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created

    public ViewPagerAdapter(FragmentManager fm,CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;
    }

    public ViewPagerAdapter(FragmentManager fm, int icon[], int mNumberofTabsumb){
        super(fm);
        this.NumbOfTabs = mNumberofTabsumb;
        this.iconRes = icon;
    }

    public ViewPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new Tab1();
            case 1:
                return new Tab2();
            case 2:
                return new Tab3();
            case 3:
                return new Tab4();
            default:
                return new Tab1();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return Titles[position];
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public int getPageIconResId(int position) {
        return iconRes[position];
    }
}
