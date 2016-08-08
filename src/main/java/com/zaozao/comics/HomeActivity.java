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
    }

    private void setView() {
        transaction = manager.beginTransaction();
        transaction.replace(R.id.content, new MainFragment());
        transaction.commit();
        navigation.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int id) {
                switch (id) {
                    case R.id.sort:
                        transaction = manager.beginTransaction();
                        transaction.replace(R.id.content,new MainFragment());
                        transaction.commit();
                        break;
                    case R.id.shujia:
                        transaction = manager.beginTransaction();
                        transaction.replace(R.id.content, new ShuJiaFragment());
                        transaction.commit();
                        break;
                    case R.id.search:
                        transaction = manager.beginTransaction();
                        transaction.replace(R.id.content, new SearchFragment());
                        transaction.commit();
                        break;
                    case R.id.mine:
                        transaction = manager.beginTransaction();
                        transaction.replace(R.id.content, new MineFragment());
                        transaction.commit();
                        break;
                    default:
                        break;
                }

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
}
