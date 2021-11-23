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
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.braver.utils.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A ChipsLayout for Android, which allows child views flow to next row when there is no enough space.
 * The spacing between child views can be calculated by the ChipsLayout so that the views are evenly placed.
 * <p>
 * How to use this,
 * Add the ChipsLayout on your XML
 * Code ~ OnCreate(where else you want)
 * ChipsLayout chipsLayout = findViewById(R.id.chipsLayout);
 * String[] childArrayTexts = getResources().getStringArray(R.array.childArray);
 * <p>
 * for (String text : childArrayTexts) {
 * TextView textView = buildTextView(text);
 * chipsLayout.addView(textView);
 * <p>
 * private TextView buildTextView(String text) {
 * TextView textView = new TextView(this);
 * textView.setText(text);
 * textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
 * textView.setPadding((int)dpToPx(18), (int)dpToPx(9), (int)dpToPx(18), (int)dpToPx(9));
 * textView.setBackgroundResource(R.drawable.label_bg);
 * <p>
 * return textView;
 * }
 * private float dpToPx(float dp){
 * return TypedValue.applyDimension(
 * TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
 * }
 */

public class ChipsLayout extends ViewGroup {
    /**
     * Special value for the child view spacing.
     * SPACING_AUTO means that the actual spacing is calculated according to the size of the
     * container and the number of the child views, so that the child views are placed evenly in
     * the container.
     */
    private static final int SPACING_AUTO = -65536;

    /**
     * Special value for the horizontal spacing of the child views in the last row
     * SPACING_ALIGN means that the horizontal spacing of the child views in the last row keeps
     * the same with the spacing used in the row above. If there is only one row, this value is
     * ignored and the spacing will be calculated according to childSpacing.
     */
    private static final int SPACING_ALIGN = -65537;

    private static final int SPACING_UNDEFINED = -65538;

    private static final int UNSPECIFIED_GRAVITY = -1;

    private static final int ROW_VERTICAL_GRAVITY_AUTO = -65536;

    private static final boolean DEFAULT_FLOW = true;
    private static final int DEFAULT_CHILD_SPACING = 0;
    private static final float DEFAULT_ROW_SPACING = 0;
    private static final boolean DEFAULT_RTL = false;
    private static final int DEFAULT_MAX_ROWS = Integer.MAX_VALUE;

    private final boolean mFlow;
    private int mChildSpacing;
    private int mMinChildSpacing;
    private int mChildSpacingForLastRow;
    private float mRowSpacing;
    private float mAdjustedRowSpacing = DEFAULT_ROW_SPACING;
    private final boolean mRtl;
    private final int mMaxRows;
    private final int mGravity;
    private final int mRowVerticalGravity;
    private int mExactMeasuredHeight;

    private final List<Float> mHorizontalSpacingForRow = new ArrayList<>();
    private final List<Integer> mHeightForRow = new ArrayList<>();
    private final List<Integer> mWidthForRow = new ArrayList<>();
    private final List<Integer> mChildNumForRow = new ArrayList<>();

    public ChipsLayout(Context context) {
        this(context, null);
    }

    /**
     * Declaration initItemDimensionIfNeeded()
     * Method used to display calendar view
     * Declared In CustomGridView.java
     */
    public ChipsLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.FlowLayout, 0, 0);
        try {
            mFlow = a.getBoolean(R.styleable.FlowLayout_flFlow, DEFAULT_FLOW);
            try {
                mChildSpacing = a.getInt(R.styleable.FlowLayout_flChildSpacing, DEFAULT_CHILD_SPACING);
            } catch (NumberFormatException e) {
                mChildSpacing = a.getDimensionPixelSize(R.styleable.FlowLayout_flChildSpacing, (int) dpToPx(DEFAULT_CHILD_SPACING));
            }
            try {
                mMinChildSpacing = a.getInt(R.styleable.FlowLayout_flMinChildSpacing, DEFAULT_CHILD_SPACING);
            } catch (NumberFormatException e) {
                mMinChildSpacing = a.getDimensionPixelSize(R.styleable.FlowLayout_flMinChildSpacing, (int) dpToPx(DEFAULT_CHILD_SPACING));
            }
            try {
                mChildSpacingForLastRow = a.getInt(R.styleable.FlowLayout_flChildSpacingForLastRow, SPACING_UNDEFINED);
            } catch (NumberFormatException e) {
                mChildSpacingForLastRow = a.getDimensionPixelSize(R.styleable.FlowLayout_flChildSpacingForLastRow, (int) dpToPx(DEFAULT_CHILD_SPACING));
            }
            try {
                mRowSpacing = a.getInt(R.styleable.FlowLayout_flRowSpacing, 0);
            } catch (NumberFormatException e) {
                mRowSpacing = a.getDimension(R.styleable.FlowLayout_flRowSpacing, dpToPx(DEFAULT_ROW_SPACING));
            }
            mMaxRows = a.getInt(R.styleable.FlowLayout_flMaxRows, DEFAULT_MAX_ROWS);
            mRtl = a.getBoolean(R.styleable.FlowLayout_flRtl, DEFAULT_RTL);
            mGravity = a.getInt(R.styleable.FlowLayout_android_gravity, UNSPECIFIED_GRAVITY);
            mRowVerticalGravity = a.getInt(R.styleable.FlowLayout_flRowVerticalGravity, ROW_VERTICAL_GRAVITY_AUTO);
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        mHorizontalSpacingForRow.clear();
        mHeightForRow.clear();
        mWidthForRow.clear();
        mChildNumForRow.clear();

        int measuredHeight = 0, measuredWidth = 0, childCount = getChildCount();
        int rowWidth = 0, maxChildHeightInRow = 0, childNumInRow = 0;
        final int rowSize = widthSize - getPaddingLeft() - getPaddingRight();
        int rowTotalChildWidth = 0;
        final boolean allowFlow = widthMode != MeasureSpec.UNSPECIFIED && mFlow;
        final int childSpacing = mChildSpacing == SPACING_AUTO && widthMode == MeasureSpec.UNSPECIFIED ? 0 : mChildSpacing;
        final float tmpSpacing = childSpacing == SPACING_AUTO ? mMinChildSpacing : childSpacing;

        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }

            LayoutParams childParams = child.getLayoutParams();
            int horizontalMargin = 0, verticalMargin = 0;
            if (childParams instanceof MarginLayoutParams) {
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, measuredHeight);
                MarginLayoutParams marginParams = (MarginLayoutParams) childParams;
                horizontalMargin = marginParams.leftMargin + marginParams.rightMargin;
                verticalMargin = marginParams.topMargin + marginParams.bottomMargin;
            } else {
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
            }

            int childWidth = child.getMeasuredWidth() + horizontalMargin;
            int childHeight = child.getMeasuredHeight() + verticalMargin;
            if (allowFlow && rowWidth + childWidth > rowSize) { // Need flow to next row
                // Save parameters for current row
                mHorizontalSpacingForRow.add(getSpacingForRow(childSpacing, rowSize, rowTotalChildWidth, childNumInRow));
                mChildNumForRow.add(childNumInRow);
                mHeightForRow.add(maxChildHeightInRow);
                mWidthForRow.add(rowWidth - (int) tmpSpacing);
                if (mHorizontalSpacingForRow.size() <= mMaxRows) {
                    measuredHeight += maxChildHeightInRow;
                }
                measuredWidth = Math.max(measuredWidth, rowWidth);

                // Place the child view to next row
                childNumInRow = 1;
                rowWidth = childWidth + (int) tmpSpacing;
                rowTotalChildWidth = childWidth;
                maxChildHeightInRow = childHeight;
            } else {
                childNumInRow++;
                rowWidth += childWidth + tmpSpacing;
                rowTotalChildWidth += childWidth;
                maxChildHeightInRow = Math.max(maxChildHeightInRow, childHeight);
            }
        }

        // Measure remaining child views in the last row
        if (mChildSpacingForLastRow == SPACING_ALIGN) {
            // For SPACING_ALIGN, use the same spacing from the row above if there is more than one
            // row.
            if (mHorizontalSpacingForRow.size() >= 1) {
                mHorizontalSpacingForRow.add(mHorizontalSpacingForRow.get(mHorizontalSpacingForRow.size() - 1));
            } else {
                mHorizontalSpacingForRow.add(getSpacingForRow(childSpacing, rowSize, rowTotalChildWidth, childNumInRow));
            }
        } else if (mChildSpacingForLastRow != SPACING_UNDEFINED) {
            // For SPACING_AUTO and specific DP values, apply them to the spacing strategy.
            mHorizontalSpacingForRow.add(getSpacingForRow(mChildSpacingForLastRow, rowSize, rowTotalChildWidth, childNumInRow));
        } else {
            // For SPACING_UNDEFINED, apply childSpacing to the spacing strategy for the last row.
            mHorizontalSpacingForRow.add(getSpacingForRow(childSpacing, rowSize, rowTotalChildWidth, childNumInRow));
        }

        mChildNumForRow.add(childNumInRow);
        mHeightForRow.add(maxChildHeightInRow);
        mWidthForRow.add(rowWidth - (int) tmpSpacing);
        if (mHorizontalSpacingForRow.size() <= mMaxRows) {
            measuredHeight += maxChildHeightInRow;
        }
        measuredWidth = Math.max(measuredWidth, rowWidth);

        if (childSpacing == SPACING_AUTO) {
            measuredWidth = widthSize;
        } else if (widthMode == MeasureSpec.UNSPECIFIED) {
            measuredWidth = measuredWidth + getPaddingLeft() + getPaddingRight();
        } else {
            measuredWidth = Math.min(measuredWidth + getPaddingLeft() + getPaddingRight(), widthSize);
        }

        measuredHeight += getPaddingTop() + getPaddingBottom();
        int rowNum = Math.min(mHorizontalSpacingForRow.size(), mMaxRows);
        float rowSpacing = mRowSpacing == SPACING_AUTO && heightMode == MeasureSpec.UNSPECIFIED ? 0 : mRowSpacing;
        if (rowSpacing == SPACING_AUTO) {
            if (rowNum > 1) {
                mAdjustedRowSpacing = (heightSize - measuredHeight) / (rowNum - 1);
            } else {
                mAdjustedRowSpacing = 0;
            }
            measuredHeight = heightSize;
        } else {
            mAdjustedRowSpacing = rowSpacing;
            if (rowNum > 1) {
                measuredHeight = heightMode == MeasureSpec.UNSPECIFIED ? ((int) (measuredHeight + mAdjustedRowSpacing * (rowNum - 1))) : (Math.min((int) (measuredHeight + mAdjustedRowSpacing * (rowNum - 1)), heightSize));
            }
        }

        mExactMeasuredHeight = measuredHeight;

        measuredWidth = widthMode == MeasureSpec.EXACTLY ? widthSize : measuredWidth;
        measuredHeight = heightMode == MeasureSpec.EXACTLY ? heightSize : measuredHeight;

        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    //This is used for set padding and gravity
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int paddingLeft = getPaddingLeft(), paddingRight = getPaddingRight(), paddingTop = getPaddingTop(), paddingBottom = getPaddingBottom();

        int x = mRtl ? (getWidth() - paddingRight) : paddingLeft;
        int y = paddingTop;

        int verticalGravity = mGravity & Gravity.VERTICAL_GRAVITY_MASK;
        int horizontalGravity = mGravity & Gravity.HORIZONTAL_GRAVITY_MASK;

        switch (verticalGravity) {
            case Gravity.CENTER_VERTICAL: {
                int offset = (b - t - paddingTop - paddingBottom - mExactMeasuredHeight) / 2;
                y += offset;
                break;
            }
            case Gravity.BOTTOM: {
                int offset = b - t - paddingTop - paddingBottom - mExactMeasuredHeight;
                y += offset;
                break;
            }
            default:
                break;
        }

        int horizontalPadding = paddingLeft + paddingRight, layoutWidth = r - l;
        x += getHorizontalGravityOffsetForRow(horizontalGravity, layoutWidth, horizontalPadding, 0);

        int verticalRowGravity = mRowVerticalGravity & Gravity.VERTICAL_GRAVITY_MASK;

        int rowCount = mChildNumForRow.size(), childIdx = 0;
        for (int row = 0; row < rowCount; row++) {
            int childNum = mChildNumForRow.get(row);
            int rowHeight = mHeightForRow.get(row);
            float spacing = mHorizontalSpacingForRow.get(row);
            for (int i = 0; i < childNum && childIdx < getChildCount(); ) {
                View child = getChildAt(childIdx++);
                if (child.getVisibility() == GONE) {
                    continue;
                } else {
                    i++;
                }

                LayoutParams childParams = child.getLayoutParams();
                int marginLeft = 0, marginTop = 0, marginBottom = 0, marginRight = 0;
                if (childParams instanceof MarginLayoutParams) {
                    MarginLayoutParams marginParams = (MarginLayoutParams) childParams;
                    marginLeft = marginParams.leftMargin;
                    marginRight = marginParams.rightMargin;
                    marginTop = marginParams.topMargin;
                    marginBottom = marginParams.bottomMargin;
                }

                int childWidth = child.getMeasuredWidth();
                int childHeight = child.getMeasuredHeight();
                int tt = y + marginTop;
                if (verticalRowGravity == Gravity.BOTTOM) {
                    tt = y + rowHeight - marginBottom - childHeight;
                } else if (verticalRowGravity == Gravity.CENTER_VERTICAL) {
                    tt = y + marginTop + (rowHeight - marginTop - marginBottom - childHeight) / 2;
                }
                int bb = tt + childHeight;
                if (mRtl) {
                    int l1 = x - marginRight - childWidth;
                    int r1 = x - marginRight;
                    child.layout(l1, tt, r1, bb);
                    x -= childWidth + spacing + marginLeft + marginRight;
                } else {
                    int l2 = x + marginLeft;
                    int r2 = x + marginLeft + childWidth;
                    child.layout(l2, tt, r2, bb);
                    x += childWidth + spacing + marginLeft + marginRight;
                }
            }
            x = mRtl ? (getWidth() - paddingRight) : paddingLeft;
            x += getHorizontalGravityOffsetForRow(horizontalGravity, layoutWidth, horizontalPadding, row + 1);
            y += rowHeight + mAdjustedRowSpacing;
        }
    }

    /**
     * Declaration getHorizontalGravityOffsetForRow()
     * Method used to set calendar horizontal row
     * Declared In CustomGridView.java
     */
    private int getHorizontalGravityOffsetForRow(int horizontalGravity, int parentWidth, int horizontalPadding, int row) {
        if (mChildSpacing == SPACING_AUTO || row >= mWidthForRow.size() || row >= mChildNumForRow.size() || mChildNumForRow.get(row) <= 0) {
            return 0;
        }

        int offset = 0;
        switch (horizontalGravity) {
            case Gravity.CENTER_HORIZONTAL:
                offset = (parentWidth - horizontalPadding - mWidthForRow.get(row)) / 2;
                break;
            case Gravity.END:
                offset = parentWidth - horizontalPadding - mWidthForRow.get(row);
                break;
            default:
                break;
        }
        return offset;
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    /**
     * Declaration getSpacingForRow()
     * Method used to set calendar spacing in a row
     * Declared In CustomGridView.java
     */
    private float getSpacingForRow(int spacingAttribute, int rowSize, int usedSize, int childNum) {
        float spacing;
        if (spacingAttribute == SPACING_AUTO) {
            if (childNum > 1) {
                spacing = (rowSize - usedSize) / (childNum - 1);
            } else {
                spacing = 0;
            }
        } else {
            spacing = spacingAttribute;
        }
        return spacing;
    }

    private float dpToPx(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}
