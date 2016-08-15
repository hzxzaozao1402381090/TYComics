package com.zaozao.comics.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.zaozao.comics.APP;

import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Created by 胡章孝 on 2016/8/5.
 */
public class AppConfig {

    private static AppConfig appConfig;
    private SQLiteDatabase db;
    private String sdCardPath;
    private SharedPreferences preferences;

    private AppConfig() {
        preferences = APP.getInstance().getSharedPreferences("tycomics", Context.MODE_PRIVATE);
    }

    public static synchronized AppConfig getInstance() {

        if (appConfig == null) {
            appConfig = new AppConfig();
        }
        return appConfig;
    }

    /**
     * 清除缓存
     *
     * @param context
     */
    public void clearCache(Context context) {
        db = SQLiteDatabase.openOrCreateDatabase(context.getFilesDir().toString() + "_nohttp_cache_db.db", null);
        db.execSQL("drop table cache_table;");
    }

    /**
     * 判断SD卡是否可用
     * 如果可用则返回SD卡的根目录路径
     *
     * @return
     */
    public static String sdCardPath() {
        String sdRoot = "";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            sdRoot = Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        return sdRoot;
    }

    public void writeToSDCard(String folder, String file) {
        //查找SD卡
        File sdFile = new File(Environment.getExternalStorageDirectory().getPath());
        //在SD卡上建立文件夹
        File newFolder = new File(sdFile, folder);
        if (!newFolder.exists()) {
            newFolder.mkdirs();
        }
    }

    /**
     * 创建根文件夹
     *
     * @param parent
     * @return
     */
    public File createRootFolder(String parent) {
        File file = new File(sdCardPath(), parent);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    /**
     * 创建子文件夹
     *
     * @param parent
     * @param child
     * @return
     */
    public File createFolder(String parent, String child) {
        File file = new File(parent, child);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    /**
     * 将LoadFile对象存储在sharedpreferences中
     *
     * @param key
     * @param loadFile
     */
    public void putLoadFile(String key, LoadFile loadFile) {


        // 创建字节输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            // 创建对象输出流，并封装字节流
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            // 将对象写入字节流
            oos.writeObject(loadFile);
            // 将字节流编码成base64的字符窜
            String oAuth_Base64 = new String(Base64.encodeBase64(baos
                    .toByteArray()));
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(key, oAuth_Base64);
            editor.commit();
        } catch (IOException e) {
            // TODO Auto-generated
        }
        Log.i("ok", "存储成功");
    }

    /**
     * 读取对象
     *
     * @param key
     * @return
     */
    public LoadFile readLoadFile(String key) {
        LoadFile loadFile = null;
        String productBase64 = preferences.getString(key, "");
        //读取字节
        byte[] base64 = Base64.decodeBase64(productBase64.getBytes());
        //封装到字节流
        ByteArrayInputStream bais = new ByteArrayInputStream(base64);
        try {
            //再次封装
            ObjectInputStream bis = new ObjectInputStream(bais);
            try {
                //读取对象
                loadFile = (LoadFile) bis.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loadFile;
    }

    /**
     * 读取所有的LoadFIle对象
     *
     * @return
     */
    public List<LoadFile> readLoadFiles(String KeyName) {
        List<LoadFile> files = new ArrayList<>();
        Map<String, ?> data = preferences.getAll();
        Set<? extends Map.Entry<String, ?>> set = data.entrySet();
        Iterator<? extends Map.Entry<String, ?>> it = set.iterator();
        while (it.hasNext()) {
            Map.Entry<String, ?> next = it.next();
            String key = next.getKey();
            if (key.startsWith(KeyName)) {
                LoadFile file = readLoadFile(key);
                files.add(file);
            }
        }
        return files;
    }

    /**
     * 删除一条记录
     *
     * @param key
     */
    public void deleteRecord(String key) {
        Map<String, ?> all = preferences.getAll();
        Set<? extends Map.Entry<String, ?>> entry = all.entrySet();
        Iterator<? extends Map.Entry<String, ?>> it = entry.iterator();
        while (it.hasNext()) {
            Map.Entry<String, ?> k = it.next();
            String realKey = k.getKey();
            if (realKey.startsWith(key)) {
                preferences.edit().remove(realKey).commit();
            }
        }
    }
    public void putInt(String key,int progress){
        preferences.edit().putInt(key,progress).commit();
    }
}
