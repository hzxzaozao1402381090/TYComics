package com.zaozao.comics.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.zaozao.comics.Constant;
import com.zaozao.comics.LoginActivity;
import com.zaozao.comics.R;

public class MineFragment extends Fragment {

	ListView listView;
	View view;
	ImageView setImageView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.activity_mine_fragment, container, false);
		listView = (ListView)view.findViewById(R.id.mine_list);
		setImageView = (ImageView)view.findViewById(R.id.set);
		setListView();
		return view;
	}
	public void setListView(){
		listView.setAdapter(new MineListAdapter(getActivity()));
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(MineFragment.this.getContext(), LoginActivity.class);
				startActivity(intent);
			}
		});
		setImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),SettingActivity.class);
				startActivity(intent);
			}
		});
	}
}
