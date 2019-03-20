package com.lxh.library.base

class NullPresenter (view: BaseView) : BasePresenter<ModelBase,BaseView>(view) {
    override fun createModel(): ModelBase? {
        return null
    }
}