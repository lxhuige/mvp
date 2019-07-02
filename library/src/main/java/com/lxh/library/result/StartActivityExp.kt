package com.lxh.library.result

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import org.jetbrains.anko.intentFor

/**
 * 启动Activity并接收Intent的扩展方法，不需要重写[#onActivityResult]方法
 * @receiver 基于[FragmentActivity]的扩展方法
 * @param params Array<out Pair<String, *>> 要携带的参数
 * @param options Bundle?   动画参数
 * @param callback (Intent) -> Unit 返回此界面时，当ResultCode为RESULT_OK时的回调
 * @return LambdaHolder<Intent> 可以在此对象上继续调用 [LambdaHolder#onCanceled]或
 *          [LambdaHolder#onDefined]方法来设置ResultCode为RESULT_CANCELED或RESULT_FIRST_USER时的回调
 */
inline fun <reified F : FragmentActivity> FragmentActivity.startActivityForResult(
    vararg params: Pair<String, *>,
    options: Bundle? = null,
    noinline callback: (Intent) -> Unit = {}): LambdaHolder<Intent> {
    return startActivityForResult(intentFor<F>(*params), options, callback)
}


/**
 * 启动Activity并接收Intent的扩展方法，不需要重写[#onActivityResult]方法
 * @receiver 基于[Fragment]的扩展方法
 * @param params Array<out Pair<String, *>> 要携带的参数
 * @param options Bundle?   动画参数
 * @param callback (Intent) -> Unit 返回此界面时，当ResultCode为RESULT_OK时的回调
 * @return LambdaHolder<Intent> 可以在此对象上继续调用 [#onCanceled]或[#onDefined]方法来
 *          设置ResultCode为RESULT_CANCELED或RESULT_FIRST_USER时的回调
 */
inline fun <reified F : FragmentActivity> Fragment.startActivityForResult(
    vararg params: Pair<String, *>,
    options: Bundle? = null,
    noinline callback: (Intent) -> Unit = {}): LambdaHolder<Intent> {
    return requireActivity().startActivityForResult(requireActivity().intentFor<F>(*params), options, callback)
}