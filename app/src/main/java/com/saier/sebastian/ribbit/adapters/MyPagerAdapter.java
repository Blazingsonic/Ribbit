package com.saier.sebastian.ribbit.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.saier.sebastian.ribbit.ui.fragments.FriendsFragment;
import com.saier.sebastian.ribbit.ui.fragments.InboxFragment;

/**
 * Created by Sebastian on 28.04.2015.
 */
public class MyPagerAdapter extends FragmentPagerAdapter {

    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int pos) {
        switch(pos) {

            case 0: return InboxFragment.newInstance("FirstFragment", "Instance 1");
            case 1: return FriendsFragment.newInstance("SecondFragment", "Instance 1");
            default: return InboxFragment.newInstance("FirstFragment", "Instance 1");
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
