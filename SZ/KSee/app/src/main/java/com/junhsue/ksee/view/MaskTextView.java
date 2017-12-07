package com.junhsue.ksee.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;

import com.junhsue.ksee.R;


/**
 * 实现遮罩效果文字显示的进度条
 * Created by user yuting.yang 2016/6/26.
 */
public class MaskTextView extends View {

    private static int DEFAULT_HEIGHT = 60;// 默认高度
    private Context mContext;
    private Canvas mCanvas;
    private int mWidth, mHeight;
    private Paint mPaint;
    private Bitmap mBitmap2; // mCanvas绘制在这上面
    private int strokeWidth = 2; // 线的高度
    private int textSize = 30; // 字体的大小
    private int mProgress = 0; // 进度
    private int maxProgress;// 最大进度

    @Override
    public void invalidateOutline() {
        super.invalidateOutline();
    }

    public MaskTextView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public MaskTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public MaskTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        mContext = context;
        if (isInEditMode()) {
            return;
        }

        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public void setProgress(int progress) {
        mProgress = progress > maxProgress ? maxProgress : progress;
        invalidateView();
    }

    public void setMaxProgress(int max) {
        maxProgress = max < 0 ? 0 : max;
        invalidateView();
    }

    public int getProgress() {
        return mProgress;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setDefaultWidthHeight(widthMeasureSpec, heightMeasureSpec);
        mBitmap2 = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
    }

    /**
     * 进度转化
     *
     * @return
     */
    private float countProgress() {
        if (mWidth != 0 && maxProgress != 0) {
            return (float) mWidth / (float) maxProgress;
        }
        return 1;
    }


    /**
     * 设置默认高度
     */
    private void setDefaultWidthHeight(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.TRANSPARENT);
        drawBorder(canvas);
        secondFloorView((int) (mProgress * countProgress()));
        drawBaseText(canvas);

        mPaint.reset();
        mPaint.setAntiAlias(true);
        if (mBitmap2 != null) {
            canvas.drawBitmap(mBitmap2, new Rect(0, 0, mBitmap2.getWidth(), mBitmap2.getHeight()), new Rect(0, 0, mBitmap2.getWidth(), mBitmap2.getHeight()), mPaint);
        }
    }

    /**
     * 画背景字体
     *
     * @param canvas
     */
    private void drawBaseText(Canvas canvas) {
        refreshPaint(Paint.Style.FILL, strokeWidth, mContext.getResources().getDimensionPixelOffset(R.dimen.f_20), mContext.getResources().getColor(R.color.c_black_242a34));
        canvas.drawText(this.mProgress + "/" + maxProgress, 20, (int) (mHeight / 2.0 - (mPaint.ascent() + mPaint.descent()) / 2.0f), mPaint);
    }

    /**
     * 绘制圆角矩形边框
     */
    private void drawBorder(Canvas canvas) {

        refreshPaint(Paint.Style.STROKE, strokeWidth);
        RectF oval3 = new RectF(strokeWidth / 2, strokeWidth / 2, mWidth - strokeWidth / 2, mHeight - strokeWidth / 2);// 设置个长方形，扫描测量
        canvas.drawRoundRect(oval3, mHeight / 2, mHeight / 2, mPaint);
    }

    /**
     * 绘制第二个view的bitmap
     */
    private void secondFloorView(int progress) {
        mCanvas = new Canvas(mBitmap2);

        refreshPaint(Paint.Style.FILL, strokeWidth);
        RectF oval1 = new RectF(strokeWidth / 2, strokeWidth / 2, mWidth - strokeWidth / 2, mHeight - strokeWidth / 2);// 设置个长方形，扫描测量
        mCanvas.drawRoundRect(oval1, mHeight / 2, mHeight / 2, mPaint);

        refreshPaint(Paint.Style.FILL, strokeWidth, mContext.getResources().getDimensionPixelOffset(R.dimen.f_20), Color.WHITE);
        mCanvas.drawText(this.mProgress + "/" + maxProgress, 20, (int) (mHeight / 2.0 - (mPaint.ascent() + mPaint.descent()) / 2.0f), mPaint);
        /**
         * 绘制透明遮罩
         */
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT)); // 设置转换模式，取下层绘制非交集部分。
        // （setXfermode方法之前表示下层，方法之后表示上层）
        Path path = new Path();
        path.moveTo(0 + progress - mHeight / 2, 0);
        path.lineTo(mWidth, 0);
        path.lineTo(mWidth, mHeight);
        path.lineTo(0 + progress - mHeight / 2, mHeight);
        //画半圆平均到矩形进度
        RectF oval3 = new RectF(progress - mHeight / 2 - mHeight / 2, 0, progress, mHeight);// 设置个长方形，扫描测量
        path.addArc(oval3, -90, 180);
        path.setFillType(Path.FillType.EVEN_ODD);
        mCanvas.drawPath(path, mPaint);
    }

    /**
     * 设置当前进度条的进度(默认动画时间1.5s)
     *
     * @param progress
     */
    public void setCurrentProgress(final int progress) {

        ObjectAnimator animator = ObjectAnimator.ofInt(this, "progress", progress).setDuration(1500);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }
        });
        animator.start();
    }

    /**
     * 刷新画笔
     *
     * @param stroke
     * @param strokeWidth
     */
    private void refreshPaint(Paint.Style stroke, int strokeWidth) {
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(stroke);
        mPaint.setColor(mContext.getResources().getColor(R.color.c_yellow_cdac8d));
        mPaint.setStrokeWidth(strokeWidth);
    }

    /**
     * 刷新画笔
     *
     * @param style
     * @param strokeWidth
     * @param textSize
     * @param textColor
     */
    private void refreshPaint(Paint.Style style, int strokeWidth, int textSize, int textColor) {
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(strokeWidth);
        mPaint.setStyle(style);
        mPaint.setTextSize(textSize);
        mPaint.setColor(textColor);
    }

    /**
     * 重绘
     */
    private void invalidateView() {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            invalidate();
        } else {
            postInvalidate();
        }
    }

}
