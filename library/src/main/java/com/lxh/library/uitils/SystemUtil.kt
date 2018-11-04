package com.lxh.library.uitils

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.support.v7.widget.AppCompatTextView
import android.view.Gravity
import android.view.View
import com.lxh.library.AppManager


/**
 * @author Created by lxh on 2018/3/31.
 */
object SystemUtil {
    /**
     * 获取手机型号
     *
     * @return  手机型号
     */
    fun getSystemModel(): String {
        return android.os.Build.MODEL
    }

    /**
     * 获取手机
     *
     * @return  手机设备号
     */
    fun getEquipmentNo(ctx: Context): String? {
        return Settings.Secure.getString(ctx.contentResolver, Settings.Secure.ANDROID_ID)
    }

    fun startActivity(v: View, intent: Intent) {
        AppManager.currentActivity()?.let { activity ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ViewCompat.setTransitionName(v, "image")
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, v, "image")
                ActivityCompat.startActivity(activity, intent, options.toBundle())
            } else {
                activity.startActivity(intent)
            }
        }
    }

    fun setCompoundDrawables(textView: AppCompatTextView, res: Int, gravity: Int) {
        val context = textView.context
        val drawable = ContextCompat.getDrawable(context, res).apply {
            this?.let {
                setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            }
        }
        when (gravity) {
            Gravity.TOP -> textView.setCompoundDrawables(null, drawable, null, null)
            Gravity.BOTTOM -> textView.setCompoundDrawables(null, null, null, drawable)
            Gravity.LEFT -> textView.setCompoundDrawables(drawable, null, null, null)
            Gravity.RIGHT -> textView.setCompoundDrawables(null, null, drawable, null)
        }


    }

    // 设置屏幕透明度
    private fun backgroundAlpha(bgAlpha: Float) {
        AppManager.currentActivity()?.let { context ->
            val lp = context.window.attributes
            lp.alpha = bgAlpha // 0.0~1.0
            context.window.attributes = lp
        }
    }

    // 设置屏幕透明度
    private fun backgroundAlpha(bgAlpha: Float, activity: Activity) {
        val lp = activity.window.attributes
        lp.alpha = bgAlpha // 0.0~1.0
        activity.window.attributes = lp
    }

    fun popIn() {
        val animator = ValueAnimator.ofFloat(0f, 0.5f)
        animator.duration = 250
        animator.addUpdateListener { animation ->
            val value = animation.animatedValue
            if (value is Float) {
                SystemUtil.backgroundAlpha(1f - value)
            }
        }
        animator.start()
    }

    fun popOut() {
        val animator = ValueAnimator.ofFloat(0f, 0.5f)
        animator.duration = 250
        animator.addUpdateListener { animation ->
            val value = animation.animatedValue
            if (value is Float) {
                SystemUtil.backgroundAlpha(value + 0.5f)
            }
        }
        animator.start()
    }

    fun popOut(activity: Activity) {
        val animator = ValueAnimator.ofFloat(0f, 0.5f)
        animator.duration = 250
        animator.addUpdateListener { animation ->
            val value = animation.animatedValue
            if (value is Float) {
                if (activity != null)
                    SystemUtil.backgroundAlpha(value + 0.5f, activity)
            }
        }
        animator.start()
    }
}