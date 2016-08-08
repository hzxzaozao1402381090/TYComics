package com.zaozao.comics.guide;

import java.util.List;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by MINE on 2016/6/15.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    List<android.support.v4.app.Fragment> lists;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    public ViewPagerAdapter(FragmentManager fm,List<android.support.v4.app.Fragment> lists){
        super(fm);
        this.lists = lists;
    }
    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        return lists.get(position);
    }

    @Override
    public int getCount() {
        return lists.size();
    }
}
