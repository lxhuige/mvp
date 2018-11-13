package com.lxh.mvp

import android.content.Intent
import android.os.Bundle
import com.lxh.library.modular.imageChoose.ImageBucketChooseActivity
import com.lxh.library.modular.imageChoose.ImageChooseActivity
import com.lxh.library.uitils.ToastUtils
import com.lxh.mvp.base.BaseActivity
import com.lxh.mvp.base.PresenterBase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<PresenterBase>() {
    override fun initCreate(savedInstanceState: Bundle?) {
        initTitle("百度")
        toolbar.setNavigationOnClickListener {
            ToastUtils.showMessageCenter("点击了")
        }
    }

    override fun createPresenter(): PresenterBase? {
        return null
    }

    override val contentView = R.layout.activity_main

    override fun initListener() {
        showP.setOnClickListener {
            showLoading()
            startActivity(Intent(this, RecyclerActivity::class.java))
        }
    }

}
