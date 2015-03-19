package com.example.rrhg5930.stickerproject.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by pierre on 19/03/2015.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {

    ArrayList<Fragment> fragmentList;

    public TabsPagerAdapter(FragmentManager fragmentManager, ArrayList<Fragment> fragmentList){
        super(fragmentManager);
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
