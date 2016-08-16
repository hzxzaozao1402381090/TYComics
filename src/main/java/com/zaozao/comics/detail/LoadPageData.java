package com.zaozao.comics.detail;

import android.content.Context;
import android.util.Log;

import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.rest.CacheMode;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;
import com.zaozao.comics.http.CallServer;
import com.zaozao.comics.http.HttpListener;
import com.zaozao.comics.http.JsonParser;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by 胡章孝 on 2016/8/2.
 * 此类主要用于加载界面数据，并将得到的数据以接口
 * 回调的方式返回给界面
 */
public class LoadPageData implements HttpListener<String> {

    /**
     * 上下文对象
     */
    private Context context;
    /**
     * 请求的URL地址
     */
    private String url;
    /**
     * 请求队列
     */
    private Request<String> request;
    /**
     * 数据回调接口对象
     */
    private DataCallback callback;

    /**
     * 构造函数
     * @param url
     */
    public LoadPageData(Context context,String url,DataCallback callback){
        this.url = url;
        this.context = context;
        this.callback = callback;
    }

    /**
     * 添加请求参数
     * @param key APPKEY
     * @param name 漫画名
     * @param id 漫画章节id
     */
    public void addRequestParams(String key,String name,int id){
        request = NoHttp.createStringRequest(url);
        request.add("key", key);
        request.add("comicName", name);
        request.add("id", id);
        Log.i("URL",url);
        request.setCacheKey(url);
        request.setCacheMode(CacheMode.NONE_CACHE_REQUEST_NETWORK);
    }

    /**
     * 添加请求任务
     * @param what
     */
    public void addTask(int what){
        CallServer.getInstance().add(context,what,request,this,false,true);
    }
    /**
     * 请求成功回调
     * @param what
     * @param response
     */
    @Override
    public void onSuccessed(int what, Response<String> response) {
        if(response!=null){
            String json = response.get();
            try {
                callback.getData(JsonParser.getContentImage(json));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 请求失败回调
     * @param what
     * @param url
     * @param tag
     * @param e
     * @param responseCode
     * @param networkMillis
     */
    @Override
    public void onFailed(int what, String url, Object tag, Exception e, int responseCode, long networkMillis) {
        e.printStackTrace();
    }
    public interface DataCallback{
        void getData( ArrayList<String> imageList);
    }
}

