package com.lxh.library.base

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.annotation.DrawableRes
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import com.lxh.library.AppManager
import com.lxh.library.R
import com.lxh.library.widget.LToolbar
import com.lxh.library.widget.SeatView
import kotlinx.android.synthetic.main.activity_app.*
import kotlinx.android.synthetic.main.base_header.*

abstract class AppActivity : AppCompatActivity(), View.OnClickListener, BaseView {
    private val loading by lazy {
        val view = viewStubLoading.inflate()
        view.setOnClickListener { loadingDismiss() }
        return@lazy view
    }

    protected val seatView by lazy {
        val seat = SeatView()
        val view = viewStubSeat.inflate()
        seat.root = view.findViewById(R.id.seatRoot)
        seat.ivSeatIcon = view.findViewById(R.id.ivSeatIcon)
        seat.tvSeatBtn = view.findViewById(R.id.tvSeatBtn)
        seat.tvSeatHint = view.findViewById(R.id.tvSeatHint)
        return@lazy seat
    }
    protected val toolbar by lazy {
        val lToolbar = baseHeader.inflate() as LToolbar
        setSupportActionBar(lToolbar)
        return@lazy lToolbar
    }

    abstract val contentView: Int
    abstract fun initCreate(savedInstanceState: Bundle?)
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppManager.addActivity(this)
        setContentView(R.layout.activity_app)
        LayoutInflater.from(this).inflate(contentView, baseRoot, true)
        initCreate(savedInstanceState)
        initListener()
    }


    fun initTitle(resId: Int) {
        initTitle(getString(resId))
    }

    fun initTitle(title: String?) {
        supportActionBar?.title = title
        toolbar.setNavigationOnClickListener { finish() }
    }

    fun redirectTo(cls: Class<*>, close: Boolean) {
        val intent = Intent(getContext(), cls)
        startActivity(intent)
        if (close) {
            finish()
        }
    }

    fun getFrameLayoutRoot(): FrameLayout? {
        return baseRoot
    }

    /**
     * 获取状态栏高度——方法
     */
    private fun getStatusBarHeight(): Int {
        var statusBarHeight1 = -1
        //获取status_bar_height资源的ID
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight1 = resources.getDimensionPixelSize(resourceId)
        }
        return statusBarHeight1
    }

    protected fun setStatusBarColor(@DrawableRes colorRes: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) run {
            initStatusBar()
            val decorView = window.decorView
            val option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            decorView.systemUiVisibility = option
            window.statusBarColor = Color.TRANSPARENT
            statusBar.setBackgroundResource(colorRes)
        }
    }

    protected fun setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            val decorView = window.decorView
            val option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            decorView.systemUiVisibility = option
            window.statusBarColor = Color.TRANSPARENT
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4到5.0
            val localLayoutParams = window.attributes
            localLayoutParams.flags = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or localLayoutParams.flags
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//android6.0以后可以对状态栏文字颜色和图标进行修改
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    /**
     * 隐藏状态栏时调用此方法
     * 可以显示出状态栏（侧滑关闭时使用此方法）
     */
    private fun initStatusBar() {
        statusBar.layoutParams.height = getStatusBarHeight()
    }

    override fun onDestroy() {
        super.onDestroy()
        AppManager.removeActivity(this)
    }

    override fun finish() {
        super.finish()
        AppManager.removeActivity(this)
    }

    override fun getContext(): AppCompatActivity {
        return this
    }

    override fun showLoading() {
        handler.sendEmptyMessage(1)
    }


    private val handler by lazy {
        return@lazy @SuppressLint("HandlerLeak")
        object : Handler() {
            override fun handleMessage(msg: Message?) {
                when (msg?.what) {
                    1 -> loading.visibility = View.VISIBLE
                    2 -> loading.visibility = View.GONE
                }
            }
        }
    }

    override fun loadingDismiss() {
        handler.sendEmptyMessage(2)
    }

    override fun onClick(v: View) {

    }

    open fun initListener() {}
}
