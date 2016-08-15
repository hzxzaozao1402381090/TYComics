package com.zaozao.comics.utils;

import android.os.Handler;
import android.os.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by 胡章孝 on 2016/8/13.
 */
public class HttpRequest {

    private HttpListener listener;
    private String urlStr;
    private StringBuffer result;
    private static final int NET_REQUEST_SUCCEED = 1;
    private static final int NET_REQUEST_FAILED = 0;

    public HttpRequest(HttpListener listener, String urlStr) {
        this.listener = listener;
        this.urlStr = urlStr;
    }

    /**
     * 获取网络输入流
     *
     * @return
     * @throws IOException
     */
    public InputStream getNetInputStream() throws IOException {
        InputStream is = null;
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);
        conn.connect();
        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            is = conn.getInputStream();
        }
        return is;
    }

    /**
     * 开始请求
     */
    public void request() {
        result = new StringBuffer();
        new Thread() {
            @Override
            public void run() {
                try {
                    MyHandler handler = new MyHandler();
                    InputStream is = getNetInputStream();
                    Message message = new Message();
                    if (is != null) {
                        InputStreamReader isr = new InputStreamReader(is, "utf-8");
                        BufferedReader br = new BufferedReader(isr);
                        String line = "";
                        while ((line = br.readLine()) != null) {
                            result.append(line);
                        }
                        message.what = NET_REQUEST_SUCCEED;
                        message.obj = result;
                    }else{
                        message.what = NET_REQUEST_FAILED;
                    }
                    handler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                super.run();
            }
        }.start();
    }

    /**
     * 处理子线程传递的消息
     */
    class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == NET_REQUEST_SUCCEED) {
                StringBuffer buffer = (StringBuffer) msg.obj;
                if(listener!=null){
                    listener.succeed(buffer.toString());
                }
            }else if(msg.what == NET_REQUEST_FAILED){
                if(listener!=null){
                    listener.failed();
                }
            }
        }
    }
}
