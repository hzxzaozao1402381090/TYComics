package com.zaozao.comics.customview;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.zaozao.comics.Constant;
import com.zaozao.comics.R;
import com.zaozao.comics.main.ViewPagerAdapter;

public class BannerView extends FrameLayout {

    private List<ImageView> lists;
    private List<ImageView> dots;
    private ViewPager pager;
    private AtomicInteger atomicInteger = new AtomicInteger(0);
    View view;
    int[] Ids = {R.drawable.ban1, R.drawable.ban2, R.drawable.ban3,
            R.drawable.ban4};// 此处先使用固定数据

    public BannerView(Context context) {
        super(context);
    }

    public BannerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public BannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = View.inflate(context, R.layout.custom_view_banner, this);
        pager = (ViewPager) view.findViewById(R.id.viewpager);
        lists = new ArrayList<>();
        dots = new ArrayList<>();
        setData(Ids, context);
        setPager(pager);
    }

    public void setPager(final ViewPager pager) {
        pager.setAdapter(new ViewPagerAdapter(lists));
        pager.addOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < dots.size(); i++) {
                    if (position > dots.size() - 1) {
                        position = position % 4;
                    }
                    if (position == i) {
                        dots.get(i).setImageResource(R.drawable.dot_brown);
                    } else {
                        dots.get(i).setImageResource(R.drawable.dot_white);
                    }
                }
            }

            @Override
            public void onPageScrolled(int position, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                    //setAutoRecycle(false);
                } else if (state == ViewPager.SCROLL_STATE_IDLE) {
                   // setAutoRecycle(true);
                }
            }
        });
    }


    /**
     * 设置viewpager上显示的数据
     *
     * @param IDs
     * @param context
     */
    public void setData(int[] IDs, Context context) {
        for (int i = 0; i < 4; i++) {
            ImageView imageView = new ImageView(context);
            imageView.setImageResource(IDs[i]);
            imageView.setScaleType(ScaleType.FIT_XY);
            lists.add(imageView);
        }
        dots.add((ImageView) view.findViewById(R.id.dot1));
        dots.add((ImageView) view.findViewById(R.id.dot2));
        dots.add((ImageView) view.findViewById(R.id.dot3));
        dots.add((ImageView) view.findViewById(R.id.dot4));
    }

    public void setAutoRecycle(final boolean isAutoRecycle) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    while (true) {
                        if (isAutoRecycle) {
                            atomicInteger.incrementAndGet();
                            handler.sendEmptyMessage(atomicInteger.get());
                            sleep(3000);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            pager.setCurrentItem(msg.what);
            super.handleMessage(msg);
        }
    };
}
