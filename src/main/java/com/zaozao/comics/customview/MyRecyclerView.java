package com.zaozao.comics.customview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by 胡章孝 on 2016/7/27.
 */
public class MyRecyclerView extends RecyclerView {
    boolean flag = false;
    public MyRecyclerView(Context context) {
        super(context);
    }

    public MyRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            Log.i("TAG", "ACTION_DOWN");
            flag = true;
        }
        if (e.getAction() == MotionEvent.ACTION_UP&&flag) {
            flag = false;

            return false;
        } else if (e.getAction() == MotionEvent.ACTION_MOVE) {
            flag = false;
            Log.i("TAG", "ACTION_Move");
            return super.onTouchEvent(e);
        }
        return true;
    }
}
