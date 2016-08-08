package com.zaozao.comics.search;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.telecom.Call;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;
import com.zaozao.comics.Constant;
import com.zaozao.comics.R;
import com.zaozao.comics.detail.BookDetail;
import com.zaozao.comics.http.CallServer;
import com.zaozao.comics.http.HttpListener;
import com.zaozao.comics.http.HttpURL;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements View.OnClickListener,HttpListener<String>{

	private GridView gridView;
	private ArrayAdapter<String> adapter ;
	private List<String> list;
	private Button searchButton;
	private EditText keywordText;
	private String result;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_search_fragment, container, false);
		init(view);
		list = new ArrayList<>();
		list.add("逆转仙师");
		list.add("永恒传说");
		list.add("星空六班");
		list.add("逆转仙师");
		list.add("永恒传说");
		list.add("星空六班");
		setView();
		return view;
	}
	public void init(View view){
		gridView = (GridView)view.findViewById(R.id.grid_view);
		searchButton = (Button)view.findViewById(R.id.search);
		keywordText = (EditText)view.findViewById(R.id.input_code);
	}
	public void setView(){
		searchButton.setOnClickListener(this);
		adapter = new ArrayAdapter<>(this.getActivity(),R.layout.grid_item,R.id.grid_text,list);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(SearchFragment.this.getActivity(), BookDetail.class);
				String name = (String) parent.getItemAtPosition(position);
				intent.putExtra(Constant.COMICS_NAME,name);
				startActivity(intent);
			}
		});
	}
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.search){
			String keyword = keywordText.getText().toString();
			Intent intent = new Intent(getActivity(),BookDetail.class);
			intent.putExtra(Constant.COMICS_NAME,keyword);
			startActivity(intent);
		}
	}

	@Override
	public void onSuccessed(int what, Response<String> response) {
		result = response.get();
		if(!TextUtils.isEmpty(result)){
			System.out.println(result);
		}
	}

	@Override
	public void onFailed(int what, String url, Object tag, Exception e, int responseCode, long networkMillis) {

	}
}
