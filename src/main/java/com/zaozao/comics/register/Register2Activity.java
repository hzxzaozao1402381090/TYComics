package com.zaozao.comics.register;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.zaozao.comics.Constant;
import com.zaozao.comics.LoginActivity;
import com.zaozao.comics.R;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class Register2Activity extends Activity {


    EditText accountText;
    EditText pwdText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
        accountText = (EditText) findViewById(R.id.edt_reg_name);
        pwdText = (EditText) findViewById(R.id.edt_reg_pwd);
        init();
    }

    public void init() {
        initSMSSDK();
    }

    /**
     * 初始化短信验证码SDK
     */
    public void initSMSSDK() {
        SMSSDK.initSDK(this, Constant.APP_KEY, Constant.APP_SECRET);
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
        SMSSDK.registerEventHandler(eh);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int event = msg.arg1;
            int result = msg.arg2;
            if (event == SMSSDK.EVENT_SUBMIT_USER_INFO) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    Toast.makeText(Register2Activity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Register2Activity.this, LoginActivity.class);
                    startActivity(intent);
                    Register2Activity.this.finish();
                }
            }
        }
    };

    public void register(View view) {
        try {
            EMClient.getInstance().createAccount(accountText.getText().toString(),pwdText.getText().toString());
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
        } catch (HyphenateException e) {
            e.printStackTrace();
            System.out.println("注册失败");
        }
    }
}
