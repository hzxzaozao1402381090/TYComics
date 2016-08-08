package com.zaozao.comics.main;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {

    List<ImageView> list;

    public ViewPagerAdapter(List<ImageView> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(list.get((position % list.size())));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        ImageView view = list.get(position % list.size());
        ViewParent vp = view.getParent();
        if(vp!=null){
            return list.get((position % list.size()));
        }else{
            container.addView(list.get((position % list.size())), 0);
        }
        return list.get((position % list.size()));
    /*	position %= list.size();
		if(position < 0){
			position = list.size() + position;
		}
		ImageView view = list.get(position);
		ViewParent vp = view.getParent();
		if(vp!=null){
			ViewGroup vg = (ViewGroup) vp;
			vg.removeView(view);
		}
		container.addView(view);*/
    }
}
