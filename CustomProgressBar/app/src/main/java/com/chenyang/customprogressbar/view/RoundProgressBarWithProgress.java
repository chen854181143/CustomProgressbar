package com.chenyang.customprogressbar.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.chenyang.customprogressbar.R;

/*
 * @创建者     Administrator
 * @创建时间   2017/8/4
 * @描述	      ${TODO}$
 *
 * @更新者     $Author
 * @更新时间   $Date
 * @更新描述   ${TODO}$
 */
public class RoundProgressBarWithProgress extends HorizontalProgressbarWithProgress {
    //半径
    private int mRadius = dp2px(30);
    private int mMaxPaintWidth;

    public RoundProgressBarWithProgress(Context context) {
        this(context, null);
    }

    public RoundProgressBarWithProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundProgressBarWithProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mHeightReach = (int) (mHeightUnreach * 2.5);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundProgressBarWithProgress);
        mRadius = (int) typedArray.getDimension(R.styleable.RoundProgressBarWithProgress_radius, mRadius);
        typedArray.recycle();

        mpaint.setStyle(Paint.Style.STROKE);
        mpaint.setAntiAlias(true);//设置抗锯齿
        mpaint.setDither(true);
        mpaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mMaxPaintWidth = Math.max(mHeightReach, mHeightUnreach);
        int expect = mRadius * 2 + mMaxPaintWidth + getPaddingRight() + getPaddingLeft();
        int width = resolveSize(expect, widthMeasureSpec);
        int height = resolveSize(expect, heightMeasureSpec);

        int readWidth = Math.min(width, height);
        mRadius = (readWidth - getPaddingLeft() - getPaddingRight() - mMaxPaintWidth) / 2;
        setMeasuredDimension(readWidth, readWidth);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        String text = getProgress() + "%";
        float textWidth = mpaint.measureText(text);
        float textHeight = (mpaint.descent() + mpaint.ascent()) / 2;
        canvas.save();
        canvas.translate(getPaddingLeft() + mMaxPaintWidth / 2, getPaddingTop() + mMaxPaintWidth / 2);
        mpaint.setStyle(Paint.Style.STROKE);
//        draw unreach bar
        mpaint.setColor(mColorUnreach);
        mpaint.setStrokeWidth(mHeightUnreach);
        canvas.drawCircle(mRadius, mRadius, mRadius, mpaint);
//        draw reach bar
        mpaint.setColor(mColorReach);
        mpaint.setStrokeWidth(mHeightReach);
        float sweepAngle = getProgress() * 1.0f / getMax() * 360;
        //绘制圆弧
        canvas.drawArc(new RectF(0, 0, mRadius * 2, mRadius * 2), 0, sweepAngle, false, mpaint);
//      draw text
        mpaint.setColor(mTextColor);
        mpaint.setStyle(Paint.Style.FILL);
        canvas.drawText(text,mRadius-textWidth/2,mRadius-textHeight,mpaint);
        canvas.restore();
    }
}
