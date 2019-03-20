package com.lxh.mvp

import android.os.Bundle
import com.lxh.library.base.AppActivity
import com.lxh.library.base.NullPresenter
import kotlinx.android.synthetic.main.activity_web.*

class WebActivity : AppActivity<NullPresenter>() {
    override fun initCreate(savedInstanceState: Bundle?) {
        initTitle("百度")
        showLoading()
        webView.init()
        webView.setPosListener { pos ->
            if (pos >= 100) {
                loadingDismiss()
            }
        }
        webView.loadUrl("http://www.baidu.com")
    }

    override fun createPresenter(): NullPresenter? {
        return null
    }

    override val contentView = R.layout.activity_web
}
