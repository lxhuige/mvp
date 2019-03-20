package com.lxh.library.widget.recyclerView;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;
import com.lxh.library.R;
import com.lxh.library.uitils.ToastUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * <p>
 * 如果子类itemViewType 有多个值即多布局 需要重写以下方法
 * {@link #getChildrenItemViewType(int)}
 * {@link #onCreateChildrenViewHolder(ViewGroup, int)}
 * 不能重写{@link #getViewResId()}
 * 只有getViewResId（）==0 以上两个方法才会执行
 * </p>
 *
 * <p>
 * 如果子类布局为单布局 有且仅需要重写{@link #getViewResId()}
 * 此时重写 {@link #getChildrenItemViewType(int)} {@link #onCreateChildrenViewHolder(ViewGroup, int)} 这两个方法是不会执行的
 * </p>
 *
 * <p>
 * 不可以重写{@link #getItemCount()} 否则有可能引起数组角标越界
 * </p>
 * <p>
 * 此类不适用{@link android.support.v7.widget.GridLayoutManager  } 添加头部或尾部
 * </p>
 *
 * @param <T>
 */
public abstract class SuperBaseAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static class VIEW_TYPE {
        private static final int HEADER = 0x0010;
        private static final int FOOTER = 0x0011;
    }

    private LinearLayoutCompat mHeaderLayout;
    private LinearLayoutCompat mFooterLayout;
    private List<T> mData;

    public SuperBaseAdapter() {
    }

    public SuperBaseAdapter(List<T> mData) {
        this.mData = mData;
    }

    public List<T> getmData() {
        return mData;
    }

    public void setData(List<T> mData) {
        this.mData = mData;
        notifyDataSetChanged();
    }

    public abstract void convertView( @NonNull RecyclerView.ViewHolder holder, @NonNull T item, int position);

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE.HEADER) {
            return new ViewHolder(mHeaderLayout);
        } else if (viewType == VIEW_TYPE.FOOTER) {
            return new ViewHolder(mFooterLayout);
        }
        if (getViewResId() == 0) return onCreateChildrenViewHolder(parent, viewType);
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(getViewResId(), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_TYPE.HEADER:
                // Do nothing
                break;
            case VIEW_TYPE.FOOTER:
                // Do nothing
                break;
            default:
                position = position - getHeaderViewCount();
                T item = getItem(position);
                if (item != null) convertView(holder, item, position);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position < getHeaderViewCount()) {
            return VIEW_TYPE.HEADER;
        } else if (position >= (mData == null ? 0 : mData.size()) + getHeaderViewCount()) {
            return VIEW_TYPE.FOOTER;
        } else if (getViewResId() == 0) {
            return getChildrenItemViewType(position - getHeaderViewCount());
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size() + getHeaderViewCount() + getFooterViewCount();
    }

    @Nullable
    public T getItem(int position) {
        if (mData == null) return null;
        return mData.get(position);
    }

    /**
     * 重写此方法后 必须不能重写{@link #getViewResId()}  因为只有getViewResId() == 0 才会执行此方法
     * <p>if (getViewResId() == 0) return onCreateChildrenViewHolder(parent, viewType);
     * return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(getViewResId(), parent, false));
     * <p>
     *
     * @return 默认实现返回空
     */
    public RecyclerView.ViewHolder onCreateChildrenViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    /**
     * 重写此方法后 必须不能重写{@link #onCreateChildrenViewHolder(ViewGroup, int)}
     * <p>if (getViewResId() == 0) return onCreateChildrenViewHolder(parent, viewType);
     * return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(getViewResId(), parent, false));
     * <p>
     *
     * @return ItemView 资源id
     */
    public int getViewResId() {
        return 0;
    }

    public int getChildrenItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public void addHeaderView(View header) {
        addHeaderView(header, -1);
    }

    public void addHeaderView(View header, int index) {
        if (mHeaderLayout == null) {
            mHeaderLayout = new LinearLayoutCompat(header.getContext());
            mHeaderLayout.setOrientation(LinearLayout.VERTICAL);
            mHeaderLayout.setLayoutParams(new RecyclerView.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        }
        index = index >= mHeaderLayout.getChildCount() ? -1 : index;
        mHeaderLayout.addView(header, index);
        notifyDataSetChanged();
    }

    public void addFooterView(View footer) {
        addFooterView(footer, -1);
    }

    public void addFooterView(View footer, int index) {
        if (mFooterLayout == null) {
            mFooterLayout = new LinearLayoutCompat(footer.getContext());
            mFooterLayout.setOrientation(LinearLayout.VERTICAL);
            mFooterLayout.setLayoutParams(new RecyclerView.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        }
        index = index >= mFooterLayout.getChildCount() ? -1 : index;
        mFooterLayout.addView(footer, index);
        this.notifyDataSetChanged();
    }

    public void removeHeaderView(View header) {
        if (mHeaderLayout == null) return;
        mHeaderLayout.removeView(header);
        if (mHeaderLayout.getChildCount() == 0) {
            mHeaderLayout = null;
        }
        this.notifyDataSetChanged();
    }

    public void removeFooterView(View footer) {
        if (mFooterLayout == null) return;
        mFooterLayout.removeView(footer);
        if (mFooterLayout.getChildCount() == 0) {
            mFooterLayout = null;
        }
        this.notifyDataSetChanged();
    }

    public void removeAllHeaderView() {
        if (mHeaderLayout == null) return;
        mHeaderLayout.removeAllViews();
        mHeaderLayout = null;
    }

    public void removeAllFooterView() {
        if (mFooterLayout == null) return;
        mFooterLayout.removeAllViews();
        mFooterLayout = null;
    }

    private int getHeaderViewCount() {
        return null == mHeaderLayout ? 0 : mHeaderLayout.getChildCount();
    }

    private int getFooterViewCount() {
        return null == mFooterLayout ? 0 : mFooterLayout.getChildCount();
    }

    //没有更多数据时显示的View
    private View noData;

    public boolean setShowNoDataFooter(boolean isShow, Context context) {
        if (isShow) {
            if (noData == null) {
                noData = LayoutInflater.from(context).inflate(R.layout.refresh_footer_no_data, null);
                addFooterView(noData, getFooterViewCount());
            }else {
                noData.setVisibility(View.VISIBLE);
            }
            ToastUtils.INSTANCE.showMessageCenter("没有更多了");
        } else {
            if (noData != null) {
                noData.setVisibility(View.GONE);
            }
        }
        return isShow;
    }

}
