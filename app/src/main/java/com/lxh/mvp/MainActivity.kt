package com.lxh.mvp

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.View
import com.lxh.library.inject.CheckLogin
import com.lxh.library.inject.CheckType
import com.lxh.library.inject.InjectCheckLogin
import com.lxh.library.network.UpdateManger
import com.lxh.library.network.WebSocketManger
import com.lxh.library.uitils.LogUtils
import com.lxh.library.uitils.ToastUtils
import com.lxh.mvp.base.BaseActivity
import com.lxh.mvp.base.PresenterBase
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.io.File

class MainActivity : BaseActivity<PresenterBase>() {
    private val savePath = Environment.getExternalStorageDirectory().path + File.separator + "sjzsz.apk"
    override fun initCreate(savedInstanceState: Bundle?) {
        initTitle("百度")
        InjectCheckLogin.inject(getContext())

//        webSocketConnect()
    }

    override fun createPresenter(): PresenterBase? {
        return null
    }

    override fun initListener() {
        toolbar.setNavigationOnClickListener {
            LogUtils.e("开始了-----")
            val url =
                "https://imtt.dd.qq.com/16891/701363E5C20B2E9E911C9A43869B3E3A.apk?fsname=com.jkhh.nurse_4.5.10_92.apk&csr=1bbd"
            UpdateManger.getInstance()
                .setNotify(R.mipmap.ic_launcher, getContext())
                .download(url, File(savePath), null)
        }
    }

    override val contentView = R.layout.activity_main

    private fun webSocketConnect() {
        WebSocketManger
            .getInstance()
            .initUrl("ws://121.40.165.18:8800")
            .webSocketConnect()
            .addSocketListener(object : WebSocketListener() {
                override fun onMessage(webSocket: WebSocket, text: String) {
                    ToastUtils.showMessageCenter(text)
                }
            })
    }

    @CheckLogin(value = [R.id.showP], checkType = [CheckType.LOGIN, CheckType.UPDATE])
    fun toWeb(v: View) {
        startActivity(Intent(getContext(), RecyclerActivity::class.java))
//        showLoading()
    }

}
