package com.lxh.library.update;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lxh.library.AppManager;
import com.lxh.library.R;

import java.io.File;

/**
 * 处理更新APK相关逻辑的服务类
 * Created by dongmingxin on 2016/12/23.
 */
public class UpdateAPKService {
    /**
     * 表示自动更新即在主页自动检查
     */
    public static final String AUTO = "auto";

    /**
     * 表示人为点击检查更新按钮
     */
    public static final String ARTI = "arti";

    /**
     * 下载新的apk的地址
     */
    private String apkDownLoadUrl;

    /**
     * 展示更新进度的对话框
     */
    private UpdateProgressDialogBase updateProgressDialog;

    /**
     * 下载apk的服务
     */
    private UpdateAPKServiceLoad loadingService;

    private UpdateAPKService() {
    }

    private static UpdateAPKService instance;

    public static UpdateAPKService getInstance() {
        if (null == instance) {
            instance = new UpdateAPKService();
        }
        return instance;
    }

    /**
     * 检查请求到的版本信息
     *
     * @param eventType auto 表示自动更新即在主页自动检查  arti 表示人为点击检查更新按钮
     */
    public void checkVersionInfo(String eventType, Activity activity, String response) throws Exception {
        try {
            VersionUpdateBean bean = new Gson().fromJson(response, VersionUpdateBean.class);
            if (bean.getResultCode() == 1 && bean.getType() != 0) {
                String downloadUrl = bean.getAppUrl();
                if (TextUtils.isEmpty(downloadUrl)) {
                    return;
                }
                apkDownLoadUrl = downloadUrl;
                judgeUpdateType(bean, eventType, activity);
            } else {
                if ("arti".equals(eventType)) {
                    Toast.makeText(activity, activity.getString(R.string.update_new_tip), Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("已经是最新的了");
        }
    }

    /**
     * 判断更新类型：1强制更新  0不强制更新
     */
    private void judgeUpdateType(VersionUpdateBean result, String eventType, Activity activity)
            throws Exception {
        int force = result.getType();
        String des = result.getContent();
        //判断版本类型
        switch (force) {
            //强制更新
            case 2:
                showOneBtnDialog(activity.getString(R.string.update_new_check_tip), des, activity);
                break;
            //普通更新
            default:
                handlerNormalUpdate(eventType, activity, des);
                break;
        }
    }

    /**
     * 处理普通更新
     */
    private void handlerNormalUpdate(String eventType, Activity activity, String des)
            throws Exception {
        if ("auto".equals(eventType)) {
            if (CacheObject.isDownLoading) {
                return;
            }
            if (!CacheObject.isShowUpdateNote) {
                CacheObject.isShowUpdateNote = true;
                showTwoBtnDialog(activity.getString(R.string.update_new_check_tip), des, activity);
            }
        } else {
            if (CacheObject.isDownLoading) {
                showMessageDefault(activity.getString(R.string.updating), activity);
                return;
            }
            showTwoBtnDialog(activity.getString(R.string.update_new_check_tip), des, activity);
        }
    }

    private void showMessageDefault(String content, Context context) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }

    // -------- 提示对话框 -------------------------------------------

    /**
     * 显示普通下载的Dialog
     */
    public void showTwoBtnDialog(String title, String msg, final Activity activity) throws Exception {
        new UpdateDialog(checkCurrActivity(activity), UpdateDialog.Type.NORMAL,
                new UpdateDialog.ImCallBack() {
                    @Override
                    public void onConfirm() {
                        super.onConfirm();
                        // 开启下载apk服务
                        startLoadingService(activity);
                    }
                }).setTitle(title).setMessage(msg).show();
    }

    /**
     * 显示强制下载的Dialog
     */
    public void showOneBtnDialog(String title, String msg, Activity activity) throws Exception {
        final Activity mActivity = checkCurrActivity(activity);
        new UpdateDialog(mActivity, UpdateDialog.Type.FORCE, new UpdateDialog.ImCallBack() {
            @Override
            public void onConfirm() {
                super.onConfirm();
                // 强制更新需要显示更新进度
                if (null == updateProgressDialog) {
                    updateProgressDialog = new UpdateProgressDialogForce(mActivity);
                }
                updateProgressDialog.show();
                // 开启下载apk服务
                startLoadingService(mActivity);
            }
        }).setTitle(title).setMessage(msg).show();
    }

    /**
     * 检查当前Activity的状态。如果已被回收，则用最近被添加的activity替换
     */
    private Activity checkCurrActivity(Activity activity) throws Exception {
        if (null == activity || activity.isFinishing()) {
            activity = AppManager.INSTANCE.currentActivity();
            if (null == activity || activity.isFinishing()) {
                throw new Exception();
            }
        }
        return activity;
    }

    // -------- 有关服务的逻辑 --------------------------------------------------

    private ServiceConnection conn = new ServiceConnection() {
        // 当组件与service连接成功时，回调该方法
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            UpdateAPKServiceLoad.UpdateAPKIBinder binder =
                    (UpdateAPKServiceLoad.UpdateAPKIBinder) service;
            loadingService = binder.getService();
            // 从服务端下载apk
            loadAPKFromServer(loadingService);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    /**
     * 从服务端下载APK
     */
    private void loadAPKFromServer(UpdateAPKServiceLoad loadingService) {
//        if (FileUtils.isSDAvaiable()) {
            try {
                if (null != updateProgressDialog && updateProgressDialog.isShowing()) {
                    loadingService.setProgressDialog(updateProgressDialog);
                }
                if (TextUtils.isEmpty(apkDownLoadUrl)) return;
                File loadFile = new File(Constant.APP_PATH + "download/");
                if (!loadFile.exists()) {
                    loadFile.mkdirs();
                }
                //TODO 修改文件名称
                File apkSaveFile = new File(loadFile, ".apk");
                loadingService.startDownLoad(apkDownLoadUrl, apkSaveFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
//        } else {
//            showMessageDefault("没有SD卡", AppManager.INSTANCE.currentActivity());
//        }
    }

    /**
     * 开启下载APK安装包服务
     */
    private void startLoadingService(Context context) {
        Intent intent = new Intent(context, UpdateAPKServiceLoad.class);
        // 启动服务
        startService(intent, context);
        // 绑定服务
        bindService(intent, context);
    }

    /**
     * 开启服务 —— 只能启动服务
     * 通过该方法启用Service，访问者与Service之间没有关联，即使访问者退出了，Service仍然运行
     * 备注：每一次startService 都会触发onStartCommand方法被执行
     */
    private void startService(Intent intent, Context context) {
        // 开启指定service
        context.startService(intent);
    }

    /**
     * 绑定服务，可以通过conn获得服务本身
     * 通过该方法启用Service，访问者与Service绑定在一起了，访问者一旦退出，Service也就终止
     * 备注：触发onBind被调用 如果service已被绑定，将不会导致onBind多次重复执行
     */
    private void bindService(Intent intent, Context context) {
        // 绑定指定service
        // 这里是绑定的Application。即应用不退出或者没有主动调用stopself，该服务将不会终止
        context.bindService(intent, conn, Service.BIND_AUTO_CREATE);
    }

    /**
     * 在退出应用的时候，调用该方法解绑服务
     */
    public void unbindService() {
        AppManager.INSTANCE.currentActivity().unbindService(conn);
    }

    public void installApk() {
        if (null != loadingService) {
            loadingService.installAPK();
        }
    }

}
