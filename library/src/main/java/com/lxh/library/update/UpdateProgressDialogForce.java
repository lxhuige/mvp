package com.lxh.library.update;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lxh.library.R;
import com.lxh.library.uitils.DensityUtil;


/**
 * author : DongMingXin
 * e-mail : dgsimle@sina.com
 * time   : 2017/8/14
 * version: 1.0
 * desc   : 显示强制下载进度的对话框
 */
public class UpdateProgressDialogForce extends UpdateProgressDialogBase {

    private TextView tv_progress;
    private TextView dialog_update_textview_right;
    private ProgressBar download_progressbar;
    private View line;

    public UpdateProgressDialogForce(Context context) {
        super(context, R.style.FullscreenDialog);
    }

    @Override
    public int setLayoutResId() {
        return R.layout.dialog_update_progess;
    }

    @Override
    public void getViewObject() {
        View view = findViewById(R.id.root);
        view.getLayoutParams().width = (int) (DensityUtil.INSTANCE.getWidthPixels(getContext()) * 0.8);
        tv_progress = findViewById(R.id.tv_progress);
        dialog_update_textview_right = findViewById(R.id.dialog_update_textview_right);
        dialog_update_textview_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateAPKService.getInstance().installApk();
            }
        });
        int px = DensityUtil.INSTANCE.dpToPx(10, getContext());
        dialog_update_textview_right.setPadding(px, px, px, px);
        dialog_update_textview_right.setTextSize(TypedValue.COMPLEX_UNIT_PX, DensityUtil.INSTANCE.getDensity(getContext()) * 13);
        download_progressbar = findViewById(R.id.download_progressbar);
        download_progressbar.getLayoutParams().height = DensityUtil.INSTANCE.dpToPx(5, getContext());
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) download_progressbar.getLayoutParams();
        params.setMargins(px, px / 2, px, px * 2);
        TextView tvTile = findViewById(R.id.tvTile);
        tvTile.setTextSize(TypedValue.COMPLEX_UNIT_PX, DensityUtil.INSTANCE.getDensity(getContext()) * 12);
        tv_progress.setTextSize(TypedValue.COMPLEX_UNIT_PX, DensityUtil.INSTANCE.getDensity(getContext()) * 12);
        line = findViewById(R.id.line);
        switchInstallLayShow(View.GONE);
    }

    @Override
    public void updateProgress() {
        tv_progress.setText(progress + "%");
        download_progressbar.setProgress(progress);
        if (100 == progress) {
            switchInstallLayShow(View.VISIBLE);
        }
    }

    @Override
    public void setWindowAttr() {
        // 设置打开对话框时，底部透明度降低
        Window window = this.getWindow();
        if (null != window) {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.dimAmount = 0.6f;
            window.setAttributes(lp);
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            setCancelable(false);
            setCanceledOnTouchOutside(false);
        }
    }

    /**
     * 控制安装布局的显示
     *
     * @param gone 默认隐藏，只有下载到百分之百后再显示
     */
    private void switchInstallLayShow(int gone) {
        line.setVisibility(gone);
        dialog_update_textview_right.setVisibility(gone);
    }


}
