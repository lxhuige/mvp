package com.lxh.library.update;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import com.lxh.library.AppManager;
import com.lxh.library.R;
import com.lxh.library.uitils.LogUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 下载APK安装包的专用服务类
 * Created by dongmingxin on 2016/12/30.
 */
public class UpdateAPKServiceLoad extends Service {

    private final String TAG = UpdateAPKServiceLoad.class.getSimpleName();

    /**
     * 定义onBind()方法所返回的对象
     */
    private UpdateAPKIBinder binder = new UpdateAPKIBinder();

    /**
     * 从下载apk的子线程中获取的进度结果
     */
    private Map<String, Object> progressResult;

    /**
     * 显示更新进度通知栏的构造对象
     */
    private Notification.Builder notifBuilder;

    /**
     * 通知管理器
     */
    private NotificationManager manager;

    /**
     * 计时器
     */
    private Timer timer;

    /**
     * 显示下载/更新进度的对话框
     */
    private UpdateProgressDialogBase progressDialog;

    private int hasRead;
    private int total;
    private int progress;
    private long speed;
    private File apkSaveFile;

    //private RemoteViews contentView;

    /**
     * 得到来自子线程的数据
     */
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    progressResult = (Map<String, Object>) msg.obj;
                    // 更新通知栏下载进度
                    updateNotiBarInfo();
                    // 更新对话框
                    if (null != progressDialog) {
                        progressDialog.setProgressData(progressResult);
                    }
                    break;
                case 2:
                    completed(true);
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 该内部类里放了两个公用方法，在服务被绑定后，Activity可以用回传的一个Binder实例调用这两个方法
     * 还有一种模式:内部类只有一个方法，就是用来返回当前的service对象，让启用这个service的对象能调用service的公有方法
     *
     * @author Administrator
     */
    public class UpdateAPKIBinder extends Binder {

        // 希望这个钩子能做什么事

        // 1、希望能获得服务的实例  ----
        // 然后通过这个实例来操作service   否则的话外部没法调用service里面的方法
        // 这点和activity不一样。activity可以new 但是service不能new
        public UpdateAPKServiceLoad getService() {
            return UpdateAPKServiceLoad.this;
        }
    }

