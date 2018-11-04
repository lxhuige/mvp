package com.lxh.library.base

import android.content.Context
import android.content.Intent

interface BaseView {
    fun showLoading()

    fun loadingDismiss()

    fun getContext(): Context?

    fun startActivity(intent: Intent)
}