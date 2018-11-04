package com.lxh.library.uitils

import android.content.Context

/**
 * @author Created by lxh on 2018/3/15.
 */

object DensityUtil {

    fun dpToPx(dpValue: Int, context: Context): Int {
        val scale = getDensity(context)
        return (dpValue * scale).toInt()
    }

    /**
     * @param context context
     * @return 屏幕宽度
     */
    fun getWidthPixels(context: Context): Int {
        return context.resources.displayMetrics.widthPixels
    }

    /**
     * @param context context
     * @return 屏幕高度
     */
    fun getHeightPixels(context: Context): Int {
        return context.resources.displayMetrics.heightPixels
    }


    /**
     * 获取手机的密度
     */
    fun getDensity(context: Context): Float {
        return context.resources.displayMetrics.density + 0.5f
    }
}
