package com.lxh.library.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.widget.TextView;
import com.lxh.library.uitils.LogUtils;

import java.lang.reflect.Field;

public class LToolbar extends Toolbar {
    public LToolbar(Context context) {
        super(context);
    }

    public LToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        LogUtils.INSTANCE.e("changed--"+changed);
        if (changed) return;

        try {
            Field mTitleTextView = Toolbar.class.getDeclaredField("mTitleTextView");
            mTitleTextView.setAccessible(true);
            TextView textView = (TextView) mTitleTextView.get(this);
            int measuredHeight = getHeight()/2;
            int measuredWidth = getWidth()/2;
            int width = textView.getWidth()/2;
            int height = textView.getHeight()/2;
            textView.layout(measuredWidth-width,measuredHeight-height,measuredWidth+width,measuredHeight+height);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
