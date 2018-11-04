package com.lxh.library.uitils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import java.io.File


object PermissionUtil {

    /**
     * 用于拍照使用 文件读写
     *
     * @param activity 当前activity
     * @param permissionCode  申请权限code
     * @return true 为需要获取权限 false 已经获取到权限
     */
    fun checkPermission(activity: Activity, permissionCode: Int): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return false
        var isApplyPermission = false
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
         || ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE), permissionCode)
            isApplyPermission = true
        }
        return isApplyPermission
    }

    /**
     * @param activity 当前activity
     * @param permissionCode  申请权限code
     * @param permission 要检查的权限
     * @return true 为获取权限 false 已经获取到权限
     *
     */
    fun checkPermission(activity: Activity, permissionCode: Int, vararg permission: String): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return false
        var isApplyPermission = false
        permission.forEach { item ->
            if (ContextCompat.checkSelfPermission(activity, item) != PackageManager.PERMISSION_GRANTED) {
                isApplyPermission = true
            }
        }
        if (isApplyPermission)
            ActivityCompat.requestPermissions(activity, permission, permissionCode)
        return isApplyPermission
    }

    /**
     * 系统版本高于24 时需要uri 需要申请
     */
    fun getUri(ur: Uri, activity: Activity): Uri {
        return if (Build.VERSION.SDK_INT >= 24) {
            FileProvider.getUriForFile(activity, activity.applicationContext.packageName + ".provider", File(ur.path))
        } else
            ur
    }
}