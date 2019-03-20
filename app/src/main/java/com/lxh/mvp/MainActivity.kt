package com.lxh.mvp

import android.os.Bundle
import android.os.Environment
import android.view.View
import com.lxh.library.base.AppActivity
import com.lxh.library.base.NullPresenter
import com.lxh.library.inject.CheckLogin
import com.lxh.library.inject.CheckType
import com.lxh.library.inject.InjectCheckLogin
import com.lxh.library.log.CrashHandler
import com.lxh.library.log.UpLoadFile
import com.lxh.library.network.WebSocketManger
import com.lxh.library.uitils.LogUtils
import com.lxh.library.uitils.ToastUtils
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.jetbrains.anko.doAsync
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader

class MainActivity : AppActivity<NullPresenter>() {
    private val savePath = Environment.getExternalStorageDirectory().path + File.separator + "sjzsz.apk"
    override fun initCreate(savedInstanceState: Bundle?) {
        initTitle("百度")
        InjectCheckLogin.inject(getContext())
//        webSocketConnect()
    }

    override fun createPresenter(): NullPresenter? {
        return null
    }

    override fun initListener() {
        toolbar.setNavigationOnClickListener {
            App.dd()
//            LogUtils.e("开始了-----")
//            val url =
//                "https://imtt.dd.qq.com/16891/701363E5C20B2E9E911C9A43869B3E3A.apk?fsname=com.jkhh.nurse_4.5.10_92.apk&csr=1bbd"
//            UpdateManger.getInstance()
//                .setNotify(R.mipmap.ic_launcher, getContext())
//                .download(url, File(savePath), null)
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

//        startActivity(Intent(getContext(), RecyclerActivity::class.java))
//        127.0.0.1

        doAsync {
            val file = CrashHandler.getInstance().file
            val inputStream = FileInputStream(file)
            val reader = BufferedReader(InputStreamReader(inputStream))
            var readLine: String? = null
            readLine = reader.readLine()
            while (readLine != null) {
                LogUtils.e(readLine)
                readLine = reader.readLine()
            }
            UpLoadFile.getInstance().upLoadFile("http://192.168.73.15:8080/upload", file)
        }
//        showLoading()
    }

}
