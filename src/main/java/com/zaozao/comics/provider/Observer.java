package com.zaozao.comics.provider;

import android.database.ContentObserver;
import android.os.Handler;

import com.zaozao.comics.shujia.DownLoadFragment;

import java.util.List;

/**
 * Created by 胡章孝 on 2016/8/9.
 */
public class Observer extends ContentObserver{

    List<LoadFile> list;
    DownLoadFragment.DownLoadAdapter adapter;
    public Observer(Handler handler, DownLoadFragment.DownLoadAdapter adapter, List<LoadFile> list) {
        super(handler);
        this.adapter = adapter;
        this.list = list;
    }

    @Override
    public void onChange(boolean selfChange) {
        adapter.notifyDataSetChanged();
    }
}
