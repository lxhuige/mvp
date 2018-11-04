package com.lxh.library.materialRefresh;

/**
 * @author lixiaohui on 2017/7/5 0005.
 */

public abstract class MaterialRefreshListener {
    public abstract void onRefresh(MaterialRefreshLayout materialRefreshLayout);
    public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout){};
}
