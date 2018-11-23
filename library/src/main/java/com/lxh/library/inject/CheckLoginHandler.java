package com.lxh.library.inject;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import com.lxh.library.uitils.ToastUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class CheckLoginHandler implements InvocationHandler {

    private Object object;
    private Method targetMethod;
    private int j = 0;

    public CheckLoginHandler(Object context, Method method) {
        this.object = context;
        this.targetMethod = method;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        CheckLogin annotation = targetMethod.getAnnotation(CheckLogin.class);
        if (annotation == null) {
            method.invoke(proxy, args);
            return proxy;
        }
        CheckType[] checkType = annotation.checkType();
        for (CheckType type : checkType) {
            switch (type) {
                case LOGIN:
                    ToastUtils.INSTANCE.showMessageCenter("我需要验证登录");
                    checkLogin(args);
                    break;
                case UPDATE:
                    ToastUtils.INSTANCE.showMessageCenter("我需要验证更新");
                    break;
                default:
                    break;
            }
        }
        return object;
    }

    /**
     * 检查登录操作
     */
    private void checkLogin( Object[] args)throws Throwable {
        if (j % 2 == 0) {
            ToastUtils.INSTANCE.showMessageCenter("登录成功后操作");
            targetMethod.invoke(object, args);
            j++;
        } else {
            Context context = null;
            if (object instanceof Activity) {
                context = (Context) object;
            } else if (object instanceof Fragment) {
                context = ((Fragment) object).getContext();
            } else if (object instanceof Context) {
                context = (Context) object;
            }
            if (context == null) return ;
            new AlertDialog.Builder(context)
                    .setTitle("温馨提示")
                    .setMessage("请登录")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ToastUtils.INSTANCE.showMessageCenter("登录成功");
                            j++;
                        }
                    })
                    .show();
        }
    }
}
