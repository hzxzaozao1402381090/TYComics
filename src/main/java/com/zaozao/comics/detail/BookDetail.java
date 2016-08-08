package com.zaozao.comics.detail;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.telecom.Call;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.download.DownloadRequest;
import com.yolanda.nohttp.rest.CacheMode;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;
import com.yolanda.nohttp.tools.ImageDownloader;
import com.zaozao.comics.APP;
import com.zaozao.comics.Constant;
import com.zaozao.comics.R;
import com.zaozao.comics.bean.Book;
import com.zaozao.comics.bean.BookChapter;
import com.zaozao.comics.guide.GuideActivity;
import com.zaozao.comics.http.CallServer;
import com.zaozao.comics.http.HttpListener;
import com.zaozao.comics.http.HttpURL;
import com.zaozao.comics.http.JsonParser;
import com.zaozao.comics.sqlite.SqliteDao;
import com.zaozao.comics.sqlite.SqliteHelper;
import com.zaozao.comics.utils.Tools;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qzone.QZone;

public class BookDetail extends AppCompatActivity implements View.OnClickListener, HttpListener<String> {

    private ImageButton back;//返回按钮
    private ImageView book_cover;//漫画封面
    private TextView book_name;//漫画名称
    private TextView last_update;//最近更新时间
    private TextView book_desc;//漫画描述
    private TextView total;//更新至
    private String jsonStr;
    private Book book;
    private List<BookChapter> chapter;
    private String comicName;
    private TextView shareTextView;
    private TextView downLoadTextView;
    private TextView keepTextView;
    private TextView desktopTextView;
    private RelativeLayout relativeLayout;
    private LinearLayout bottomNav;
    private PagerSlidingTabStrip tabStrip;
    private ViewPager viewPager;

