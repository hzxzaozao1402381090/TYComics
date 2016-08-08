package com.zaozao.comics.detail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zaozao.comics.Constant;
import com.zaozao.comics.R;
import com.zaozao.comics.bean.BookChapter;
import com.zaozao.comics.bean.LoadFile;
import com.zaozao.comics.customview.MyGridView;
import com.zaozao.comics.utils.AppConfig;

import java.util.ArrayList;

public class DownLoadActivity extends Activity implements View.OnClickListener {

    private GridView gridView;
    private Button chooseAll, startDownLoad;
    private GridAdapter adapter;
    private ArrayList<String> data;
    private ArrayList<CheckBox> checkBoxes;
    private ArrayList<BookChapter> chapters;//总的章节列表
    private ArrayList<BookChapter> choosedChapters;//选中的章节列表
    private boolean isCheckedAll;
    private ImageView back;
    private TextView title;
    private TextView manager;
    private String comicName;
    private String comic_cover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down_load);
        findView();
        getData();
        setAdapterListener();
    }

    /**
     * 查找所有控件
     */
    public void findView() {
        gridView = (GridView) findViewById(R.id.download_grid);
        chooseAll = (Button) findViewById(R.id.checkall);
        startDownLoad = (Button) findViewById(R.id.start_download);
        back = (ImageView) findViewById(R.id.back_arrow);
        title = (TextView) findViewById(R.id.title);
        manager = (TextView) findViewById(R.id.other);
    }

    /**
     * 设置适配器和监听器
     */
    public void setAdapterListener() {
        choosedChapters = new ArrayList<>();
        adapter = new GridAdapter();
        gridView.setAdapter(adapter);
        chooseAll.setOnClickListener(this);
        startDownLoad.setOnClickListener(this);
        back.setOnClickListener(this);
        manager.setOnClickListener(this);
    }

    /**
     * 取得上个界面传来的数据
     */
    public void getData() {
        data = new ArrayList<>();
        Intent intent = getIntent();
        chapters = intent.getParcelableArrayListExtra(Constant.CHAPTER_LIST);
        comicName = intent.getStringExtra(Constant.COMICS_NAME);
        comic_cover = intent.getStringExtra(Constant.COMICS_COVER);
        for (int i = 0; i < chapters.size(); i++) {
            data.add(i + 1 + "");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.checkall:
                if (!isCheckedAll) {
                    for (int i = 0; i < checkBoxes.size(); i++) {
                        checkBoxes.get(i).setChecked(true);
                    }
                    isCheckedAll = true;
                    chooseAll.setText("取消");
                    startDownLoad.setEnabled(true);
                } else {
                    for (int i = 0; i < checkBoxes.size(); i++) {
                        checkBoxes.get(i).setChecked(false);
                    }
                    isCheckedAll = false;
                    chooseAll.setText("全选");
                    startDownLoad.setEnabled(false);
                }
                break;
            case R.id.start_download:
                Intent intent = new Intent(this, DownLoadManagerActivity.class);
                intent.putParcelableArrayListExtra(Constant.CHAPTER_LIST, choosedChapters);
                intent.putExtra(Constant.COMICS_NAME, comicName);
                intent.putExtra(Constant.COMICS_COVER, comic_cover);
                saveLoadFile();
                startActivity(intent);
                this.finish();
                break;
            case R.id.back_arrow:
                finish();
                break;
        }
    }

    /**
     * 记录下来，每个文件的下载进度，以及其他相关信息
     */
    public void saveLoadFile() {
        for (int i = 0; i < choosedChapters.size(); i++) {
            LoadFile loadFile = new LoadFile();
            loadFile.setComicName(comicName);
            loadFile.setCoverImaage(comic_cover);
            loadFile.setName(choosedChapters.get(i).getName());
            loadFile.setMax(100);
            loadFile.setProgress(0);
            AppConfig.getInstance().putLoadFile(comicName + choosedChapters.get(i).getName(), loadFile);
        }
    }

    class GridAdapter extends BaseAdapter {

        public GridAdapter() {
            checkBoxes = new ArrayList<>();
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.download_grid_item, parent, false);
            }
            CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.download_checkbox);
            checkBox.setText(data.get(position));
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        choosedChapters.add(chapters.get(position));
                        startDownLoad.setEnabled(true);
                    }
                }
            });
            checkBoxes.add(checkBox);
            return convertView;
        }
    }
}
