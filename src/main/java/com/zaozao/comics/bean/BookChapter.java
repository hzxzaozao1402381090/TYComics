package com.zaozao.comics.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 胡章孝 on 2016/7/11.
 */
public class BookChapter implements Parcelable{


    /**
     * name : 第1话 狼少女的选择
     * id : 227893
     */

    private String name;
    private int id;
    private int total;
    protected BookChapter(Parcel in) {
        name = in.readString();
        id = in.readInt();
        total = in.readInt();
    }

    public static final Creator<BookChapter> CREATOR = new Creator<BookChapter>() {
        @Override
        public BookChapter createFromParcel(Parcel in) {
            return new BookChapter(in);
        }

        @Override
        public BookChapter[] newArray(int size) {
            return new BookChapter[size];
        }
    };

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(id);
        dest.writeInt(total);
    }
}
