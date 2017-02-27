package com.syz.magicbox.magicbox.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chx on 2016/12/2.
 */

public class MyFragmentPagerAdapter extends FragmentPagerAdapter{
    private List<Fragment> fragments;
    private String[] mTitles;
    public MyFragmentPagerAdapter(FragmentManager fm,String[] titles,List<Fragment> fragments) {
        super(fm);
        this.mTitles = titles;
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }
}
