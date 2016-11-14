package com.samzc.anim.lib;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Author: zhaocheng
 * Date: 2016-03-01
 * Time: 10:18
 * Name:LiveLeftInLayout
 * Introduction:
 */
public class LiveLeftInLayout extends LinearLayout {

    private final static int MAX_LEFT= 2;

    private LayoutParams lp;

    private List<String> data;

    public LiveLeftInLayout(Context context) {
        super(context);
        init();
    }

    public LiveLeftInLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LiveLeftInLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LiveLeftInLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void init(){
        setOrientation(VERTICAL);
        data = new ArrayList<>();
        lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 40);
    }

    public void addLeftViewIn(){
        if (getChildCount()<MAX_LEFT&&data.size()==0){
            LiveLeftView liveLeftView = new LiveLeftView(getContext());
            showLeftIn(liveLeftView);
        }else{
            data.add("asd");
        }
    }

    private void showLeftIn(LiveLeftView liveLeftView) {
        Random random = new Random();
        int s = random.nextInt(10000)%(10000-1000+1) + 1000;
        liveLeftView.setTag(s);
        addView(liveLeftView, lp);
        AnimatorSet set = getLeftInAnimtor(liveLeftView);
        set.addListener(new AnimLeftInListener(liveLeftView));
        set.start();
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private AnimatorSet getLeftInAnimtor(final View target) {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, View.ALPHA, 0.2f, 1f);
        ObjectAnimator translationX = ObjectAnimator.ofFloat(target, View.TRANSLATION_X, -150, 0);
        AnimatorSet enter = new AnimatorSet();
        enter.setDuration(500);
        enter.setInterpolator(new LinearInterpolator());
        enter.playTogether(alpha, translationX);
        enter.setTarget(target);
        return enter;
    }


    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private AnimatorSet getTopOutInAnimtor(final View target) {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, View.ALPHA, 1f, 0f);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(target, View.TRANSLATION_Y, 0,-100);
        AnimatorSet enter = new AnimatorSet();
        enter.setDuration(500);
        enter.setInterpolator(new LinearInterpolator());
        enter.playTogether(alpha, translationY);
        enter.setTarget(target);
        return enter;
    }


    Handler handler = new Handler();


    private class AnimLeftInListener extends AnimatorListenerAdapter {
        private View target;

        public AnimLeftInListener(View target) {
            this.target = target;
        }
        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
             int d = (int) target.getTag();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    AnimatorSet set = getTopOutInAnimtor(target);
                    set.addListener(new AnimEndListener(target));
                    set.start();
                }
            }, d);
        }
    }

    private class AnimEndListener extends AnimatorListenerAdapter {
        private View target;

        public AnimEndListener(View target) {
            this.target = target;
        }


        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            //因为不停的add 导致子view数量只增不减,所以在view动画结束后remove掉
            removeView((target));
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (data.size()>0){
                        LiveLeftView liveLeftView = new LiveLeftView(getContext());
                        showLeftIn(liveLeftView);
                        data.remove(0);
                    }
                }
            });
        }
    }

}
