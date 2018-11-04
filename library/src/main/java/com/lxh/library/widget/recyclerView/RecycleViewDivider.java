package com.lxh.library.widget.recyclerView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

import com.lxh.library.R;


/**
 * @author Created by lxh on 2017/3/1.
 */

public class RecycleViewDivider extends RecyclerView.ItemDecoration {

    private Context mContext;
    private Drawable mDivider;
    private int mDividerHeight;//分割线高度，默认为1dp
    private int mOrientation;//列表的方向：LinearLayoutManager.VERTICAL或LinearLayoutManager.HORIZONTAL
    private int mLeft = 0;
    private int mRight = 0;

    /**
     * @param context     上下文
     * @param orientation 列表方向
     */
    public RecycleViewDivider(Context context, int orientation, int res) {
        if (orientation != LinearLayoutManager.VERTICAL && orientation != LinearLayoutManager.HORIZONTAL) {
            throw new IllegalArgumentException("请输入正确的参数！");
        }
        mContext = context;
        mDividerHeight = dpToPx(1);
        mOrientation = orientation;
        if (res == 0)
            mDivider = ContextCompat.getDrawable(context, R.drawable.line);
        else
            mDivider = ContextCompat.getDrawable(context, res);
    }

    /**
     * @param context 上下文
     */
    public RecycleViewDivider(Context context) {
        mContext = context;
        mDividerHeight = dpToPx(1);
        mOrientation = LinearLayoutManager.VERTICAL;
        mDivider = ContextCompat.getDrawable(context, R.drawable.line);
    }

    public void setDividerHeight(int mDividerHeight) {
        this.mDividerHeight = dpToPx(mDividerHeight);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(0, 0, 0, mDividerHeight);
    }

    //绘制分割线
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);

        if (mOrientation == LinearLayoutManager.VERTICAL) {
            drawHorizontal(c, parent);
        } else {
            drawVertical(c, parent);
        }
    }

    private void drawHorizontal(Canvas canvas, RecyclerView parent) {
        final int left = parent.getPaddingLeft() + dpToPx(mLeft);
        final int right = parent.getWidth() - parent.getPaddingRight() - dpToPx(mRight);
        final int childCount = parent.getChildCount();
        int index = 1;
        for (int i = 0; i < childCount; i++, index++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDividerHeight;
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(canvas);
        }
    }

    private void drawVertical(Canvas canvas, RecyclerView parent) {
        int top = parent.getPaddingTop();
        int bottom = parent.getMeasuredHeight() - parent.getPaddingBottom();
        int count = parent.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int left = child.getRight() + params.rightMargin;
            int right = left + mDividerHeight;
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(canvas);
        }
    }

    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, mContext.getResources().getDisplayMetrics());
    }

    public void setLeft(int mLeft) {
        this.mLeft = mLeft;
    }

    public void setRight(int mRight) {
        this.mRight = mRight;
    }
}
