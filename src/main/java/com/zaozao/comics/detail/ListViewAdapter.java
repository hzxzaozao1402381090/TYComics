package com.zaozao.comics.detail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.zaozao.comics.R;
import com.zaozao.comics.bean.LoadFile;


import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by 胡章孝 on 2016/8/14.
 */
public class ListViewAdapter extends BaseAdapter {

    List<LoadFile> fileList;
    Context context;

    public ListViewAdapter(Context context, List<LoadFile> fileList) {
        this.fileList = fileList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return fileList.size();
    }

    @Override
    public Object getItem(int i) {
        return fileList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder;
        if(view==null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        holder = (ViewHolder) view.getTag();
        holder.title.setText(fileList.get(i).getName());
        holder.progressbar.setProgress(fileList.get(i).getProgress());
        holder.progressbar.setMax(fileList.get(i).getMax());
        holder.state.setText(fileList.get(i).getState());
        return view;
    }

    static class ViewHolder {
        @InjectView(R.id.title)
        TextView title;
        @InjectView(R.id.state)
        TextView state;
        @InjectView(R.id.progressbar)
        ProgressBar progressbar;
        @InjectView(R.id.linear)
        LinearLayout linear;
        @InjectView(R.id.options)
        ImageView options;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
