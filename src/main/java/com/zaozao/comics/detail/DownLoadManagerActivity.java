package com.zaozao.comics.detail;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.zaozao.comics.Constant;
import com.zaozao.comics.R;
import com.zaozao.comics.bean.BookChapter;
import com.zaozao.comics.bean.LoadFile;
import com.zaozao.comics.services.DownLoadService;
import com.zaozao.comics.utils.SharedPre;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class DownLoadManagerActivity extends AppCompatActivity {

    @InjectView(R.id.back_arrow)
    ImageView backArrow;
    @InjectView(R.id.title)
    TextView title;
    @InjectView(R.id.other)
    TextView other;
    @InjectView(R.id.download_recycle)
    String comicName;
    private ArrayList<BookChapter> choosedChapters;
    static String action = "com.zaozao.comics.detail.downloadmanageactivity";
    String comic_cover;
    static int progress;
    static List<LoadFile> fileList;
    ListViewAdapter adapter;
    SharedPre sharedPre;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down_load_manager);
        ButterKnife.inject(this);
        getIntentData();
        init();
        startDownload();
        setAdapterListener();

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    /**
     * 初始化
     */
    public void init() {
        sharedPre = new SharedPre("comics",this);
        fileList = sharedPre.getAll();
        adapter = new ListViewAdapter(this, fileList);
        other.setText("删除");
        title.setText(comicName);
    }

    /**
     * 获取Intent传来的数据
     * 其中包含了每个章节的所有图片地址
     */
    public void getIntentData() {
        Intent intent = getIntent();
        comicName = intent.getStringExtra(Constant.COMICS_NAME);
        comic_cover = intent.getStringExtra(Constant.COMICS_COVER);
        choosedChapters = intent.getParcelableArrayListExtra(Constant.CHAPTER_LIST);
    }

    /**
     * 开启一个后台服务来下载文件
     */
    public void startDownload() {
        Intent intent = new Intent(this, DownLoadService.class);
        intent.putExtra(Constant.COMICS_NAME, comicName);
        intent.putExtra(Constant.COMICS_COVER, comic_cover);
        intent.putParcelableArrayListExtra(Constant.CHAPTER_LIST, choosedChapters);
        startService(intent);
    }

    /**
     * 设置适配器和监听器
     */
    public void setAdapterListener() {

    }


    public static class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println(action.equals(intent.getAction()) + "收到广播");
            if (action.equals(intent.getAction())) {
                int max = intent.getIntExtra("max", 0);
                progress += intent.getIntExtra("update_progress", 0);
                System.out.println(progress + "进度" + max);

            }
        }
    }
}
