package com.zaozao.comics.http;

import com.google.gson.Gson;
import com.zaozao.comics.bean.Book;
import com.zaozao.comics.bean.BookChapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 解析Json数据
 */
public class JsonParser {

    /**
     * 解析Json字符串，获得漫画书的对象集合
     *
     * @param jsonStr Json字符串
     * @return 返回漫画书列表
     * @throws JSONException
     */
    public static List<Book> getBooks(String jsonStr) throws JSONException {
        List<Book> books = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(jsonStr);
        JSONObject jsonObject2 = jsonObject.getJSONObject("result");
        JSONArray jsonArray = jsonObject2.getJSONArray("bookList");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = (JSONObject) jsonArray.get(i);
            String json = object.toString();
            Gson gson = new Gson();
            Book book = gson.fromJson(json, Book.class);
            books.add(book);
        }
        return books;
    }

    /**
     * 解析Json字符串，获得漫画书的章节列表
     *
     * @param jsonStr Json字符串
     * @return
     */
    public static List<BookChapter> getBookChapter(String jsonStr) throws JSONException {

        List<BookChapter> chapters = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(jsonStr);
        JSONObject jsonObject2 = jsonObject.getJSONObject("result");
        JSONArray jsonArray = jsonObject2.getJSONArray("chapterList");
        String total = jsonObject2.getString("total");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = (JSONObject) jsonArray.get(i);
            String json = object.toString();
            Gson gson = new Gson();
            BookChapter chapter = gson.fromJson(json, BookChapter.class);
            chapter.setTotal(Integer.valueOf(total));
            chapters.add(chapter);
        }
        return chapters;
    }

    public static ArrayList<String> getContentImage(String jsonStr) throws JSONException {
        ArrayList<String> list = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(jsonStr);
        JSONObject jsonObject2 = jsonObject.getJSONObject("result");
        JSONArray jsonArray = jsonObject2.getJSONArray("imageList");
        for (int i = 0; i < jsonArray.length(); i++) {
            String url = ((JSONObject) jsonArray.get(i)).getString("imageUrl");
            list.add(url);
        }
        return list;
    }

}
