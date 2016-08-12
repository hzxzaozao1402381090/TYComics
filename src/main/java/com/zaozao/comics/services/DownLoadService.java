package com.zaozao.comics.services;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;

import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.download.DownloadRequest;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DownLoadService extends IntentService implements LoadPageData.DataCallback, HttpListener<Bitmap> {


    private ArrayList<BookChapter> list;
    private LoadPageData loadPageData;
    private List<ArrayList<String>> imgList;
    private String comicName;
    private AppConfig config;
    private String comic_cover;
    private List<List<Request<Bitmap>>> allRequests;
    private List<LoadFile> loadFiles;

    public DownLoadService() {
        super("DownLoadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        loadPageData = new LoadPageData(this, HttpURL.COMICS_CHAPTER_CONTENT, this);
        comicName = intent.getStringExtra(Constant.COMICS_NAME);
        comic_cover = intent.getStringExtra(Constant.COMICS_COVER);
        list = intent.getParcelableArrayListExtra(Constant.CHAPTER_LIST);
        init();
    }

    public void init() {
        allRequests = new ArrayList<>();
        config = AppConfig.getInstance();
        loadFiles = config.readLoadFiles(comicName);
        imgList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            loadPageData.addRequestParams(HttpURL.APP_KEY, comicName, list.get(i).getId());
            loadPageData.addTaskInService(list.get(i).getId(), list.get(i).getName());
        }
    }

    @Override
    public void getData(ArrayList<String> imageList, String chapter_name) {
        Log.i("REQUEST", imageList.size() + "");
        if (imageList != null) {
            this.imgList.add(imageList);
            Log.i("REQUEST", imgList.size() + "," + list.size());
            if (imgList.size() == list.size()) {
                for (int i = 0; i < imgList.size(); i++) {
                    List<Request<Bitmap>> requests = new ArrayList<>();
                    for (int j = i; j < imgList.get(i).size(); j++) {
                        String url = imgList.get(i).get(j);
                        Request<Bitmap> request = NoHttp.createImageRequest(url);
                        requests.add(request);
                        allRequests.add(requests);
                    }
                }
                startDownload(allRequests);
            }
        }
    }

    /**
     * 开始下载
     *
     * @param urls 请求集合
     */
    public void startDownload(List<List<Request<Bitmap>>> urls) {

        ExecutorService es = Executors.newFixedThreadPool(3);
        for (int i = 0; i < urls.size(); i++) {
            config.putInt(comicName + i, 0);
            es.execute(new DownLoadTask(urls.get(i), i));
        }
    }

    @Override
    public void onSuccessed(int what, Response<Bitmap> response) {
        synchronized (this){
            Intent intent = new Intent();
            intent.setAction("com.zaozao.comics.detail.downloadmanageactivity");
            intent.putExtra("update_progress", 1);
            intent.putExtra("max", allRequests.get(what).size());
            sendBroadcast(intent);
        }
    }

    @Override
    public void onFailed(int what, String url, Object tag, Exception e, int responseCode, long networkMillis) {

    }

    class DownLoadTask implements Runnable {
        List<Request<Bitmap>> list;
        int what;

        public DownLoadTask(List<Request<Bitmap>> list, int what) {
            this.list = list;
            this.what = what;
        }

        @Override
        public void run() {
            for (int i = 0; i < list.size(); i++) {
                Request<Bitmap> request = list.get(i);
                request.setCacheKey(UUID.randomUUID().toString());
                request.setCacheMode(CacheMode.NONE_CACHE_REQUEST_NETWORK);
                CallServer.getInstance().add(APP.getInstance(), what, request, DownLoadService.this, true, false);
            }
        }
    }
}
