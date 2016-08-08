package com.zaozao.comics.shujia;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zaozao.comics.APP;
import com.zaozao.comics.LoginActivity;
import com.zaozao.comics.R;
import com.zaozao.comics.bean.Book;
import com.zaozao.comics.detail.ContentActivity;
import com.zaozao.comics.http.JsonParser;
import com.zaozao.comics.sqlite.SqliteDao;
import com.zaozao.comics.utils.Tools;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;


public class ShouCangFragment extends ShuJiaFragment implements ShuJiaFragment.OnDeleteClickListener {

    TextView tip;
    LinearLayout ll;//未登录
    LinearLayout ll_nodata;//登陆后无数据
    ListView listView;
    ListAdapter adapter;
    View view;
    boolean hasChoosed;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_shou_cang, container, false);
        }
        ll = (LinearLayout) view.findViewById(R.id.shoucang_fragment);
        ll_nodata = (LinearLayout) view.findViewById(R.id.shoucang_fragment_nodata);
        listView = (ListView) view.findViewById(R.id.shoucang_list);
        tip = (TextView) view.findViewById(R.id.tip);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("MM", "onResume");
        if (!APP.isLogined) {
            SpannableString span = new SpannableString(tip.getText());
            span.setSpan(new MyClickSpan(), 1, 3, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            tip.setText(span);
            tip.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            ll.setVisibility(View.GONE);
            data = new SqliteDao(getContext()).query();
            if (data.isEmpty()) {
                ll_nodata.setVisibility(View.VISIBLE);
            }
            adapter = new ListAdapter(data, this);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String json = null;
                    try {
                        json = Tools.getReadText("content.txt", getContext());
                        ArrayList<String> imageList = JsonParser.getContentImage(json);
                        Intent intent = new Intent(getActivity(), ContentActivity.class);
                        intent.putStringArrayListExtra("imageList", imageList);
                        startActivity(intent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public Book mapToBook(Map<String, Object> map) {
        Book book = new Book();
        book.setName(map.get("name").toString());
        return book;
    }

    @Override
    public void onDelete() {
        for (CheckBox checkBox : adapter.checkBoxes) {
            checkBox.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDeleteFile() {
        for (CheckBox checkBox : adapter.checkBoxes) {
            if (checkBox.isChecked())
                hasChoosed = true;
        }
        if (hasChoosed) {
            hasChoosed = false;
        } else {
            Toast.makeText(getContext(), "没有选中任何漫画", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 更新界面数据
     */
    public void refresh(SqliteDao sqliteDao) {
        data.clear();
        data.addAll(sqliteDao.query());
        if (data.isEmpty()) {
            adapter.notifyDataSetChanged();
            ll_nodata.setVisibility(View.VISIBLE);
        } else {
            adapter.notifyDataSetChanged();
        }
    }


    class MyClickSpan extends ClickableSpan {

        @Override
        public void updateDrawState(TextPaint ds) {
            //设置链接文字的颜色
            ds.setColor(Color.BLUE);
            //设置显示下划线
            ds.setUnderlineText(true);
        }

        @Override
        public void onClick(View widget) {
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
        }
    }
}
