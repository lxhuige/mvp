package com.lxh.mvp.base;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.transition.Fade;
import android.view.animation.AccelerateInterpolator;
import com.lxh.library.base.AppActivity;
import com.lxh.library.base.Presenter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class BaseActivity<presenterLayer extends Presenter> extends AppActivity {
    public presenterLayer presenter;

    @Nullable
    public abstract presenterLayer createPresenter();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().setEnterTransition(new Explode().setDuration(300).setInterpolator(new AccelerateInterpolator()));
//            getWindow().setExitTransition(new Explode().setDuration(300).setInterpolator(new AccelerateInterpolator()));
//            getWindow().setEnterTransition(new Slide().setDuration(200).setInterpolator(new AccelerateInterpolator()));
//            getWindow().setExitTransition(new Slide().setDuration(200).setInterpolator(new AccelerateInterpolator()));
            getWindow().setEnterTransition(new Fade().setDuration(300).setInterpolator(new AccelerateInterpolator()));
            getWindow().setExitTransition(new Fade().setDuration(300).setInterpolator(new AccelerateInterpolator()));
        }
        presenter = createPresenter();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        if (null != presenter) presenter.destroy();
        super.onDestroy();
    }



    @Override
    public void startActivity(@NotNull Intent intent) {
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this);
        super.startActivity(intent, options.toBundle());
    }


}
