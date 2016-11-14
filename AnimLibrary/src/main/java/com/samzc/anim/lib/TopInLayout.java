package com.samzc.anim.lib;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import java.util.ArrayList;
import java.util.List;

/**
 * Author: zhaocheng
 * Date: 2016-03-03
 * Time: 16:36
 * Name:TopInLayout
 * Introduction:
 */
public class TopInLayout extends RelativeLayout {

    private Interpolator acc = new AccelerateInterpolator();//加速
    private Interpolator dce = new DecelerateInterpolator();//减速
    private LayoutParams lp;
    private int mHeight;
    private int mWidth;
    private int middleX;
    private int middleY;
    private int drawableX;
    private int drawableY;
    private List<Drawable> data;

    public TopInLayout(Context context) {
        super(context);
        init();
    }

    public TopInLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TopInLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TopInLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }

    public void init(){
        lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        data= new ArrayList<>();
    }

    public void addTopInAnim(Drawable drawable) {
        if (drawable==null) return;
        if (getChildCount() == 0) {
            ImageView imageView = new ImageView(getContext());
            drawableX = drawable.getIntrinsicWidth();
            drawableY = drawable.getIntrinsicHeight();
            middleX = (mWidth/2)-(drawableX/2);
            middleY = (mHeight/2)-(drawableY/2);
            imageView.setImageDrawable(drawable);
            showAnim(imageView);
        }else{
            data.add(drawable);
        }
    }

    private void showAnim(ImageView imageView) {
        addView(imageView, lp);
        Animator set = getStartAnimator(imageView);
        set.addListener(new AnimTopInListener(imageView));
        set.start();
    }

    private Animator getStartAnimator(View target) {
        AnimatorSet set = getEnterAnimtor(target);
        AnimatorSet finalSet = new AnimatorSet();
        finalSet.playSequentially(set);
        finalSet.setTarget(target);
        return finalSet;
    }
    private Animator getEndAnimator(View target) {
        AnimatorSet set = getEndAnimtor(target);
        AnimatorSet finalSet = new AnimatorSet();
        finalSet.playSequentially(set);
        finalSet.setTarget(target);
        return finalSet;
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private AnimatorSet getEnterAnimtor(final View target) {
        ObjectAnimator tranX = ObjectAnimator.ofFloat(target, View.TRANSLATION_X, -drawableX,middleX);
        ObjectAnimator tranY = ObjectAnimator.ofFloat(target, View.TRANSLATION_Y, -drawableY,middleY);
        AnimatorSet enter = new AnimatorSet();
        enter.setDuration(2000);
        enter.setInterpolator(dce);
        enter.playTogether(tranX, tranY);
        enter.setTarget(target);
        return enter;
    }


    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private AnimatorSet getEndAnimtor(final View target) {
        ObjectAnimator tranX = ObjectAnimator.ofFloat(target, View.TRANSLATION_X, middleX, mWidth);
        ObjectAnimator tranY = ObjectAnimator.ofFloat(target, View.TRANSLATION_Y, middleY, mHeight);
        AnimatorSet enter = new AnimatorSet();
        enter.setDuration(1000);
        enter.setInterpolator(acc);
        enter.playTogether(tranX, tranY);
        enter.setTarget(target);
        return enter;
    }

    private void removeTarget(View target) {
        removeView((target));
        if (data.size()>0){
            ImageView imageView = new ImageView(getContext());
            imageView.setImageResource(R.drawable.demo_car);
            showAnim(imageView);
            data.remove(0);
        }
    }

    Handler mHanlder = new Handler();


    private class AnimTopInListener extends AnimatorListenerAdapter {
        private View target;

        public AnimTopInListener(View target) {
            this.target = target;
        }
        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            mHanlder.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Animator endAnimator = getEndAnimator(target);
                    endAnimator.addListener(new AnimEndListener(target));
                    endAnimator.start();
                }
            },1000);
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
            removeTarget(target);
        }

    }



}
