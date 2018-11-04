package com.lxh.mvp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.lxh.mvp.base.BaseActivity
import com.lxh.mvp.base.PresenterBase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<PresenterBase>() {
    override fun initCreate(savedInstanceState: Bundle?) {
    }

    override fun createPresenter(): PresenterBase? {
        return null
    }

    override val contentView = R.layout.activity_main

    override fun initListener() {
        showP.setOnClickListener {
            showLoading()
        }
    }

}