    /**
     * service第一次被创建时，回调该方法
     * 除非用户stop了service，再次startServie或者bindServie都不会调用该方法
     */
    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * 必须实现的方法——绑定该service时，回调的方法
     * 该方法返回IBinder对象，即UpdateAPKIBinder（继承Binder,Binder继承IBinder）
     * 该IBinder对象在ServiceConnection的回调中被传给了onServiceConnected方法作为形参
     * 可见，在context.bindService(intent, conn, Service.BIND_AUTO_CREATE)绑定服务的时候，传入的
     * conn就类似于观察者的角色。一旦绑定成功，就通过这个观察者将一个钩子即IBinder传给conn的引用者。
     * 这样一来就实现了服务和调用服务者的关联了。
     * 用户要怎么关联，或在需要这个关联作什么事，都决定于自己在service中实现的IBinder的实例
     * 于是乎，在service中实现一个IBinder的实例是必须的！！！
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    /**
     * 使用startService()开启service时回调该方法
     * 一般在开启服务就需要执行的任务放在这个方法中
     * 备注：
     * 1、该方法的早期版本是void onStart（Intent intent, int startId）
     * 2、每次使用startService()开启service时回调该方法
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * service被断开连接此回调该方法
     * 备注：当该Service上绑定的所有客户端都断开连接时将会回调该方法
     * 当调用者退出(即使没有调用unbindService)或者主动停止服务时会调用
     *
     * @param intent
     * @return
     */
    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    /**
     * service被关闭之前回调该方法
     * 当调用者退出(即使没有调用unbindService)或者主动停止服务时会调用
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        //LogUtils.d(TAG, "onDestroy被执行了");
        // Service被关闭
        // 回收资源
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
    }

    // 从服务器下载安装包到SD卡
    public void startDownLoad(String url, File apkSaveFile) {
        LogUtils.INSTANCE.d(TAG, "startDownLoad");
        if (CacheObject.isDownLoading) return; // 过滤掉正在下载的情况
        new DownThread(url, apkSaveFile).start();
        openNotification();
        CacheObject.isDownLoading = true; // 标识正在下载apk
    }

    // 这是一个负责下载资源的线程类
    private class DownThread extends Thread {
        private String apkurl;

        public DownThread(String apkurl, File apkSavePath) {
            this.apkurl = apkurl;
            apkSaveFile = apkSavePath;
            LogUtils.INSTANCE.d(TAG, "apkurl: " + apkurl);
            LogUtils.INSTANCE.d(TAG, "apkSaveFile: " + apkSaveFile.getAbsolutePath());
        }

        @Override
        public void run() {
            super.run();
            InputStream inputStream = null;
            FileOutputStream fos = null;
            HttpURLConnection conn = null;
            try {
                URL url = new URL(apkurl);
                //LogUtils.d(TAG, "apkurl->" + apkurl);
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5 * 1000);
                conn.setRequestMethod("GET");
                if (200 == conn.getResponseCode()) {
                    // 获得文件尺寸——下载文件的大小
                    int fileSize = conn.getContentLength();
                    inputStream = conn.getInputStream();
                    fos = new FileOutputStream(apkSaveFile);  // 需要清空原来的吗？
                    byte[] buffer = new byte[1024];
                    hasRead = 0;
                    total = 0;
                    speed = 0;
                    // 得到系统当前事件
                    long currTime = System.currentTimeMillis();
                    while ((hasRead = inputStream.read(buffer)) > 0) {
                        // 将数据写入目标文件
                        fos.write(buffer, 0, hasRead);
                        // 对读取的数据进行累计
                        total = total + hasRead;
                        // 计算出本次读取数据耗时,从而计算速度
                        long timeGap = System.currentTimeMillis() - currTime;
                        if (timeGap == 0) {
                            speed = 1000;
                        } else {
                            speed = 1000 / timeGap; // 每秒xxkb.因为buffer为1024b:speed*period=1kb->speed=1kb/period
                            //LogUtils.d(TAG, "speed->" + speed + " timeGap-> " + timeGap);
                        }
                        // 计算出进度值
                        progress = (int) (total * 100.0 / fileSize);
                        // 一次循环结束，重设当前时间
                        currTime = System.currentTimeMillis();
                    }
                    fos.flush();
                }
            } catch (Exception e) {
                e.printStackTrace();
                completed(false);
            } finally {
                try {
                    if (null != fos) {
                        fos.close();
                    }
                    if (null != inputStream) {
                        inputStream.close();
                    }
                    if (null != conn) {
                        conn.disconnect();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    // 安装下载来的软件包——要开子线程执行耗时任务吗？ 不用
    public void installAPK() {
        if (apkSaveFile != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (getPackageManager().canRequestPackageInstalls()) {
                    install();
                } else {
                    final Activity activity = AppManager.INSTANCE.currentActivity();
                    String msg = "安装应用需要打开未知来源权限，请去设置中开启权限";
                    new UpdateDialog(activity, UpdateDialog.Type.NORMAL,
                            new UpdateDialog.ImCallBack() {
                                @TargetApi(Build.VERSION_CODES.O)
                                @Override
                                public void onConfirm() {
                                    Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    activity.startActivityForResult(intent, 10086);
                                }
                                @Override
                                public void onCancel() {
                                    installAPK();
                                }
                            }).setTitle("提示").setMessage(msg).show();
                }

            } else {
                install();
            }
        } else {
            Toast.makeText(this, getString(R.string.file_failed), Toast.LENGTH_SHORT).show();
        }
    }

    private void install() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(getUriFromFile(apkSaveFile), "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);
    }

    private Uri getUriFromFile(File file) {
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //TODO 上线前还需修改 .provider
            uri = FileProvider.getUriForFile(AppManager.INSTANCE.currentActivity(), ".provider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    /**
     * 开启通知
     */
    private void openNotification() {
        //LogUtils.d(TAG, "openNotification！！");
        try {
            //通知栏显示所用到的布局文件
//            contentView = new RemoteViews(getPackageName(), R.layout.notify_layout);
            manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notifBuilder = new Notification.Builder(this);
            notifBuilder.setAutoCancel(false);
            notifBuilder.setTicker(getString(R.string.update_notification));
            notifBuilder.setContentTitle(getString(R.string.app_name));
            notifBuilder.setContentText(getString(R.string.downloading));
            notifBuilder.setProgress(100, 0, false);
            manager.notify(0, notifBuilder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }

        startTimer();
    }

    /**
     * 更新通知栏下载进度
     */
    private void updateNotiBarInfo() {
        try {
            // 更新通知栏
            if (null != notifBuilder && null != manager) {
                int mprogress = (int) progressResult.get("progress");
                //contentView.setTextViewText(R.id.content_view_text1, mprogress + "%");
                //contentView.setProgressBar(R.id.content_view_progress, 100, mprogress, false);
                notifBuilder.setProgress(100, mprogress, false);
                notifBuilder.setContentText(getString(R.string.download) + mprogress + "%");
                manager.notify(0, notifBuilder.build());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 开启计时器
     */
    private void startTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // 把数据传到主线程去
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("hasRead", hasRead);
                map.put("readTotal", total);
                map.put("progress", progress);
                map.put("speed", speed);
                Message msg = Message.obtain();
                msg.what = 1;
                msg.obj = map;
                handler.sendMessage(msg);
                if (progress == 100) {
                    timer.cancel();
                    handler.sendEmptyMessage(2);
                }
            }
        }, 0, 1000);
    }

    /**
     * 设置显示更新进度的对话框
     */
    public void setProgressDialog(UpdateProgressDialogBase progressDialog) {
        UpdateAPKServiceLoad.this.progressDialog = progressDialog;
    }

    /**
     * 下载完成的处理
     *
     * @param type true表示正常结束 false表示异常结束
     */
    public void completed(boolean type) {
        hasRead = 0;
        total = 0;
        progress = 0;
        speed = 0;
        // 设置通知栏结束状态
        finishNotiBarInfo(type);
        // 下载完后关闭服务
        stopSelf();
        CacheObject.isDownLoading = false;
        if (type) {
            installAPK();
            Toast.makeText(this, getString(R.string.download_finish), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 设置通知栏结束状态
     *
     * @param type 是否正常结束。true正常结束。false表示异常结束
     */
    private void finishNotiBarInfo(boolean type) {
        try {
            if (null != notifBuilder && null != manager) {
                //下载完成后更改标题以及提示信息
                if (type) {
                    notifBuilder.setContentTitle(getString(R.string.start_install));
                    notifBuilder.setContentText(getString(R.string.installing));
                    //设置进度为不确定，用于模拟安装
                    notifBuilder.setProgress(0, 0, true);
                }
                manager.notify(0, notifBuilder.build());
                manager.cancel(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
