package com.zaozao.comics.detail;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zaozao.comics.APP;
import com.zaozao.comics.R;
import com.zaozao.comics.utils.LoadImage;

import java.util.List;

/**
 * Created by Administrator on 2016/8/2.
 */
class RecycleAdapter extends RecyclerView.Adapter {

    MyHolder myHolder;
    List<String> urlList;
    Context context;

    public RecycleAdapter(List<String> urlList,Context context) {
        this.urlList = urlList;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false);
        myHolder = new MyHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyHolder) {
           // APP.imageLoader.displayImage(urlList.get(position), ((MyHolder) holder).imageView);
            LoadImage loadImage = new LoadImage(null,context);
            loadImage.loadNetImage(urlList.get(position),((MyHolder) holder).imageView,"comics",true);
        }
    }

    @Override
    public int getItemCount() {
        return urlList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public MyHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.recycler_item_image);
        }
    }
}
