package com.samzc.anim.lib;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;



/**
 * Author: zhaocheng
 * Date: 2016-03-01
 * Time: 10:58
 * Name:LiveLeftView
 * Introduction:
 */
public class LiveLeftView extends LinearLayout {

    ViewHolder viewHolder;

    public LiveLeftView(Context context) {
        super(context);
        init();
    }

    public LiveLeftView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LiveLeftView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LiveLeftView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        viewHolder = new ViewHolder();
        inflate(getContext(), R.layout.view_left_in,this);
        viewHolder.civ_user_icon= (CircleImageView) findViewById(R.id.civ_user_icon);
        viewHolder.iv_gift = (ImageView) findViewById(R.id.iv_gift);
        viewHolder.tv_msg = (TextView) findViewById(R.id.tv_msg);
        viewHolder.tv_user_name = (TextView) findViewById(R.id.tv_user_name);
//        String url = "http://xuexibao1.b0.upaiyun.com/2016/1/20/56f2b590-1eea-4432-a7b6-2411811b2ea1.png!";
//        Glide.with(getContext()).load(url).asBitmap().into(viewHolder.civ_user_icon);

    }



    class ViewHolder{
        CircleImageView civ_user_icon;
        TextView tv_user_name;
        TextView tv_msg;
        ImageView iv_gift;
    }
}
