package com.lxh.library.update;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lxh.library.R;
import com.lxh.library.uitils.DensityUtil;

/**
 * author : DonMingXin
 * e-mail : sanjiinmr@sina.com
 * time   : 2017/9/4
 * version: 1.0
 * desc   : apk有新更新和提示描述对话框 ——常规或非强制更新通过Type区分
 */
public class UpdateDialog extends Dialog {

    private static final String TAG = UpdateDialog.class.getSimpleName();

    /**
     * 对话框标题控件
     */
    private TextView titleView;

    /**
     * 对话框显示更新内容的控件
     */
    private TextView msgView;

    /**
     * 更新对话框的类型
     */
    private Type type;

    /**
     * 对话框显示的标题
     */
    private String title;

    /**
     * 对话框显示的提示内突
     */
    private String msg;

    /**
     * 对话框的回调事件（确认和取消）
     */
    private final CallBack events;

    public UpdateDialog(@NonNull Context context, Type type, CallBack events) {
        super(context);
        this.type = type;
        this.events = events;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 窗口属性
        //设置此dialog不可取消，除非使用dismiss
        setCancelable(false);
        //设置点击空白处不可取消
        setCanceledOnTouchOutside(false);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //将布局添加到Dialog中
        setContentView(R.layout.dialog_update_normal);
        View view = findViewById(R.id.root);
        view.getLayoutParams().width= (int) (DensityUtil.INSTANCE.getWidthPixels(getContext()) * 0.8);
        //布局控件
        // 标题
        titleView = findViewById(R.id.dialog_update_textview_title);
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, DensityUtil.INSTANCE.getDensity(getContext())*14);
        // 内容
        msgView = findViewById(R.id.dialog_update_textview_title_note);
        msgView.setTextSize(TypedValue.COMPLEX_UNIT_PX, DensityUtil.INSTANCE.getDensity(getContext())*13);
        int px = DensityUtil.INSTANCE.dpToPx(10, getContext());
        msgView.setPadding(px,px,px,px);
        // 取消按钮
        TextView cancelView = findViewById(R.id.dialog_update_textview_left);
        cancelView.setTextSize(TypedValue.COMPLEX_UNIT_PX, DensityUtil.INSTANCE.getDensity(getContext())*13);
        cancelView.setPadding(px,px,px,px);
        // 确认更新按钮
        TextView confimView = findViewById(R.id.dialog_update_textview_right);
        confimView.setTextSize(TypedValue.COMPLEX_UNIT_PX, DensityUtil.INSTANCE.getDensity(getContext())*13);
        confimView.setPadding(px,px,px,px);
        // 包浩取消和确认按钮的父控件
        ViewGroup controlLay = findViewById(R.id.control_lay);

        // 设置listener
        cancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 点击取消
                if (null != events) events.onCancel();
                dismiss();
            }
        });
        confimView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 点击确认
                if (null != events) events.onConfirm();
                dismiss();
            }
        });
        setControlBnShow(type, controlLay, cancelView);
    }

    // 定义两种对话框类型（普通更新和强制更新）
    public enum Type {
        NORMAL,
        FORCE
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

    @Override
    public void show() {
        try {
            if (!isShowing()) {
                super.show();
                updateView();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置控制按钮的显示
     * 备注：根据对话框类型确定是否显示取消按钮。规定强制更新不显示取消按钮
     *
     * @param type       对话框类型（普通更新/强制更新）
     * @param controlLay 包含取消和确认按钮的父控件
     * @param cancelView 取消按钮
     */
    private void setControlBnShow(Type type, ViewGroup controlLay, TextView cancelView) {
        if (Type.FORCE == type) {
            LinearLayout ll = (LinearLayout) controlLay;
            ll.setWeightSum(1);
            cancelView.setVisibility(View.GONE);
        } else {
            LinearLayout ll = (LinearLayout) controlLay;
            ll.setWeightSum(2);
            cancelView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置更新内容
     *
     * @param msg
     * @return
     */
    public UpdateDialog setMessage(String msg) {
        this.msg = msg;
        return this;
    }

    /**
     * 设置更新对话框的标题
     *
     * @param title
     * @return
     */
    public UpdateDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * 更新对话框的控件显示
     */
    private void updateView() {
        // 更新标题
        if (null != titleView && !TextUtils.isEmpty(title)) {
            titleView.setText(title);
        }
        // 更新内容
        if (null != msgView && !TextUtils.isEmpty(msg)) {
            msgView.setText(msg);
        }
    }

    /**
     * 为取消和确认按钮定义一个回调
     */
    public interface CallBack {
        // 点击了确认按钮
        void onConfirm();

        // 点击了取消按钮
        void onCancel();
    }

    /**
     * 自定义一个实现了取消/确认更新回调的实现类
     */
    public static class ImCallBack implements CallBack {
        @Override
        public void onConfirm() {
        }

        @Override
        public void onCancel() {
        }
    }
}
