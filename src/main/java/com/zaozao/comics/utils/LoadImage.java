package com.zaozao.comics.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.ImageView;

import com.zaozao.comics.sqlite.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by 胡章孝 on 2016/8/13.
 * 图片加载类，可用于加载网络图片和本地图片
 */
public class LoadImage extends AsyncTask<String, Integer, Bitmap> {

    HttpListener listener;
    Bitmap bitmap = null;
    HttpRequest request;
    ImageView imageView;
    String folder;
    String url;
    boolean allowCache;
    SqliteDao sqliteDao;
    Context context;

    public LoadImage(HttpListener listener, Context context) {
        this.listener = listener;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if (bitmap != null) {
            listener.succeed(bitmap);
            if (imageView != null)
                imageView.setImageBitmap(bitmap);
        }
        if (allowCache) {
            try {
                sqliteDao = SqliteDao.getInstance(context);
                sqliteDao.insert(getImageName(url), Environment.getExternalStorageDirectory() + File.separator + folder + File.separator + getImageName(url));
                SDCard.getInstance().saveBitmap(bitmap, folder, getImageName(url));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        sqliteDao = SqliteDao.getInstance(context);
        if (!sqliteDao.query(getImageName(strings[0]))) {
            request = new HttpRequest(null, strings[0]);
            try {
                InputStream is = request.getNetInputStream();
                bitmap = BitmapFactory.decodeStream(is);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    /**
     * 加载本地图片
     *
     * @param path      图片地址
     * @param imageView
     */
    public void loadLoaclImage(String path, ImageView imageView) {
        bitmap = BitmapFactory.decodeFile(path);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        }
    }

    /**
     * 加载网络图片
     * http://i9.qhimg.com/t01ebbacb7c6215bdfd.jpg
     *
     * @param url        图片地址
     * @param imageView
     * @param allowCache 是否允许缓存图片
     */
    public void loadNetImage(String url, ImageView imageView, String folder, boolean allowCache) {
        this.url = url;
        this.imageView = imageView;
        this.allowCache = allowCache;
        this.folder = folder;
        this.execute(url);
    }

    /**
     * 下载图片
     *
     * @param url
     * @param folder
     * @param allowCache
     */
    public void downLoadImage(String url, String folder, boolean allowCache) {
        this.url = url;
        this.allowCache = allowCache;
        this.folder = folder;
        this.execute(url);
    }

    /**
     * 获得图片名称
     *
     * @param url
     * @return
     */
    public String getImageName(String url) {
        String[] temp = url.split("/");
        return temp[temp.length - 1];
    }
}
