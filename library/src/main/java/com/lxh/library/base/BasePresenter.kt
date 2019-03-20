package com.lxh.library.base

import android.content.Context
import java.lang.ref.SoftReference

abstract class BasePresenter<M : ModelBase, V : BaseView>(view: V) : Presenter {
    var mView: SoftReference<V>? = null

    init {
        mView = SoftReference(view)
    }
    private val mode: M? by lazy { this.createModel() }
    abstract fun createModel(): M?
    override fun destroy() {
        mView?.clear()
        mView = null
        getModel()?.destroy()
    }

    /**
     * 获取model
     */
    fun getModel(): M? {
        return mode
    }

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