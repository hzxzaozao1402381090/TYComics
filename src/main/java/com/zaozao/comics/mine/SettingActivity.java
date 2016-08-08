package com.zaozao.comics.mine;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.zaozao.comics.R;
import com.zaozao.comics.utils.AppConfig;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SettingActivity extends Activity implements View.OnClickListener {

    @InjectView(R.id.set_back)
    ImageView setBack;
    @InjectView(R.id.clear_cache)
    RelativeLayout clearCache;
    AppConfig config;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.inject(this);
        config = AppConfig.getInstance();
        setBack.setOnClickListener(this);
        clearCache.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.set_back:
                finish();
                break;
            case R.id.clear_cache:
                config.clearCache(this);
                break;
        }
    }
}
