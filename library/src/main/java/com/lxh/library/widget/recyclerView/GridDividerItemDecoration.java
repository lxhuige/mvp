package com.lxh.library.widget.recyclerView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * @author Created by lxh on 2018/4/23.
 * 少于5列时 不画左右边界 2018/11/13
 */
public class GridDividerItemDecoration extends RecyclerView.ItemDecoration {
    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};
    private Drawable mDivider;
    private Paint mPaint;
    private int mDividerHeight = 2;

    public GridDividerItemDecoration(Context context) {
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
    }

    /**
     * 自定义分割线
     *
     * @param drawableId 分割线图片
     */
    public GridDividerItemDecoration(Context context, @DrawableRes int drawableId) {
        mDivider = ContextCompat.getDrawable(context, drawableId);
        if (mDivider != null)
            mDividerHeight = mDivider.getIntrinsicHeight();
    }

    /**
     * 自定义分割线
     *
     * @param dividerHeight 分割线高度
     * @param dividerColor  分割线颜色
     */
    public GridDividerItemDecoration(Context context, int dividerHeight, @ColorRes int dividerColor) {
        mDividerHeight = dividerHeight;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(context.getResources().getColor(dividerColor));
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        drawHorizontal(c, parent);
        drawVertical(c, parent);
    }

    private int getSpanCount(@NonNull RecyclerView parent) {
        // 列数
        int spanCount = -1;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            spanCount = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
        }
        return spanCount;
    }

    // 绘制水平线
    private void drawHorizontal(Canvas c, @NonNull RecyclerView parent) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            if (isLastRaw(parent, params.getViewLayoutPosition(), getSpanCount(parent), childCount)) {
                return;
            }
            final int left = child.getLeft() - params.leftMargin - mDividerHeight;
            final int right = child.getRight() + params.rightMargin + mDividerHeight;
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDividerHeight;
            if (mDivider != null) {
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
            if (mPaint != null) {
                c.drawRect(left, top, right, bottom, mPaint);
            }
        }
    }

    // 绘制垂直线
    private void drawVertical(Canvas c, @NonNull RecyclerView parent) {
        final int childCount = parent.getChildCount();
//        int j = childCount % getSpanCount(parent) == 0 ? 0 : 1;
//        for (int i = 0; i < childCount - j; i++) {
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int top = child.getTop() - params.topMargin;
            int bottom = child.getBottom() + params.bottomMargin;
            int left = child.getRight() + params.rightMargin;
            int right = left + mDividerHeight;
            if (mDivider != null) {
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
            if (mPaint != null) {
                c.drawRect(left, top, right, bottom, mPaint);
            }
            int spanCount = getSpanCount(parent);
            if (spanCount > 4 || !isFirst(parent, params.getViewLayoutPosition(), spanCount)) {
                top = child.getTop() - params.topMargin;
                bottom = child.getBottom() + params.bottomMargin;
                left = child.getLeft() - params.rightMargin - mDividerHeight;
                right = left + mDividerHeight;
                if (mDivider != null) {
                    mDivider.setBounds(left, top, right, bottom);
                    mDivider.draw(c);
                }
                if (mPaint != null) {
                    c.drawRect(left, top, right, bottom, mPaint);
                }
            }
        }
    }

    // 判断是否是最后一列
    private boolean isLastColumn(@NonNull RecyclerView parent, int pos, int spanCount, int childCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            int orientation = ((GridLayoutManager) layoutManager).getOrientation();
            if (orientation == GridLayoutManager.VERTICAL) {
                // 如果是最后一列，则不需要绘制右边
                return (pos + 1) % spanCount == 0;
            } else {
                childCount = childCount - childCount % spanCount;
                //如果是最后一列，则不需要绘制右边
                return pos >= childCount;
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager).getOrientation();
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                // 如果是最后一列，则不需要绘制右边
                return (pos + 1) % spanCount == 0;
            } else {
                childCount = childCount - childCount % spanCount;
                //如果是最后一列，则不需要绘制右边
                return pos >= childCount;
            }
        }
        return false;
    }

    // 判断是否是最后一行，最后一行需要占位不需要划线

    /**
     * @return false 是最后一行  true 不是最后一行
     */
    private boolean isLastRaw(@NonNull RecyclerView parent, int pos, int spanCount, int childCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            int orientation = ((GridLayoutManager) layoutManager).getOrientation();
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                int row = childCount % spanCount == 0 ? (childCount / spanCount) - 1 : childCount / spanCount;
                int count = childCount - row * spanCount;
                // 如果是最后一行，则不需要绘制底部
                return count >= childCount - pos;
            } else {
                // StaggeredGridLayoutManager 且横向滚动
                // 如果是最后一行，则不需要绘制底部
                return (pos + 1) % spanCount != 0;
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager).getOrientation();
            // StaggeredGridLayoutManager 且纵向滚动
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                int row = childCount % spanCount == 0 ? (childCount / spanCount) - 1 : childCount / spanCount;
                int count = childCount - row * spanCount;
                // 如果是最后一行，则不需要绘制底部
                return count >= childCount - pos;
            } else {
                // StaggeredGridLayoutManager 且横向滚动
                // 如果是最后一行，则不需要绘制底部
                return (pos + 1) % spanCount != 0;
            }
        }
        return true;
    }

    //是否第一列
    private boolean isFirst(@NonNull RecyclerView parent, int pos, int spanCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            int orientation = ((GridLayoutManager) layoutManager).getOrientation();
            if (orientation == GridLayoutManager.VERTICAL) {
                // 如果是第一列，则不需要绘制左边
                return (pos) % spanCount == 0;
            } else {
                //如果是第一列，则不需要绘制左边
                return pos < spanCount;
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager).getOrientation();
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                // 如果是第一列，则不需要绘制左边
                return (pos) % spanCount == 0;
            } else {
                //如果是第一列，则不需要绘制左边
                return pos < spanCount;
            }
        }
        return false;
    }


    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        if (parent.getAdapter() == null) return;
        int itemPosition = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        int spanCount = getSpanCount(parent);
        int childCount = parent.getAdapter().getItemCount();
        if (spanCount <= 3) {
            if (isFirst(parent, itemPosition, spanCount)) {
                outRect.set(0, 0, mDividerHeight * (spanCount - 1) / spanCount, mDividerHeight);
            } else if (isLastColumn(parent, itemPosition, spanCount, childCount)) {
                outRect.set(mDividerHeight * (spanCount - 1) / spanCount, 0, 0, mDividerHeight);
            } else {
                int left = mDividerHeight * (spanCount - 2) / spanCount;
                outRect.set(left, 0, left, mDividerHeight);
            }
            return;
        }
        if (spanCount == 4) {
            if (isFirst(parent, itemPosition, spanCount)) {
                outRect.set(0, 0, mDividerHeight * (spanCount - 1) / spanCount, mDividerHeight);
            } else if (isLastColumn(parent, itemPosition, spanCount, childCount)) {
                outRect.set(mDividerHeight * (spanCount - 1) / spanCount, 0, 0, mDividerHeight);
            } else if (itemPosition % spanCount == 1) {
                int left = mDividerHeight * (spanCount - 3) / spanCount;
                int right = mDividerHeight * (spanCount - 2) / spanCount;
                outRect.set(left, 0, right, mDividerHeight);
            } else if (itemPosition % spanCount == 2) {
                int left = mDividerHeight * (spanCount - 2) / spanCount;
                int right = mDividerHeight * (spanCount - 3) / spanCount;
                outRect.set(left, 0, right, mDividerHeight);
            }
            return;
        }
        //如果spanCount 列数大于4 那么左右都需要画1/2的宽度
        if (isLastColumn(parent, itemPosition, spanCount, childCount)) {
            outRect.set(mDividerHeight / 2, 0, mDividerHeight / 2, mDividerHeight);
        } else {
            outRect.set(mDividerHeight / 2, 0, mDividerHeight / 2, mDividerHeight);
        }
    }
}
