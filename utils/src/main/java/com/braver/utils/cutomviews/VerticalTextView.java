/*
 *
 *  * Created by https://github.com/braver-tool on 16/11/21, 10:30 AM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 23/11/21, 03:40 PM
 *
 */

package com.braver.utils.cutomviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.text.TextPaint;
import android.util.AttributeSet;

import com.braver.utils.R;

/**
 * How to use VerticalTextView
 * <p>
 * On your XML
 * <com.braver.utils.cutomviews.VerticalTextView
 * android:id="@+id/verticalTextView"
 * android:layout_width="81dp"
 * android:layout_height="266dp"
 * android:layout_marginTop="213dp"
 * android:padding="5dp"
 * android:text="@string/braver_tool"
 * android:textColor="@color/black"
 * android:textSize="12sp"
 * app:vt_align_top_to_btm="true" />
 */
public class VerticalTextView extends androidx.appcompat.widget.AppCompatTextView {
    private boolean vTopToDown = true;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec);
        setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        TextPaint textPaint = getPaint();
        textPaint.setColor(getCurrentTextColor());
        //Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.ti_ti_lli_um_web_regular);
        //textPaint.setTypeface(typeface);
        textPaint.drawableState = getDrawableState();
        canvas.save();
        if (vTopToDown) {
            canvas.translate(0, getHeight());
            canvas.rotate(-90);
        } else {
            canvas.translate(getWidth(), 0);
            canvas.rotate(90);
        }
        canvas.translate(getCompoundPaddingLeft(), getExtendedPaddingTop());
        getLayout().draw(canvas);
        canvas.restore();
    }

    public VerticalTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
       /* final int gravity = getGravity();
        if (Gravity.isVertical(gravity) && (gravity & Gravity.VERTICAL_GRAVITY_MASK) == Gravity.BOTTOM) {
            setGravity((gravity & Gravity.HORIZONTAL_GRAVITY_MASK) | Gravity.TOP);
            topDown = false;
        } else topDown = true;*/
    }

    private void init(final Context context, final AttributeSet attrs) {
        if (isInEditMode())
            return;
        if (null == attrs) {
            throw new IllegalArgumentException("Attributes should be provided to this view,");
        }
        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.VerticalTextView);
        vTopToDown = typedArray.getBoolean(R.styleable.VerticalTextView_vt_align_top_to_btm, true);
        typedArray.recycle();
    }
}
