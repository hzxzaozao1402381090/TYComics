package com.zaozao.comics.shujia;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListView;

import com.zaozao.comics.R;
import com.zaozao.comics.bean.Book;
import com.zaozao.comics.sqlite.SqliteDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HistroyFragment extends ShuJiaFragment {


    public static List<Book> books = new ArrayList<>();
    ListView listView;
    ListAdapter adapter;
    ArrayList<Map<String, Object>> data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_histroy, container, false);
        listView = (ListView) view.findViewById(R.id.histroy_list);
        listView.setEmptyView(view.findViewById(R.id.histroy_empty));
        setAdapterListener();
        return view;
    }

    public void setAdapterListener() {
        data = booksToMap(books);
        adapter = new ListAdapter(data, this);
        listView.setAdapter(adapter);
    }

    /**
     * 数据形式转换
     *
     * @param books
     * @return
     */
    public ArrayList<Map<String, Object>> booksToMap(List<Book> books) {
        ArrayList<Map<String, Object>> list = new ArrayList<>();
        for (Book book : books) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", book.getName());
            map.put("readto", book.getChapterId());
            map.put("updateto", book.getTotal());
            map.put("lastupdate", book.getLastUpdate());
            map.put("picture", book.getCoverImg());
            list.add(map);
        }
        return list;
    }

    /**
     * 更新界面数据
     */
    public void refresh(ArrayList<Map<String, Object>> delete_list) {
        List<Book> bookList = new ArrayList<>();
        for (Map<String, Object> map : delete_list) {
            Book book = new Book();
            book.setName(map.get("name").toString());
            book.setChapterId((int)map.get("readto"));
            book.setTotal((int)map.get("updateto"));
            book.setCoverImg(map.get("picture").toString());
            book.setLastUpdate((int)map.get("lastupdate"));
            bookList.add(book);
            System.out.println(books.contains(book));
            System.out.println(book.toString());
            System.out.println(books.toString());
        }
        data.removeAll(delete_list);
        adapter.notifyDataSetChanged();
        System.out.println(books.size());
        books.removeAll(bookList);
        System.out.println(books.size());
     //  adapter = new ListAdapter(data,this);
      //  listView.setAdapter(adapter);

    }
}
