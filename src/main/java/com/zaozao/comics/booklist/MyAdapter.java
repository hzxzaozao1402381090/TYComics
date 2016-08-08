package com.zaozao.comics.booklist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zaozao.comics.APP;
import com.zaozao.comics.R;
import com.zaozao.comics.bean.Book;

import java.util.List;

/**
 * Created by 胡章孝 on 2016/8/4.
 */

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    MyHolder holder;
    Context context;
    LayoutInflater inflate;
    List<Book> books;
    String type;
    OnRecycleViewItemClickListener listener;

    public MyAdapter(Context context, List<Book> books, String type) {
        this.context = context;
        this.books = books;
        this.type = type;
        inflate = LayoutInflater.from(context);
    }

    /**
     * 设置监听器
     *
     * @param listener
     */
    public void setOnRecycleItemClickListener(OnRecycleViewItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = inflate.inflate(R.layout.book_list_item, parent, false);
        convertView.setOnClickListener(this);
        holder = new MyHolder(convertView);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyHolder) {
            ((MyHolder) holder).nameTextView.setText(books.get(position).getName());
            ((MyHolder) holder).areaTextView.setText(books.get(position).getArea());
            String progress = books.get(position).isFinish() ? "已完结" : "未完结";
            ((MyHolder) holder).progressTextView.setText(progress);
            ((MyHolder) holder).updateTextView.setText("最近更新：" + books.get(position).getLastUpdate());
            ((MyHolder) holder).itemView.setTag(books.get(position));
            APP.imageLoader.displayImage(books.get(position).getCoverImg(), ((MyHolder) holder).imageView);
        }
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    /**
     * 更新界面数据
     *
     * @param newList
     */
    public void addData(List<Book> newList) {
        books.addAll(books.size() - 1, newList);
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        listener.onItemClick((Book) v.getTag());
    }

    class MyHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView nameTextView;
        TextView progressTextView;
        TextView areaTextView;
        TextView updateTextView;
        Book position;

        public MyHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageId);
            nameTextView = (TextView) itemView.findViewById(R.id.read);
            areaTextView = (TextView) itemView.findViewById(R.id.nation);
            progressTextView = (TextView) itemView.findViewById(R.id.progress);
            updateTextView = (TextView) itemView.findViewById(R.id.updateprogress);
        }

        public void setTag(Book position) {
            this.position = position;
        }

        public Book getTag() {
            return position;
        }
    }

    public interface OnRecycleViewItemClickListener {
        void onItemClick(Book book);
    }
}
