package com.lxh.mvp.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import com.lxh.library.base.AppFragment;
import com.lxh.library.base.Presenter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class BaseFragment<presenterLayer extends Presenter> extends AppFragment {

    public presenterLayer presenter;

    @Nullable
    public abstract presenterLayer createPresenter();

    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        presenter = createPresenter();
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != presenter) presenter.destroy();
    }

    @Override
    public void startActivity(@NotNull Intent intent) {
        FragmentActivity activity = getActivity();
        if (null != activity) {
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity);
            super.startActivity(intent, options.toBundle());
        } else {
            super.startActivity(intent);
        }

    }


}
