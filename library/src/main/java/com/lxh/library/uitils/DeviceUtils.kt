package com.lxh.library.uitils

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings

object DeviceUtils {

    /**
     * 获取设备唯一识别码 SDK版本大于等26时需要权限
     * https://blog.csdn.net/sunsteam/article/details/73189268
     */
    @SuppressLint( "MissingPermission", "HardwareIds")
    fun getUniqeId(context :Context):String{
        val androidID = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            return androidID+ Build.getSerial()
        }else{
            return androidID + Build.SERIAL
        }
    }

}