package com.example.atry.zhbj.imple;

import android.app.Activity;
import android.graphics.Color;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.atry.zhbj.MainActivity;
import com.example.atry.zhbj.base.BaseMenuDetailPager;
import com.example.atry.zhbj.base.BasePager;
import com.example.atry.zhbj.domain.NewsMenu;
import com.example.atry.zhbj.fragment.LeftMenuFragment;
import com.example.atry.zhbj.imple.menu.InteractMenuDetailPager;
import com.example.atry.zhbj.imple.menu.NewsMenuDetailPager;
import com.example.atry.zhbj.imple.menu.PhotosMenuDetailPager;
import com.example.atry.zhbj.imple.menu.TopicMenuDetailPager;
import com.example.atry.zhbj.utils.ConstantValues;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class NewsPager extends BasePager {
    private List<BaseMenuDetailPager> mlist = new ArrayList<>();
    private NewsMenu mnewsMenu;

    public NewsPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        btnMenu.setVisibility(View.VISIBLE);
        getDataFromService();
    }

    private void getDataFromService() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/categories.json";
        RequestParams params = new RequestParams(ConstantValues.CATEGORY_URL);
        params.setSaveFilePath(path);
        Gson gson = new Gson();
        params.setAutoRename(true);
        Log.i("TAG","请求数据");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                processData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                    ex.printStackTrace();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void processData(String result) {
        Gson gson = new Gson();
        mnewsMenu = gson.fromJson(result, NewsMenu.class);
        MainActivity mainActivity = (MainActivity) mactivity;
        LeftMenuFragment leftMenuFragment = mainActivity.getLeftMenuuFragment();
        leftMenuFragment.setMenuData(mnewsMenu.data);

        mlist.add(new NewsMenuDetailPager(mainActivity,mnewsMenu.data.get(0).children));
        mlist.add(new TopicMenuDetailPager(mainActivity));
        mlist.add(new PhotosMenuDetailPager(mainActivity));
        mlist.add(new InteractMenuDetailPager(mainActivity));

        setCurrentNewsDetail(0);
    }

    public void setCurrentNewsDetail(int i){
        BaseMenuDetailPager pager = mlist.get(i);
        View view = pager.mRootView;
        fl_content.removeAllViews();
        fl_content.addView(view);
        tvTitle.setText(mnewsMenu.data.get(i).title);
        pager.initData();
    }


}
