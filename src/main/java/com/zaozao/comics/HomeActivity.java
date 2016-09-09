package com.zaozao.comics;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.zaozao.comics.main.MainFragment;
import com.zaozao.comics.mine.MineFragment;
import com.zaozao.comics.search.SearchFragment;
import com.zaozao.comics.shujia.ShuJiaFragment;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends FragmentActivity {

    RadioGroup navigation;
    FragmentManager manager;
    FragmentTransaction transaction;
    List<Fragment> fragments = new ArrayList<>();
    //当前显示的Fragment
    Fragment currentFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setStatusBar();
        initView();
        setView();
    }

    private void initView() {
        navigation = (RadioGroup) findViewById(R.id.navigation);
        manager = getSupportFragmentManager();
        fragments.add(new MainFragment());
        fragments.add(new ShuJiaFragment());
        fragments.add(new SearchFragment());
        fragments.add(new MineFragment());
        currentFragment = fragments.get(0);
        manager.beginTransaction().add(R.id.content, currentFragment).commit();
    }

    private void setView() {

        navigation.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int id) {
                Fragment fragment = null;
                switch (id) {
                    case R.id.sort:
                        fragment = fragments.get(0);
                        break;
                    case R.id.shujia:
                        fragment = fragments.get(1);
                        break;
                    case R.id.search:
                        fragment = fragments.get(2);
                        break;
                    case R.id.mine:
                        fragment = fragments.get(3);
                        break;
                }
                if (!fragment.isAdded()) {
                    manager.beginTransaction().hide(currentFragment).add(R.id.content, fragment).commit();
                } else {
                    manager.beginTransaction().hide(currentFragment).show(fragment).commit();
                }
                currentFragment = fragment;
            }
        });
    }
    @TargetApi(19)
    public void setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            SystemBarTintManager mTintManager = new SystemBarTintManager(this);
            mTintManager.setStatusBarTintColor(this.getResources().getColor(R.color.theme));
            mTintManager.setStatusBarTintEnabled(true);
        }
    }
    public void setBack(){

    }
}
