package com.zaozao.comics.services;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


import com.zaozao.comics.utils.HttpListener;
import com.zaozao.comics.utils.LoadImage;
import com.zaozao.comics.utils.SharedPre;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MyIntentService extends IntentService {

    List<String> list = new ArrayList<>();
    int i = 0;

    public MyIntentService() {
        super("MyIntentService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        ExecutorService es = Executors.newFixedThreadPool(3);
        Map<String, List<String>> map = getMapData();
        Set<Map.Entry<String, List<String>>> entry = map.entrySet();
        Iterator<Map.Entry<String, List<String>>> it = entry.iterator();
        while (it.hasNext()) {
            Map.Entry<String, List<String>> next = it.next();
            List<String> value = next.getValue();
            String key = next.getKey();
            System.out.println(key + ":" + value);
            es.execute(new DownLoadTask(value, key));
        }
        es.shutdown();
    }

    class DownLoadTask implements Runnable, HttpListener {

        List<String> list;
        String tag;
        int progress;
        SharedPre sharedPre;

        public DownLoadTask(List<String> list, String tag) {
            this.list = list;
            this.tag = tag;
            sharedPre = new SharedPre("comics", MyIntentService.this);
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

    public Map<String, List<String>> getMapData() {
        Map<String, List<String>> map = new HashMap<>();
        for (int i = 0; i < 20; i++) {
            list.add("http://i9.qhimg.com/t01ebbacb7c6215bdfd.jpg");
        }
        for (int i = 0; i < 5; i++) {
            map.put("comic" + i, list);
        }
        return map;
    }

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String m = (String) msg.obj;
            Intent intent = new Intent();
            intent.putExtra("type", m);
            intent.putExtra("max", msg.arg1);
            intent.setAction("com.zaozao.others.MyReceiver");
            Log.i("TAG", "收到消息");
            sendBroadcast(intent);
            Log.i("TAG", "发送广播");
        }
    }
}
