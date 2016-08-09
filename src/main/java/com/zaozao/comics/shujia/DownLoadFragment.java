package com.zaozao.comics.shujia;


import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zaozao.comics.APP;
import com.zaozao.comics.R;
import com.zaozao.comics.bean.LoadFile;
import com.zaozao.comics.provider.Observer;
import com.zaozao.comics.utils.AppConfig;

import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class DownLoadFragment extends Fragment {


    @InjectView(R.id.download_list)
    ListView downloadList;
    SharedPreferences preferences;
    List<LoadFile> fileList = null;
    List<String> keys = new ArrayList<>();
    DownLoadAdapter adapter;
    View view;
    int screenWidth;
    FrameLayout parentFrame;
    ContentResolver cr;
    Uri uri;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_down_load, container, false);
        parentFrame = (FrameLayout) view.findViewById(R.id.histroy_delete);
        ButterKnife.inject(this, view);
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        return view;
    }

    public void init() {
        uri = Uri.parse("content://zaozao.hu");
        cr = getActivity().getContentResolver();
        preferences = getActivity().getSharedPreferences("tycomics", Context.MODE_PRIVATE);
        Map<String, ?> all = preferences.getAll();
        if (all != null) {
            Set<? extends Map.Entry<String, ?>> entry = all.entrySet();
            Iterator<? extends Map.Entry<String, ?>> it = entry.iterator();
            while (it.hasNext()) {
                Map.Entry<String, ?> next = it.next();
                String key = next.getKey();
                keys.add(key);
                fileList.add(AppConfig.getInstance().readLoadFile(key));
            }
            adapter = new DownLoadAdapter(getContext(), fileList);
        }
        initObserver();
    }
    public void initObserver(){
        Uri uri = Uri.parse("content://zaozao.hu");
        Observer observer = new Observer(new Handler(),adapter, fileList);
        cr.registerContentObserver(uri,true,observer);
    }
    @Override
    public void onResume() {
        super.onResume();
        fileList = new ArrayList<>();
        init();
        downloadList.setEmptyView(view.findViewById(R.id.download_empty));
        downloadList.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);

    }

    public class DownLoadAdapter extends BaseAdapter {

        LayoutInflater inflater;
        List<LoadFile> fileList;

        public DownLoadAdapter(Context context, List<LoadFile> fileList) {
            inflater = LayoutInflater.from(context);
            this.fileList = fileList;
        }

        @Override
        public int getCount() {
            return fileList.size();
        }

        @Override
        public Object getItem(int position) {
            return fileList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = inflater.inflate(R.layout.download_listview_item, parent, false);
                viewHolder.coverImage = (ImageView) convertView.findViewById(R.id.download_comic_cover);
                viewHolder.comicName = (TextView) convertView.findViewById(R.id.download_comic_name);
                viewHolder.delete = (ImageView) convertView.findViewById(R.id.image_delete);
                convertView.setTag(viewHolder);
            }
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.comicName.setText(fileList.get(position).getComicName());
            viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                    dialog.setTitle("确定删除" + "《" + fileList.get(position).getComicName() + "》吗？");
                    dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String key = keys.get(position);
                            fileList.remove(position);
                            cr.delete(uri,key,null);
                        }
                    });
                    dialog.create().show();

                }
            });
            APP.imageLoader.displayImage(fileList.get(position).getCoverImaage(), viewHolder.coverImage);
            return convertView;
        }

        class ViewHolder {
            ImageView coverImage;
            TextView comicName;
            ImageView delete;
        }
    }
}
