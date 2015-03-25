package com.example.rrhg5930.stickerproject.adapter;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.rrhg5930.stickerproject.fragment.FriendListFragment;
import com.example.rrhg5930.stickerproject.fragment.MainStickerFragment;

import java.util.ArrayList;

/**
 * Created by pierre on 19/03/2015.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {

    ArrayList<Fragment> fragments;

    public TabsPagerAdapter(FragmentManager fragmentManager, ArrayList<Fragment> fragments){
        super(fragmentManager);
        this.fragments = fragments;

    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }



    @Override
    public int getCount() {
        return fragments.size();
    }
}
