
package com.jiec.basketball.widget;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiec.basketball.R;


/**
 * Create by jiec at 2017/3/10
 */
public class TipsLayout extends RelativeLayout {

    /**
     * 显示正在加载提示
     */
    public static final int TYPE_LOADING = 1;

    /**
     * 显示加载错误提示
     */
    public static final int TYPE_FAILED = 2;

    /**
     * 显示空白提示
     */
    public static final int TYPE_EMPTY_CONTENT = 3;

    /**
     * 显示自定义提示界面
     */
    public static final int TYPE_CUSTOM_VIEW = 4;

    private View mLoadingView;

    private View mLoadFaileView;

    private View mEmptyView;

    private TextView mLoadingText, mEmptyText, mErrorText, mRetryBtn;

    private ImageView mEmptyIcon;

    public TipsLayout(Context context) {
        this(context, null);
    }

    public TipsLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TipsLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mLoadingView = inflater.inflate(R.layout.tipslayout_loading, null);
        mLoadingText = (TextView) mLoadingView.findViewById(R.id.loading_tips);

        mLoadFaileView = inflater.inflate(R.layout.tipslayout_load_failed, null);
        mErrorText = (TextView) mLoadFaileView.findViewById(R.id.tv_error_message);
        mRetryBtn = (TextView) mLoadFaileView.findViewById(R.id.btn_retry);

        mEmptyView = inflater.inflate(R.layout.tipslayout_load_empty, null);
        mEmptyText = (TextView) mEmptyView.findViewById(R.id.tv_empty);
        mEmptyIcon = (ImageView) mEmptyView.findViewById(R.id.iv_empty_icon);


        LayoutParams rlp = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        rlp.addRule(RelativeLayout.CENTER_IN_PARENT);

        addView(mLoadingView, rlp);
        addView(mLoadFaileView, rlp);
        addView(mEmptyView, rlp);
    }

    /**
     * 设置重试按钮点击事件
     *
     * @param listener
     */
    public void setOnRetryButtonClickListener(OnClickListener listener) {
        if (mLoadFaileView != null) {
            mLoadFaileView.setOnClickListener(listener);
        }

        if (mRetryBtn != null) {
            mRetryBtn.setOnClickListener(listener);
        }
    }

    /**
     * 设置刷新按钮点击事件
     *
     * @param listener
     */
    public void setOnRefreshButtonClickListener(OnClickListener listener) {
        if (mEmptyView != null) {
            mEmptyView.setOnClickListener(listener);
        }
    }

    /**
     * 设置加载提示文字
     *
     * @param text
     */
    public void setLoadingText(String text) {
        if (mLoadingText != null) {
            mLoadingText.setText(text);
        }
    }

    /**
     * 设置失败显示文字
     *
     * @param text
     */
    public void setErrorText(String text) {
        if (mErrorText != null) {
            mErrorText.setText(text);
        }
    }

    /**
     * 设置加载提示文字颜色
     *
     * @param color
     */
    public void setLoadingTextColor(int color) {
        if (mLoadingText != null) {
            mLoadingText.setTextColor(color);
        }
    }

    /**
     * 设置空白数据显示界面
     *
     * @param desc
     */
    public void setEmptyText(String desc) {
        if (mEmptyText != null) {
            mEmptyText.setText(desc);
        }
    }

    public void setEmptyIcon(int iconID) {
        if (mEmptyIcon != null) {
            mEmptyIcon.setImageResource(iconID);
        }
    }

    /**
     * 显示提示界面
     *
     * @param showType 显示类型，分为显示loading和显示刷新按钮
     */
    public void show(int showType) {
        setVisibility(View.VISIBLE);
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).setVisibility(View.GONE);
        }
        if (showType == TYPE_LOADING) {
            getChildAt(0).setVisibility(View.VISIBLE);
        } else if (showType == TYPE_FAILED) {
            getChildAt(1).setVisibility(View.VISIBLE);
        } else if (showType == TYPE_EMPTY_CONTENT) {
            getChildAt(2).setVisibility(View.VISIBLE);
        } else if (showType == TYPE_CUSTOM_VIEW) {
            if (getChildCount() == 4) {
                getChildAt(3).setVisibility(View.VISIBLE);
            }
        }

        if (mLoadingView != null) {
            if (mLoadingView.getVisibility() == View.VISIBLE) {
                mLoadingView.findViewById(R.id.progress_loading_bar).setVisibility(View.VISIBLE);
            } else {
                mLoadingView.findViewById(R.id.progress_loading_bar).setVisibility(View.GONE);
            }
        }
    }

    /**
     * 隐藏提示界面
     */
    public void hide() {
        setVisibility(View.GONE);
    }

    /**
     * 设置自定义提示View
     *
     * @param customTipsView
     */
    public void setCustomView(View customTipsView) {
        if (getChildCount() == 3) {
            LayoutParams rlp = new LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT);
            rlp.addRule(RelativeLayout.CENTER_IN_PARENT);

            addView(customTipsView, rlp);
        }
    }

}
