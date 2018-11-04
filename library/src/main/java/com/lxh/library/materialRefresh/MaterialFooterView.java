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

import com.lxh.library.R;


/**
 * @author lixiaohui 2017/7/5 0005.
 */

public class MaterialFooterView extends FrameLayout {

    public static final int STATE_REFRESHED = 2;//松开刷新
    public static final int STATE_REFRESH = 0;//下拉刷新
    public static final int STATE_REFRESHING = 1;//刷新中
    private boolean flagIConX = false;
    private boolean flagIConY = true;
    private ProgressBar mProgress;
    private AppCompatImageView mIconRefresh;
    private AppCompatTextView mFooterTv;

    public MaterialFooterView(@NonNull Context context) {
        super(context);
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.refresh_footer, this, false);
        mIconRefresh = view.findViewById(R.id.icon_refresh);
        mFooterTv = view.findViewById(R.id.ptj_task_rv_header_tv);
        mProgress = view.findViewById(R.id.progress);
        mProgress.setVisibility(INVISIBLE);
        addView(view);
    }

    public void Refreshing(float offsetY, MaterialRefreshLayout layout) {
        if (offsetY >= layout.getFooterHeight()) {
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
                if (flagIConX) {
                    flagIConX = false;
                    flagIConY = true;
                    ObjectAnimator anim = ObjectAnimator.ofFloat(mIconRefresh, "rotation", 180f, 360f);
                    anim.setDuration(300);
                    anim.start();
                }
                mFooterTv.setText(R.string.ptjob_footer_load);
                break;
            case STATE_REFRESHING:
                mIconRefresh.setVisibility(INVISIBLE);
                mProgress.setVisibility(VISIBLE);
                mFooterTv.setText(R.string.jloading_default_content);
                break;
            case STATE_REFRESHED:
                if (flagIConY) {
                    flagIConY = false;
                    flagIConX = true;
                    ObjectAnimator animator = ObjectAnimator.ofFloat(mIconRefresh, "rotation", 0f, 180f);
                    animator.setDuration(300);
                    animator.start();
                }
                mFooterTv.setText(R.string.ptjob_rv_release_to_load);
                break;
        }
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
        mProgress.setVisibility(INVISIBLE);
    }

    public void setShowDefault(MaterialRefreshLayout layout) {
        if (layout.getLoadMore()) {
            mFooterTv.setText(R.string.ptjob_footer_load);
        } else {
            mFooterTv.setText("已经到最底啦");
        }

    }
}
