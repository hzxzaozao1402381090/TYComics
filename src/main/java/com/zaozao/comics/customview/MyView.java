package com.zaozao.comics.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zaozao.comics.R;

/**
 * Created by 胡章孝 on 2016/6/29.
 */
public class MyView extends LinearLayout implements View.OnClickListener {

    TextView sortTextView;
    ImageView imageView1, imageView2, imageView3;
    TextView textView1, textView2, textView3;
    TextView text_more;
    View view;
    MyViewListener myViewListener = null;

    /**
     * 初始化布局
     *
     * @param context
     */
    public void initView(Context context) {
        view = View.inflate(context, R.layout.home_list_item1, this);
        sortTextView = (TextView) view.findViewById(R.id.item_sort);
        imageView1 = (ImageView) findViewById(R.id.img_01);
        imageView2 = (ImageView) findViewById(R.id.img_02);
        imageView3 = (ImageView) findViewById(R.id.img_03);
        textView1 = (TextView) findViewById(R.id.text_01);
        textView2 = (TextView) findViewById(R.id.text_02);
        textView3 = (TextView) findViewById(R.id.text_03);
        text_more = (TextView)findViewById(R.id.item_more);
    }

    /**
     * 设置漫画种类
     *
     * @param sort
     */
    public void setSort(String sort) {
        sortTextView.setText(sort);
    }

    /**
     * 设置封面显示的图片
     *
     * @param b1
     * @param b2
     * @param b3
     */
    public void setImages(String b1, String b2, String b3, ImageLoader imageLoader) {
        imageLoader.displayImage(b1, imageView1);
        imageLoader.displayImage(b2, imageView2);
        imageLoader.displayImage(b3, imageView3);
    }

    /**
     * 设置三本漫画的名称
     *
     * @param s1
     * @param s2
     * @param s3
     */
    public void setTexts(String s1, String s2, String s3) {
        textView1.setText(s1);
        textView2.setText(s2);
        textView3.setText(s3);
    }

    public MyView(Context context) {
        super(context);
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnImageItemClickListener(MyViewListener myViewListener) {
        this.myViewListener = myViewListener;
        imageView1.setOnClickListener(this);
        imageView2.setOnClickListener(this);
        imageView3.setOnClickListener(this);
        text_more.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.img_01:
                myViewListener.img1Click();
                break;
            case R.id.img_02:
                myViewListener.img2Click();
                break;
            case R.id.img_03:
                myViewListener.img3Click();
                break;
            case R.id.item_more:
                myViewListener.moreClick();
                break;
        }
    }
}
