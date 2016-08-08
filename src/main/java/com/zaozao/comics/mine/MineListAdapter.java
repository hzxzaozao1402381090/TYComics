package com.zaozao.comics.mine;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zaozao.comics.R;

/**
 * Created by Administrator on 2016/6/23.
 */
public class MineListAdapter extends BaseAdapter {

    Context context;
    int[] imgID ;
    String[] names ;
    public MineListAdapter(Context context){
        this.context = context;
        imgID = new int[]{R.drawable.message,R.drawable.shoucang,R.drawable.quanzi,R.drawable.about};
        names = context.getResources().getStringArray(R.array.mine_list_name);
    }
    @Override
    public int getCount() {
        return names.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.mine_list_item,parent,false);
            holder.imageView = (ImageView)convertView.findViewById(R.id.mine_list_item_arrow);
            holder.textView = (TextView)convertView.findViewById(R.id.mine_list_item_name);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        holder.textView.setText(names[position]);
        Drawable drawable = context.getResources().getDrawable(imgID[position]);
        drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
        holder.textView.setCompoundDrawables(drawable,null,null,null);
        holder.textView.setCompoundDrawablePadding(20);
        holder.imageView.setImageResource(R.drawable.arrow);
        return convertView;
    }
    class ViewHolder{
        TextView textView;
        ImageView imageView;
    }
}
