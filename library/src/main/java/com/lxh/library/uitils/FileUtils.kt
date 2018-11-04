package com.lxh.library.uitils

import android.annotation.TargetApi
import android.app.Activity
import android.os.Build
import com.lxh.library.AppManager
import java.io.*

/**
 * 文件管理
 */
object FileUtils {


    fun <T> saveSer(t: T, fileName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //存入数据
            try {
                getFile(fileName)?.let { file ->
                    LogUtils.e("file", "file-----$file")
                    val fileOutputStream = FileOutputStream(file)
                    val objectOutputStream = ObjectOutputStream(fileOutputStream)
                    objectOutputStream.writeObject(t)
                    objectOutputStream.flush()
                    fileOutputStream.flush()
                    objectOutputStream.close()
                    fileOutputStream.close()
                    LogUtils.e("file", "保存成功了-----$fileName")
                }
            } catch (e: IOException) {
                LogUtils.e("file", "保存失败了-----" + e.message)
                e.printStackTrace()
            }
        }
    }

    fun <T> getSer(fileName: String): T? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                getFile(fileName)?.let { file ->
                    if (file.exists()) {
                        //取出数据
                        val fileInputStream = FileInputStream(file)
                        val objectInputStream = ObjectInputStream(fileInputStream)
                        fileInputStream.close()
                        objectInputStream.close()
                        return objectInputStream.readObject() as T
                    }
                    LogUtils.e("file-" + file.exists())
                }
            } catch (e: Exception) {
                LogUtils.e("file", "获取失败了-----" + e.message)
                e.printStackTrace()
            }
        }
        return null
    }

    /**
     * 删除app指定的缓存文件
     */
    fun clearFile(fileName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getFile(fileName)?.let { file ->
                if (file.exists()) {
                    file.delete()
                }
            }
        }
    }

    /**
     *
     * 获取文件不需要权限
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun getFile(fileName: String): File? {
        return getActivity()?.let {
            return File(it.codeCacheDir.absolutePath + File.separator + fileName + ".log")
        }
    }


    private fun getActivity(): Activity? {
        return AppManager.currentActivity()
    }
}