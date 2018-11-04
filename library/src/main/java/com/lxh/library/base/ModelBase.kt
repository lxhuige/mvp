package com.lxh.library.base

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.lxh.library.R
import com.lxh.library.network.Subscribe
import com.lxh.library.uitils.ToastUtils
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class ModelBase(private val presenter: Presenter?):IMode {

    private val observers by lazy {
        CompositeDisposable()
    }

    private val gson by lazy {
        GsonBuilder().create()
    }

    /**
     * 是不是gson数据 ，还有服务端返回错误拦截，统一处理
     */
    abstract fun isGson(dada: String, gson: Gson): Boolean

    fun newObserver(sub: Subscribe): Observer<String> {
        return object : Observer<String> {
            override fun onComplete() {
                presenter?.loadingDismiss()
                sub.onComplete()
            }

            override fun onSubscribe(d: Disposable) {
                observers.add(d)
            }

            override fun onNext(data: String) {
                when {
                    data.isEmpty() -> {
                        ToastUtils.showMessageCenter(R.string.common_server_error)
                        sub.fail(data, gson)
                    }
                    isGson(data, gson) -> sub.onReceive(data, gson)
                    else -> sub.fail(data, gson)
                }
            }

            override fun onError(e: Throwable) {
                onComplete()
                sub.fail(null, gson)
            }
        }

    }


    /**
     * 释放资源
     */
    override fun destroy() {
        observers.dispose()
    }

    open fun getContext(): Context? {
        return presenter?.getContext()
    }
}