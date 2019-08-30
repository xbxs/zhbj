package com.example.atry.zhbj.fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.atry.zhbj.MainActivity;
import com.example.atry.zhbj.R;
import com.example.atry.zhbj.domain.NewsMenu;
import com.example.atry.zhbj.imple.NewsPager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class LeftMenuFragment extends BaseFragment {
    private List<NewsMenu.NewsMenuData> menuData = new ArrayList<>();
    private int lastchoose = 0;
    private ListView lv_list;
    private LeftMenuAdapter adapter;

    @Override
    public View initView() {
        View view = View.inflate(mactivity, R.layout.fragment__left_menu,null);
        lv_list = view.findViewById(R.id.lv_list);
        return view;
    }

    @Override
    public void initData() {

    }

    public void setMenuData(ArrayList<NewsMenu.NewsMenuData> data){
        lastchoose = 0;
        menuData = data;
        adapter = new LeftMenuAdapter();
        lv_list.setAdapter(adapter);

        lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                lastchoose = i;
                adapter.notifyDataSetChanged();
                toggle();
                // 点击左边的fragment
                setCurrentDetailPager(i);
            }
        });
    }

    private void setCurrentDetailPager(int i) {
        // 由于fragment之间的通信是通过activity 进行的，所以先得到activity

        MainActivity mainActivity = (MainActivity) mactivity;
        // 得到主页面显示的fragment
        ContentMenuFragment contentMenuFragment = mainActivity.getContentMenuuFragment();
        // 因为右边的页面被分为五个部分，所以再得到主页面中的新闻页面
        NewsPager newsPager = contentMenuFragment.getNewsCenerPager();
        // 在新闻页提供设置各个页面的的方法
        newsPager.setCurrentNewsDetail(i);
    }

    /**
     *  打开或者关闭侧边栏 如果打开则会关闭，相反则打开
     */
    private void toggle() {
        MainActivity mainActivity = (MainActivity) mactivity;
        SlidingMenu slidingMenu = mainActivity.getSlidingMenu();
        slidingMenu.toggle();
    }

    class LeftMenuAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return menuData.size();
        }

        @Override
        public NewsMenu.NewsMenuData getItem(int i) {
            return menuData.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View view1 = View.inflate(mactivity,R.layout.list_item_left,null);
            TextView tv_menu = view1.findViewById(R.id.tv_menu);
            tv_menu.setText(menuData.get(i).title);
            if(lastchoose == i){
                tv_menu.setEnabled(true);
            }
            return view1;
        }
    }
}
