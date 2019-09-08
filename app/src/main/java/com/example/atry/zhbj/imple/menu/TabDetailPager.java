package com.example.atry.zhbj.imple.menu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.atry.zhbj.NewsDetailActivity;
import com.example.atry.zhbj.R;
import com.example.atry.zhbj.base.BaseMenuDetailPager;
import com.example.atry.zhbj.domain.NewTabBean;
import com.example.atry.zhbj.domain.NewsMenu;
import com.example.atry.zhbj.utils.ConstantValues;
import com.example.atry.zhbj.utils.PrefUtils;
import com.example.atry.zhbj.view.PullToRefreshListView;
import com.google.gson.Gson;
import com.viewpagerindicator.CirclePageIndicator;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.List;

// 用于显示具体的新闻信息
public class TabDetailPager extends BaseMenuDetailPager {
    private NewsMenu.NewsTabData mnewsTabData;
    private ViewPager mVp_top_news;
    private List<NewTabBean.TopNewsData> mTopNewsDatas;
    private List<NewTabBean.NewsData> mNewsDatas;
    private String mUri;
    private TextView mtv_topnew_title;
    private CirclePageIndicator idc_topnes_title;
    private PullToRefreshListView mlv_news;
    private NewsAdapter mMNewsAdapter;
    private String mLoadmoreurl;
    private MyHandler mMyHandler;
    private ImageView mImageView;

    public TabDetailPager(Activity activity, NewsMenu.NewsTabData newsTabData) {
        super(activity);
        this.mnewsTabData = newsTabData;
    }

