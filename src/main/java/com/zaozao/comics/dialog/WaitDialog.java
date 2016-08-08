package com.zaozao.comics.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.Window;

import com.zaozao.comics.R;

/**
 * Created by 胡章孝 on 2016/6/28.
 */
public class WaitDialog extends ProgressDialog {

    public WaitDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置不可通过点击外部区域使对话框消失
        setCanceledOnTouchOutside(false);
        //设置进度对话框的样式
        setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }
}
