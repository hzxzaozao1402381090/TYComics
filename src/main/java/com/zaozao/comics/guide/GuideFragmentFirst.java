package com.zaozao.comics.guide;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zaozao.comics.R;

/**
 * Created by MINE on 2016/6/15.
 */
public class GuideFragmentFirst extends android.support.v4.app.Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_guide_fragment1,container,false);
        return view;
    }
}
