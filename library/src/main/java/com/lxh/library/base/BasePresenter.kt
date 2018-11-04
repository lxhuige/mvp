package com.lxh.library.base

import android.content.Context
import java.lang.ref.SoftReference

 abstract class BasePresenter<V : BaseView>(view: V) : Presenter {
    var mView: SoftReference<V>? = null

    init {
        mView = SoftReference(view)
    }

    override fun destroy() {
        mView?.clear()
        mView = null
        getModel<ModelBase>()?.destroy()
    }

    /**
     * 获取model
     */
    abstract fun <M : ModelBase> getModel(): M?

    override fun loadingDismiss() {
        mView?.get()?.loadingDismiss()
    }

    override fun showLoading() {
        mView?.get()?.showLoading()
    }

    override fun getContext(): Context? {
        return mView?.get()?.getContext()
    }

}