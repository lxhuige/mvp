package com.lxh.library.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lxh.library.R;


/**
 * This class is for loading view.
 * Created by s1823 on 2016/4/8.
 */
public class Loading extends Dialog {

    private TextView mTextView = null;
    private ProgressBar mProgress = null;
    private LinearLayout mLayout = null;
    private boolean mCancelable = false;
    private OnCancelListener mListener = null;

    private Loading(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, R.style.JLoading);
        mCancelable = cancelable;
        mListener = cancelListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.jloading);
        mTextView = findViewById(R.id.jloading_tv);
        mProgress = findViewById(R.id.jloading_pb);
        mLayout = findViewById(R.id.jloading_layout);
        init();
    }

    private void init() {
//        setCancelable(mCancelable);
        setCanceledOnTouchOutside(mCancelable);
        if (mCancelable) {
            setOnCancelListener(mListener);
        }
    }

    private void setProgressVisibility(boolean visible) {
        if (!visible) {
            mProgress.setVisibility(View.GONE);
        } else {
            mProgress.setVisibility(View.VISIBLE);
        }
    }

    private void setBackground(int color) {
        mLayout.setBackgroundColor(color);
    }

    private void setTextColor(int color) {
        mTextView.setTextColor(color);
    }

    public void setMessage(@StringRes int id) {
        if (id == 0) {
            mTextView.setText("");
        } else {
            mTextView.setText(id);
        }
    }

    public void setMessage(@Nullable String text) {
        if (null == text) {
            mTextView.setText("");
        } else {
            mTextView.setText(text);
        }
    }

    /**
     * Create and show a loading dialog.
     *
     * @param context the activity context.
     * @param message the showing message.
     * @return the loading dialog instance.
     */
    private static Loading createLoading(Context context, String message) {
        Loading loading = new Loading(context, false, null);
        loading.show();
        loading.setMessage(message);
        return loading;
    }

    /**
     * @param context    the activity context.
     * @param message    the showing message.
     * @param Cancelable true 可以取消 false 不能取消
     * @return Loading
     */
    public static Loading createLoading(Context context, String message, boolean Cancelable, OnCancelListener mListener) {
        Loading loading = new Loading(context, Cancelable, mListener);
        loading.show();
        loading.setMessage(message);
        return loading;
    }

    /**
     * Create and show a text only loading dialog.
     *
     * @param context     the activity context.
     * @param message     the showing message.
     * @param bgColorId   the background color resource ID. Use the default color if it is 0.
     * @param textColorId the text color resource ID. Use the default color if it is 0.
     * @return the loading dialog instance.
     */
    public static Loading createTextLoading(Context context, String message, int bgColorId, int textColorId) {
        Loading loading = createLoading(context, message);
        loading.setProgressVisibility(false);
        if (bgColorId != 0) {
            loading.setBackground(ContextCompat.getColor(context, bgColorId));
        }
        if (textColorId != 0) {
            loading.setTextColor(ContextCompat.getColor(context, textColorId));
        }
        return loading;
    }
}
