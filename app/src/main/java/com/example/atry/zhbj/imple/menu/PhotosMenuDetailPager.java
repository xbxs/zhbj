package com.example.atry.zhbj.imple.menu;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.atry.zhbj.R;
import com.example.atry.zhbj.base.BaseMenuDetailPager;
import com.example.atry.zhbj.domain.PhotoBean;
import com.example.atry.zhbj.utils.ConstantValues;
import com.example.atry.zhbj.utils.PrefUtils;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.List;

public class PhotosMenuDetailPager extends BaseMenuDetailPager {

    private GridView mGv_photos;
    private ListView mLv_photos;
    private List<PhotoBean.PhotoInfo> mPhotos;
    private ImageView mImageView;
    private String mUri;
    private ImageButton ib_photoshown;
    private boolean isListview = true;

    public PhotosMenuDetailPager(Activity activity,ImageButton ib_photoshown) {
        super(activity);
        this.ib_photoshown = ib_photoshown;
    }

    @Override
    public View initView() {
        View view = View.inflate(mactivity, R.layout.pager_news_detail,null);
        mGv_photos = view.findViewById(R.id.gv_photos);
        mLv_photos = view.findViewById(R.id.lv_photos);
        return view;
    }

    @Override
    public void initData() {
        String uri = PrefUtils.getString(mactivity,ConstantValues.PHOTOS_URL,"");
        // 为点击按钮设置监听
        ib_photoshown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isListview){
                    mLv_photos.setVisibility(View.GONE);
                    mGv_photos.setVisibility(View.VISIBLE);
                    isListview = false;
                }else{
                    mLv_photos.setVisibility(View.VISIBLE);
                    mGv_photos.setVisibility(View.GONE);
                    isListview = true;
                }
            }
        });
        if(TextUtils.isEmpty(uri)){
            getPhotosFromService();
        }else{
            precessData(uri);
        }


    }

    /**
     * 从服务器请求数据
     */
    private void getPhotosFromService() {
        RequestParams params = new RequestParams(ConstantValues.PHOTOS_URL);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                precessData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(mactivity,"请求失败",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * 解析数据
     * @param result
     */
    private void precessData(String result) {
        Gson gson = new Gson();
        PhotoBean photoBean = gson.fromJson(result,PhotoBean.class);
        mPhotos = photoBean.data.news;
        //做缓存
        PrefUtils.setString(mactivity,ConstantValues.PHOTOS_URL,result);

        mLv_photos.setAdapter(new PhotosAdapter());

        mGv_photos.setAdapter(new PhotosAdapter());
    }

    class PhotosAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mPhotos.size();
        }

        @Override
        public PhotoBean.PhotoInfo getItem(int position) {
            return mPhotos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView == null){
                convertView = View.inflate(mactivity,R.layout.list_item_photos,null);
                holder = new ViewHolder();
                holder.iv_photo = convertView.findViewById(R.id.iv_photo);
                holder.tv_photo = convertView.findViewById(R.id.tv_photo);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            PhotoBean.PhotoInfo info = mPhotos.get(position);
            displayUriPicture(holder.iv_photo,info.listimage);
            holder.tv_photo.setText(info.title);

            return convertView;
        }

    }

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

    static class ViewHolder{
        ImageView iv_photo;
        TextView tv_photo;
    }
    
}
