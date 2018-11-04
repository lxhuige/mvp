package com.lxh.library.base

import android.content.Context

interface Presenter {
    fun destroy()
    fun loadingDismiss()
    fun showLoading()
    fun getContext(): Context?
}