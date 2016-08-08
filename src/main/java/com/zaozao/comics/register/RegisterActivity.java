package com.zaozao.comics.register;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zaozao.comics.Constant;
import com.zaozao.comics.R;

import cn.smssdk.DefaultOnSendMessageHandler;
import cn.smssdk.EventHandler;
import cn.smssdk.OnSendMessageHandler;
import cn.smssdk.SMSSDK;

public class RegisterActivity extends Activity implements View.OnClickListener, Handler.Callback {

    EditText edit_Phone, edit_Code;
    String phoneStr;
    String codeStr;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        initSMSSDK();
    }

    /**
     * 初始化界面布局，并设置监听
     */
    public void initView() {
        edit_Phone = (EditText) findViewById(R.id.edit_phone);
        edit_Code = (EditText) findViewById(R.id.input_code);
        Button btn_Click = (Button) findViewById(R.id.click);
        Button yz_Btn = (Button) findViewById(R.id.yz_Btn);
        btn_Click.setOnClickListener(this);
        yz_Btn.setOnClickListener(this);
    }

    /**
     * 初始化短信验证码SDK
     */
    public void initSMSSDK() {
        SMSSDK.initSDK(this, Constant.APP_KEY, Constant.APP_SECRET);
        handler = new Handler(this);
        EventHandler eh = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {

                Message message = new Message();
                message.arg1 = event;
                message.arg2 = result;
                message.obj = data;
                handler.sendMessage(message);
            }
        };
        //注册回调接口
        SMSSDK.registerEventHandler(eh);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.click:
                //获取验证码
                Toast.makeText(RegisterActivity.this, "获取验证码中", Toast.LENGTH_SHORT).show();
                getCode();
                break;
            case R.id.yz_Btn:
                //提交验证码
                Toast.makeText(RegisterActivity.this, "提交验证码中", Toast.LENGTH_SHORT).show();
                submitCode();
                break;
        }
    }


    /**
     * 点击获取验证码
     */
    public void getCode() {
        phoneStr = edit_Phone.getText().toString();
        if (!TextUtils.isEmpty(phoneStr)) {
            SMSSDK.getVerificationCode(Constant.COUNTRY_CODE, phoneStr);
        }
    }

    /**
     * 向服务器提交验证码
     */
    public void submitCode() {
        codeStr = edit_Code.getText().toString();
        if (!TextUtils.isEmpty(codeStr)) {
            System.out.println("提交验证码");
            SMSSDK.submitVerificationCode(Constant.COUNTRY_CODE, phoneStr, codeStr);
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        int event = msg.arg1;
        int result = msg.arg2;
        Object data = msg.obj;
        if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
            if (result == SMSSDK.RESULT_COMPLETE) {
                Toast.makeText(this, "获取验证码成功", Toast.LENGTH_LONG).show();
            } else {
                ((Throwable) data).printStackTrace();
            }
        } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
            if (result == SMSSDK.RESULT_COMPLETE) {
                Toast.makeText(this, "验证码验证成功！", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this,Register2Activity.class);
                startActivity(intent);
            } else {
                ((Throwable) data).printStackTrace();
            }

        }
        return false;
    }

    @Override
    protected void onDestroy() {
        //注销掉所有的回掉接口
        SMSSDK.unregisterAllEventHandler();
        super.onDestroy();
    }

}
