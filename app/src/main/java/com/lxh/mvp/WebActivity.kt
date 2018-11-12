package com.lxh.mvp

import android.os.Bundle
import com.lxh.mvp.base.BaseActivity
import com.lxh.mvp.base.PresenterBase
import kotlinx.android.synthetic.main.activity_web.*

class WebActivity : BaseActivity<PresenterBase>() {
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

    override fun createPresenter(): PresenterBase? {
        return null
    }

    override val contentView = R.layout.activity_web
}
