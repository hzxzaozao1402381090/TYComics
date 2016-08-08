package com.zaozao.comics.bean;

import java.io.Serializable;

/**
 * Created by 胡章孝 on 2016/8/6.
 */
public class LoadFile implements Serializable{

    /**
     * 漫画名称
     */
    private String comicName;
    /**
     * 章节名称
     */
    private String name;
    /**
     * 文件下载进度
     */
    private int progress;
    /**
     * 文件总大小
     */
    private int max;
    /**
     * 漫画封面
     */
    private String coverImaage;

    public String getCoverImaage() {
        return coverImaage;
    }

    public void setCoverImaage(String coverImaage) {
        this.coverImaage = coverImaage;
    }

    public String getComicName() {
        return comicName;
    }

    public void setComicName(String comicName) {
        this.comicName = comicName;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    @Override
    public String toString() {
        return name+","+progress+","+max+coverImaage;
    }
}
