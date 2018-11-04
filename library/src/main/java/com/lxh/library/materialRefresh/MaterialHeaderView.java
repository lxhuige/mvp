package com.lxh.library.materialRefresh;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;


import com.lxh.library.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author lixiaohui 2017/7/5 0005.
 */

public class MaterialHeaderView extends FrameLayout {
    private AppCompatTextView textView;
    private AppCompatImageView mIconRefresh;
    private AppCompatTextView mRefreshTime;
    private RelativeLayout mHeader;
    public static final long ONE_DAY = 1000 * 60 * 24;
    public static final int STATE_REFRESHED = 2;//松开刷新
    public static final int STATE_REFRESH = 0;//下拉刷新
    public static final int STATE_REFRESHING = 1;//刷新中
    private long lastRefreshTime;
    private boolean flagIConX = false;
    private boolean flagIConY = true;
    private ProgressBar mProgress;

    public RelativeLayout getHeader() {
        return mHeader;
    }

    public MaterialHeaderView(@NonNull Context context) {
        super(context);
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.refresh_header, this, false);
        textView = view.findViewById(R.id.ptj_task_rv_header_tv);
        mHeader = view.findViewById(R.id.header);
        mIconRefresh = view.findViewById(R.id.icon_refresh);
        mRefreshTime = view.findViewById(R.id.refresh_time);
        mProgress = view.findViewById(R.id.progress);
        mProgress.setVisibility(INVISIBLE);
        lastRefreshTime = System.currentTimeMillis();
        addView(view);
    }

    public void Refreshing(float offsetY, MaterialRefreshLayout layout) {
        if (offsetY >= layout.getHeadHeight()) {
            setState(STATE_REFRESHED);
        } else {
            setState(STATE_REFRESH);
        }
    }

    public void setState(int state) {
        mProgress.setVisibility(INVISIBLE);
        mIconRefresh.setVisibility(VISIBLE);
        switch (state) {
            case STATE_REFRESH:
                lastRefreshTime();
                if (flagIConX) {
                    flagIConX = false;
                    flagIConY = true;
                    ObjectAnimator anim = ObjectAnimator.ofFloat(mIconRefresh, "rotation", 180f, 360f);
                    anim.setDuration(300);
                    anim.start();
                }
                textView.setText(R.string.ptjob_header_refresh);
                break;
            case STATE_REFRESHING:
                mIconRefresh.setVisibility(INVISIBLE);
                mProgress.setVisibility(VISIBLE);
                lastRefreshTime = System.currentTimeMillis();
                textView.setText(R.string.jloading_default_content);
                break;
            case STATE_REFRESHED:
                if (flagIConY) {
                    flagIConY = false;
                    flagIConX = true;
                    ObjectAnimator animator = ObjectAnimator.ofFloat(mIconRefresh, "rotation", 0f, 180f);
                    animator.setDuration(300);
                    animator.start();
                }
                textView.setText(R.string.ptjob_rv_release_to_refresh);
                break;
        }
    }

    private void lastRefreshTime() {
        long l = lastRefreshTime - System.currentTimeMillis();
        String format;
        if (l > ONE_DAY) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy MM dd  HH:mm", Locale.SIMPLIFIED_CHINESE);
            Date date = new Date(lastRefreshTime);
            format = dateFormat.format(date);
            format = "最后更新：" + format;
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.SIMPLIFIED_CHINESE);
            Date date = new Date(lastRefreshTime);
            format = dateFormat.format(date);
            format = "最后更新：今天 " + format;
        }
        mRefreshTime.setText(format);
    }

    /**
     * 刷新完成后重置箭头
     */
    public void ResetIcon() {
        flagIConX = false;
        flagIConY = true;
        ObjectAnimator anim = ObjectAnimator.ofFloat(mIconRefresh, "rotation", 180f, 360f);
        anim.setDuration(300);
        anim.start();
    }
}
