package com.example.atry.zhbj.imple.menu;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.atry.zhbj.R;
import com.example.atry.zhbj.base.BaseMenuDetailPager;
import com.example.atry.zhbj.domain.NewsMenu;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

public class NewsMenuDetailPager extends BaseMenuDetailPager {
    private ViewPager pager_news_menu_detail;
    private ArrayList<NewsMenu.NewsTabData> mTabData;
    private List<TabDetailPager> mpagers;
    public NewsMenuDetailPager(Activity activity, ArrayList<NewsMenu.NewsTabData> children) {
        super(activity);
        this.mTabData = children;
    }

    @Override
    public View initView() {
        View view = View.inflate(mactivity, R.layout.pager_news_menu_detail,null);
        pager_news_menu_detail = view.findViewById(R.id.pager_news_menu_detail);
        return view;
    }

    @Override
    public void initData() {
        mpagers = new ArrayList<>();
        for(int i=0;i < mTabData.size();i++){
            TabDetailPager tabDetailPager = new TabDetailPager(mactivity,mTabData.get(i));
            mpagers.add(tabDetailPager);
        }

        pager_news_menu_detail.setAdapter(new NewsMenuDetailAdapter());
        TabPageIndicator pageIndicator = new TabPageIndicator(mactivity);


    }

    class NewsMenuDetailAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mpagers.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            TabDetailPager tabDetailPager = mpagers.get(position);
            View view = tabDetailPager.mRootView;
            container.addView(view);
            tabDetailPager.initData();
            return view;

        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
           container.removeView((View)object);
        }
    }

}
