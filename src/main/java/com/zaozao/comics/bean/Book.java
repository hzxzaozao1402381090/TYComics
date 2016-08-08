package com.zaozao.comics.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Config;

/**
 * Created by 胡章孝 on 2016/6/30.
 */
public class Book implements Parcelable {

    /**
     * name : 虹色妖姬
     * type : 少年漫画
     * area : 国漫
     * des :
     * chapterID 章节ID
     * total:更新至
     * finish : false
     * lastUpdate : 20160602
     * coverImg : http://imgs.juheapi.com/comic_xin/5559b86938f275fd560ad61f.jpg
     */

    private String name;
    private String type;
    private String area;
    private String des;
    private int chapterId;
    private boolean finish;
    private int lastUpdate;
    private String coverImg;
    private int total;


    public void setType(String type) {
        this.type = type;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public void setLastUpdate(int lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }

    public int getChapterId() {
        return chapterId;
    }

    public void setChapterId(int chapterId) {
        this.chapterId = chapterId;
    }

    public Book() {

    }

    protected Book(Parcel in) {
        name = in.readString();
        type = in.readString();
        area = in.readString();
        des = in.readString();
        finish = in.readByte() != 0;
        lastUpdate = in.readInt();
        coverImg = in.readString();
        total = in.readInt();

    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
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

    public String getType() {
        return type;
    }


    public String getArea() {
        return area;
    }


    public String getDes() {
        return des;
    }


    public boolean isFinish() {
        return finish;
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
    }

    public int getLastUpdate() {
        return lastUpdate;
    }


    public String getCoverImg() {
        return coverImg;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(type);
        dest.writeString(area);
        dest.writeString(des);
        dest.writeByte((byte) (finish ? 1 : 0));
        dest.writeInt(lastUpdate);
        dest.writeString(coverImg);
        dest.writeInt(total);
    }

    @Override
    public String toString() {
        return name + "," + chapterId + "," + total + "," + lastUpdate + "," + coverImg;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Book) {
            Book book = (Book) o;
            return name == book.name && chapterId == book.chapterId && total == book.total && lastUpdate == book.lastUpdate && coverImg == book.coverImg;
        } else {
            return false;
        }
    }
}
