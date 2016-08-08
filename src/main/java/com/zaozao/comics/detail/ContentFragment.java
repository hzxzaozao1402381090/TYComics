package com.zaozao.comics.detail;


import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;
import com.zaozao.comics.Constant;
import com.zaozao.comics.R;
import com.zaozao.comics.bean.Book;
import com.zaozao.comics.bean.BookChapter;
import com.zaozao.comics.http.CallServer;
import com.zaozao.comics.http.HttpListener;
import com.zaozao.comics.http.HttpURL;
import com.zaozao.comics.http.JsonParser;
import com.zaozao.comics.shujia.HistroyFragment;
import com.zaozao.comics.utils.Tools;

import org.json.JSONException;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class ContentFragment extends Fragment{

    GridView gridView;
    ArrayAdapter<String> adapter;
    int lastupdate;
    List<String> list = new ArrayList<>();
    ArrayList<BookChapter> listAll;
    String json;
    String comicName;
    String picture;
    ArrayList<BookChapter> listBeen;

    /**
     * 提供可供传递参数的实例化方法
     *
     * @param start     显示的起始位置
     * @param size      显示的章节数量
     * @param listBeen  章节列表
     * @param comicName 漫画名称
     * @return Fragment
     */
    public static ContentFragment newInstance(int start, int size, List<BookChapter> listAll,List<BookChapter> listBeen, String comicName, int lastupdate, String coverImage) {
        ContentFragment fragment = new ContentFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("start", start);
        bundle.putInt("size", size);
        bundle.putParcelableArrayList("all", (ArrayList<? extends Parcelable>) listAll);
        bundle.putInt("lastupdate", lastupdate);
        bundle.putString("picture", coverImage);
        bundle.putParcelableArrayList(Constant.CHAPTER_LIST, (ArrayList<? extends Parcelable>) listBeen);
        bundle.putString("comicName", comicName);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content, container, false);
        gridView = (GridView) view.findViewById(R.id.content_grid);
        int size = this.getArguments().getInt("size");
        final int start = this.getArguments().getInt("start");
        picture = this.getArguments().getString("picture");
        lastupdate = this.getArguments().getInt("lastupdate");
        comicName = this.getArguments().getString("comicName");
        listAll = this.getArguments().getParcelableArrayList("all");
        listBeen = this.getArguments().getParcelableArrayList(Constant.CHAPTER_LIST);
        for (int i = 0; i < size; i++) {
            list.add(String.valueOf(start + i));
        }
        adapter = new ArrayAdapter<>(getContext(), R.layout.content_grid_item, R.id.content_grid_item, list);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(),ContentActivity.class);
                intent.putExtra("what",listBeen.get(position).getId());
                intent.putExtra("chapter_name",listBeen.get(position).getName());
                intent.putExtra("comicName",comicName);
                intent.putParcelableArrayListExtra("all",  listAll);
                intent.putParcelableArrayListExtra("chapters",listBeen);
                startActivity(intent);
            }
        });
        return view;
    }
}
