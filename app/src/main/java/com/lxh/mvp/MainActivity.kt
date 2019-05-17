package com.lxh.mvp

import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import com.lxh.library.ApkUtils
import com.lxh.library.Constants
import com.lxh.library.PatchUtils
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
        initTitle("阿里")
        InjectCheckLogin.inject(getContext())
//        webSocketConnect()
    }

    override fun createPresenter(): NullPresenter? {
        return null
    }

    override fun initListener() {
        toolbar.setNavigationOnClickListener {
            Toast.makeText(it.context,"最新版本", Toast.LENGTH_LONG).show()
//            Thread(Runnable {
//                Log.e("TAG","开始合并了----")
//                //获取当前应用的apk文件/data/app/app
//                val oldfile = ApkUtils.getSourceApkPath(this@MainActivity, packageName)
//                //2.合并得到最新版本的APK文件
//                val newfile = Constants.NEW_APK_PATH
//                val patchfile = Constants.PATCH_FILE_PATH
//                PatchUtils.patch(oldfile, newfile, patchfile)
//                runOnUiThread {
//                    //7.0 以后安装需要权限了 这里只是测试没有适配
//                    Toast.makeText(it.context,"合并完成", Toast.LENGTH_LONG).show()
//                }
//            }).start()
        }
    }

    override val contentView = R.layout.activity_main

    private fun webSocketConnect() {
//        WebSocketManger
//            .getInstance()
//            .initUrl("ws://121.40.165.18:8800")
//            .webSocketConnect()
//            .addSocketListener(object : WebSocketListener() {
//                override fun onMessage(webSocket: WebSocket, text: String) {
//                    ToastUtils.showMessageCenter(text)
//                }
//            })
    }

    @CheckLogin(value = [R.id.showP], checkType = [CheckType.LOGIN, CheckType.UPDATE])
    fun toWeb(v: View) {

//        startActivity(Intent(getContext(), RecyclerActivity::class.java))
//        127.0.0.1
//
//        doAsync {
//            val file = CrashHandler.getInstance().file
//            val inputStream = FileInputStream(file)
//            val reader = BufferedReader(InputStreamReader(inputStream))
//            var readLine: String? = null
//            readLine = reader.readLine()
//            while (readLine != null) {
//                LogUtils.e(readLine)
//                readLine = reader.readLine()
//            }
//            UpLoadFile.getInstance().upLoadFile("http://192.168.73.15:8080/upload", file)
//        }
//        showLoading()
    }

}
