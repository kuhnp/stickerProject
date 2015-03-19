package com.example.rrhg5930.stickerproject.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by pierre on 19/03/2015.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {

    Fragment fragment;

    public TabsPagerAdapter(FragmentManager fragmentManager, Fragment fragment){
        super(fragmentManager);
        this.fragment = fragment;
    }

    @Override
    public Fragment getItem(int position) {
        return fragment;
    }

    @Override
    public int getCount() {
        return 1;
    }
}
