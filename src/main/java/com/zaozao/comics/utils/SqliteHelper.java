package com.zaozao.comics.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 胡章孝 on 2016/8/13.
 */
public class SqliteHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "comics";

    public SqliteHelper(Context context,int version) {
        super(context, DB_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "create table comic(_id integer primary key autoincrement,name text,path text)";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