    private ContentPagerAdapter adapter;
    private List<Fragment> list = new ArrayList<>();
    private List<String> title = new ArrayList<>();
    private ContentFragment fragment;
    PopupWindow popupWindow;//分享显示的popupWindow
    private int updateTotal;//总的章节数
    final int preSize = 15;//预读取章节数
    private int size;//实际读取的章节数
    private int start = 1;//读取章节的开始位置

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        findView();
        getData();

    }

    /**
     * 查找控件
     */
    public void findView() {
        back = (ImageButton) findViewById(R.id.back);
        book_cover = (ImageView) findViewById(R.id.book_cover);
        book_name = (TextView) findViewById(R.id.book_name);
        last_update = (TextView) findViewById(R.id.lastupdate);
        book_desc = (TextView) findViewById(R.id.desc);
        total = (TextView) findViewById(R.id.total);
        shareTextView = (TextView) findViewById(R.id.share);
        downLoadTextView = (TextView) findViewById(R.id.download);
        keepTextView = (TextView) findViewById(R.id.keep);
        desktopTextView = (TextView) findViewById(R.id.desktop);
        relativeLayout = (RelativeLayout) findViewById(R.id.parent);
        bottomNav = (LinearLayout) findViewById(R.id.frame);
        tabStrip = (PagerSlidingTabStrip) findViewById(R.id.content_tab);
        viewPager = (ViewPager) findViewById(R.id.content_pager);
        tabStrip.setIndicatorColorResource(R.color.theme);
        tabStrip.setIndicatorHeight(2);
        tabStrip.setDividerColorResource(R.color.transparent);
        tabStrip.setUnderlineColorResource(R.color.transparent);
    }

    /**
     * 设置监听器和适配器
     */
    public void setListenerAdapter() {
        back.setOnClickListener(this);
        shareTextView.setOnClickListener(this);
        downLoadTextView.setOnClickListener(this);
        keepTextView.setOnClickListener(this);
        desktopTextView.setOnClickListener(this);
        adapter = new ContentPagerAdapter(getSupportFragmentManager(), list, title);
        viewPager.setAdapter(adapter);
        tabStrip.setViewPager(viewPager);
    }

    /**
     * 设置漫画章节的排版
     */
    public void setContentOrder() {
        try {
            updateTotal = chapter.get(0).getTotal();
            System.out.println(updateTotal + "total");
            while (updateTotal - preSize > 0) {
                List<BookChapter> listBeen = chapter.subList(start - 1, start + preSize - 1);
                ArrayList<BookChapter> chapters = new ArrayList<>();
                chapters.addAll(listBeen);
                System.out.println(chapters.size() + "chapters");
                fragment = ContentFragment.newInstance(start, preSize, chapter, chapters, comicName, book.getLastUpdate(), book.getCoverImg());
                title.add(start + "-" + preSize);
                updateTotal = updateTotal - preSize;
                start = start + preSize;
                list.add(fragment);
            }
            if (updateTotal <= preSize) {
                size = updateTotal;
                List<BookChapter> listBeen = chapter.subList(start - 1, start + size - 1);
                ArrayList<BookChapter> chapters = new ArrayList<>();
                chapters.addAll(listBeen);
                fragment = ContentFragment.newInstance(start, size, chapter, chapters, comicName, book.getLastUpdate(), book.getCoverImg());
                if (size == 1) {
                    title.add(start + "");
                } else {
                    title.add(start + "-" + (start + size - 1));
                }
                list.add(fragment);
            }
            setListenerAdapter();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getData() {

        Intent intent = getIntent();
        comicName = intent.getStringExtra(Constant.COMICS_NAME);
        Request<String> request = NoHttp.createStringRequest(HttpURL.COMICS_LIST);
        request.add("key", HttpURL.APP_KEY);
        request.add("name", comicName);
        request.setCacheKey(Constant.CACHE_DETAIL_ONE + comicName);
        request.setCacheMode(CacheMode.NONE_CACHE_REQUEST_NETWORK);
        Request<String> request_chapter = NoHttp.createStringRequest(HttpURL.COMICS_CHAPTER);
        request_chapter.add("key", HttpURL.APP_KEY);
        request_chapter.add("comicName", comicName);
        request_chapter.setCacheKey(Constant.CACHE_DETAIL_TWO + comicName);
        request_chapter.setCacheMode(CacheMode.NONE_CACHE_REQUEST_NETWORK);

        if (request != null && request_chapter != null) {
            CallServer.getInstance().add(this, Constant.COMIC_NAME_WHAT, request, this, false, false);
            CallServer.getInstance().add(this, Constant.COMIC_CHAPTER_WHAT, request_chapter, this, false, true);
        }
    }

    public void setData() {
        book_name.setText(book.getName());
        total.setText("更新至" + book.getTotal() + "话");
        last_update.setText("最近更新：" + Tools.formatDate(book.getLastUpdate()));
        if (!TextUtils.isEmpty(book.getDes())) {
            book_desc.setText(book.getDes());
        }
        APP.imageLoader.displayImage(book.getCoverImg(), book_cover);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                this.finish();
                break;
            case R.id.share:
                showSharePopup(v);
                break;
            case R.id.download:
                Intent intent = new Intent(this, DownLoadActivity.class);
                intent.putParcelableArrayListExtra(Constant.CHAPTER_LIST, (ArrayList<? extends Parcelable>) chapter);
                intent.putExtra(Constant.COMICS_NAME, comicName);
                intent.putExtra(Constant.COMICS_COVER,book.getCoverImg());
                startActivity(intent);
                break;
            case R.id.keep:
                SqliteDao sqliteDao = new SqliteDao(this);
                sqliteDao.insert(book, SqliteHelper.TABLE_SHOUCANG);
                Toast.makeText(this, "收藏成功！", Toast.LENGTH_SHORT).show();
                break;
            case R.id.desktop:
                Toast.makeText(this, "创建快捷方式", Toast.LENGTH_SHORT).show();
                Intent addIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
                Parcelable icon = Intent.ShortcutIconResource.fromContext(this, R.mipmap.ic_launcher);
                Intent myIntent = new Intent(BookDetail.this, BookDetail.class);
                myIntent.putExtra(Constant.COMICS_NAME, comicName);
                addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "快捷方式");
                addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
                addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, myIntent);
                sendBroadcast(addIntent);
                Toast.makeText(this, "已添加至桌面快捷方式", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onSuccessed(int what, Response<String> response) {
        if (response != null) {
            jsonStr = response.get();
            if (what == Constant.COMIC_NAME_WHAT) {
                if (!TextUtils.isEmpty(jsonStr)) {
                    Log.i(Constant.TAG, jsonStr);
                    try {
                        book = JsonParser.getBooks(jsonStr).get(0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else if (what == Constant.COMIC_CHAPTER_WHAT) {
                if (!TextUtils.isEmpty(jsonStr)) {
                    Log.i(Constant.TAG, jsonStr);
                    try {
                        chapter = JsonParser.getBookChapter(jsonStr);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (book != null && chapter != null) {
                setData();
                setContentOrder();
            }
        }
    }

    @Override
    public void onFailed(int what, String url, Object tag, Exception e, int responseCode, long networkMillis) {

    }

    public void showSharePopup(View v) {
        bottomNav.setVisibility(View.GONE);
        View view = getLayoutInflater().inflate(R.layout.share_layout, null);
        Button quxiao = (Button) view.findViewById(R.id.cancel);
        popupWindow = new PopupWindow();//初始化popupWindow
        DisplayMetrics dm = new DisplayMetrics();//获取屏幕宽高
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        popupWindow.setWidth(dm.widthPixels); //设置popupWindow的宽高
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(view);//为popupWindow设置界面显示
        popupWindow.setAnimationStyle(R.style.popwin_anim_style);
        relativeLayout.setBackgroundColor(getResources().getColor(R.color.tp_black));
        back.setEnabled(false);
        popupWindow.showAtLocation(relativeLayout, Gravity.BOTTOM, 0, view.getHeight());
        quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomNav.setVisibility(View.VISIBLE);
                relativeLayout.setBackgroundColor(getResources().getColor(R.color.mine_bg));
                back.setEnabled(true);
                popupWindow.dismiss();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (popupWindow != null && popupWindow.isShowing()) {
                popupWindow.dismiss();
                relativeLayout.setBackgroundColor(getResources().getColor(R.color.mine_bg));
                back.setEnabled(true);
                bottomNav.setVisibility(View.VISIBLE);
                popupWindow = null;
            } else {
                finish();
            }
        }
        return false;
    }
}
