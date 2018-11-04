package com.lxh.library.materialRefresh;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.lxh.library.R;


/**
 * @author lixiaohui on 2017/7/5 0005.
 */

public class MaterialRefreshLayout extends FrameLayout {

    private final static int HIGHER_WAVE_HEIGHT = 180;
    private final static int hIGHER_HEAD_HEIGHT = 70;

    private DecelerateInterpolator decelerateInterpolator;
    private View mChildView;
    private MaterialFooterView mMaterialFooterView;
    private MaterialHeaderView mMaterialHeaderView;
    private boolean isRefreshing;
    /**
     * 是否需要上拉加载
     */
    private boolean isLoadMore;
    /**
     * 是否是否显示底部 默认不显示
     */
    private boolean isShowFooterView;
    private boolean isLoadMoreing;
    private float mTouchY;
    private float mCurrentY;
    protected float mWaveHeight;
    protected float mFooterHeight;
    protected float mHeadHeight;
    private int downY;//手指按下的X坐标
    private int headerBgColor;//背景颜色
    private MaterialRefreshListener refreshListener;

    public MaterialRefreshLayout(@NonNull Context context) {
        this(context, null);
    }

    public MaterialRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MaterialRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        initAttrs(attrs);
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.MaterialRefreshLayout, 0, 0);
        try {
            headerBgColor = typedArray.getColor(R.styleable.MaterialRefreshLayout_headerBgColor, ContextCompat.getColor(getContext(), R.color.main_bg));
        } finally {
            typedArray.recycle();
        }
    }

    public void setDownY(int downY) {
        this.downY = downY;
    }

    private void init() {
        downY = dip2px(135);
        if (isInEditMode()) {
            return;
        }
        if (getChildCount() > 1) {
            throw new RuntimeException("can only have one child widget");
        }
        decelerateInterpolator = new DecelerateInterpolator(10);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Context context = getContext();
        mChildView = getChildAt(0);
        if (mChildView == null) {
            return;
        }

        mWaveHeight = dip2px(HIGHER_WAVE_HEIGHT);
        mHeadHeight = dip2px(hIGHER_HEAD_HEIGHT);
        mFooterHeight = dip2px(hIGHER_HEAD_HEIGHT);

        mMaterialHeaderView = new MaterialHeaderView(context);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        layoutParams.gravity = Gravity.TOP;
        mMaterialHeaderView.setLayoutParams(layoutParams);
        addView(mMaterialHeaderView);
        mMaterialHeaderView.getHeader().setBackgroundColor(headerBgColor);


        mMaterialFooterView = new MaterialFooterView(getContext());
        LayoutParams layoutParams2 = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        layoutParams2.gravity = Gravity.BOTTOM;
        mMaterialFooterView.setLayoutParams(layoutParams2);
        mMaterialFooterView.setVisibility(GONE);
        addView(mMaterialFooterView);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isRefreshing || isLoadMoreing || ev.getY() < downY)
            return super.onInterceptTouchEvent(ev);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchY = ev.getY();
                mCurrentY = mTouchY;
                break;
            case MotionEvent.ACTION_MOVE:
                float currentY = ev.getY();
                float dy = currentY - mTouchY;
                if (dy > 0 && !canChildScrollUp()) {
                    if (mMaterialHeaderView != null) {
                        mMaterialHeaderView.setVisibility(View.VISIBLE);
                    }
                    return true;
                } else if (dy < 0 && !canChildScrollDown() && isLoadMore) {
                    if (mMaterialFooterView != null) {
                        mMaterialFooterView.setVisibility(View.VISIBLE);
                    }
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (isRefreshing || isLoadMoreing) {
            return super.onTouchEvent(e);
        }
        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                mCurrentY = e.getY();
                float dy = mCurrentY - mTouchY;
                if (mChildView != null) {
                    float offsetY;
                    if (mMaterialHeaderView != null && mMaterialHeaderView.getVisibility() == VISIBLE) {
                        dy = Math.min(mWaveHeight * 2, dy);
                        dy = Math.max(0, dy);
                        offsetY = decelerateInterpolator.getInterpolation(dy / mWaveHeight / 2) * dy / 2;
                        mMaterialHeaderView.getLayoutParams().height = (int) offsetY;
                        mMaterialHeaderView.requestLayout();
                        mMaterialHeaderView.Refreshing(offsetY, this);
                        mChildView.setTranslationY(offsetY);
                    } else if (dy < 0 && mMaterialFooterView != null && mMaterialFooterView.getVisibility() == VISIBLE) {
                        dy = Math.abs(dy);
                        dy = Math.min(mWaveHeight * 2, dy);
                        offsetY = decelerateInterpolator.getInterpolation(dy / mWaveHeight / 2) * dy / 2;
                        if (isShowFooterView && mFooterRootHeight > offsetY) {
                            offsetY = mFooterRootHeight;
                            mFooterRoot.setVisibility(GONE);
                        }
                        mMaterialFooterView.getLayoutParams().height = (int) offsetY;
                        mMaterialFooterView.requestLayout();
                        mMaterialFooterView.Refreshing(offsetY, this);
                        mChildView.setTranslationY(-offsetY);
                    }
                }
                return true;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (mChildView != null) {
                    if (mMaterialHeaderView != null && mMaterialHeaderView.getVisibility() == VISIBLE) {
                        if (mMaterialHeaderView.getLayoutParams().height >= mHeadHeight) {
                            updateListener();
                            mMaterialHeaderView.getLayoutParams().height = (int) mHeadHeight;
                            mMaterialHeaderView.requestLayout();
                            mChildView.setTranslationY(mHeadHeight);
                        } else {
                            finishRefreshing();
                        }
                    } else if (isLoadMore && mMaterialFooterView != null && mMaterialFooterView.getVisibility() == VISIBLE) {
                        if (mMaterialFooterView.getLayoutParams().height >= mFooterHeight) {
                            loadMoreListener();
                            mMaterialFooterView.getLayoutParams().height = (int) mFooterHeight;
                            mMaterialFooterView.requestLayout();
                            mChildView.setTranslationY(-mFooterHeight);
                        } else {
                            finishLoadMore();
                        }
                    }
                }
                return true;
        }
        return super.onTouchEvent(e);


    }

    public void updateListener() {
        isRefreshing = true;
        mMaterialHeaderView.setState(MaterialHeaderView.STATE_REFRESHING);
        if (refreshListener != null) {
            refreshListener.onRefresh(MaterialRefreshLayout.this);
        }
    }

    public void loadMoreListener() {
        isLoadMoreing = true;
        if (refreshListener != null) {
            refreshListener.onRefreshLoadMore(MaterialRefreshLayout.this);
        }
        mMaterialFooterView.setState(MaterialFooterView.STATE_REFRESHING);
    }

    public void finishLoadMore() {
        this.post(new Runnable() {
            @Override
            public void run() {
                isLoadMoreing = false;
                if (mChildView != null) {
                    ViewPropertyAnimatorCompat viewPropertyAnimatorCompat = ViewCompat.animate(mChildView);
                    viewPropertyAnimatorCompat.setDuration(200);
                    viewPropertyAnimatorCompat.y(mChildView.getTranslationY());
                    viewPropertyAnimatorCompat.translationY(0);
                    viewPropertyAnimatorCompat.setInterpolator(new DecelerateInterpolator());
                    viewPropertyAnimatorCompat.start();
                    int offsetY = 0;
                    if (isShowFooterView && null != mFooterRoot) {
                        offsetY = mFooterRootHeight;
                        mFooterRoot.setVisibility(VISIBLE);
                    }
                    mMaterialFooterView.setVisibility(GONE);
                    mMaterialFooterView.getLayoutParams().height = offsetY;
                    mMaterialFooterView.requestLayout();
                    mChildView.setTranslationY(-offsetY);
                    mMaterialFooterView.ResetIcon();
                }
            }
        });
    }

    /**
     * 自动刷新
     */
    public void autoRefresh() {
        updateListener();
        mMaterialHeaderView.getLayoutParams().height = (int) mHeadHeight;
        mMaterialHeaderView.requestLayout();
    }
    /**
     * 自动刷新
     */
    public void autoLoadMore() {
        loadMoreListener();
        mMaterialFooterView.getLayoutParams().height = (int) mHeadHeight;
        mMaterialFooterView.requestLayout();
        mMaterialFooterView.setVisibility(VISIBLE);
    }

    private void finishRefreshing() {
        if (mChildView != null) {
            ViewPropertyAnimatorCompat viewPropertyAnimatorCompat = ViewCompat.animate(mChildView);
            viewPropertyAnimatorCompat.setDuration(200);
            viewPropertyAnimatorCompat.y(mChildView.getTranslationY());
            viewPropertyAnimatorCompat.translationY(0);
            viewPropertyAnimatorCompat.setInterpolator(new DecelerateInterpolator());
            viewPropertyAnimatorCompat.start();
            mMaterialHeaderView.getLayoutParams().height = 0;
            mMaterialHeaderView.requestLayout();
            mChildView.setTranslationY(0);
            mMaterialHeaderView.setVisibility(GONE);
            mMaterialHeaderView.ResetIcon();
        }
        isRefreshing = false;
    }

    public void finishRefresh() {
        this.post(new Runnable() {
            @Override
            public void run() {
                finishRefreshing();
            }
        });
    }


    /**
     * @return Whether it is possible for the child view of this layout to
     * scroll up. Override this if the child view is a custom view.
     */
    public boolean canChildScrollUp() {
        return mChildView != null && mChildView.canScrollVertically(-1);
    }

    public boolean canChildScrollDown() {
        return mChildView != null && mChildView.canScrollVertically(1);
    }

    public void setRefreshListener(MaterialRefreshListener refreshListener) {
        this.refreshListener = refreshListener;
    }

    public int dip2px(float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, Resources.getSystem().getDisplayMetrics());
    }

    public float getWaveHeight() {
        return mWaveHeight;
    }

    public void setWaveHeight(float mWaveHeight) {
        this.mWaveHeight = mWaveHeight;
    }

    public float getHeadHeight() {
        return mHeadHeight;
    }

    public void setHeadHeight(float mHeadHeight) {
        this.mHeadHeight = mHeadHeight;
    }

    public float getFooterHeight() {
        return mFooterHeight;
    }

    public void setFooterHeight(float mFooterHeight) {
        this.mFooterHeight = mFooterHeight;
    }

    /**
     * 默认是不能上拉加载的
     *
     * @param loadMore true 还有更多 false 没有更多了
     */
    public void setLoadMore(boolean loadMore) {
        isLoadMore = loadMore;
        if (isShowFooterView && null != mFooterRoot) {
            mMaterialFooterView.setShowDefault(this);
            mMaterialFooterView.setVisibility(VISIBLE);
            mFooterRoot.setVisibility(View.VISIBLE);
            mFooterRootHeight = mFooterRoot.getMeasuredHeight();
            if (mFooterRootHeight == 0)
                mFooterRootHeight = mFooterRoot.getHeight();
            mMaterialFooterView.getLayoutParams().height = (int) getFooterHeight();
            mMaterialFooterView.requestLayout();
        }
    }

    public boolean getLoadMore() {
        return isLoadMore;
    }

    /**
     * 调用此方法前，需先调用setLoadMore（）否侧此方法无效
     *
     * @param showFooterView true 显示底部 false 隐藏底部
     */
    public void setShowFooterView(boolean showFooterView) {
        isShowFooterView = showFooterView;
    }

    private RelativeLayout mFooterRoot;
    private int mFooterRootHeight;

    public void setFooterRoot(RelativeLayout mFooterRoot) {
        this.mFooterRoot = mFooterRoot;
    }

    /**
     * true 可刷新 false 不可刷新
     *
     * @param refreshing 是否可刷新
     */
    public void setRefreshing(boolean refreshing) {
        isRefreshing = refreshing;
    }

    public boolean isLoadMoreing() {
        return isLoadMoreing;
    }
}