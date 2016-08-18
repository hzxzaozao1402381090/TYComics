package com.zaozao.comics.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 胡章孝 on 2016/8/13.
 */
public class SqliteDao {

    private static SqliteDao sqliteDao;
    private SQLiteDatabase db;
    private SQLiteOpenHelper helper;

    private SqliteDao(Context context) {
        helper = new SqliteHelper(context, 1);
        db = helper.getWritableDatabase();
    }

    public synchronized static SqliteDao getInstance(Context context) {
        if (sqliteDao == null) {
            sqliteDao = new SqliteDao(context);
        }
        return sqliteDao;
    }

    /**
     * 插入数据
     * @param name 图片名称
     * @param path 图片存放在SD上的地址
     */
    public void insert(String name, String path) {
        db.execSQL("insert into comic(name,path) values(?,?)", new String[]{name, path});
    }
    public boolean query(String name){
        Cursor cursor = db.rawQuery("select path from comic where name = ?",new String[]{name});
        if(cursor!=null&&cursor.moveToNext()){
            return true;
        }
        return false;
    }
    public String queryPath(String name){

        Cursor cursor = db.rawQuery("select path from comic where name = ?",new String[]{name});
        if(cursor!=null&&cursor.moveToNext()){
            return  cursor.getString(cursor.getColumnIndex("path"));
        }
        return "";
    }
    /**
     * 关闭数据库
     */
    public void close() {
        if (db.isOpen()){
            db.close();
        }
    }
}
