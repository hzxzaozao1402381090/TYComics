package com.zaozao.comics.guide;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.zaozao.comics.Constant;
import com.zaozao.comics.R;
import com.zaozao.comics.SplashActivity;

/**
 * Created by MINE on 2016/6/15.
 */
public class GuideFragmentThird extends android.support.v4.app.Fragment{

    SharedPreferences preferences;//首次安装完成后，记下标记，下次不在进入主界面

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_guide_fragment3,container,false);
        Button button = (Button) view.findViewById(R.id.enter);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),SplashActivity.class);
                startActivity(intent);
                preferences = getActivity().getSharedPreferences(Constant.FIRST_INSTALL, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean(Constant.FIRST_INSTALL,false);
                editor.commit();
                GuideFragmentThird.this.getActivity().finish();
            }
        });
        return view;
    }


}
