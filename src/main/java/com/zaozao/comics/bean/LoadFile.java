package com.zaozao.comics.bean;

/**
 * Created by 胡章孝 on 2016/8/14.
 */
public class LoadFile {
    /**
     * 下载文件名
     */
    private String name;
    /**
     * 下载进度
     */
    private int progress;
    /**
     * 文件的长度
     */
    private int max;
    /**
     * 文件状态
     */
    private String state;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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
}
