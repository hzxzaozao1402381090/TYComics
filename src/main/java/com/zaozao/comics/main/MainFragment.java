package com.zaozao.comics.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.rest.CacheMode;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;
import com.zaozao.comics.APP;
import com.zaozao.comics.booklist.BookListActivity;
import com.zaozao.comics.Constant;
import com.zaozao.comics.R;
import com.zaozao.comics.bean.Book;
import com.zaozao.comics.customview.BannerView;
import com.zaozao.comics.customview.MyView;
import com.zaozao.comics.customview.MyViewListener;
import com.zaozao.comics.detail.BookDetail;
import com.zaozao.comics.dialog.WaitDialog;
import com.zaozao.comics.http.CallServer;
import com.zaozao.comics.http.HttpListener;
import com.zaozao.comics.http.HttpURL;
import com.zaozao.comics.http.JsonParser;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainFragment extends Fragment implements HttpListener<String> {

    MyView view1, view2, view3, view4;
    BannerView view5;
    List<Book> bookList;
    Map<String, List<Book>> map_BookList;
    WaitDialog dialog;
    Request<String> request;
    @Override
    public void onSuccessed(int what, Response<String> response) {
        if (response != null) {
            if(dialog!=null&&dialog.isShowing()){
                dialog.dismiss();
            }
            String result = response.get();
            if (!TextUtils.isEmpty(result)) {
                Log.i(Constant.TAG, result);
                try {
                    bookList = JsonParser.getBooks(result);
                    switch (what) {
                        case Constant.SHAONIAN_WHAT:
                            setCustomView(Constant.SHAONIAN, bookList, view1);
                            setListener(view1,bookList,Constant.SHAONIAN);
                            break;
                        case Constant.QINGNIAN_WHAT:
                            setCustomView(Constant.QINGNIAN, bookList, view2);
                            setListener(view2,bookList,Constant.QINGNIAN);
                            break;
                        case Constant.SHAONV_WHAT:
                            setCustomView(Constant.SHAONV, bookList, view3);
                            setListener(view3,bookList,Constant.SHAONV);
                            break;
                        case Constant.DANMEI_WHAT:
                            setCustomView(Constant.DANMEI, bookList, view4);
                            setListener(view4,bookList,Constant.DANMEI);
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onFailed(int what, String url, Object tag, Exception e, int responseCode, long networkMillis) {
        Log.i(Constant.TAG, url + "获取数据失败！");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main_fragment, container, false);
        map_BookList = new HashMap<>();
        findView(view);
        dialog = new WaitDialog(getContext());
        dialog.show();
        addRequest(Constant.SHAONIAN_WHAT, Constant.SHAONIAN,Constant.CACHE_MAINVIEW_ONE);
        addRequest(Constant.QINGNIAN_WHAT, Constant.QINGNIAN,Constant.CACHE_MAINVIEW_TWO);
        addRequest(Constant.SHAONV_WHAT, Constant.SHAONV,Constant.CACHE_MAINVIEW_THREE);
        addRequest(Constant.DANMEI_WHAT, Constant.DANMEI,Constant.CACHE_MAINVIEW_FOUR);
        return view;
    }

    /**
     * 请求网络数据
     * @param what
     * @param type
     */
    public void addRequest(int what, String type,String CacheKey) {
        request = NoHttp.createStringRequest(HttpURL.COMICS_LIST);
        request.add("type", type);
        request.add("key", HttpURL.APP_KEY);
        request.setCacheKey(CacheKey);
        request.setCacheMode(CacheMode.NONE_CACHE_REQUEST_NETWORK);
        CallServer.getInstance().add(getContext(), what, request, this, true, false);
    }

    @Override
    public void onResume() {
        // view5.setAutoRecycle(true);
        super.onResume();

    }

    /**
     * 集中查找所有控件
     *
     * @param view
     */
    public void findView(View view) {
        view1 = (MyView) view.findViewById(R.id.view1);
        view2 = (MyView) view.findViewById(R.id.view2);
        view3 = (MyView) view.findViewById(R.id.view3);
        view4 = (MyView) view.findViewById(R.id.view4);
        view5 = (BannerView) view.findViewById(R.id.banner_view);
    }

    /**
     * 设置自定义控件的内容
     * @param type
     * @param bookList
     * @param view
     */
    public void setCustomView(String type, List<Book> bookList, MyView view) {
        view.setTexts(bookList.get(0).getName(), bookList.get(1).getName(), bookList.get(2).getName());
        view.setImages(bookList.get(0).getCoverImg(), bookList.get(1).getCoverImg(), bookList.get(2).getCoverImg(), APP.imageLoader);
        view.setSort(type);
    }

    /**
     * 设置每一种类上上个图片的点击监听事件
     *
     * @param view
     */
    public void setListener(MyView view, final List<Book> bookList,final String type) {
        view.setOnImageItemClickListener(new MyViewListener() {
            @Override
            public void img1Click() {
                Intent intent = new Intent(getActivity(), BookDetail.class);
                List<Book> list = bookList;
                intent.putExtra(Constant.COMICS_NAME,list.get(0).getName() );
                startActivity(intent);
            }

            @Override
            public void img2Click() {
                Intent intent = new Intent(getActivity(), BookDetail.class);
                List<Book> list = bookList;
                intent.putExtra(Constant.COMICS_NAME,list.get(1).getName() );
                startActivity(intent);

            }

            @Override
            public void img3Click() {
                Intent intent = new Intent(getActivity(), BookDetail.class);
                List<Book> list = bookList;
                intent.putExtra(Constant.COMICS_NAME,list.get(2).getName() );
                startActivity(intent);
            }

            @Override
            public void moreClick() {
                Intent intent = new Intent(getActivity(), BookListActivity.class);
                List<Book> list = bookList;
                intent.putParcelableArrayListExtra(Constant.COMICS_NAME, (ArrayList<? extends Parcelable>) list);
                intent.putExtra(Constant.COMIC_SORT,type);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onPause() {
        // view5.setAutoRecycle(false);
        super.onPause();

    }
}
