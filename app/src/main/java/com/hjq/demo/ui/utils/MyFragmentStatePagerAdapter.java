/*
 * CopyRight(c) WYZE CO.LTD 2018.
 */

package com.hjq.demo.ui.utils;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;


public class MyFragmentStatePagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> fragments = null;

    public MyFragmentStatePagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public int getCount() {
        return fragments == null ? 0 : fragments.size();
    }

}
