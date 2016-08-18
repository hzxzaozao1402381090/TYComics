package com.zaozao.comics.detail;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zaozao.comics.Constant;
import com.zaozao.comics.R;
import com.zaozao.comics.bean.Book;
import com.zaozao.comics.bean.BookChapter;
import com.zaozao.comics.bean.LoadFile;
import com.zaozao.comics.services.DownLoadService;
import com.zaozao.comics.utils.SharedPre;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class DownLoadManagerActivity extends AppCompatActivity {


    String comicName;
    @InjectView(R.id.back_arrow)
    ImageView backArrow;
    @InjectView(R.id.title)
    TextView title;
    @InjectView(R.id.other)
    TextView other;
    @InjectView(R.id.download_recycle)
    ListView downloadRecycle;
    private ArrayList<BookChapter> choosedChapters;
    private ArrayList<BookChapter> allChapters;
    static String action = "com.zaozao.comics.detail.downloadmanageactivity";
    String comic_cover;
    static List<LoadFile> fileList;
    static ListViewAdapter adapter;
    SharedPre sharedPre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down_load_manager);
        ButterKnife.inject(this);
        getIntentData();
        init();
        setAdapterListener();
        startDownload();
    }

    /**
     * 初始化
     */
    public void init() {
        sharedPre = new SharedPre("comics", this);
        fileList = sharedPre.getAll(comicName);
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
        allChapters = intent.getParcelableArrayListExtra("all");
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
        downloadRecycle.setAdapter(adapter);
        downloadRecycle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LoadFile file = (LoadFile) parent.getItemAtPosition(position);
                if (file.getState().equals("下载完成")) {
                    Intent intent = new Intent(DownLoadManagerActivity.this, ContentActivity.class);
                    intent.putExtra("comicName", comicName);
                    intent.putExtra("what",choosedChapters.get(position).getId());
                    intent.putExtra("chapter_name",choosedChapters.get(position).getName());
                    intent.putExtra("all",allChapters);
                    startActivity(intent);
                }
            }
        });
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownLoadManagerActivity.this.finish();
            }
        });
    }


    public static class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(action)) {
                System.out.println("收到广播----------------------------------------------------");
                String type = intent.getStringExtra("type");
                int max = intent.getIntExtra("max", 0);
                for (LoadFile file : fileList) {
                    if (file.getName().equals(type)) {
                        int progress = file.getProgress();
                        file.setMax(max);
                        file.setProgress(++progress);
                        if (progress == max) {
                            file.setState("下载完成");
                        } else {
                            file.setState((progress * 100) / max + "%下载中");
                        }
                    }
                }
                for (LoadFile file : fileList) {
                    System.out.println(file.getName() + "," + file.getProgress());
                }
                adapter.notifyDataSetChanged();
            }
        }
    }
}
