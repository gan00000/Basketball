package com.jiec.basketball.widget;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;

/**
 * RecycleView分割线
 * 支持LinearLayoutManager、GridLayoutManager、StaggeredGridLayoutManager
 */
public class RvDividerItemDecoration extends RecyclerView.ItemDecoration {

    private Drawable mDivider;
    private int dividerHeight = 1;
    private int verticalDividerHeight = 1; //垂直方向间距

    public RvDividerItemDecoration(int color, int dividerHeight) {
        mDivider = new ColorDrawable(color);
        this.dividerHeight = dividerHeight;
    }

    public RvDividerItemDecoration(int color, int dividerHeight, int verticalDividerHeight) {
        mDivider = new ColorDrawable(color);
        this.dividerHeight = dividerHeight;
        this.verticalDividerHeight = verticalDividerHeight;
    }

    public RvDividerItemDecoration(Drawable mDivider){
        this.mDivider = mDivider;
        this.dividerHeight = mDivider.getIntrinsicHeight();
    }

    public RvDividerItemDecoration(Drawable mDivider, int dividerHeight){
        this.mDivider = mDivider;
        this.dividerHeight = dividerHeight;
        this.verticalDividerHeight = verticalDividerHeight;
    }

    public RvDividerItemDecoration(Drawable mDivider, int dividerHeight, int verticalDividerHeight){
        this.mDivider = mDivider;
        this.dividerHeight = dividerHeight;
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {

        RecyclerView.LayoutManager mManager = parent.getLayoutManager();

        if (mManager instanceof GridLayoutManager) {
            drawGridVerticalDivider(c, parent);
            drawGridHorizontalDivider(c, parent);
        } else if (mManager instanceof LinearLayoutManager) {
            int orientation = ((LinearLayoutManager) mManager).getOrientation();
            if (orientation == LinearLayoutManager.VERTICAL) {
                drawLinearVerticalDivider(c, parent);
            } else {
                drawLinearHorizontalDivider(c, parent);
            }
        } else if (mManager instanceof StaggeredGridLayoutManager) {
            drawStaggVerticalDivider(c, parent);
            drawStaggHorizontalDivider(c, parent);
        }
    }

    public void drawLinearVerticalDivider(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();

        final int childCount = parent.getChildCount();

        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin +
                    Math.round(ViewCompat.getTranslationY(child));
            final int bottom = top + dividerHeight;
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    public void drawLinearHorizontalDivider(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        int childCount = parent.getChildCount();
        final int bottom = parent.getHeight() - parent.getPaddingTop() - parent.getPaddingBottom();

        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = child.getRight() + params.rightMargin +
                    Math.round(ViewCompat.getTranslationX(child));
            final int right = left + dividerHeight;
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    public void drawGridVerticalDivider(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();

        for (int i = 0; i < childCount; i++) {

            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = child.getLeft() + params.leftMargin;
            final int right = left + child.getRight();
            final int top = child.getBottom() + params.bottomMargin +
                    Math.round(ViewCompat.getTranslationY(child));
            final int bottom = top + verticalDividerHeight;
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    public void drawGridHorizontalDivider(Canvas c, RecyclerView parent) {
        final int childCount = parent.getChildCount();
        int spanCount = ((GridLayoutManager) parent.getLayoutManager()).getSpanCount();

        /**--------------RecyciewView有headView时不使用计算规则画图----------------------*/
        for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                final int left = child.getRight() + params.rightMargin +
                        Math.round(ViewCompat.getTranslationX(child));
                final int top = child.getTop() + params.topMargin;
                final int right = left + dividerHeight;
                final int bottom = top + child.getBottom() + params.bottomMargin;
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
        }

        /**----------使用计算规则画图------------------------*/
//        for (int i = 0; i < childCount; i++) {
//            if (i % spanCount != spanCount - 1) {
//                final View child = parent.getChildAt(i);
//                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
//                final int left = child.getRight() + params.rightMargin +
//                        Math.round(ViewCompat.getTranslationX(child));
//                final int top = child.getTop() + params.topMargin;
//                final int right = left + dividerHeight;
//                final int bottom = top + child.getBottom() + params.bottomMargin;
//                mDivider.setBounds(left, top, right, bottom);
//                mDivider.draw(c);
//            }
//        }
    }

    public void drawStaggVerticalDivider(Canvas c, RecyclerView parent) {
        final int childCount = parent.getChildCount();

        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);

            final int left = child.getLeft();
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + dividerHeight;
            final int right = child.getRight() + params.rightMargin;
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    public void drawStaggHorizontalDivider(Canvas c, RecyclerView parent) {
        final int childCount = parent.getChildCount();

        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getTop() + params.topMargin;
            final int left = child.getRight() + params.rightMargin +
                    Math.round(ViewCompat.getTranslationX(child));
            final int bottom = top + child.getBottom() + dividerHeight ;
            final int right = left + dividerHeight;
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        RecyclerView.LayoutManager mManager = parent.getLayoutManager();

        if (mManager instanceof GridLayoutManager) {
            outRect.set(0,0, dividerHeight / 2, dividerHeight / 2);
        } else if (mManager instanceof LinearLayoutManager) {
            LinearLayoutManager mLinearLayoutManager = (LinearLayoutManager) mManager;
            int orientation = mLinearLayoutManager.getOrientation();

            if (orientation == LinearLayoutManager.VERTICAL) {
                outRect.set(0, 0, 0, dividerHeight);
            } else {
                outRect.set(0, 0, dividerHeight, 0);
            }
        } else if (mManager instanceof StaggeredGridLayoutManager) {
            outRect.set(0,0,dividerHeight,dividerHeight);
        }
    }

    private void logMsg(String msg) {
        Log.e("DD", msg);
    }

}
