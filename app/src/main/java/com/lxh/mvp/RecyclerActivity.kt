package com.lxh.mvp

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.lxh.library.widget.recyclerView.GridDividerItemDecoration
import com.lxh.mvp.base.BaseActivity
import com.lxh.mvp.base.PresenterBase
import com.lxh.mvp.base.ViewHolder
import kotlinx.android.synthetic.main.activity_recycler.*

class RecyclerActivity : BaseActivity<PresenterBase>() {
    override fun initCreate(savedInstanceState: Bundle?) {
        initTitle("阿里")
        recyclerView.layoutManager = GridLayoutManager(getContext(), 3)
        recyclerView.adapter = Adapter()
        recyclerView.addItemDecoration(GridDividerItemDecoration(getContext(),30,R.color.cardview_dark_background))
    }
    override fun createPresenter(): PresenterBase? {
        return null
    }
    override val contentView = R.layout.activity_recycler

    class Adapter : RecyclerView.Adapter<ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val inflate = LayoutInflater.from(parent.context).inflate(R.layout.item_re, parent, false)
            return ViewHolder(inflate)
        }

        override fun getItemCount() = 10

        override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        }

    }
}
