package com.zaozao.comics.shujia;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.astuetz.PagerSlidingTabStrip;
import com.zaozao.comics.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShuJiaFragment extends Fragment {

    private ViewPager viewPager;
    private List<Fragment> list;
    private PagerSlidingTabStrip tabStrip;
    public ImageView editImageView;
    private String[] titles = {"收藏", "历史", "下载"};
    private ShouCangFragment shouCangFragment;
    private HistroyFragment histroyFragment;
    private DownLoadFragment downLoadFragment;
    private boolean changed;
    private int index;
    static FragmentAdapter adapter;
    public ArrayList<Map<String, Object>> data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_shujia_fragment,
                container, false);
        init(view);
        setData();
        return view;
    }

    /**
     * 初始化界面控件
     *
     * @param view
     */
    public void init(View view) {
        editImageView = (ImageView) view.findViewById(R.id.edit);
        viewPager = (ViewPager) view.findViewById(R.id.shujia_pager);
        tabStrip = (PagerSlidingTabStrip) view.findViewById(R.id.tabstrip);
        tabStrip.setIndicatorHeight(5);
        tabStrip.setIndicatorColorResource(R.color.theme);
        tabStrip.setUnderlineHeight(1);
    }

    /**
     * 为viewpager设置显示数据
     * 为编辑按钮设置逻辑
     */
    public void setData() {
        list = new ArrayList<>();
        shouCangFragment = new ShouCangFragment();
        histroyFragment = new HistroyFragment();
        downLoadFragment = new DownLoadFragment();
        list.add(shouCangFragment);
        list.add(histroyFragment);
        list.add(downLoadFragment);
        editImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!changed) {
                    changed = true;
                    editImageView.setImageResource(R.drawable.delete);
                    if (index == 0) {
                        shouCangFragment.adapter.onDelete();
                    } else if (index == 1) {
                        histroyFragment.adapter.onDelete();
                    }
                } else {
                    changed = false;
                    if (index == 0) {
                        shouCangFragment.adapter.onDeleteFile();
                    } else if (index == 1) {
                        histroyFragment.adapter.onDeleteFile();
                    }
                    editImageView.setImageResource(R.drawable.edit);
                }
            }
        });
        adapter = new FragmentAdapter(getActivity().getSupportFragmentManager(), list, titles);
        viewPager.setAdapter(adapter);
        tabStrip.setViewPager(viewPager);
        tabStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                index = position;
                if(index == 2){
                    editImageView.setVisibility(View.GONE);
                }else{
                    editImageView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    interface OnDeleteClickListener {
        void onDelete();

        void onDeleteFile();
    }
}

class FragmentAdapter extends FragmentStatePagerAdapter {

    List<Fragment> list;
    String[] titles;

    public FragmentAdapter(FragmentManager fm, List<Fragment> list, String[] titles) {
        super(fm);
        this.list = list;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
