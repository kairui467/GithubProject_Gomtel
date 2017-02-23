package com.igeak.customwatchface.view.view;

/**
 * Created by xuqiang on 16-7-19.
 */

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ItemDecorationAlbumColumns extends RecyclerView.ItemDecoration {

    private int mSizeGridSpacingPx_horizon;
    private int mSizeGridSpacingPx_vertical;
    private int mGridSize;

    private boolean mNeedLeftSpacing = false;

    public ItemDecorationAlbumColumns(int gridSpacingPx_horizon, int gridSpacingPx_vertical, int
            gridSize) {
        mSizeGridSpacingPx_horizon = gridSpacingPx_horizon;
        mSizeGridSpacingPx_vertical = gridSpacingPx_vertical;
        mGridSize = gridSize;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State
            state) {
        int frameWidth = (int) ((parent.getWidth() - (float) mSizeGridSpacingPx_horizon *
                (mGridSize - 1)
        ) / mGridSize);
        int padding = parent.getWidth() / mGridSize - frameWidth;
        int itemPosition = ((RecyclerView.LayoutParams) view.getLayoutParams())
                .getViewAdapterPosition();
        if (itemPosition < mGridSize) {
            outRect.top = 0;
        } else {
            outRect.top = mSizeGridSpacingPx_vertical;
        }
        if (itemPosition % mGridSize == 0) {
            outRect.left = 0;
            outRect.right = padding;
            mNeedLeftSpacing = true;
        } else if ((itemPosition + 1) % mGridSize == 0) {
            mNeedLeftSpacing = false;
            outRect.right = 0;
            outRect.left = padding;
        } else if (mNeedLeftSpacing) {
            mNeedLeftSpacing = false;
            outRect.left = mSizeGridSpacingPx_horizon - padding;
            if ((itemPosition + 2) % mGridSize == 0) {
                outRect.right = mSizeGridSpacingPx_horizon - padding;
            } else {
                outRect.right = mSizeGridSpacingPx_horizon / 2;
            }
        } else if ((itemPosition + 2) % mGridSize == 0) {
            mNeedLeftSpacing = false;
            outRect.left = mSizeGridSpacingPx_horizon / 2;
            outRect.right = mSizeGridSpacingPx_horizon - padding;
        } else {
            mNeedLeftSpacing = false;
            outRect.left = mSizeGridSpacingPx_horizon / 2;
            outRect.right = mSizeGridSpacingPx_horizon / 2;
        }
        outRect.bottom = 0;
    }
}