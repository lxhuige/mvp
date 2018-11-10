package com.lxh.mvp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import com.lxh.library.uitils.ToastUtils
import com.lxh.mvp.base.BaseActivity
import com.lxh.mvp.base.PresenterBase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<PresenterBase>() {
    override fun initCreate(savedInstanceState: Bundle?) {

        setSupportActionBar(toobar)
        supportActionBar?.title = "我是标题"
        toobar.setNavigationOnClickListener {

        }
        val drawable = ContextCompat.getDrawable(getContext(), R.drawable.abc_ic_ab_back_material)
        drawable?.setBounds(0,0,toobar.height,toobar.height)
        toobar.navigationIcon = drawable
    }

    override fun createPresenter(): PresenterBase? {
        return null
    }

    override val contentView = R.layout.activity_main

    override fun initListener() {
        showP.setOnClickListener {
            showLoading()
            startActivity(Intent(this,WebActivity::class.java))
        }
    }

}
