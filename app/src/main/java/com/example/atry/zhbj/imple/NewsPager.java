package com.example.atry.zhbj.imple;

import android.app.Activity;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

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
import com.example.atry.zhbj.utils.PrefUtils;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

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
        String result = PrefUtils.getString(mactivity,ConstantValues.CATEGORY_URL,"");
        if(TextUtils.isEmpty(result)){
            getDataFromService();
        }else{
            processData(result);
        }

    }

    private void getDataFromService() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/categories.json";

        RequestParams params = new RequestParams(ConstantValues.CATEGORY_URL);
        params.setSaveFilePath(path);
        params.setAutoRename(true);

        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                PrefUtils.setString(mactivity,ConstantValues.CATEGORY_URL,result);
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
        mlist.add(new PhotosMenuDetailPager(mainActivity,ib_photoshown));
        mlist.add(new InteractMenuDetailPager(mainActivity));

        setCurrentNewsDetail(0);
    }
    // 将不同的专题加到右边的Fragment里
    public void setCurrentNewsDetail(int i){
        BaseMenuDetailPager pager = mlist.get(i);
        View view = pager.mRootView;
        fl_content.removeAllViews();
        fl_content.addView(view);
        tvTitle.setText(mnewsMenu.data.get(i).title);


        if(pager instanceof PhotosMenuDetailPager){
            ib_photoshown.setVisibility(View.VISIBLE);
        }else{
            ib_photoshown.setVisibility(View.GONE);
        }
        pager.initData();
    }


}
