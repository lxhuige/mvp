package com.lxh.library.inject;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class InjectCheckLogin {
    public static void inject(final Activity activity) {
        final Class<?> aClass = activity.getClass();
        Method[] methods = aClass.getMethods();
        for (final Method method : methods) {
            CheckLogin annotation = method.getAnnotation(CheckLogin.class);
            if (null != annotation) {
                int[] resIds = annotation.value();
                for (int resId : resIds) {
                    try {
                        View view = getViewById(activity,resId);
                        if (view == null) continue;
                        Method setOnClickListener = view.getClass().getMethod("setOnClickListener", View.OnClickListener.class);
                        View.OnClickListener listener = (View.OnClickListener) Proxy.newProxyInstance(activity.getClassLoader(), new Class[]{View.OnClickListener.class},
                                new CheckLoginHandler(activity, method));
                        setOnClickListener.invoke(view, listener);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }

    public static void inject(final Fragment fragment, View rootView) {
        final Class<?> aClass = fragment.getClass();
        Method[] methods = aClass.getMethods();
        for (final Method method : methods) {
            CheckLogin annotation = method.getAnnotation(CheckLogin.class);
            if (null != annotation) {
                int[] resIds = annotation.value();
                for (int resId : resIds) {
                    try {
                        View view = getViewById(rootView,resId);
                        if (view == null) continue;
                        Method setOnClickListener = view.getClass().getMethod("setOnClickListener", View.OnClickListener.class);
                        View.OnClickListener listener = (View.OnClickListener) Proxy.newProxyInstance(aClass.getClassLoader(), new Class[]{View.OnClickListener.class},
                                new CheckLoginHandler(fragment, method));
                        setOnClickListener.invoke(view, listener);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }


    private static View getViewById(@NonNull View view, int resId) {
        return view.findViewById(resId);
    }
    private static View getViewById(@NonNull Activity activity, int resId) {
        return activity.findViewById(resId);
    }
}
