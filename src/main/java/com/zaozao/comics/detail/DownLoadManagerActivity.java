package com.zaozao.comics.detail;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zaozao.comics.Constant;
import com.zaozao.comics.R;
import com.zaozao.comics.bean.BookChapter;
import com.zaozao.comics.bean.LoadFile;
import com.zaozao.comics.services.DownLoadService;
import com.zaozao.comics.utils.AppConfig;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
    RecyclerView downloadRecycle;
    String comicName;
    List<LoadFile> loadFiles;
    private ArrayList<BookChapter> choosedChapters;
    static String action = "com.zaozao.comics.detail.downloadmanageactivity";
    LinearLayoutManager llManager;
    String comic_cover;
    static int progress;
    static MyHolder holder;

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
        llManager = new LinearLayoutManager(this);
        llManager.setOrientation(LinearLayoutManager.VERTICAL);
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
        loadFiles = AppConfig.getInstance().readLoadFiles(comicName);
    }
    /**
     * 开启一个后台服务来下载文件
     */
    public void startDownload() {
        Intent intent = new Intent(this, DownLoadService.class);
        intent.putExtra(Constant.COMICS_NAME, comicName);
        intent.putExtra(Constant.COMICS_COVER, comic_cover);
        intent.putParcelableArrayListExtra(Constant.CHAPTER_LIST,choosedChapters);
        startService(intent);
    }

    /**
     * 设置适配器和监听器
     */
    public void setAdapterListener() {
        downloadRecycle.setLayoutManager(llManager);
        downloadRecycle.setAdapter(new DownLoadRecyAdapter());
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownLoadManagerActivity.this.finish();
            }
        });
    }

    class DownLoadRecyAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.download_recy_item, parent, false);
            holder = new MyHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof MyHolder) {
                ((MyHolder) holder).chapter.setText(loadFiles.get(position).getName());
                ((MyHolder) holder).progressBar.setProgress(loadFiles.get(position).getProgress());
            }
        }

        @Override
        public int getItemCount() {
            return loadFiles.size();
        }
    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView chapter;
        TextView download_state;
        ProgressBar progressBar;
        ImageButton options;

        public MyHolder(View itemView) {
            super(itemView);
            chapter = (TextView) itemView.findViewById(R.id.download_title);
            download_state = (TextView) itemView.findViewById(R.id.download_state);
            progressBar = (ProgressBar) itemView.findViewById(R.id.download_progress);
            options = (ImageButton) itemView.findViewById(R.id.download_options);
        }

        public void setProgress(int progress) {
            progressBar.setProgress(progress);
        }

        public void setMax(int max) {
            progressBar.setMax(max);
        }
    }

    public static class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println(action.equals(intent.getAction()) + "收到广播");
            if (action.equals(intent.getAction())) {
                int max = intent.getIntExtra("max", 0);
                progress += intent.getIntExtra("update_progress", 0);
                System.out.println(progress + "进度"+max);
                holder.setMax(max);
                holder.setProgress(progress);
                holder.download_state.setText((progress*100)/max  + "%" + "下载中");
                if (progress == max) {
                    holder.download_state.setText("下载完成");
                }
            }
        }
    }
}
