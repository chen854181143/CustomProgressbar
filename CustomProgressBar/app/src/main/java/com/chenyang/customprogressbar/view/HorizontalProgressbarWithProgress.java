package com.chenyang.customprogressbar.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;

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
public class HorizontalProgressbarWithProgress extends ProgressBar {

    private static final int DEFAULT_TEXT_SIZE = 10;//sp
    private static final int DEFAULT_TEXT_COLOR = 0XFFFC00D1;
    private static final int DEFAULT_COLOR_UNREACH = 0XFFD3D6DA;
    private static final int DEFAULT_HEIGHT_UNREACH = 2;//dp
    private static final int DEFAULT_COLOR_REACH = DEFAULT_TEXT_COLOR;
    private static final int DEFAULT_HEIGHT_REACH = 2;//dp
    private static final int DEFAULT_TEXT_OFFSET = 10;//dp
    //字体大小
    protected int mTextSize = sp2px(DEFAULT_TEXT_SIZE);
    protected int mHeightUnreach = dp2px(DEFAULT_HEIGHT_UNREACH);
    protected int mHeightReach = dp2px(DEFAULT_HEIGHT_REACH);
    protected int mTextColor = DEFAULT_TEXT_COLOR;
    protected int mColorUnreach = DEFAULT_COLOR_UNREACH;
    protected int mColorReach = DEFAULT_COLOR_REACH;
    //字体与进度条的间距
    protected int mTextOffset = dp2px(DEFAULT_TEXT_OFFSET);
    protected Paint mpaint = new Paint();
    //宽度
    protected int mRealWith;

    public HorizontalProgressbarWithProgress(Context context) {
        this(context, null);
    }

    public HorizontalProgressbarWithProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalProgressbarWithProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        obtainStyleAttrs(attrs);
    }

    /**
     * 获取自定义属性的值
     *
     * @param attrs
     */
    private void obtainStyleAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs
                , R.styleable.HorizontalProgressbarWithProgress);

        mTextSize = (int) typedArray.getDimension(R.styleable.HorizontalProgressbarWithProgress_progress_text_size, mTextSize);
        mTextColor = typedArray.getColor(R.styleable.HorizontalProgressbarWithProgress_progress_text_color, mTextColor);
        mHeightUnreach = (int) typedArray.getDimension(R.styleable.HorizontalProgressbarWithProgress_progress_unreach_height, mHeightUnreach);
        mColorUnreach = typedArray.getColor(R.styleable.HorizontalProgressbarWithProgress_progress_unreach_color, mColorUnreach);
        mHeightReach = (int) typedArray.getDimension(R.styleable.HorizontalProgressbarWithProgress_progress_reach_height, mHeightReach);
        mColorReach = typedArray.getColor(R.styleable.HorizontalProgressbarWithProgress_progress_reach_color, mColorReach);
        mTextOffset = (int) typedArray.getDimension(R.styleable.HorizontalProgressbarWithProgress_progress_text_offset, mTextOffset);
        typedArray.recycle();
//      设置字体大小
        mpaint.setTextSize(mTextSize);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);
        //确定view的宽和高
        setMeasuredDimension(widthSize, height);
        //获取真实的宽度
        mRealWith = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
    }

    /**
     * 测量高度
     *
     * @param heightMeasureSpec
     * @return
     */
    private int measureHeight(int heightMeasureSpec) {
        int result = 0;
        //获取高度的模式
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        //获取高度
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (heightMode == MeasureSpec.EXACTLY) {//确切模式
            result = heightSize;
        } else {
//            descent():根据当前的字体和文字大小，返回下方（正）基线（下降）的距离。
//            ascent():根据当前的字体和文字大小返回上方（负）基线（上升）的距离
            int textHeight = (int) (mpaint.descent() - mpaint.ascent());
            result = getPaddingTop() + getPaddingBottom() +
                    Math.max(Math.max(mHeightReach, mHeightUnreach), Math.abs(textHeight));
            if (heightMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, heightSize);
            }
        }
        return result;
    }

    protected int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension
                (TypedValue.COMPLEX_UNIT_DIP, dpVal,
                        getResources().getDisplayMetrics());
    }

    protected int sp2px(int spVal) {
        return (int) TypedValue.applyDimension
                (TypedValue.COMPLEX_UNIT_SP, spVal,
                        getResources().getDisplayMetrics());
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate(getPaddingLeft(), getHeight() / 2);
        boolean noNeedUnRech = false;
        //draw reach bar
        String text = getProgress() + "%";
        int textWidth = (int) mpaint.measureText(text);//获取文本的宽度
        float radio = getProgress() * 1.0f / getMax();
        float progressX = radio * mRealWith;
        if (progressX + textWidth > mRealWith) {
            progressX = mRealWith - textWidth;
            noNeedUnRech = true;
        }
        float endX = progressX - mTextOffset / 2;

        /**
         * 此调用与先前的save（）调用相平衡，用于从上次保存调用后删除对矩阵/剪辑状态的所有修改。
         * 调用restore（）比save（）被调用多一次是一个错误。
         */
        if (endX > 0) {
            mpaint.setColor(mColorReach);
            mpaint.setStrokeWidth(mHeightReach);
            canvas.drawLine(0, 0, endX, 0, mpaint);
        }

        //draw text
        mpaint.setColor(mTextColor);
        int y = (int) (-(mpaint.descent() + mpaint.ascent()) / 2);
        canvas.drawText(text,progressX,y,mpaint);



        //draw unreach
        if(!noNeedUnRech){
            float start=progressX+mTextOffset/2+textWidth;
            mpaint.setColor(mColorUnreach);
            mpaint.setStrokeWidth(mHeightUnreach);
            canvas.drawLine(start,0,mRealWith,0,mpaint);
        }
        canvas.restore();
    }
}
