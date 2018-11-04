package com.lxh.library.widget.recyclerView;

import android.view.View;

/**
 * Created by chensuilun on 2016/12/16.
 */
public class ScaleTransformer implements GalleryLayoutManager.ItemTransformer {

    private static final String TAG = "CurveTransformer";


    @Override
    public void transformItem(GalleryLayoutManager layoutManager, View item, float fraction) {
        item.setPivotX(item.getWidth() / 2.0f);
        item.setPivotY(item.getHeight()/2.0f);
        float scale = 1 - 0.08f * Math.abs(fraction);
        item.setScaleX(scale);
        item.setScaleY(scale);
    }
}
