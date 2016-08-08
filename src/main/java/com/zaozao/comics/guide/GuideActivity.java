package com.zaozao.comics.guide;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ShareCompat;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;

import com.zaozao.comics.Constant;
import com.zaozao.comics.R;
import com.zaozao.comics.SplashActivity;

public class GuideActivity extends FragmentActivity {

    ViewPager viewPager;
    List<Fragment> lists = new ArrayList<>();
    int[] IDs = {R.id.point1,R.id.point2,R.id.point3};
    List<ImageView> images = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isFirstInstall()){
            Intent intent = new Intent(this, SplashActivity.class);
            startActivity(intent);
            finish();
        }
        setContentView(R.layout.activity_guide);
        init();
        initView();

    }

    public void init() {
        lists.add(new GuideFragmentFirst());
        lists.add(new GuideFragmentSecond());
        lists.add(new GuideFragmentThird());
        for(int i = 0; i < IDs.length; i++){
        	ImageView imageview= (ImageView)findViewById(IDs[i]);
        	images.add(imageview);
        }
    }
    public void initView(){
        viewPager = (ViewPager)findViewById(R.id.viewpager);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(),lists));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for(int i = 0; i < images.size(); i++){
                    if(position != i){
                        images.get(i).setImageResource(R.drawable.point);
                    }else{
                        images.get(i).setImageResource(R.drawable.point1);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    public boolean isFirstInstall(){
        boolean result = false;
        SharedPreferences preferences = getSharedPreferences(Constant.FIRST_INSTALL,MODE_PRIVATE);
        result = preferences.getBoolean(Constant.FIRST_INSTALL,true);
        return  result;
    }
}
