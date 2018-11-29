package com.lxh.mvp

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import com.lxh.library.widget.recyclerView.GridDividerItemDecoration
import com.lxh.library.widget.recyclerView.SuperBaseAdapter
import com.lxh.mvp.base.BaseActivity
import com.lxh.mvp.base.PresenterBase
import kotlinx.android.synthetic.main.activity_recycler.*
import kotlinx.android.synthetic.main.item_re.view.*

class RecyclerActivity : BaseActivity<PresenterBase>() {
    private val adapter by lazy {
        object : SuperBaseAdapter<String>() {
            override fun convertView(holder: RecyclerView.ViewHolder, item: String?, position: Int) {
                holder.itemView.rrr.text = item
                holder.itemView.setOnClickListener {
                  addHeaderView(LayoutInflater.from(getContext()).inflate(R.layout.base_header, recyclerView,false))
                  addFooterView(LayoutInflater.from(getContext()).inflate(R.layout.base_header, recyclerView,false))
                }
            }
            override fun getViewResId() = R.layout.item_re
        }.apply {
            setData(arrayListOf("ss", "dd", "ff", "gg"))
        }
    }

    override fun initCreate(savedInstanceState: Bundle?) {
        initTitle("阿里")
        recyclerView.layoutManager = GridLayoutManager(getContext(), 3)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(GridDividerItemDecoration(getContext(), 30, R.color.cardview_dark_background))
    }

    override fun createPresenter(): PresenterBase? {
        return null
    }

    override val contentView = R.layout.activity_recycler

}
