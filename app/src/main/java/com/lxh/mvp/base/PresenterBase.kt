package com.lxh.mvp.base

import com.lxh.library.base.BasePresenter
import com.lxh.library.base.BaseView
import com.lxh.library.base.ModelBase

class PresenterBase (view: BaseView): BasePresenter<BaseView>(view) {
    override fun <M : ModelBase> getModel(): M? {
        return null
    }
}