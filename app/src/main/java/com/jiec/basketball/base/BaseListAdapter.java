package com.jiec.basketball.base;

import androidx.annotation.StringRes;
import androidx.recyclerview.widget.RecyclerView;

import com.jiec.basketball.core.BallApplication;
import com.jiec.basketball.utils.EmptyUtils;

import java.util.List;

/**
 * 列表适配器
 * Created by jiec on 2017/6/10.
 */

public abstract class BaseListAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected List<T> mData;
    protected OnItemClickedListener mOnItemClickedListener;

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        mOnItemClickedListener = onItemClickedListener;
    }

    /**
     * 设置列表数据
     *
     * @param data
     */
    public void setData(List<T> data) {
        this.mData = data;
        this.notifyDataSetChanged();
    }

    /**
     * 添加列表数据
     * @param data
     */
    public void addData(List<T> data) {
        if (this.mData == null) {
            setData(data);
        } else {
            if(EmptyUtils.emptyOfList(data)){
                return;
            }
            this.mData.addAll(data);
            this.notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        if (mData != null) {
            return mData.size();
        }

        return 0;
    }

    /**
     * 获取数据
     * @param position
     * @return
     */
    public T getItem(int position) {
        if (mData != null && position >= 0 && position < mData.size()) {
            return mData.get(position);
        }

        return null;
    }

    protected String getString(int stringId) {
        return BallApplication.getContext().getString(stringId);
    }

    public final String getString(@StringRes int resId, Object... formatArgs) {
        return BallApplication.getContext().getString(resId, formatArgs);
    }

    public interface OnItemClickedListener<V> {
        void onClick(V data);
    }
}
