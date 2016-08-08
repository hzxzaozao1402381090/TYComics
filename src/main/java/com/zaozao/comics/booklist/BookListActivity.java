package com.zaozao.comics.booklist;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;
import com.zaozao.comics.Constant;
import com.zaozao.comics.R;
import com.zaozao.comics.bean.Book;
import com.zaozao.comics.booklist.MyAdapter;
import com.zaozao.comics.detail.BookDetail;
import com.zaozao.comics.http.CallServer;
import com.zaozao.comics.http.HttpListener;
import com.zaozao.comics.http.HttpURL;
import com.zaozao.comics.http.JsonParser;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class BookListActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener, HttpListener<String>, View.OnClickListener,MyAdapter.OnRecycleViewItemClickListener {

    public ImageView back;
    public TextView title;
    public List<Book> books;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    MyAdapter adapter;
    int lastVisibleItem;
    String type;//漫画类别
    LinearLayoutManager manager;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list2);
        getIntentData();
        findView();
        init();
        setAdapterListener();
    }

    /**
     * 查找控件
     */
    public void findView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyle_view);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        back = (ImageView) findViewById(R.id.list_back);
        title = (TextView) findViewById(R.id.book_sort);
    }

    /**
     * 获得上一界面传递的值
     */
    public void getIntentData() {
        Intent intent = this.getIntent();
        books = intent.getParcelableArrayListExtra(Constant.COMICS_NAME);
        type = intent.getStringExtra(Constant.COMIC_SORT);
    }

    /**
     * 初始化RecycleView和SwipRefreshLayout
     */
    public void init() {
        recyclerView.setHasFixedSize(true);
        manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        title.setText(type);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
    }

    /**
     * 设置适配器和监听器
     */
    public void setAdapterListener() {
        adapter = new MyAdapter(this, books, type);
        adapter.setOnRecycleItemClickListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = manager.findLastVisibleItemPosition();
                if (lastVisibleItem > manager.getItemCount() - 3) {
                    loadMore();
                }
            }
        });
        back.setOnClickListener(this);
    }

    /**
     * 上拉加载更多数据
     */
    public void loadMore() {
        Request<String> loadRequest = NoHttp.createStringRequest(HttpURL.COMICS_LIST);
        loadRequest.add("key", HttpURL.APP_KEY);
        loadRequest.add("type", type);
        loadRequest.add("skip", adapter.getItemCount());
        CallServer.getInstance().add(this, Constant.LOADMORE, loadRequest, this, true, false);
    }

    /**
     * 下拉刷新数据
     */
    @Override
    public void onRefresh() {
        Request<String> request = NoHttp.createStringRequest(HttpURL.COMICS_LIST);
        request.add("type", type);
        request.add("key", HttpURL.APP_KEY);
        CallServer.getInstance().add(this, Constant.UPDATE, request, this, true, false);
    }

    @Override
    public void onSuccessed(int what, Response<String> response) {
        swipeRefreshLayout.setRefreshing(false);
        List<Book> newBooks = null;
        if (response != null && !TextUtils.isEmpty(response.get())) {
            if (what == Constant.UPDATE) {
                try {
                    newBooks = JsonParser.getBooks(response.get());
                    books.addAll(0, newBooks);
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (what == Constant.LOADMORE) {
                try {
                    newBooks = JsonParser.getBooks(response.get());
                    adapter.addData(newBooks);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Toast.makeText(this, Constant.NOMOREDATA, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onFailed(int what, String url, Object tag, Exception e, int responseCode, long networkMillis) {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.list_back) {
            finish();
        }
    }

    @Override
    public void onItemClick(Book book) {
        if(book!=null){
            Intent intent = new Intent(this, BookDetail.class);
            intent.putExtra(Constant.COMICS_NAME,book.getName());
            startActivity(intent);
        }
    }
}
