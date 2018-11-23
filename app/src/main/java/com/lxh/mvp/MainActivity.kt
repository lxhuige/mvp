package com.lxh.mvp

import android.app.ProgressDialog
import android.os.Bundle
import android.os.Environment
import android.view.View
import com.lxh.library.network.DownloadManger
import com.lxh.library.inject.CheckLogin
import com.lxh.library.inject.CheckType
import com.lxh.library.inject.InjectCheckLogin
import com.lxh.library.network.WebSocketManger
import com.lxh.library.uitils.LogUtils
import com.lxh.library.uitils.ToastUtils
import com.lxh.mvp.base.BaseActivity
import com.lxh.mvp.base.PresenterBase
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.jetbrains.anko.toast
import java.io.File

class MainActivity : BaseActivity<PresenterBase>() {
    private val savePath = Environment.getExternalStorageDirectory().path + File.separator + "sjzsz.apk"
    private var progressDialog: ProgressDialog? = null
    override fun initCreate(savedInstanceState: Bundle?) {
        initTitle("百度")
        InjectCheckLogin.inject(getContext())
        progressDialog = ProgressDialog(this)
        progressDialog?.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        progressDialog?.setMessage("亲，努力下载中...")
        progressDialog?.setProgressNumberFormat("%1d Mb/%2d Mb")
        toolbar.setNavigationOnClickListener {
            LogUtils.e("开始了-----")
            val url =
                "https://imtt.dd.qq.com/16891/701363E5C20B2E9E911C9A43869B3E3A.apk?fsname=com.jkhh.nurse_4.5.10_92.apk&csr=1bbd"
            DownloadManger.download(url, File(savePath), object : DownloadManger.CallBack {
                override fun doingLoad(fileTotal: Long, current: Long) {
                    LogUtils.e("fileTotal--$fileTotal   current--$current")
                    if (!progressDialog!!.isShowing) progressDialog!!.show()
                    progressDialog?.max = (fileTotal / 1024 / 1024).toInt()
                    progressDialog?.progress = (current / 1024 / 1024).toInt()
                    progressDialog?.setCancelable(false)
                }

                override fun success(file: File?) {
                    supportActionBar?.title = "下载成功"
                }

                override fun fail(e: Exception?) {
                    supportActionBar?.title = "下载失败"
                }
            })
            LogUtils.e("结束了-----")
        }
        webSocketConnect()
    }

    override fun createPresenter(): PresenterBase? {
        return null
    }

    override val contentView = R.layout.activity_main

    override fun initListener() {
//        showP.setOnClickListener {
//            toWeb()
//        }
    }
    private fun webSocketConnect() {
        WebSocketManger
            .getInstance()
            .initUrl("ws://121.40.165.18:8800")
            .webSocketConnect()
            .addSocketListener(object :WebSocketListener(){
                override fun onMessage(webSocket: WebSocket, text: String) {
                    ToastUtils.showMessageCenter(text)
                }
            })
    }

    @CheckLogin(value = [R.id.showP], checkType = [CheckType.LOGIN, CheckType.UPDATE])
    fun toWeb(v: View) {
        showLoading()
    }

}
