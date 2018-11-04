package com.lxh.mvp.base;

import com.lxh.library.base.BasePresenter;
import com.lxh.library.base.BaseView;
import com.lxh.library.base.ModelBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class MPresenter<M extends ModelBase> extends BasePresenter {

    public MPresenter(@NotNull BaseView view) {
        super(view);
        mode = createModel();
    }

    public M mode;

    public abstract M createModel();


    @Nullable
    @Override
    public M getModel() {
        return mode;
    }
}
