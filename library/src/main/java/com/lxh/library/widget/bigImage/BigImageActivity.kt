package com.lxh.library.widget.bigImage

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewCompat
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.lxh.library.R
import com.lxh.library.base.AppActivity
import com.lxh.library.base.NullPresenter
import kotlinx.android.synthetic.main.activity_big_image_activity.*
import java.io.File

class BigImageActivity : AppActivity<NullPresenter>() {
    override fun createPresenter(): NullPresenter? {
        return null
    }

    override val contentView = R.layout.activity_big_image_activity

    companion object {
        val imageList = "imageList"
        val imageStr = "imageStr"
        val clickPosition = "clickPosition"
    }

    private val imageList by lazy {
        arrayListOf<String>()
    }
    private var imageStr = ""
    private var clickPosition: Int = 0
    override fun initCreate(savedInstanceState: Bundle?) {
        val list = intent.getStringArrayListExtra("imageList")
        if (null != list && list.size > 0) {
            imageList.clear()
            imageList.addAll(list)
        }
        val str = intent.getStringExtra("imageStr")
        if (!TextUtils.isEmpty(str)) {
            imageStr = str
        }
        clickPosition = intent.getIntExtra("clickPosition", 0)
        initView()
        // 这里指定了被共享的视图元素
        ViewCompat.setTransitionName(viewPager, "image")
    }

    private fun initView() {
        viewPager.adapter = MyPagerAdapter()
        // 当图片数量大于1时
        if (imageList.size > 1) {
            viewPager.currentItem = clickPosition
            showLoading()
            tvBigImage.text = getString(R.string.bigImage_pos, (clickPosition + 1).toString(), imageList.size.toString())
            viewPager.setOnPageChangeListener(object : PinchImageViewPager.OnPageChangeListener {
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
                override fun onPageSelected(position: Int) {
                    val i = position + 1
                    tvBigImage.text = getString(R.string.bigImage_pos, i.toString(), imageList.size.toString())
                }

                override fun onPageScrollStateChanged(state: Int) {}
            })
        }
    }


    private inner class MyPagerAdapter : PagerAdapter() {
        override fun getCount(): Int {
            // 如果不是现实图片集合，则是显示一张图片
            return if (imageList.size == 0) 1 else imageList.size
        }

        override fun isViewFromObject(arg0: View, arg1: Any): Boolean {
            return arg0 === arg1
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val bigImage = PinchImageView(getContext())
            bigImage.setOnClickListener { onBackPressed() } // ViewPager单独页面设置监听
            bigImage.scaleType = ImageView.ScaleType.FIT_CENTER
            showLoading()
            // 如果图片集合size为0，且是显示一张图片
            if (imageList.size == 0 && !TextUtils.isEmpty(imageStr)) {
                loadImage(imageStr, bigImage)
            } else if (imageList.size > position) {
                loadImage(imageList[position], bigImage)
            }
            container.addView(bigImage)
            return bigImage
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }

        override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
            val bigImage = `object` as PinchImageView
            if (imageList.size == 0 && !TextUtils.isEmpty(imageStr)) {
                loadImage(imageStr, bigImage)
            } else if (imageList.size > position) {
                loadImage(imageList[position], bigImage)
            } else {
                finish()
                return
            }
            viewPager.setMainPinchImageView(bigImage)
        }


        private fun loadImage(imageStr: String, bigImage: ImageView) {
            val file = File(imageStr)
            if (file.exists()) {
                loadingDismiss()
                Glide.with(getContext())
                        .load(file)
                        .into(bigImage)
            } else {
                Glide.with(getContext())
                        .load(imageStr)
                        .listener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                                loadingDismiss()
                                return false
                            }

                            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                                loadingDismiss()
                                return false
                            }

                        })
                        .into(bigImage)
            }
        }
    }
}
