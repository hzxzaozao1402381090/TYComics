package com.zaozao.comics.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zaozao.comics.bean.Book;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 胡章孝 on 2016/7/18.
 * 用于数据库的增删改查
 */
public class SqliteDao {

    private SQLiteDatabase db;
    private SqliteHelper dbHelper;

    /**
     * 初始化SqliteDao对象
     *
     * @param context
     */
    public SqliteDao(Context context) {
        dbHelper = new SqliteHelper(context, "mydb", SqliteHelper.VERSION);
        db = dbHelper.getWritableDatabase();
    }

    /**
     * 插入数据
     *
     * @param book
     */
    public void insert(Book book, String tableName) {
        String isFinish = book.isFinish() ? "已完结" : "未完结";
        String sql = "insert into " + tableName + "(name,picture,readto,updateto,lastupdate) values" + "('" + book.getName() + "','" + book.getCoverImg() + "','" + isFinish + "','" + book.getLastUpdate() + "','" + book.getArea() + "')";
        db.execSQL(sql);
    }

    /**
     * 删除数据
     */
    public void remove(ArrayList<Map<String, Object>> list) {
        for (int i = 0; i < list.size(); i++) {
            String sql = "delete from " + SqliteHelper.TABLE_SHOUCANG + " where _id = " + list.get(i).get("id");
            db.execSQL(sql);
        }
    }

    /**
     * 更新数据
     */
    public void update() {

    }

    /**
     * 查询数据
     */
    public ArrayList<Map<String, Object>> query() {
        ArrayList<Map<String, Object>> list = new ArrayList<>();
        String sql = "select * from " + SqliteHelper.TABLE_SHOUCANG;
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", cursor.getInt(0));
            map.put("name", cursor.getString(1));
            map.put("picture", cursor.getString(2));
            map.put("readto", cursor.getString(3));
            map.put("updateto", cursor.getString(4));
            map.put("lastupdate", cursor.getString(5));
            list.add(map);
        }
        return list;
    }
}
