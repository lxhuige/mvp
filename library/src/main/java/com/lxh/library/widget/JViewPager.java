package com.lxh.library.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;


/**
 * This class is for customized view pager with drag paging disabled.
 * Created by s1823 on 2016/3/31.
 */
public class JViewPager extends ViewPager {

    private boolean isCanScroll = true;
    private int topScr = 0;

    public JViewPager(Context context) {
        super(context);
    }

    public JViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 设置其是否能滑动换页
     *
     * @param isCanScroll false 不能换页， true 可以滑动换页
     */
    public void setScanScroll(boolean isCanScroll) {
        this.isCanScroll = isCanScroll;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (!isCanScroll) {
            if (topScr==0||event.getY() <topScr) {
                return false;
            }
        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isCanScroll) {
            if (topScr==0||event.getY() < topScr) {
                return false;
            }
        }
        return super.onTouchEvent(event);
    }

    public void setTopScr(int topScr) {
        this.topScr = topScr;
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item, false);
    }
}
