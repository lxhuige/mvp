package com.lxh.library;

/**
 * 查分包可移植
 * 对应 libnative.so 一块搬过去就能用
 *  val oldfile = ApkUtils.getSourceApkPath(this@MainActivity, packageName)
 * //2.合并得到最新版本的APK文件
 *  val newfile = Constants.NEW_APK_PATH
 *  val patchfile = Constants.PATCH_FILE_PATH
 *  PatchUtils.patch(oldfile, newfile, patchfile)
 * 之后就可以安装了
 *
 * 获取旧包方法
 * ApplicationInfo appInfo = context.getPackageManager()
 * 					.getApplicationInfo(packageName, 0);
 * 			return appInfo.sourceDir;
 *
 *
 */
public class PatchUtils {
    static {
        System.loadLibrary("native");
    }

    public static native void patch(String oldFile, String newfile, String patchfile);
}
