package com.zaozao.comics.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 * Created by Administrator on 2016/8/7.
 */
public class MyGridView extends GridView {
    public MyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(ev.getAction() == MotionEvent.ACTION_DOWN){
            return false;
        }else if(ev.getAction() == MotionEvent.ACTION_UP){
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);
    }
}
