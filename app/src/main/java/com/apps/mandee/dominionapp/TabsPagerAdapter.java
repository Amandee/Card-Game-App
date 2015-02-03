package com.apps.mandee.dominionapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Mandee on 1/14/2015.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {

    CoinFragment coin;
    PointFragment point;
    KingdomFragment kingdom;
    public TabsPagerAdapter(FragmentManager fm, Cards card) {
        super(fm);
        coin = new CoinFragment(card);
        point = new PointFragment(card);
        kingdom = new KingdomFragment(card);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                return coin;
            case 1:
                return point;
            case 2:
                return kingdom;

        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
