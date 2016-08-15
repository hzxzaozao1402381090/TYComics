package com.zaozao.comics.utils;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by 胡章孝 on 2016/8/13.
 * SD卡的工具类，用于在读写SD卡上的文件
 */
public class SDCard {

    private static SDCard sdCard;
    private SDCard(){}
    public static SDCard getInstance(){
        if (sdCard==null)
            sdCard = new SDCard();
        return  sdCard;
    }

    /**
     *
     * @param bm
     * @param folderName 文件夹名
     * @param fileName 文件名
     */
    public void saveBitmap(Bitmap bm,String folderName,String fileName) throws IOException {
        if(sdCardAvailable())
        if(bm!=null){
            File root = Environment.getExternalStorageDirectory();
            File folder = new File(root,folderName);
            if(!folder.exists()){
                folder.mkdirs();
            }
            File file = new File(folder,fileName);
            FileOutputStream fos = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG,100,fos);
            fos.flush();
            fos.close();
        }
    }

    /**
     * 判断SD卡是否可用
     * @return
     */
    public boolean sdCardAvailable(){
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }
}
