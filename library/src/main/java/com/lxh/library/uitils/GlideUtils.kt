package com.lxh.library.uitils

import android.content.Context
import android.graphics.Bitmap
import android.support.annotation.DrawableRes
import android.text.TextUtils
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.lxh.library.R
import java.io.File

/**
 * @author Created by lxh on 2018/3/21.
 */
object GlideUtils {

    fun onLowMemory(context: Context) {
        Glide.get(context).clearMemory()
    }

    fun onTrimMemory(context: Context, level: Int) {
        Glide.get(context).onTrimMemory(level)
    }

    /**
     * 使用默认占位图
     */
    fun displayImageSeat(view: ImageView, url: String?) {
        url?.let { it ->
            Glide.with(view)
                    .load(it)
                    .apply(RequestOptions.errorOf(R.mipmap.seat)
                            .placeholder(R.mipmap.seat))
                    .into(view)
        }
    }

    /**
     * 使用默认占位图
     */
    fun displayImageSeat(view: ImageView, file: File?) {
        file?.let { it ->
            Glide.with(view)
                    .load(it)
                    .apply(RequestOptions.errorOf(R.mipmap.seat)
                            .placeholder(R.mipmap.seat))
                    .into(view)
        }
    }

    /**
     * 使用默认占位图
     */
    fun displayImageSeat(view: ImageView, res: Int) {
        Glide.with(view)
                .load(res)
                .apply(RequestOptions.errorOf(R.mipmap.seat)
                        .placeholder(R.mipmap.seat))
                .into(view)
    }

    /**
     * 使用默认占位图
     */
    fun displayImageSeat(view: ImageView, file: Bitmap?) {
        file?.let { it ->
            Glide.with(view)
                    .load(it)
                    .apply(RequestOptions.errorOf(R.mipmap.seat)
                            .placeholder(R.mipmap.seat))
                    .into(view)
        }
    }

    /**
     * 不使用占位图
     */
    fun displayImage(view: ImageView, url: String?) {
        url?.let { it ->
            Glide.with(view)
                    .load(it)
                    .apply(RequestOptions.errorOf(R.mipmap.seat)
                            .placeholder(R.mipmap.seat).dontAnimate())
                    .into(view)
        }
    }

    fun clear(view: ImageView) {
        Glide.with(view)
                .clear(view)
    }

    fun displayCircleImage(view: ImageView, file: File) {
        Glide.with(view)
                .load(file)
                .apply(RequestOptions.circleCropTransform())
                .into(view)
    }

    fun displayCircleImage(view: ImageView,@DrawableRes file: Int) {
        Glide.with(view)
                .load(file)
                .apply(RequestOptions.circleCropTransform())
                .into(view)
    }
    fun displayCircleImage(view: ImageView?, url: String?) {
        if (view == null || TextUtils.isEmpty(url)) return
        Glide.with(view)
                .load(url)
                .apply(RequestOptions.circleCropTransform()
                        .placeholder(R.mipmap.avatar)
                        .error(R.mipmap.avatar))
                .into(view)
    }
}