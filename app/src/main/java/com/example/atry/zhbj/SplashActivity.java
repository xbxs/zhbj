package com.example.atry.zhbj;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.example.atry.zhbj.utils.ConstantValues;
import com.example.atry.zhbj.utils.PrefUtils;

public class SplashActivity extends AppCompatActivity {

    private ImageView splash_bg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        splash_bg = findViewById(R.id.splash_bg);
        initAnimation();

    }

    private void initAnimation() {
        AnimationSet animationSet = new AnimationSet(true);

        RotateAnimation rotateAnimation = new RotateAnimation(0,360, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        rotateAnimation.setDuration(1000);
        rotateAnimation.setFillAfter(true);

        ScaleAnimation scaleAnimation = new ScaleAnimation(0,1,0,1,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        scaleAnimation.setDuration(1000);
        scaleAnimation.setFillAfter(true);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
        alphaAnimation.setFillAfter(true);
        alphaAnimation.setDuration(2000);

        animationSet.addAnimation(rotateAnimation);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);

        splash_bg.startAnimation(animationSet);

        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                boolean is_firstenter = PrefUtils.getBoolean(SplashActivity.this, ConstantValues.IS_FIRSTENTER,true);
                Intent intent = null;
                if(is_firstenter){
                    intent = new Intent(SplashActivity.this,GuideActivity.class);
                }else{
                    intent = new Intent(SplashActivity.this,MainActivity.class);
                }
                startActivity(intent);

                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
