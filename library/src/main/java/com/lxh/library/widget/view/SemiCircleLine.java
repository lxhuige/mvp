package com.lxh.library.widget.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import com.lxh.library.R;

public class SemiCircleLine extends View {

    private int width;
    private int height;
    private int lineColor;
    private int circleColor;

    private Paint mPaint;
    private Path mPath;
private  DashPathEffect dashPathEffect;
    private boolean isShowLine;
    private boolean isShowCircle;


    public SemiCircleLine(Context context) {
        super(context);
    }

    public SemiCircleLine(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SemiCircleLine(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.SemiCircleLine);
        circleColor = array.getColor(R.styleable.SemiCircleLine_circleColor, Color.BLUE);
        lineColor = array.getColor(R.styleable.SemiCircleLine_lineColor, Color.BLUE);
        isShowLine = array.getBoolean(R.styleable.SemiCircleLine_isShowLine, true);
        isShowCircle = array.getBoolean(R.styleable.SemiCircleLine_isShowCircle, true);

        //实线的宽度
        float solidLineWidth = array.getDimension(R.styleable.SemiCircleLine_solidLineWidth, 15);
        float dottedLineWidth = array.getDimension(R.styleable.SemiCircleLine_dottedLineWidth, 5);
        mPaint.setColor(circleColor);
        array.recycle();
        mPath = new Path();
        dashPathEffect = new DashPathEffect(new float[]{solidLineWidth, dottedLineWidth}, 0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isShowLine && isShowCircle) {
            CircleAndLine(canvas);
        } else if (isShowLine) {
            canvas.save();
            mPaint.reset();
            mPaint.setColor(lineColor);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(3);
            mPaint.setPathEffect(dashPathEffect);
            mPath.moveTo(0, height / 2);
            mPath.lineTo(width, height / 2);
            canvas.drawPath(mPath, mPaint);
            canvas.save();
        } else if (isShowCircle) {
            canvas.drawCircle(0, height / 2, height / 2, mPaint);
            canvas.drawCircle(width, height / 2, height / 2, mPaint);
        }
    }

    //线跟 半圆都显示
    private void CircleAndLine(@NonNull Canvas canvas) {
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(circleColor);
        canvas.drawCircle(0, height / 2, height / 2, mPaint);
        canvas.drawCircle(width, height / 2, height / 2, mPaint);
        canvas.save();
        mPaint.reset();
        mPaint.setColor(lineColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(3);

        mPaint.setPathEffect(dashPathEffect);
        mPath.moveTo(height / 2, height / 2);
        mPath.lineTo(width - height / 2, height / 2);
        canvas.drawPath(mPath, mPaint);
    }


}
