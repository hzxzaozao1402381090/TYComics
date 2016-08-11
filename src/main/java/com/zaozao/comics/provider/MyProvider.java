package com.zaozao.comics.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.zaozao.comics.utils.AppConfig;

/**
 * Created by 胡章孝 on 2016/8/9.
 */
public class MyProvider extends ContentProvider {

    SharedPreferences preferences;
    @Override
    public boolean onCreate() {
        preferences = getContext().getSharedPreferences("tycomics", Context.MODE_PRIVATE);
        return false;
    }
    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }
    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }
    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        AppConfig.getInstance().deleteRecord(selection);
        getContext().getContentResolver().notifyChange(uri,null);
        return 0;
    }
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
