package com.zaozao.comics.http;

/**
 * Created by 胡章孝 on 2016/6/28.
 * 此类用于产生向服务器发送请求的各种URL
 */
public class HttpURL {

    //聚合数据APP_KEY
    public static final String APP_KEY = "2abb4911c0a9d9319a2e84205ba86bdd";

    //请求URL的公共部分
    public static final String ROOT = "http://japi.juhe.cn/comic/";

    //漫画书分类查询
    public static final String COMICS_SORT = ROOT + "category";

    //漫画书列表
    public static final String COMICS_LIST = ROOT + "book";

    //漫画书章节列表
    public static final String COMICS_CHAPTER = ROOT + "chapter";

    //漫画书章节详细内容
    public static final String COMICS_CHAPTER_CONTENT = ROOT + "chapterContent";

}
