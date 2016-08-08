package com.zaozao.comics.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.CheckBox;

/**
 * Created by Administrator on 2016/8/5.
 */
public class MyCheckBox extends CheckBox {
    public MyCheckBox(Context context) {
        super(context);
    }

    public MyCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){

        }
        return super.onTouchEvent(event);
    }
}
