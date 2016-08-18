package com.zaozao.comics.shujia;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zaozao.comics.APP;
import com.zaozao.comics.R;
import com.zaozao.comics.sqlite.SqliteDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 胡章孝 on 2016/7/22.
 */
class ListAdapter extends BaseAdapter {
    MyHolder holder = null;
    ArrayList<Map<String, Object>> data;
    ShuJiaFragment context;
    SqliteDao sqliteDao;
    boolean hasChoosed;
    public List<CheckBox> checkBoxes;
    public ArrayList<Map<String, Object>> delete_list;

    public ListAdapter(ArrayList<Map<String, Object>> data, ShuJiaFragment context) {
        this.data = data;
        this.context = context;
        checkBoxes = new ArrayList<>();
        delete_list = new ArrayList<>();
        sqliteDao = new SqliteDao(context.getContext());
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
            holder = new MyHolder();
            convertView = LayoutInflater.from(context.getContext()).inflate(R.layout.book_list_item2, parent, false);
            holder.nameTextView = (TextView) convertView.findViewById(R.id.name_text);
            holder.readTextView = (TextView) convertView.findViewById(R.id.progress_text);
            holder.updateTextView = (TextView) convertView.findViewById(R.id.update_text);
            holder.lastUpdateTextView = (TextView) convertView.findViewById(R.id.lastupdate_text);
            holder.coverImage = (ImageView) convertView.findViewById(R.id.cover_imageId);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.chose);
            checkBoxes.add(holder.checkBox);
            convertView.setTag(holder);
        }
        holder = (MyHolder) convertView.getTag();
        holder.nameTextView.setText(data.get(position).get("name").toString());
        holder.readTextView.setText("阅读至第"+data.get(position).get("readto").toString()+"话");
        holder.updateTextView.setText("更新到："+data.get(position).get("updateto").toString()+"话");
        holder.lastUpdateTextView.setText("上次更新："+data.get(position).get("lastupdate").toString());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    delete_list.add(data.get(position));
                }
            }
        });
        APP.imageLoader.displayImage(data.get(position).get("picture").toString(), holder.coverImage);
        return convertView;
    }

    class MyHolder {
        ImageView coverImage;
        TextView nameTextView;
        TextView readTextView;
        TextView updateTextView;
        TextView lastUpdateTextView;
        CheckBox checkBox;
    }

    public void onDelete() {
        for (CheckBox checkBox : checkBoxes) {
            checkBox.setVisibility(View.VISIBLE);
        }
    }

    public void onDeleteFile() {
        for (CheckBox checkBox : checkBoxes) {
            if (checkBox.isChecked())
                hasChoosed = true;
        }
        if (hasChoosed) {
            hasChoosed = false;
            showDialog();
        } else {
            Toast.makeText(context.getContext(), "没有选中任何漫画", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 确认删除的对话框
     */
    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context.getContext());
        builder.setTitle("删除");
        builder.setIcon(R.drawable.delete);
        builder.setCancelable(false);
        builder.setMessage("删除后不可恢复，确定删除？");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (context instanceof ShouCangFragment) {
                    sqliteDao.remove(delete_list);
                    ((ShouCangFragment) context).refresh(sqliteDao);
                } else if (context instanceof HistroyFragment) {
                    ((HistroyFragment) context).refresh(delete_list);
                }
            }
        });
        builder.create();
        builder.show();
    }

}
