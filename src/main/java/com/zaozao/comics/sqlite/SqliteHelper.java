package com.zaozao.comics.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/7/18.
 */
public class SqliteHelper extends SQLiteOpenHelper {

    public static final String TABLE_SHOUCANG = "shoucang";
    public static final String TABLE_HISTROY = "histroy";
    public static final int VERSION = 1;


    public SqliteHelper(Context context, String name, int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table " + TABLE_SHOUCANG + "(_id integer not null primary key autoincrement,name text,picture text,readto text,updateto text,lastupdate text)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
