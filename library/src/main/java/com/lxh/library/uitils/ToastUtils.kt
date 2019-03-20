package com.lxh.library.uitils

import android.support.annotation.StringRes
import android.view.Gravity
import android.widget.Toast
import com.lxh.library.AppManager
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 *@author Created by lxh on 2018/1/15 0015.
 */
object ToastUtils {

    private var oldMsg: String? = null
     var toast: Toast? = null
    private var oneTime: Long = 0
    private var twoTime: Long = 0

    /**
     * Show toast with specified string resource.
     * @param strId the resource ID of the content.
     */
    fun showMessageCenter(@StringRes strId: Int) {
        val msg = AppManager.currentActivity().resources?.getString(strId)
        showMessageCenter(msg)
    }

    fun showMessageCenter(str: String?) {
        doAsync {
            uiThread {
                if (toast == null) {
                    toast = Toast.makeText(AppManager.currentActivity(), str, Toast.LENGTH_SHORT)
                    toast!!.setGravity(Gravity.CENTER, 0, 0)
                    toast!!.show()
                    oneTime = System.currentTimeMillis()
                } else {
                    twoTime = System.currentTimeMillis()
                    if (str == oldMsg) {
                        if (twoTime - oneTime > Toast.LENGTH_SHORT) {
                            toast!!.show()
                        }
                    } else {
                        oldMsg = str
                        toast!!.setText(str)
                        toast!!.show()
                    }
                }
                oneTime = twoTime
            }
        }
    }

    fun showMessageDefault(str: String){
        doAsync {
            uiThread {
                if (toast == null) {
                    toast = Toast.makeText(AppManager.currentActivity(), str, Toast.LENGTH_SHORT)
                    toast!!.show()
                    oneTime = System.currentTimeMillis()
                } else {
                    twoTime = System.currentTimeMillis()
                    if (str == oldMsg) {
                        if (twoTime - oneTime > Toast.LENGTH_SHORT) {
                            toast!!.show()
                        }
                    } else {
                        oldMsg = str
                        toast!!.setText(str)
                        toast!!.show()
                    }
                }
                oneTime = twoTime
            }
        }

    }
}