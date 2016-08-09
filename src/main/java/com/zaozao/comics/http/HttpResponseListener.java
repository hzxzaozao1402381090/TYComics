package com.zaozao.comics.http;

import android.content.Context;
import android.content.DialogInterface;
import android.view.WindowManager;

import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;
import com.zaozao.comics.Constant;
import com.zaozao.comics.dialog.WaitDialog;

/**
 * Created by 胡章孝 on 2016/6/28.
 */
public class HttpResponseListener<T> implements OnResponseListener<T> {

    private WaitDialog waitDialog;
    private Context context;
    private Request<T> request;
    HttpListener<T> callback;
    public HttpResponseListener(Context context, final Request<T> request, HttpListener<T> callback, boolean canCancel, boolean isLoading){
        this.context = context;
        this.callback = callback;
        this.request = request;
        if(context!=null&&isLoading){
            waitDialog = new WaitDialog(context);
            waitDialog.setCancelable(canCancel);
            waitDialog.setMessage(Constant.LOADING);
            waitDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    request.cancel();
                }
            });
        }
    }


    /**
     * 开始请求
     * 此处显示一个对话框
     */
    @Override
    public void onStart(int what) {
        if(waitDialog!=null&&!waitDialog.isShowing()){
            waitDialog.show();
            WindowManager.LayoutParams lp = waitDialog.getWindow().getAttributes();
            lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            waitDialog.getWindow().setAttributes(lp);
        }
    }

    /**
     * 成功回调
     */
    @Override
    public void onSucceed(int what, Response<T> response) {
        int responseCode = response.getHeaders().getResponseCode();
        if(responseCode > 400 && context!=null){
            waitDialog.dismiss();
        }
        if (callback != null){
            callback.onSuccessed(what,response);
        }
    }

    /**
     * 回调失败
     * @param what
     * @param url
     * @param tag
     * @param e
     * @param i1
     * @param l
     */
    @Override
    public void onFailed(int what, String url, Object tag, Exception e, int i1, long l) {
        if (callback!=null){
            callback.onFailed(what,url,tag,e,i1,l);
        }
    }

    /**
     * 结束请求
     */
    @Override
    public void onFinish(int what) {
        if(waitDialog!=null&&waitDialog.isShowing()){
            waitDialog.dismiss();
        }
    }
}
