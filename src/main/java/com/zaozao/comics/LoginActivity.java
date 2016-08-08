package com.zaozao.comics;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.zaozao.comics.register.Register2Activity;
import com.zaozao.comics.register.RegisterActivity;

public class LoginActivity extends Activity implements View.OnClickListener{
	
	EditText userName,userPwd;
	TextView textView;
	ImageView back;
	Button loginButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		back = (ImageView)findViewById(R.id.login_back);
		userName = (EditText)findViewById(R.id.edt_login_name);
		userPwd = (EditText)findViewById(R.id.edt_pwd);
		textView = (TextView)findViewById(R.id.register);
		loginButton = (Button)findViewById(R.id.login);
		loginButton.setOnClickListener(this);
		textView.setOnClickListener(this);
		back.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.register:
				Intent intent = new Intent(LoginActivity.this, Register2Activity.class);
				startActivity(intent);
				break;
			case R.id.login_back:
				finish();
				break;
			case R.id.login:
				String name = userName.getText().toString();
				String pwd = userPwd.getText().toString();
				if("asd".equals(name)&&"123".equals(pwd)){
					APP.isLogined = true;
					this.finish();
				}
				break;
		}
	}
}
