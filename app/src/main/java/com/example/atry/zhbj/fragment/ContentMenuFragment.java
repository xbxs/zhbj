package com.example.atry.zhbj.fragment;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.example.atry.zhbj.MainActivity;
import com.example.atry.zhbj.R;
import com.example.atry.zhbj.base.BasePager;
import com.example.atry.zhbj.imple.GovPager;
import com.example.atry.zhbj.imple.HomePager;
import com.example.atry.zhbj.imple.NewsPager;
import com.example.atry.zhbj.imple.SettingPager;
import com.example.atry.zhbj.imple.SmartServicePager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;
import java.util.List;

public class ContentMenuFragment extends BaseFragment {

    private ViewPager vp_content;
    private List<BasePager> pagerList;
    RadioGroup rg_group;
    @Override
    public View initView() {
        View view = View.inflate(mactivity, R.layout.fragment__content_menu,null);
        vp_content = view.findViewById(R.id.vp_content);
        rg_group = view.findViewById(R.id.rg_group);
        pagerList = new ArrayList<>();
        return view;
    }

    @Override
    public void initData() {
        pagerList.add(new HomePager(mactivity));
        pagerList.add(new NewsPager(mactivity));
        pagerList.add(new SmartServicePager(mactivity));
        pagerList.add(new GovPager(mactivity));
        pagerList.add(new SettingPager(mactivity));

        vp_content.setAdapter(new ContentAdapter());
        rg_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.rb_home:
                        vp_content.setCurrentItem(0);
                        break;
                    case R.id.rb_news:
                        vp_content.setCurrentItem(1);
                        break;
                    case R.id.rb_smartservice:
                        vp_content.setCurrentItem(2);
                        break;
                    case R.id.rb_gov:
                        vp_content.setCurrentItem(3);
                        break;
                    case R.id.rb_setting:
                        vp_content.setCurrentItem(4);
                        break;
                    default:
                        break;

                }
            }
        });

        vp_content.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                BasePager pager = pagerList.get(position);
                pager.initData();

                if(position == 0 || position == pagerList.size() -1){
                    setSlidingMenuEnable(false);
                }else{
                    setSlidingMenuEnable(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        // 先把第一个页面的数据加载出来
        pagerList.get(0).initData();
        setSlidingMenuEnable(false);
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

    class ContentAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return pagerList.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            BasePager pager = pagerList.get(position);
//            pager.initData();  由于ViewPager会提前加载下一个页面，因此在这里初始化数据会造成流量的浪费和性能的浪费
            View view = pager.mRootView;
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }

    public NewsPager getNewsCenerPager(){
        NewsPager newsPager = (NewsPager) pagerList.get(1);
        return newsPager;
    }
}
