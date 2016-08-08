package com.zaozao.comics.http;

import com.yolanda.nohttp.rest.Response;

/**
 * Created by 胡章孝 on 2016/6/28.
 */
public interface HttpListener<T> {

    //请求成功后
    void onSuccessed(int what, Response<T> response);
    //请求失败后
    void onFailed(int what,String url,Object tag,Exception e,int responseCode,long networkMillis);
}
