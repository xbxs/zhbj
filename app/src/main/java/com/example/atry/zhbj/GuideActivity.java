package com.example.atry.zhbj;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.atry.zhbj.utils.ConstantValues;
import com.example.atry.zhbj.utils.DensityUtils;
import com.example.atry.zhbj.utils.PrefUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends AppCompatActivity {

    private ViewPager vp_guide;
    private LinearLayout ll_guide;
    private Button btn_guide;
    private int[] picture = new int[]{R.drawable.guide_1,R.drawable.guide_2,R.drawable.guide_3};
    private List<ImageView> list = new ArrayList<>();
    private ImageView iv_dot_red;
    private int mPointDis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_guide);

        initUI();

        initData();

        vp_guide.setAdapter(new GuideAdapter());
    }

    private void initUI() {
        vp_guide = findViewById(R.id.vp_guide);
        btn_guide = findViewById(R.id.btn_guide);
        ll_guide = findViewById(R.id.ll_guide);
        iv_dot_red = findViewById(R.id.iv_dot_red);
        vp_guide.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //mPointDis为一个成员变量，他的值为viewpager的宽度，设置视图树的观察者，当生成这个视图时，即可
                //得到几个小圆点的宽度，positionOffset在viewpager上滑动的百分比,leftmargin等于百分比乘以他们之间
                // 的间距，即可使小红点跟随滑动
                int leftmargin = (int) (mPointDis * positionOffset) + position * mPointDis;
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) iv_dot_red.getLayoutParams();
                params.leftMargin = leftmargin;
                iv_dot_red.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int position) {
                if(position == list.size() -1){
                    btn_guide.setVisibility(View.VISIBLE);
                }else {
                    btn_guide.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        iv_dot_red.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                iv_dot_red.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mPointDis = ll_guide.getChildAt(1).getLeft() - ll_guide.getChildAt(0).getLeft();
            }
        });

        btn_guide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrefUtils.setBoolean(GuideActivity.this, ConstantValues.IS_FIRSTENTER,false);
                startActivity(new Intent(GuideActivity.this,MainActivity.class));
            }
        });
    }

    private void initData() {
        for(int i= 0;i<picture.length;i++){
            //初始化图片
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(picture[i]);
            list.add(imageView);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            // 初始化小圆点
            if(i > 0){
                params.leftMargin = DensityUtils.dip3px(15,this);
            }
            ImageView dotview = new ImageView(this);
            dotview.setImageResource(R.drawable.shape_point_gray);
            ll_guide.addView(dotview,params);
        }

    }
    class GuideAdapter extends PagerAdapter{
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ImageView imageView = list.get(position);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View)object);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
