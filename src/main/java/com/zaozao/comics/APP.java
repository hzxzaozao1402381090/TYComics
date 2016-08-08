package com.zaozao.comics;

import android.app.Application;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.cache.disc.DiskCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.IoUtils;
import com.yolanda.nohttp.NoHttp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import cn.smssdk.SMSSDK;

/**
 * Created by 胡章孝 on 2016/6/24.
 */
public class APP extends Application {

    //判断用户是否登录
    public static boolean isLogined = false;

    public static ImageLoader imageLoader;

    private static APP app_Instance;
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化短信验证码SDK
        SMSSDK.initSDK(this, Constant.APP_KEY, Constant.APP_SECRET);
        //初始化NoHttp
        NoHttp.initialize(this);
        //初始化ImageLoader
        initImageLoader();
        app_Instance = this;
    }
    public static APP getInstance(){
        return app_Instance;
    }

    /**
     * 初始化ImageLoader
     */
    public void initImageLoader() {
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
        builder.cacheInMemory(true);//设置允许图片缓存在内存中
        builder.bitmapConfig(Bitmap.Config.RGB_565);//设置图片存储的质量
        builder.cacheOnDisk(true);//设置允许图片缓存在SD卡上
        builder.showImageForEmptyUri(R.drawable.loading);//设置ImageView的图片Uri地址为空时显示的图片
        builder.showImageOnLoading(R.drawable.loading);//设置图片加载过程中显示的图片
        builder.showImageOnFail(R.drawable.loading);//设置图片加载失败时显示的图片
        DisplayImageOptions options = builder.build();

        ImageLoaderConfiguration.Builder configBuilder = new ImageLoaderConfiguration.Builder(getApplicationContext());
        configBuilder.defaultDisplayImageOptions(options);
        configBuilder.threadPoolSize(3);//设置允许开启的最大线程数
        configBuilder.diskCacheFileCount(300).diskCacheSize(1024 * 1024 * 1024);//设置SD卡上允许缓存的最大文件数，及允许缓存的最大容量
        ImageLoaderConfiguration imageConfig = configBuilder.build();

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(imageConfig);
    }
}
