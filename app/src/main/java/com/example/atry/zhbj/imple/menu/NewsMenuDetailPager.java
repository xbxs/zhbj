package com.example.atry.zhbj.imple.menu;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.atry.zhbj.MainActivity;
import com.example.atry.zhbj.R;
import com.example.atry.zhbj.base.BaseMenuDetailPager;
import com.example.atry.zhbj.domain.NewsMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

public class NewsMenuDetailPager extends BaseMenuDetailPager {
    private ViewPager pager_news_menu_detail;
    private ArrayList<NewsMenu.NewsTabData> mTabData;
    private List<TabDetailPager> mpagers;
    private TabPageIndicator mIndicator;
    private ImageButton igb_nextpage;

    public NewsMenuDetailPager(Activity activity, ArrayList<NewsMenu.NewsTabData> children) {
        super(activity);
        this.mTabData = children;
    }

    @Override
    public View initView() {
        View view = View.inflate(mactivity, R.layout.pager_news_menu_detail,null);
        pager_news_menu_detail = view.findViewById(R.id.pager_news_menu_detail);
        mIndicator = view.findViewById(R.id.vp_indictor);
        igb_nextpage = view.findViewById(R.id.igb_nextpage);

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
        //点击下一页，这里需得到当前的item位置，将其加一，之后再设置当前item的位置即可
        igb_nextpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentitem = pager_news_menu_detail.getCurrentItem();
                currentitem++;
                pager_news_menu_detail.setCurrentItem(currentitem);
            }
        });
        // 为指示器设置viewpager需要在 设置适配器的后面，不然无法无法测量出viewpager的总数
        mIndicator.setViewPager(pager_news_menu_detail);
        // 为指示器设置滑动监听，用于控制 sliddingmenu 的打开和关闭
        mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0){
                    setSlidingMenuEnable(true);
                }else {
                    setSlidingMenuEnable(false);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setSlidingMenuEnable(boolean b) {
        MainActivity UIActivity = (MainActivity) mactivity;
        SlidingMenu menu = UIActivity.getSlidingMenu();
        if(b){
            menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        }else{
            menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }
    // 这个适配器为viewpager的适配器
    class NewsMenuDetailAdapter extends PagerAdapter {
        /**
         *   其中，这个方法为显示viewpager指示器的标题
         * @param position
         * @return
         */
        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            NewsMenu.NewsTabData newsTabData = mTabData.get(position);
            return newsTabData.title;
        }

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
