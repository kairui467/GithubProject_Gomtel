package com.igeak.customwatchface.view.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by xuqiang on 16-7-18.
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    private static final int[] ATTRS = new int[]{
            android.R.attr.listDivider
    };

    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;
    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;
    public static final int GRID = 2;

    private Drawable mDivider_horizon;
    private Drawable mDivider_vertical;

    private int mOrientation;

    public DividerItemDecoration(Context context, int orientation, Drawable drawable_horizon,
                                 Drawable drawable_vertical) {
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        if (drawable_horizon != null) {
            mDivider_horizon = drawable_horizon;
        }else {
            mDivider_horizon = a.getDrawable(0);
        }
        if (mDivider_vertical != null) {
            mDivider_vertical = drawable_vertical;
        }else {
            mDivider_vertical = a.getDrawable(0);
        }


        a.recycle();
        setOrientation(orientation);
    }

    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST && orientation != GRID) {
            throw new IllegalArgumentException("invalid orientation");
        }
        mOrientation = orientation;
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (mOrientation == VERTICAL_LIST) {
            drawVertical(c, parent);
        } else if (mOrientation == HORIZONTAL_LIST) {
            drawHorizontal(c, parent);
        } else {
            drawVertical(c, parent);
            drawHorizontal(c, parent);
        }
    }

    public void drawVertical(Canvas c, RecyclerView parent) {
        if (parent.getChildCount() == 0) return;

        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();

        final View child = parent.getChildAt(0);
        if (child.getHeight() == 0) return;

        final RecyclerView.LayoutParams params =
                (RecyclerView.LayoutParams) child.getLayoutParams();
        int top = child.getBottom() + params.bottomMargin + mDivider_vertical.getIntrinsicHeight();
        int bottom = top + mDivider_vertical.getIntrinsicHeight();

        final int parentBottom = parent.getHeight() - parent.getPaddingBottom();
        while (bottom < parentBottom) {
            mDivider_vertical.setBounds(left, top, right, bottom);
            mDivider_vertical.draw(c);

            top += mDivider_vertical.getIntrinsicHeight() + params.topMargin + child.getHeight()
                    + params
                    .bottomMargin + mDivider_vertical.getIntrinsicHeight();
            bottom = top + mDivider_vertical.getIntrinsicHeight();
        }
    }

    public void drawHorizontal(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params =
                    (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getRight() + params.rightMargin + mDivider_horizon
                    .getIntrinsicHeight();
            final int right = left + mDivider_horizon.getIntrinsicWidth();
            mDivider_horizon.setBounds(left, top, right, bottom);
            mDivider_horizon.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State
            state) {
        if (mOrientation == VERTICAL_LIST) {
            outRect.set(0, 0, 0, mDivider_vertical.getIntrinsicHeight());
        } else if (mOrientation == HORIZONTAL_LIST) {
            outRect.set(0, 0, mDivider_horizon.getIntrinsicWidth(), 0);
        } else {
            outRect.set(0, 0, mDivider_horizon.getIntrinsicWidth(), mDivider_vertical
                    .getIntrinsicHeight());
        }
    }
}