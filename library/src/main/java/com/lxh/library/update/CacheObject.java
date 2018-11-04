package com.lxh.library.update;

/**
 * author : DongMingXin
 * e-mail : sanjinmr@sina.com
 * time   : 2017/8/14
 * version: 1.0
 * desc   : 全局缓存类
 */
public class CacheObject {

    /**
     * 判断是否提示过普通更新的旗标，默认没有提示过普通更新
     * 一旦普通更新提示一次后，本次使用app时不再进行提示。除非重新启动app将会再次进行提示
     */
    public static boolean isShowUpdateNote = false;

    /**
     * 判断是否正在下载apk。默认没有下载
     */
    public static boolean isDownLoading = false;

}
