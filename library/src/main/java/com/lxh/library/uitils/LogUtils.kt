package com.lxh.library.uitils

import android.text.TextUtils
import android.util.Log
import com.lxh.library.BuildConfig

/**
 * 日志管理
 */
object LogUtils {

    private const val TAG = "LogUtils"
    // 下面四个是默认tag的函数
    fun i(msg: String) {
        i(TAG, msg)
    }

    fun d(msg: String) {
        d(TAG, msg)
    }

    fun e(msg: String) {
        e(TAG, msg)
    }

    fun v(msg: String) {
        v(TAG, msg)
    }

    // 下面是传入自定义tag的函数
    fun i(tag: String, msg: String) {
        if (BuildConfig.DEBUG && !TextUtils.isEmpty(msg))
            Log.i(tag, msg)
    }

    fun d(tag: String, msg: String) {
        if (BuildConfig.DEBUG && !TextUtils.isEmpty(msg))
            Log.d(tag, msg)
    }

    fun e(tag: String, msg: String) {
        if (BuildConfig.DEBUG && !TextUtils.isEmpty(msg))
            Log.e(tag, msg)
    }

    fun v(tag: String, msg: String) {
        if (BuildConfig.DEBUG)
            Log.v(tag, msg)
    }
}