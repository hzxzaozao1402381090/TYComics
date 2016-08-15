package com.zaozao.comics.utils;

import android.graphics.Bitmap;

/**
 * Created by 胡章孝 on 2016/8/13.
 */
public interface HttpListener {
    /**
     * 请求成功
     */
    void succeed(String result);

    /**
     * 请求成功，图片
     * @param bitmap
     */
    void succeed(Bitmap bitmap);
    /**
     * 请求失败
     */
    void failed();
}
