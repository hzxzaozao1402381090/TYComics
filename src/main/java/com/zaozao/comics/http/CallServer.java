package com.zaozao.comics.http;

import android.content.Context;

import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.download.DownloadQueue;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;

/**
 * Created by 胡章孝 on 2016/6/28.
 */
public class CallServer {

    private static CallServer callServer;
    /**
     * 请求队列
     */
    private RequestQueue requestQueue;
    /**
     * 下载队列
     */
    private static DownloadQueue downloadQueue;

    private CallServer() {
        requestQueue = NoHttp.newRequestQueue();
    }

    /**
     * 使用单例模式获取当前类对象
     *
     * @return
     */
    public synchronized static CallServer getInstance() {
        if (callServer == null) {
            callServer = new CallServer();
        }
        return callServer;
    }

    public static DownloadQueue getDownloadQueueInstance() {
        if (downloadQueue == null) {
            downloadQueue = NoHttp.newDownloadQueue();
        }
        return downloadQueue;
    }

    public <T> void add(Context context, int what, Request<T> request, HttpListener<T> callback, boolean canCancel, boolean isLoading) {
        requestQueue.add(what, request, new HttpResponseListener<T>(context, request, callback, canCancel, isLoading));
    }
}
