package com.lxh.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import com.lxh.library.R;
import com.lxh.library.uitils.LogUtils;

import java.lang.reflect.Field;

public class LToolbar extends Toolbar {

    private AppCompatTextView rightMenuTextView;
    private int rightMenuTextSize = 14;
    private int rightMenuTextColor;

    public LToolbar(Context context) {
        this(context, null);
    }

    public LToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LToolbar);
        rightMenuTextColor = typedArray.getColor(R.styleable.LToolbar_rightMenuTextColor, Color.WHITE);
        rightMenuTextSize = typedArray.getDimensionPixelOffset(R.styleable.LToolbar_rightMenuTextSize, 28);
        Drawable drawable = typedArray.getDrawable(R.styleable.LToolbar_rightMenuIcon);
        if (null != drawable) {
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            setRightMenuIcon(drawable);
        }
        String rightMenuText = typedArray.getString(R.styleable.LToolbar_rightMenuText);
        setRightMenu(rightMenuText);
        typedArray.recycle();
    }

    public LToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        LogUtils.INSTANCE.e("changed--" + changed);
        if (changed) return;
        //保证title在中间位置
        try {
            Field mTitleTextView = Toolbar.class.getDeclaredField("mTitleTextView");
            mTitleTextView.setAccessible(true);
            TextView textView = (TextView) mTitleTextView.get(this);
            int measuredHeight = getHeight() / 2;
            int measuredWidth = getWidth() / 2;
            int width = textView.getWidth() / 2;
            int height = textView.getHeight() / 2;
            textView.layout(measuredWidth - width, measuredHeight - height, measuredWidth + width, measuredHeight + height);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public LToolbar setRightMenu(String textContent) {
        if (TextUtils.isEmpty(textContent)) return this;
        this.ensureRightMenuView();
        rightMenuTextView.setText(textContent);
        return this;
    }

    /**
     * 此方法必须在 {@link #setRightMenu(String) }
     * 或者{@link #setRightMenuIcon(Drawable)}之后调用
     *
     * @param size 字体大小
     */
    public LToolbar setRightMenuTextSize(int size) {
        if (rightMenuTextView == null) throw new RuntimeException("哥们儿，还没设置menu呢");
        rightMenuTextView.setTextSize(size);
        return this;
    }

    /**
     * 此方法必须在 {@link #setRightMenu(String) }
     * 或者{@link #setRightMenuIcon(Drawable)}之后调用
     *
     * @param color 字体颜色
     */
    public LToolbar setRightMenuTextColor(@ColorInt int color) {
        if (rightMenuTextView == null) throw new RuntimeException("哥们儿，还没设置menu呢");
        rightMenuTextView.setTextColor(color);
        return this;
    }

    public LToolbar setRightMenuIcon(Drawable drawable) {
        this.ensureRightMenuView();
        rightMenuTextView.setCompoundDrawables(null, null, drawable, null);
        return this;
    }

    public AppCompatTextView getRightMenuTextView() {
        this.ensureRightMenuView();
        return rightMenuTextView;
    }

    /**
     * 此方法必须在 {@link #setRightMenu(String) }
     * 或者{@link #setRightMenuIcon(Drawable)}之后调用
     *
     * @param listener 回调接口
     */
    public LToolbar setRightMenuOnClick(View.OnClickListener listener) {
        if (rightMenuTextView == null) throw new RuntimeException("哥们儿，还没设置menu呢");
        rightMenuTextView.setOnClickListener(listener);
        return this;
    }

    private void ensureRightMenuView() {
        if (rightMenuTextView == null) {
            rightMenuTextView = new AppCompatTextView(getContext());
            rightMenuTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, rightMenuTextSize);
            rightMenuTextView.setTextColor(rightMenuTextColor);
            Toolbar.LayoutParams lp = this.generateDefaultLayoutParams();
            lp.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
            lp.height = LayoutParams.MATCH_PARENT;
            lp.width = LayoutParams.WRAP_CONTENT;
            rightMenuTextView.setLayoutParams(lp);
            rightMenuTextView.setGravity(Gravity.CENTER_VERTICAL);
            int offset = getContext().getResources().getDimensionPixelOffset(R.dimen.base10dp);
            rightMenuTextView.setPadding(0, 0, offset, 0);
            this.addView(rightMenuTextView);
        }
    }
}
