package com.example.atry.zhbj;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.umeng.analytics.MobclickAgent;

import cn.sharesdk.onekeyshare.OnekeyShare;

public class NewsDetailActivity extends AppCompatActivity {

    private WebView mWb_newsdetail;
    ImageButton imgb_webview_back,imgb_webview_textsize,imgb_webview_share;
    private ProgressBar mPb_webview_newsdetail;
    private String mUri;
    private int selectionloaction = 2;
    private WebSettings mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        initUI();

        initData();

    }

    private void initUI() {
        mWb_newsdetail = findViewById(R.id.wb_newsdetail);
        imgb_webview_back = findViewById(R.id.imgb_webview_back);
        imgb_webview_textsize = findViewById(R.id.imgb_webview_textsize);
        imgb_webview_share = findViewById(R.id.imgb_webview_share);
        mPb_webview_newsdetail = findViewById(R.id.pb_webview_newsdetail);

        mUri = getIntent().getStringExtra("news_id");
        imgb_webview_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imgb_webview_textsize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDialog();
            }
        });

        imgb_webview_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare();
            }
        });
    }
    //选择字体时的弹框
    private void initDialog(){
        AlertDialog dialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("字体大小选择");
        String[] items = new String[]{"超大号字体","大号字体","正常字体","小号字体","超小号字体"};
        builder.setSingleChoiceItems(items, selectionloaction, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectionloaction = which;
            }
        });

        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (selectionloaction){
                    case 0:
                        mSettings.setTextZoom(150);
                        break;
                    case 1:
                        mSettings.setTextZoom(125);
                        break;
                    case 2:
                        mSettings.setTextZoom(100);
                        break;
                    case 3:
                        mSettings.setTextZoom(80);
                        break;
                    case 4:
                        mSettings.setTextZoom(60);
                        break;
                    default:
                        break;

                }

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(dialog != null){
                    dialog.dismiss();
                }
            }
        });
         dialog = builder.show();


    }

    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        // title标题，微信、QQ和QQ空间等平台使用
        oks.setTitle(getString(R.string.share));
        // titleUrl QQ和QQ空间跳转链接
        oks.setTitleUrl("http://www.baidu.com");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，确保SDcard下面存在此张图片
        oks.setImagePath("/sdcard/test.jpg");
        // url在微信、Facebook等平台中使用
        oks.setUrl("http://sharesdk.cn");
        // 启动分享GUI
        oks.show(this);
    }

    private void initData() {
        mWb_newsdetail.loadUrl(mUri);

        mSettings = mWb_newsdetail.getSettings();
        mSettings.setBuiltInZoomControls(true); // 允许页面放大
        mSettings.setUseWideViewPort(true); // 允许双击放大
        mSettings.setJavaScriptEnabled(true);

        mWb_newsdetail.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mPb_webview_newsdetail.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            /**
             *
             * @param view
             * @param request
             * @return
             *
             * 防止页面跳转到浏览器
             */
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String str_uri = request.getUrl().toString();
                if(str_uri == null){
                    return false;
                }
                try {
                    if (str_uri.startsWith("http:") || str_uri.startsWith("https:")){
                        view.loadUrl(str_uri);
                        return false;
                    }else{
                        Intent intent = new Intent(Intent.ACTION_VIEW,request.getUrl());
                        startActivity(intent);
                        return true;
                    }
                }catch (Exception e){
                    return false;
                }

            }
        });
        /**
         * 可以得到页面加载的进度
         */
        mWb_newsdetail.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if(newProgress > 50){
                    mPb_webview_newsdetail.setVisibility(View.INVISIBLE);
                }
            }
        });

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
