package com.zaozao.comics.utils;

import android.content.Context;
import android.content.SharedPreferences;


import com.zaozao.comics.bean.LoadFile;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by 胡章孝 on 2016/8/14.
 */
public class SharedPre {

    private SharedPreferences sp;

    public SharedPre(String name, Context context) {
        sp = context.getSharedPreferences(name,Context.MODE_PRIVATE);
    }

    /**
     * 存放整型
     * @param key
     * @param value
     */
    public void putInt(String key,int value){
        sp.edit().putInt(key,value).commit();
    }

    /**
     * 得到整形值
     * @param key
     * @return
     */
    public int getInt(String key){
        return sp.getInt(key,0);
    }

    /**
     * 获取所有的数据
     * @return
     */
    public List<LoadFile> getAll(String key){
        List<LoadFile> fileList = new ArrayList<>();
        Map<String, ?> all = sp.getAll();
        Set<? extends Map.Entry<String, ?>> entry = all.entrySet();
        Iterator<? extends Map.Entry<String, ?>> it = entry.iterator();
        while(it.hasNext()){
            Map.Entry<String, ?> next = it.next();
            if(next.getKey().startsWith(key)){
                LoadFile loadFile = new LoadFile();
                loadFile.setName(next.getKey());
                loadFile.setProgress((Integer) next.getValue());
                fileList.add(loadFile);
            }
        }
        return fileList;
    }

    /**
     * 删除一条记录
     * @param key
     */
    public void delete(String key){
        sp.edit().remove(key).commit();
    }
}
