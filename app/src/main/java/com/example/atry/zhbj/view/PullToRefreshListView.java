package com.example.atry.zhbj.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.atry.zhbj.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 李维: TZZ on 2019-09-03 19:32
 * 邮箱: 3182430026@qq.com
 */
public class PullToRefreshListView extends ListView implements AbsListView.OnScrollListener {


    private View mMMHeaderView;
    private int mMHeaderViewHeight;
    private int mStartY = -1;
    //下拉刷新
    public static final int STATE_PULL_TO_REFRESH = 1;
    //释放刷新
    public static final int STATE_RELEASE_TO_REFRESH = 2;
    //正在刷新
    public static final int STATE_REFRESHING = 3;
    //当前刷新的栏的状态
    public int currentstate = STATE_PULL_TO_REFRESH;
    private ImageView mIv_listheader;
    private ProgressBar mPb_listheader;
    private TextView mTv_listheader_title,tv_listheader_data;
    private RotateAnimation mUpanimation;
    private RotateAnimation mDownanimation;
    private View mMFooterView;
    private int mFooterViewHeight;
    private boolean isLoadMore;
    //接口对象，外部通过实现接口的对象进行操作
    private OnRefreshListener mListener;


    public PullToRefreshListView(Context context) {
        super(context);
        initHeaderView();
        initFooterView();
    }

    public PullToRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeaderView();
        initFooterView();
    }

    public PullToRefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView();
        initFooterView();
    }

    /**
     * 初始化头布局
     */

    private void initHeaderView(){
        mMMHeaderView = View.inflate(getContext(), R.layout.pull_to_refresh_header,null);
        this.addHeaderView(mMMHeaderView);

        //找到各个布局
        mIv_listheader = mMMHeaderView.findViewById(R.id.iv_listheader);
        mPb_listheader = mMMHeaderView.findViewById(R.id.pb_listheader);
        mTv_listheader_title = mMMHeaderView.findViewById(R.id.tv_listheader_title);
        tv_listheader_data = mMMHeaderView.findViewById(R.id.tv_listheader_data);

        // 在生成布局时得到布局的宽高
        mMMHeaderView.measure(0,0);
        mMHeaderViewHeight = mMMHeaderView.getMeasuredHeight();
        mMMHeaderView.setPadding(0,-mMHeaderViewHeight,0,0);
        initAnimation();
    }

    public void initAnimation(){
        mUpanimation = new RotateAnimation(0,-180,
                Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        mUpanimation.setDuration(200);
        mUpanimation.setFillAfter(true);
        mDownanimation = new RotateAnimation(-180,0,
                Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        mDownanimation.setFillAfter(true);
        mDownanimation.setDuration(200);
    }

    /**
     * 初始化加载更多布局
     */

    private void initFooterView(){
        mMFooterView = View.inflate(getContext(),R.layout.pull_to_refresh_footer,null);
        this.addFooterView(mMFooterView);
        mMFooterView.measure(0,0);
        mFooterViewHeight = mMFooterView.getMeasuredHeight();
        mMFooterView.setPadding(0,-mMHeaderViewHeight,0,0);
        this.setOnScrollListener(this);
    }

    /**
     *  不能将 startY 定义在 onTouchEvent中，因为在移动过程中会将 startY重新赋值
     * @param ev
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                mStartY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if(currentstate == STATE_REFRESHING){
                    break;
                }
                if(mStartY == -1){
                    mStartY = (int)ev.getY();
                }
                int endy = (int)ev.getY();
                //dy 为移动的距离，如果向下滑动，则dy大于0，当前的头布局已经设置padding为 负的布局高度，再用正的
                //减去布局高度，则得到的隐藏值将会缩小
                int dy = endy - mStartY;
                int firstVisiblePosition = getFirstVisiblePosition();
                if(dy > 0 && firstVisiblePosition == 0){
                    int padding = dy - mMHeaderViewHeight;
                    mMMHeaderView.setPadding(0,padding,0,0);
                    if(padding > 0 && currentstate != STATE_RELEASE_TO_REFRESH){
                        currentstate = STATE_RELEASE_TO_REFRESH;
                        refreshState();
                    }else if(padding < 0 && currentstate != STATE_PULL_TO_REFRESH){
                        currentstate = STATE_PULL_TO_REFRESH;
                        refreshState();
                    }

                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                mStartY = -1;
                // 如果当前
                if(currentstate == STATE_RELEASE_TO_REFRESH){
                    currentstate = STATE_REFRESHING;
                    refreshState();
                    mMMHeaderView.setPadding(0,
                            0,0,0);
                }else if(currentstate == STATE_PULL_TO_REFRESH){
                    mMMHeaderView.setPadding(0,-mMHeaderViewHeight,0,0);
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    private void refreshState() {
        switch (currentstate){
            case STATE_PULL_TO_REFRESH:
                mTv_listheader_title.setText("下拉刷新");
                mPb_listheader.setVisibility(View.INVISIBLE);
                mIv_listheader.setVisibility(View.VISIBLE);
                mIv_listheader.startAnimation(mDownanimation);
                break;
            case STATE_REFRESHING:
                mIv_listheader.clearAnimation();
                mTv_listheader_title.setText("正在刷新...");
                mPb_listheader.setVisibility(View.VISIBLE);
                mIv_listheader.setVisibility(View.INVISIBLE);
                //调用刷新的方法
                mListener.onrefresh();
                break;
            case STATE_RELEASE_TO_REFRESH:
                mTv_listheader_title.setText("释放刷新");
                mPb_listheader.setVisibility(View.INVISIBLE);
                mIv_listheader.setVisibility(View.VISIBLE);
                mIv_listheader.startAnimation(mUpanimation);
                break;
            default:
                break;
        }
    }

    public void setCurrentDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = dateFormat.format(new Date());
        tv_listheader_data.setText(time);
    }
    // 加载结束后更新布局
    public void setRefreshComplement(boolean check) {
        if(!isLoadMore){
            mMMHeaderView.setPadding(0,-mMHeaderViewHeight,0,0);
            currentstate = STATE_PULL_TO_REFRESH;
            mTv_listheader_title.setText("下拉刷新");
            mPb_listheader.setVisibility(View.INVISIBLE);
            mIv_listheader.setVisibility(View.VISIBLE);
            mIv_listheader.startAnimation(mDownanimation);
            if(check){
                setCurrentDate();
            }
        }else{
            mMFooterView.setPadding(0,-mFooterViewHeight,0,0);
            isLoadMore = false;
        }

    }

    /**
     *
     * @param view
     * @param scrollState
     * 在滑动的监听中更新加载更多的布局的位置，其中 isLoadMore是为了防止用户在加载更多的时候再次滑动，造成数据得重复
     * 加载，其中setSelection(getCount() -1)是让加载更多时直接全部拖出,不然用户有时滑至底部不知道下滑加载更多，调用
     * 让监听执行loadmore方法
     *
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(scrollState == SCROLL_STATE_IDLE){
            int lastVisiblePosition = getLastVisiblePosition();
            if(lastVisiblePosition == getCount() -1 && !isLoadMore){
                isLoadMore = true;
                mMFooterView.setPadding(0,0,0,0);
                //将listview的位置设置到最后一个item布局
                setSelection(getCount() -1);
                mListener.loadmore();
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    // 定义解控供外部调用
    public interface OnRefreshListener{
        void onrefresh();

        void loadmore();
    }

    public void setOnRefreshListener(OnRefreshListener listener){
        this.mListener = listener;
    }

}
