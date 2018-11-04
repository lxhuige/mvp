package com.lxh.library.update;

import android.app.Dialog;
import android.content.Context;

import java.util.Map;

/**
 * author : DongMingXin
 * e-mail : dgsimle@sina.com
 * time   : 2017/8/14
 * version: 1.0
 * desc   :
 */
public abstract class UpdateProgressDialogBase extends Dialog {


    public int hasRead;
    public int total;
    public int progress;
    public long speed;

    public abstract int setLayoutResId();

    public abstract void getViewObject();

    public abstract void updateProgress();

    public abstract void setWindowAttr();

    public UpdateProgressDialogBase(Context context, int themeResId) {
        super(context, themeResId);
        // 加载布局文件
        setContentView(setLayoutResId());
        // 设置window属性
        setWindowAttr();
        // 获取布局控件
        getViewObject();
    }

    /**
     *
     * 更新进度数据
     * @param map
     */
    public void setProgressData(Map<String , Object> map) {
        try {
            hasRead = (int) map.get("hasRead");
            total = (int) map.get("readTotal");
            progress = (int) map.get("progress");
            speed = (long) map.get("speed");
            updateProgress();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void show() {
        try {
            if (!isShowing()) {
                super.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dismiss() {
        try {
            if (isShowing()) {
                super.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
