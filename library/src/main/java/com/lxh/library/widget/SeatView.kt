package com.lxh.library.widget

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.support.annotation.DrawableRes
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import android.view.View
import com.bumptech.glide.Glide

class SeatView {
    var root: ConstraintLayout? = null
    var tvSeatHint: AppCompatTextView? = null
    var ivSeatIcon: AppCompatImageView? = null
    var tvSeatBtn: AppCompatTextView? = null

    private var isShowIng = false
    private var isDismissIng = false

    fun setHintText(hint: String): SeatView {
        tvSeatHint?.text = hint
        return this
    }

    fun setSeatBtnText(text: String): SeatView {
        tvSeatBtn?.visibility = View.VISIBLE
        tvSeatBtn?.text = text
        return this
    }

    fun setSeatOnClickListener(listener: View.OnClickListener) {
        tvSeatBtn?.setOnClickListener(listener)
    }

    fun setSeatBtnBackGround(@DrawableRes res: Int): SeatView {
        tvSeatBtn?.setBackgroundResource(res)
        return this
    }

    fun setSeatIcon(@DrawableRes iconRes: Int) {
        ivSeatIcon?.let {
            Glide.with(it)
                .load(iconRes)
                .into(it)
        }
    }

    fun show() {
        animatorSet?.let {
            if (it.isRunning) it.cancel()
        }
        if (isShowIng || isDismissIng) return
        root?.let {
            it.visibility = View.VISIBLE
            val ofFloatX = ObjectAnimator.ofFloat(it, "scaleX", 0.1f, 1f)
            val ofFloatY = ObjectAnimator.ofFloat(it, "scaleY", 0.1f, 1f)
            val animatorSet = AnimatorSet()
            animatorSet.playTogether(ofFloatX, ofFloatY)
            animatorSet.duration = 1000
            animatorSet.addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    isShowIng = false
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationStart(animation: Animator?) {
                    isShowIng = true
                }
            })
            animatorSet.start()
        }
    }

    var animatorSet: AnimatorSet? = null

    fun dismiss() {
        if (isShowIng || isDismissIng) return
        root?.let {
            val ofFloatX = ObjectAnimator.ofFloat(it, "scaleX", 1f, 0.1f)
            val ofFloatY = ObjectAnimator.ofFloat(it, "scaleY", 1f, 0.1f)
            animatorSet = AnimatorSet()
            return@let animatorSet?.let { animatorSet ->
                animatorSet.playTogether(ofFloatX, ofFloatY)
                animatorSet.duration = 1000
                animatorSet.addListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(animation: Animator?) {
                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        isDismissIng = false
                        it.visibility = View.GONE
                    }

                    override fun onAnimationCancel(animation: Animator?) {
                    }

                    override fun onAnimationStart(animation: Animator?) {
                        isDismissIng = true
                    }
                })
                animatorSet.start()
            }

        }
    }
}