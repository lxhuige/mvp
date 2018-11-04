package com.lxh.library.base

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.lxh.library.R
import com.lxh.library.widget.SeatView
import kotlinx.android.synthetic.main.base_fragment.view.*

abstract class AppFragment : Fragment() {
    abstract fun initCreate(view: View, savedInstanceState: Bundle?)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initCreate(view, savedInstanceState)
        initListener()
    }

    open fun contentView(): Int {
        return 0
    }

    protected val seatView by lazy {
        val seat = SeatView()
        if (seat.root == null) {
            val view = baseRootView.viewStubSeat.inflate()
            seat.root = view.findViewById(R.id.seatRoot)
            seat.ivSeatIcon = view.findViewById(R.id.ivSeatIcon)
            seat.tvSeatBtn = view.findViewById(R.id.tvSeatBtn)
            seat.tvSeatHint = view.findViewById(R.id.tvSeatHint)
        }
        return@lazy seat
    }

    private lateinit var baseRootView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (contentView() == 0) return super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.base_fragment, container, false)
        inflater.inflate(contentView(), view.baseRoot as FrameLayout)
        baseRootView = view
        return baseRootView
    }

    open fun initListener() {}

    fun showLoading() {
        activity?.let { activity ->
            if (activity is AppActivity) {
                activity.showLoading()
            }
        }
    }

    open fun loadingDismiss() {
        activity?.let { activity ->
            if (activity is AppActivity) {
                activity.loadingDismiss()
            }
        }
    }

    fun redirectTo(cls: Class<*>, close: Boolean) {
        activity?.let { activity ->
            if (activity is AppActivity) {
                activity.redirectTo(cls, close)
            }
        }
    }
}