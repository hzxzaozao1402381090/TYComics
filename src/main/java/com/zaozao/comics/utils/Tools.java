package com.zaozao.comics.utils;

import android.content.Context;
import android.content.res.Resources;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by 胡章孝 on 2016/6/30.
 * 用于测试保存在本地的Json数据
 * 为了减少多次使用http请求而浪费聚合数据上的数据的使用次数，我们将首次请求得到的json字符串保存在文本文件中
 * 之后通过读取文本文件的内容获得json字符串
 */
public class Tools {

    /**
     * 获取assets内文件的内容
     * @param fileName 文件名称
     * @return 返回文件内保存的字符串
     * @throws IOException
     */
    public static String getReadText(String fileName, Context context) throws IOException {
        StringBuffer result = new StringBuffer();

        Resources resources = context.getResources();
        InputStream inputStream = resources.getAssets().open(fileName);
        byte[] buffer = new byte[1024];
        int temp = 0;
        if (inputStream != null) {
            while ((temp = inputStream.read(buffer)) != -1) {
                String str = new String(buffer, 0, temp);
                result.append(str);
            }
            inputStream.close();
        }
        return result.toString();
    }

    /**
     * 格式化日期
     * @param date
     * @return
     */
    public static String formatDate(long date){
        String dateStr = String.valueOf(date);
        String year = dateStr.substring(0,4);
        String month = dateStr.substring(4,6);
        String day = dateStr.substring(6,8);
        return year+"/"+month+"/"+day;
    }
}
