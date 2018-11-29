package com.lxh.library.network;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.TypedValue;
import com.lxh.library.R;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * 使用指南
 * UpdateManger.
 * getInstance()
 * .setNotify(R.mipmap.water, context, BuildConfig.VERSION_NAME)
 * .setDialog(appType, false, true)
 * .setProgressDialog(context)
 * .download(url, new File(savePath));
 */
public class UpdateManger {
    private static final UpdateManger ourInstance = new UpdateManger();
    private static final int NO_3 = 0x03;
    private NotificationCompat.Builder builder;
    private NotificationManager manager;
    private WeakReference<Context> contextWeakReference;
    //如果正在下载则直接返回不开始新的下载
    private volatile boolean isDowning = false;

    /**
     * 提示消息
     */
    private String message;
    /**
     * true 强制更新
     * 默认不强制更新 false
     */
    private boolean isMustUpdate = false;
    /**
     * true 显示提示框
     * 默认不显示提示框 false
     */
    private boolean isShowDialog = false;

    private ProgressDialog progressDialog;
    private File file;

    public static UpdateManger getInstance() {
        return ourInstance;
    }

    private UpdateManger() {
    }

    public UpdateManger setNotify(@DrawableRes int resgitId, @NonNull Context context) {
        if (null != contextWeakReference) {
            contextWeakReference.clear();
            contextWeakReference = null;
        }
        contextWeakReference = new WeakReference<>(context);
        if (this.file != null) return ourInstance;
        builder = new NotificationCompat.Builder(context, getAppName(context));
        builder.setSmallIcon(resId);
        builder.setContentTitle("正在下载");
        builder.setContentText("下载");
        //设置进度为不确定，用于模拟安装
        builder.setProgress(0, 0, true);
        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        int color = ContextCompat.getColor(context, typedValue.resourceId);
        builder.setColor(color);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setContentTitle(getAppName(context));
            NotificationChannel notificationChannel = new NotificationChannel(getAppName(context), getAppName(context), NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(notificationChannel);
        }
        manager.notify(NO_3, builder.build());
        return ourInstance;
    }

    /**
     * 设置弹出框
     *
     * @param message      提示消息
     * @param isMustUpdate 是否强制更新
     * @param isShowDialog 是否显示提示框
     * @return UpdateManger
     */
    public UpdateManger setDialog(String message, boolean isMustUpdate, boolean isShowDialog) {
        this.message = message;
        if (TextUtils.isEmpty(this.message)) this.message = "发现新版本，请安装";
        this.isMustUpdate = isMustUpdate;
        this.isShowDialog = isShowDialog;
        return ourInstance;
    }

    public UpdateManger setProgressDialog(Context context) {
        if (this.file != null) return ourInstance;
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage("亲，努力下载中..");
        progressDialog.setProgressNumberFormat("%1d Mb/%2d Mb");
        progressDialog.setCancelable(!isMustUpdate);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        return ourInstance;
    }

    public File getFile() {
        return file;
    }

    public void download(final String url, final File file) {
        download(url, file, null);
    }

    public void download(final String url, final File file, final UpdateManger.CallBack callBack) {
        if (isDowning) return;
        if (this.file != null) {
            if (callBack != null) callBack.success(this.file);
            if (isShowDialog) showUpdateDialog(this.file);
            if (null != manager) manager.cancel(NO_3);
            return;
        }
        isDowning = true;
        DownloadManger.download(url, file, new DownloadManger.CallBack() {
            @Override
            public void success(File file) {
                if (progressDialog != null) progressDialog.dismiss();
                progressDialog = null;
                getInstance().file = file;
                isDowning = false;
                manager.cancel(NO_3);
                if (null != callBack) {
                    callBack.success(file);
                    clearMemory();
                    return;
                }
                if (isShowDialog) {
                    showUpdateDialog(file);
                    return;
                }
                installByNotify();
            }

            @Override
            public void fail(Exception e) {
                isDowning = false;
                clearMemory();
                if (null != callBack) callBack.fail(e);
            }

            @Override
            public void doingLoad(long fileTotal, long current) {
                if (null != callBack) callBack.doingLoad(fileTotal, current);
                if (null != builder && fileTotal != 0L) {
                    manager.notify(NO_3, builder.build());
                    Integer max = Integer.valueOf(String.valueOf(fileTotal / 1024 / 1024));
                    Integer value = Integer.valueOf(String.valueOf(current / 1024 / 1024));
                    builder.setProgress(max, value, false);
                    //下载进度提示
                    float v = (current * 100f / fileTotal);
                    builder.setContentText("下载" + Math.ceil(v) + "%");
                }
                if (null != progressDialog) {
                    if (!progressDialog.isShowing()) progressDialog.show();
                    progressDialog.setMax(((int) (fileTotal / 1024 / 1024)));
                    progressDialog.setProgress((int) (current / 1024 / 1024));
                }
            }
        });
    }

    private void showUpdateDialog(final File file) {
        if (contextWeakReference == null) return;
        final Context context = contextWeakReference.get();
        if (null == context) return;
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle("温馨提示")
                .setMessage(message)
                .setCancelable(false)
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        clearMemory();
                    }
                })
                .setPositiveButton("安装", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        installApp(file, context);
                    }
                });
        if (!isMustUpdate) builder.setNegativeButton("取消", null);
        builder.show();
    }

    private Uri getUriFromFile(File file, Context context) {
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    public void installApp(final File file, @NonNull final Context context) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(getUriFromFile(file, context), "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(intent);
    }

    /**
     * 通过通知点击安装
     */
    private void installByNotify() {
        //如果callBack不为空 即认为此处不需要再做安装处理
        if (contextWeakReference == null) return;
        final Context context = contextWeakReference.get();
        if (null != builder && null != context) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(getUriFromFile(file, context), "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            builder.setContentIntent(PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT));
            //下载完成后更改标题以及提示信息
            builder.setContentTitle("下载完成");
            builder.setContentText("点击安装");
            builder.setProgress(100, 100, false);
            manager.notify(NO_3, builder.build());
        }
    }

    /**
     * 所有资源在此统一释放，避免内存泄漏。此方法回自动调用。
     * 也可在activity onDestroy 方法中调用
     */
    private void clearMemory() {
        if (null != manager) manager.cancel(NO_3);
        if (progressDialog != null) progressDialog.dismiss();
        if (null != contextWeakReference) contextWeakReference.clear();
        contextWeakReference = null;
        manager = null;
        progressDialog = null;
        builder = null;
    }

    @NonNull
    private synchronized String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "lxh";
    }

    public abstract static class CallBack {
        public abstract void success(File file);

        public void fail(Exception e) {
        }

        public void doingLoad(long fileTotal, long current) {
        }
    }
}

//    @RequiresApi(api = Build.VERSION_CODES.O)
//    private void installO(File file, @NonNull final Context context) {
//        if (context.getPackageManager().canRequestPackageInstalls()) {
//            install(file, context);
//        } else {
//            String msg = "安装应用需要打开未知来源权限，请去设置中开启权限";
//            new AlertDialog.Builder(context)
//                    .setTitle("温馨提示")
//                    .setMessage(msg)
//                    .setCancelable(false)
//                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                                Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                ((Activity) context).startActivityForResult(intent, PACKAGE_MANAGER_CODE);
//                            }
//                        }
//                    });
//
//        }
//    }
