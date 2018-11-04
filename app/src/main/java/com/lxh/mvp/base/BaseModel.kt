package com.lxh.mvp.base

import com.google.gson.Gson
import com.lxh.library.base.ModelBase
import com.lxh.library.base.Presenter
import com.lxh.library.uitils.LogUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

abstract class BaseModel(presenter: Presenter?) : ModelBase(presenter) {
    override fun isGson(dada: String, gson: Gson): Boolean {
        try {
            LogUtils.e(dada)
//            val bean = gson.fromJson(dada, BaseBean::class.java)
//            when {
//                bean.status == 1001 -> return true
//
//                bean.status == 1010 -> {
//                    val activity = AppManager.getActivity(MainActivity::class.java)
//                    activity?.let {
//                        if (it is MainActivity){
//                            it.showLoginPop()
//                        }
//                        ToastUtils.showMessageCenter("请从新登录")
//                    }
//                }
//                else -> ToastUtils.showMessageCenter(bean.message)
//            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }


//    fun getRetrofit(): Services {
//        return NetWorks.getHostRetrofit().create(Services::class.java)
//    }

    fun setRetrofit(observable: Observable<String>): Observable<String> {
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}