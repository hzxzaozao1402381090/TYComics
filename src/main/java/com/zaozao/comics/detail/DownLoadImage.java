package com.zaozao.comics.detail;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;

import com.zaozao.comics.customview.TextProgress;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Administrator on 2016/7/18.
 */
public class DownLoadImage extends AsyncTask<String, Integer, Bitmap> {

    TextProgress textProgress;
    ImageView imageView;

    public void displayImage(String urlStr, ImageView imageView, TextProgress textProgress) {
        this.imageView = imageView;
        this.textProgress = textProgress;
        execute(urlStr);
    }

    @Override
    protected void onPreExecute() {
        textProgress.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        textProgress.setVisibility(View.GONE);
        if(bitmap!=null){
            imageView.setImageBitmap(bitmap);
        }
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        URL url = null;
        Bitmap bitmap = null;
        try {
            url = new URL(params[0]);
            URLConnection connection = url.openConnection();
            connection.setReadTimeout(1000);
            connection.setConnectTimeout(5000);

            InputStream inputStream = connection.getInputStream();
            BufferedInputStream in = new BufferedInputStream(inputStream);
            bitmap = BitmapFactory.decodeStream(in);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