    @Override
    public View initView() {
        View view = View.inflate(mactivity,R.layout.pager_tab_detail,null);
        //加载ListView的头布局   view为新闻显示的ListView布局，mHeaderView为轮播的那一部分，为Listview的头布局
        final View mHeaderView = View.inflate(mactivity,R.layout.list_item_header,null);
        mVp_top_news = mHeaderView.findViewById(R.id.vp_top_news);
        mtv_topnew_title = mHeaderView.findViewById(R.id.tv_topnew_title);
        idc_topnes_title = mHeaderView.findViewById(R.id.idc_topnes_title);

        mlv_news = view.findViewById(R.id.lv_news);
        mlv_news.addHeaderView(mHeaderView);
        mlv_news.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            @Override
            public void onrefresh() {
                getDataFromServer();
            }

            @Override
            public void loadmore() {
                if(mLoadmoreurl != null) {
                    getDataMoreFromServer();
                }else{
                    Toast.makeText(mactivity,"没有更多数据了",Toast.LENGTH_SHORT).show();
                    mlv_news.setRefreshComplement(false);
                }
            }
        });
        // 为list中的条目设置监听
        mlv_news.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int headerViewCount = mlv_news.getHeaderViewsCount();
                position  = position - headerViewCount;

                NewTabBean.NewsData news = mNewsDatas.get(position);
                String readIds = PrefUtils.getString(mactivity,ConstantValues.READ_IDS,"");
                if(!readIds.contains(news.id+",")){
                    readIds = readIds + news.id+",";
                    PrefUtils.setString(mactivity,ConstantValues.READ_IDS,readIds);
                }
                Intent intent = new Intent(mactivity,NewsDetailActivity.class);
                intent.putExtra("news_id",news.url);
                ((TextView)view.findViewById(R.id.tv_item_title)).setTextColor(Color.GRAY);
                mactivity.startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void initData() {
        mUri = ConstantValues.SERVER_URL+mnewsTabData.url;
        String cache = PrefUtils.getString(mactivity,mUri,"");
        if(!TextUtils.isEmpty(cache)){
             processData(cache,false);
        }
        getDataFromServer();
    }

    class TopNewsAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return mTopNewsDatas.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ImageView imageView = new ImageView(mactivity);
            String imagerUrl = mTopNewsDatas.get(position).topimage;
            //解析uri 的图片
            displayUriPicture(imageView,imagerUrl);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View)object);
        }

    }
    // 从服务器获取数据
    private void getDataFromServer() {
        RequestParams params = new RequestParams(mUri);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                processData(result,false);
                // 保存缓存
                PrefUtils.setString(mactivity,mUri,result);
                mlv_news.setRefreshComplement(true);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(mactivity,"获取数据失败",Toast.LENGTH_SHORT).show();
                mlv_news.setRefreshComplement(false);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    //获取更多的数据
    private void getDataMoreFromServer(){
        RequestParams params = new RequestParams(mLoadmoreurl);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                processData(result,true);
                mlv_news.setRefreshComplement(false);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(mactivity,"获取数据失败",Toast.LENGTH_SHORT).show();
                mlv_news.setRefreshComplement(false);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }


    @SuppressLint("ClickableViewAccessibility")
    private void processData(String result, boolean isMore) {
        Gson gson = new Gson();
        NewTabBean newTabBean  = gson.fromJson(result, NewTabBean.class);

        //获取到加载更多的url
        String loadmore = newTabBean.data.more;
        if(!TextUtils.isEmpty(loadmore)){
            mLoadmoreurl = ConstantValues.SERVER_URL+loadmore;
        }else {
            mLoadmoreurl = null;
        }

        if(!isMore) {
            mTopNewsDatas = newTabBean.data.topnews;
            mNewsDatas = newTabBean.data.news;
            if (mTopNewsDatas != null) {
                mVp_top_news.setAdapter(new TopNewsAdapter());
                //为listview设置适配器
                mMNewsAdapter = new NewsAdapter();

                mlv_news.setAdapter(mMNewsAdapter);
                idc_topnes_title.setViewPager(mVp_top_news);
                //按照快照的方式展示
                idc_topnes_title.setSnap(true);

                //滑动时设置标题
                idc_topnes_title.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        mtv_topnew_title.setText(mTopNewsDatas.get(position).title);
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
                // 更新第一个新闻标题
                mtv_topnew_title.setText(mTopNewsDatas.get(0).title);
                //由于viewpageindictor会保存小圆点的位置，所以当再次销毁后再生成的图片顺序与小圆点不相符，所以需将其赋值为0
                idc_topnes_title.onPageSelected(0);
                //当第一次加载数据是，创建Handler,确保只有一个handler
                if(mMyHandler == null){
                    mMyHandler = new MyHandler();
                }
                //发送一个延迟消息去更新Viewpager的item
                mMyHandler.sendEmptyMessageDelayed(0,3000);
                //此处的代码的作用用于用户点击时不轮播
                mVp_top_news.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        switch (event.getAction()){
                            case MotionEvent.ACTION_DOWN:
                                mMyHandler.removeCallbacksAndMessages(null);
                                break;
                            case MotionEvent.ACTION_CANCEL:
                                mMyHandler.sendEmptyMessageDelayed(0,3000);
                                break;
                            case MotionEvent.ACTION_UP:
                                mMyHandler.sendEmptyMessageDelayed(0,3000);
                                break;
                            default:
                                break;
                        }

                        return false;
                    }
                });
            }
        }else{
            List<NewTabBean.NewsData> moredata =  newTabBean.data.news;
            mNewsDatas.addAll(moredata);
            mMNewsAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 用来展示图片
     * @param imageView
     * @param iconUri
     */

    private void displayUriPicture(ImageView imageView,String iconUri){
        mImageView = imageView;
        mUri = iconUri;
        ImageOptions imageOptions = new ImageOptions.Builder()
                .setIgnoreGif(true)
                .setImageScaleType(ImageView.ScaleType.FIT_XY)
                .setFailureDrawableId(R.drawable.load_failed)
                .setLoadingDrawableId(R.drawable.loading)
                .build();
        x.image().bind(mImageView,mUri,imageOptions);
    }

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            //得到当前viewpager的位置，更新位置，并发送一个延迟消息，形成循环
            int currentItem = mVp_top_news.getCurrentItem();
            currentItem++;
            if(currentItem > mTopNewsDatas.size()-1){
                currentItem = 0;
            }
            mVp_top_news.setCurrentItem(currentItem);
            mMyHandler.sendEmptyMessageDelayed(0,3000);
        }
    }

    class NewsAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mNewsDatas.size();
        }

        @Override
        public NewTabBean.NewsData getItem(int position) {
            return mNewsDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView == null){
                convertView = View.inflate(mactivity,R.layout.list_item_news,null);
                holder = new ViewHolder();
                holder.list_item_picture = convertView.findViewById(R.id.iv_item_picture);
                holder.list_item_title = convertView.findViewById(R.id.tv_item_title);
                holder.list_item_data = convertView.findViewById(R.id.tv_item_date);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
            NewTabBean.NewsData newsData = mNewsDatas.get(position);
            displayUriPicture(holder.list_item_picture,newsData.listimage);
            String read_ids = PrefUtils.getString(mactivity,ConstantValues.READ_IDS,"");
            holder.list_item_title.setText(newsData.title);
            if(read_ids.contains(newsData.id+",")){
                holder.list_item_title.setTextColor(Color.GRAY);
            }
            holder.list_item_data.setText(newsData.pubdate);
            return convertView;
        }
    }

    static class ViewHolder{
        ImageView list_item_picture;
        TextView list_item_title;
        TextView list_item_data;
    }
}
