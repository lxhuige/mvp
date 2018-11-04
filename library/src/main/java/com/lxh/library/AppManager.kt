package com.lxh.library

import android.app.Activity
import java.util.*

object AppManager {

    private val activityStack: Vector<Activity> by lazy {
        Vector<Activity>()
    }

    /**
     * Add activity to vector.
     */
    fun addActivity(activity: Activity) {
        if (!contains(activity)) {
            activityStack.addElement(activity)
        }
    }

    private fun contains(act: Activity): Boolean {
        return activityStack.contains(act)
    }

    /**
     * Get current activity (the last one pushed into vector).
     */
    fun currentActivity(): Activity? {
        return if (activityStack.size > 0) {
            activityStack.lastElement()
        } else {
            null
        }
    }

    fun removeActivity(activity: Activity) {
        activityStack.removeElement(activity)
    }

    private fun finishActivity(activity: Activity?) {
        if (null != activity && !activity.isFinishing) {
            activity.finish()
            removeActivity(activity)
        }
    }

    /**
     * Finish all the activities.
     */
    fun finishAllActivity() {
        if (activityStack.size > 0) {
            var activity: Activity?
            val list = ArrayList(activityStack)
            val size = list.size
            for (i in size - 1 downTo 0) {
                activity = list[i]
                if (null != activity && !activity.isFinishing) {
                    activity.finish()
                }
            }
            activityStack.clear()
        }
    }

    /**
     * Finish all the activities. 除activityClass 以外
     */
    fun finishAllActivity(activityClass: Activity) {
        if (activityStack.size > 0) {
            val list = ArrayList(activityStack)
            val size = list.size
            for (i in size - 1 downTo 0) {
                val activity = list[i]
                if (null != activity && !activity.isFinishing && activity != activityClass) {
                    activity.finish()
                    activityStack.remove(activity)
                }
            }
        }
    }

    /**
     * 得到指定activity前一个activity的实例
     *
     * @param curActivity 当前activity
     * @return 可能为null
     */
    fun getPreviousActivity(curActivity: Activity): Activity? {
        val activities = activityStack
        var preActivity: Activity? = null
        for (cur in activities.indices.reversed()) {
            val activity = activities[cur]
            if (activity === curActivity) {
                val pre = cur - 1
                if (pre >= 0) {
                    preActivity = activities[pre]
                }
            }
        }

        return preActivity
    }

    /**
     * Get the activity object matching the class name.
     */
    fun getActivity(cls: Class<*>): Activity? {
        for (activity in activityStack) {
            if (activity.javaClass == cls) {
                return activity
            }
        }
        return null
    }
}