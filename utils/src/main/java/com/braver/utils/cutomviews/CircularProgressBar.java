/*
 *
 *  * Created by https://github.com/braver-tool on 16/11/21, 10:30 AM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 23/11/21, 03:40 PM
 *
 */

package com.braver.utils.cutomviews;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

/**
 * How to use CircularProgressBar
 * <p>
 * On your XML
 * <com.braver.utils.cutomviews.CircularProgressBar
 * android:id="@+id/cProgressBar"
 * android:layout_width="115dp"
 * android:layout_height="115sp"
 * android:background="@drawable/bg_circle" />
 * <p>
 * On your coding
 * CircularProgressBar cProgressBar = v.findViewById(R.id.cProgressBar);
 * cProgressBar.setMaxProgress(100);
 * cProgressBar.setProgressColor(getResources().getColor(R.color.circle_color));
 * cProgressBar.setProgressWidth((int) getResources().getDimension(R.dimen.width_circle));
 * cProgressBar.showProgressText(false);
 * cProgressBar.setMaxProgress(85);
 */

public class CircularProgressBar extends View {
    private int mViewWidth;
    private int mViewHeight;
    private float mStartAngle = 90;             // Always start from top (default is: "3 o'clock on a watch.")
    private float mSweepAngle = 0;              // How long to sweep from mStartAngle
    private float mMaxSweepAngle = 360;         // Max degrees to sweep = full circle
    private int mStrokeWidth = 20;              // Width of outline
    private int mMaxProgress = 100;             // Max progress to use
    private boolean mDrawText = true;           // Set to true if progress text should be drawn
    private int mProgressColor = Color.BLACK;   // Outline color

    private final Paint mPaint;                 // Allocate paint outside onDraw to avoid unnecessary object creation

    public CircularProgressBar(Context context) {
        this(context, null);
    }

    public CircularProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircularProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initMeasurements();
        drawOutlineArc(canvas);

        if (mDrawText) {
            drawText(canvas);
        }
    }

    //This method is used for set color
    public void setProgressColor(int color) {
        mProgressColor = color;
        invalidate();
    }

    //This method is used for progress width
    public void setProgressWidth(int width) {
        mStrokeWidth = width;
        invalidate();
    }

    //This method is used for maximum progress
    public void setMaxProgress(int maxProgress) {
        mMaxProgress = maxProgress;
        invalidate();
    }


    //This method is used for set progress text
    public void showProgressText(boolean show) {
        mDrawText = show;
        invalidate();
    }

    //This method is used for set start angle
    public void setStartAngle(float val) {
        mStartAngle = val;
        invalidate();
    }

    //This method is used for validate sweep angle
    public void setSweepAngle(float val) {
        mSweepAngle = val;
        invalidate();
    }

    //This method is used for set maximum sweep angle
    public void setMaxSweepAngle(float val) {
        mMaxSweepAngle = val;
        invalidate();
    }

    //This method is used for initiate measurements
    private void initMeasurements() {
        mViewWidth = getWidth();
        mViewHeight = getHeight();
    }

    //This method is used for sweep angle progress
    private float calcSweepAngleFromProgress(int progress) {
        return (mMaxSweepAngle / mMaxProgress) * progress;
    }

    //This method is used for progress sweep angle
    private int calcProgressFromSweepAngle(float sweepAngle) {
        return (int) ((sweepAngle * mMaxProgress) / mMaxSweepAngle);
    }

    /**
     * Declaration drawOutlineArc()
     * Method used to draw out line
     * Declared In CircularProgressBar.java
     */
    private void drawOutlineArc(Canvas canvas) {
        final int diameter = Math.min(mViewWidth, mViewHeight);
        final float pad = (float) mStrokeWidth / 2;
        final RectF outerOval = new RectF(pad, pad, diameter - pad, diameter - pad);

        mPaint.setColor(mProgressColor);
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawArc(outerOval, mStartAngle, mSweepAngle, false, mPaint);
    }

    /**
     * Declaration drawOutlineArc()
     * Method used to draw text
     * Declared In CircularProgressBar.java
     */
    private void drawText(Canvas canvas) {
        mPaint.setTextSize(Math.min(mViewWidth, mViewHeight) / 5f);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setStrokeWidth(0);
        // Progress text color
        int mTextColor = Color.BLACK;
        mPaint.setColor(mTextColor);

        // Center text
        int xPos = (canvas.getWidth() / 2);
        int yPos = (int) ((canvas.getHeight() / 2) - ((mPaint.descent() + mPaint.ascent()) / 2));

        canvas.drawText(calcProgressFromSweepAngle(mSweepAngle) + "%", xPos, yPos, mPaint);
    }

    /**
     * Set progress of the circular progress bar.
     *
     * @param progress progress between 0 and 100.
     */
    public void setProgress(int progress) {
        ValueAnimator animator = ValueAnimator.ofFloat(mSweepAngle, calcSweepAngleFromProgress(progress));
        animator.setInterpolator(new DecelerateInterpolator());
        // Animation duration for progress change
        int mAnimationDuration = 800;
        animator.setDuration(mAnimationDuration);
        animator.addUpdateListener(valueAnimator -> {
            mSweepAngle = (float) valueAnimator.getAnimatedValue();
            invalidate();
        });
        animator.start();
    }
}
