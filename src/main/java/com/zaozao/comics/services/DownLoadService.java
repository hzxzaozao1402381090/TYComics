package com.zaozao.comics.services;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.rest.CacheMode;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;
import com.zaozao.comics.APP;
import com.zaozao.comics.Constant;
import com.zaozao.comics.bean.BookChapter;
import com.zaozao.comics.bean.LoadFile;
import com.zaozao.comics.detail.LoadPageData;
import com.zaozao.comics.http.CallServer;
import com.zaozao.comics.http.HttpListener;
import com.zaozao.comics.http.HttpURL;
import com.zaozao.comics.utils.AppConfig;
import com.zaozao.comics.utils.LoadImage;
import com.zaozao.comics.utils.SharedPre;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DownLoadService extends IntentService implements LoadPageData.DataCallback {


    private ArrayList<BookChapter> list;
    private LoadPageData loadPageData;
    private Map<String, ArrayList<String>> imgList;
    private String comicName;
    private String comic_cover;
    int i = 0;

    public DownLoadService() {
        super("DownLoadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        loadPageData = new LoadPageData(this, HttpURL.COMICS_CHAPTER_CONTENT, this);
        comicName = intent.getStringExtra(Constant.COMICS_NAME);
        comic_cover = intent.getStringExtra(Constant.COMICS_COVER);
        list = intent.getParcelableArrayListExtra(Constant.CHAPTER_LIST);
        for(BookChapter chapter:list){
            System.out.println(chapter.getName()+"---------"+chapter.getId());
        }
        init();
    }

    public void init() {
        imgList = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            loadPageData.addRequestParams(HttpURL.APP_KEY, comicName, list.get(i).getId());
            loadPageData.addTask(list.get(i).getId());
        }
    }

    @Override
    public void getData(ArrayList<String> imageList) {
        Log.i("REQUEST__", imageList.size() + "--------------------------------");
        if (imageList != null) {
            this.imgList.put(comicName+(++i), imageList);
            Log.i("REQUEST__", imgList.size()+ "," + list.size());
            Set<Map.Entry<String, ArrayList<String>>> entry = imgList.entrySet();
            Iterator<Map.Entry<String, ArrayList<String>>> it = entry.iterator();
            while(it.hasNext()){
                Map.Entry<String, ArrayList<String>> next = it.next();
                Log.i("REQUEST__",next.getKey()+","+next.getValue());
            }
            if(imgList.size()==list.size()){
                ExecutorService es = Executors.newFixedThreadPool(3);
                for(int i = 0; i< imgList.size();i++){
                    String key = comicName+(i+1);
                    Log.i("REQUEST__",key);
                    es.execute(new DownLoadTask(imgList.get(key),key));
                }
            }
        }
    }

    class DownLoadTask implements Runnable, com.zaozao.comics.utils.HttpListener {

        List<String> list;
        String tag;
        int progress;
        SharedPre sharedPre;

        public DownLoadTask(List<String> list, String tag) {
            this.list = list;
            this.tag = tag;
            sharedPre = new SharedPre("comics", DownLoadService.this);
        }

        @Override
        public void run() {
            for (int i = 0; i < list.size(); i++) {
                LoadImage loadImage = new LoadImage(this,getApplicationContext());
                loadImage.downLoadImage(list.get(i), "comics", true);
            }
        }

        @Override
        public void succeed(String result) {
        }

        @Override
        public void succeed(Bitmap bitmap) {
            progress++;
            sharedPre.putInt(tag, progress);
            MyHandler handler = new MyHandler();
            Message message = new Message();
            message.obj = tag;
            message.arg1 = list.size();
            handler.sendMessage(message);
            Log.i("TAG", "下载完成" + (++i));
        }

        @Override
        public void failed() {

        }
    }

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String m = (String) msg.obj;
            Intent intent = new Intent();
            intent.putExtra("type", m);
            intent.putExtra("max", msg.arg1);
            intent.setAction("com.zaozao.comics.detail.downloadmanageactivity");
            Log.i("TAG", "收到消息");
            sendBroadcast(intent);
            Log.i("TAG", "发送广播");
        }
    }
}
