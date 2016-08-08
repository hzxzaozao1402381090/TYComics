package com.zaozao.comics.detail;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.zaozao.comics.Constant;
import com.zaozao.comics.R;
import com.zaozao.comics.bean.BookChapter;
import com.zaozao.comics.customview.MyRecyclerView;
import com.zaozao.comics.http.HttpURL;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.RecyclerView.OnScrollListener;

public class ContentActivity extends AppCompatActivity implements View.OnClickListener, LoadPageData.DataCallback {

    private List<String> urlList = new ArrayList<>();
    private RecycleAdapter adapter;
    RelativeLayout topView;
    LinearLayout bottomView;
    boolean isshowing, showPop;
    MyRecyclerView recyclerView;
    ImageView back;
    TextView hxTextView, ldTextView, mlTextView,yjTextView;
    TextView title;//章节标题
    LinearLayoutManager llManager;
    PopupWindow p;
    FrameLayout frameLayout;
    String titleText, comicName;
    private int screenWidth, screenHeight;
    private int what;
    float density;
    int values;
    LoadPageData loadPageData;
    ArrayList<BookChapter> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        getScreenSize();
        getPageData();
        init();
        setListenerAdapter();
        setOnScrollListener();
    }

    /**
     * 关闭隐藏布局
     */
    public void hideView() {
        topView.setVisibility(View.GONE);
        bottomView.setVisibility(View.GONE);
        Animation topAnim = AnimationUtils.loadAnimation(this, R.anim.top_to_bottom_out);
        topView.setAnimation(topAnim);
        Animation bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_to_top_out);
        bottomView.setAnimation(bottomAnim);
    }

    /**
     * 打开隐藏布局
     */
    public void showView() {
        topView.setVisibility(View.VISIBLE);
        bottomView.setVisibility(View.VISIBLE);
        Animation topAnim = AnimationUtils.loadAnimation(this, R.anim.top_to_bottom_in);
        topView.setAnimation(topAnim);
        title.setText(titleText);
        Animation bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_to_top_in);
        bottomView.setAnimation(bottomAnim);
    }

    /**
     * 控件初始化
     */
    public void init() {
        recyclerView = (MyRecyclerView) findViewById(R.id.recycler);
        topView = (RelativeLayout) findViewById(R.id.top);
        bottomView = (LinearLayout) findViewById(R.id.bottom);
        back = (ImageView) findViewById(R.id.back);
        hxTextView = (TextView) findViewById(R.id.hx);
        ldTextView = (TextView) findViewById(R.id.ld);
        mlTextView = (TextView) findViewById(R.id.ml);
        yjTextView = (TextView)findViewById(R.id.yj);
        hxTextView.setTag("vertical");
        frameLayout = (FrameLayout) findViewById(R.id.frame);
        title = (TextView) findViewById(R.id.chapter_name);
        adapter = new RecycleAdapter(urlList, this);
    }

    /**
     * 获得屏幕参数
     */
    public void getScreenSize() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenHeight = metrics.heightPixels;
        screenWidth = metrics.widthPixels;
        density = metrics.density;
    }

    /**
     * 获取图片地址的集合
     */
    public void getPageData() {
        Intent intent = getIntent();
        what = intent.getIntExtra("what", 0);
        titleText = intent.getStringExtra("chapter_name");
        comicName = intent.getStringExtra("comicName");
        list = intent.getParcelableArrayListExtra("all");
        loadPageData = new LoadPageData(this, HttpURL.COMICS_CHAPTER_CONTENT, this);
        loadPageData.addRequestParams(HttpURL.APP_KEY, comicName, what);
        loadPageData.addTask(what);
        Log.i("TAG", titleText);
      /*  urlList = new ArrayList<>();
        urlList.add("http://imgs.juheapi.com/comic_xin/uufJq9H9vKc=/227893/0-MjI3ODkzMA==.jpg");
        urlList.add("http://imgs.juheapi.com/comic_xin/uufJq9H9vKc=/227893/1-MjI3ODkzMQ==.jpg");
        urlList.add("http://imgs.juheapi.com/comic_xin/uufJq9H9vKc=/227893/2-MjI3ODkzMg==.jpg");*/
    }

    public void setListenerAdapter() {
        llManager = new LinearLayoutManager(this);
        llManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llManager);
        recyclerView.setAdapter(adapter);
        back.setOnClickListener(this);
        hxTextView.setOnClickListener(this);
        ldTextView.setOnClickListener(this);
        mlTextView.setOnClickListener(this);
        yjTextView.setOnClickListener(this);
    }

    /**
     * 为RecycleView添加滑动监听事件
     */
    public void setOnScrollListener() {
        recyclerView.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                Log.i("TAG", "newState:" + newState);
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.hx:
                if (Constant.HORIZONTAL.equals(hxTextView.getText().toString())) {
                    llManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    recyclerView.setLayoutManager(llManager);
                    hxTextView.setText(Constant.VERTICAL);
                    hxTextView.setTag("horizontal");
                } else if (Constant.VERTICAL.equals(hxTextView.getText().toString())) {
                    llManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(llManager);
                    hxTextView.setText(Constant.HORIZONTAL);
                    hxTextView.setTag("vertical");
                }
                break;
            case R.id.ld:
                showPopupWindow(v);
                break;
            case R.id.ml:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                View view = getLayoutInflater().inflate(R.layout.content_dialog,null);
                ListView listView = (ListView)view.findViewById(R.id.content_dialog_list);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.content_dialog_list_item,R.id.content_dialog_list_item_text,getContent());
                listView.setAdapter(adapter);
                builder.setView(view);
                final AlertDialog dialog = builder.create();
                dialog.show();
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        int chapterID = list.get(position).getId();
                        loadPageData.addRequestParams(HttpURL.APP_KEY, comicName, chapterID);
                        loadPageData.addTask(chapterID);
                        title.setText(list.get(position).getName());
                        dialog.dismiss();
                    }
                });
                WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
                params.height = screenHeight/2;
                params.width = (int) (screenWidth-100*density);
                dialog.getWindow().setAttributes(params);
                break;
            case R.id.yj:
                WindowManager manager = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
                WindowManager.LayoutParams param = new WindowManager.LayoutParams(
                        WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.TYPE_APPLICATION,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE|WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                        PixelFormat.TRANSLUCENT);

                param.gravity = Gravity.TOP;
                param.y = 10;// 距离底部的距离是10像素 如果是 top 就是距离top是10像素

                TextView tv = new TextView(this);
                tv.setBackgroundColor(0x55000000);
                manager.addView(tv,param);
                break;
        }
    }

    /**
     * 获取章节目录
     *
     * @return
     */
    public String[] getContent() {
        System.out.println(list.size() + "");
        String[] cs = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            cs[i] = list.get(i).getName();
        }
        return cs;
    }

    /**
     * 调节屏幕亮度的弹窗
     */
    public void showPopupWindow(View v) {
        if (p == null) {
            p = new PopupWindow(this);
            View view = getLayoutInflater().inflate(R.layout.pop_bright, null);
            SeekBar seekBar = (SeekBar) view.findViewById(R.id.seek);
            ContentResolver cr = this.getContentResolver();
            try {
                values = Settings.System.getInt(cr, Settings.System.SCREEN_BRIGHTNESS);
                seekBar.setProgress(values);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
            addScreenBrightListener(seekBar);
            p.setWidth((int) (screenWidth - 50 * density));
            p.setHeight((int) (50 * density));
            p.setContentView(view);
            p.setOutsideTouchable(false);
            p.setBackgroundDrawable(new BitmapDrawable());
            if (!showPop) {
                showPop = true;
                p.showAtLocation(frameLayout, Gravity.CENTER, 0, 0);
            }
        } else {
            if (p.isShowing()) {
                showPop = false;
                p.dismiss();
                p = null;
            }
        }

    }

    /**
     * 屏幕亮度调节的监听
     *
     * @param seekBar
     */
    public void addScreenBrightListener(SeekBar seekBar) {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Window window = ContentActivity.this.getWindow();
                WindowManager.LayoutParams lp = window.getAttributes();
                lp.screenBrightness = progress / 255f;
                window.setAttributes(lp);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.i("SEEKBAR", "onStartTrackingTouch");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.i("SEEKBAR", "onStopTrackingTouch");
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                Log.i("TAG", "ACTION_UP-ACTIVITY");
                if (event.getX() > 100 * density && event.getX() < screenWidth - 100 * density && event.getY() > 100 * density && event.getY() < screenHeight - 100 * density) {
                    if (!isshowing) {
                        isshowing = true;
                        showView();
                    } else {
                        isshowing = false;
                        hideView();
                    }
                }
                if ("horizontal".equals(hxTextView.getTag())) {
                    if (event.getX() < 100 * density) {
                        recyclerView.scrollBy(-screenWidth, 0);
                    } else if (event.getX() > screenWidth - 100 * density) {
                        recyclerView.scrollBy(screenWidth, 0);
                    }
                } else if ("vertical".equals(hxTextView.getTag())) {
                    if (event.getY() < 100 * density) {
                        recyclerView.scrollBy(0, -screenHeight);
                    } else if (event.getY() > screenHeight - 100 * density) {
                        recyclerView.scrollBy(0, screenHeight);
                    }
                }
                break;
        }
        return false;
    }

    @Override
    public void getData(ArrayList<String> imageList,String name) {
        Log.i("NEWDATA", "kkjii");
        if (imageList != null) {
            if (!urlList.isEmpty()) {
                urlList.clear();
            }
            urlList.addAll(imageList);
            adapter.notifyDataSetChanged();
        }
    }
}
