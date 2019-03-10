package com.jiec.basketball.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import com.jiec.basketball.R;
import com.jiec.basketball.core.UserManager;
import com.jiec.basketball.entity.response.NewsCommentResponse.ResultBean.CommentsBean;
import com.jiec.basketball.network.NetSubscriber;
import com.jiec.basketball.network.NetTransformer;
import com.jiec.basketball.network.NewsApi;
import com.jiec.basketball.network.RetrofitClient;
import com.jiec.basketball.network.base.CommResponse;
import com.jiec.basketball.utils.AppUtil;
import com.jiec.basketball.utils.ImageLoaderUtils;
import com.wangcj.common.utils.ToastUtil;
import com.wangcj.common.widget.CircleSImageView;

/**
 * Created by Deng
 * 新聞評論列表适配器
 */

public class PostCommentAdapter extends BaseQuickAdapter<CommentsBean> {

    public PostCommentAdapter(List<CommentsBean> data) {
        super(R.layout.item_news_comment, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, CommentsBean hisBean) {
        CircleSImageView ivHead = baseViewHolder.convertView.findViewById(R.id.iv_head);
        ImageLoaderUtils.display(mContext, ivHead,
                hisBean.getUser_img(),
                R.drawable.img_default_head, R.drawable.img_default_head);
        baseViewHolder.setText(R.id.tv_name, hisBean.getComment_author())
                .setText(R.id.tv_time, AppUtil.getStandardDate(hisBean.getComment_date()))
                .setText(R.id.tv_comment, hisBean.getComment_content())
                .setText(R.id.tv_like, hisBean.getTotal_like())
                .addOnClickListener(R.id.tv_reply)
                ;

        ImageView mIvLike = baseViewHolder.convertView.findViewById(R.id.iv_like);
        TextView mTvLike = baseViewHolder.convertView.findViewById(R.id.tv_like);
        boolean isLike = false;
        if (hisBean.getMy_like() == 0) {
           mIvLike.setImageResource(R.drawable.icon_great_normal);
          mTvLike.setTextColor(mContext.getResources().getColor(R.color.gray));
        } else {
            isLike = true;
            mIvLike.setImageResource(R.drawable.icon_great_pressed);
            mTvLike.setTextColor(mContext.getResources().getColor(R.color.red));
        }

        final int _isLike = isLike ? 0 : 1;
       mIvLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                like(hisBean.getPost_id(), hisBean.getComment_id(), _isLike,
                        mTvLike, mIvLike);
            }
        });

    }

    /**
     * 提交点赞
     * @param postId
     * @param commentId
     * @param like
     * @param tvLikeNum
     * @param ivLike
     */
    private void like(String postId, String commentId, final int like, final TextView tvLikeNum, final ImageView ivLike) {
        NewsApi newsApi = RetrofitClient.getInstance().create(NewsApi.class);
        newsApi.likeComment(UserManager.instance().getToken(), postId, commentId, like)
                .compose(new NetTransformer<>())
                .subscribe(new NetSubscriber<CommResponse>() {
                    @Override
                    protected void onSuccess(CommResponse result) {
                        if (like == 1) {
                            ivLike.setImageResource(R.drawable.icon_great_pressed);
                            tvLikeNum.setText(String.valueOf(Integer.valueOf(tvLikeNum.getText().toString()) + 1));
                        } else {
                            ivLike.setImageResource(R.drawable.icon_great_normal);
                            tvLikeNum.setText(String.valueOf(Integer.valueOf(tvLikeNum.getText().toString()) - 1));
                        }

                        if (Integer.valueOf(tvLikeNum.getText().toString()) == 0) {
                            tvLikeNum.setTextColor(mContext.getResources().getColor(R.color.gray));
                        } else {
                            tvLikeNum.setTextColor(mContext.getResources().getColor(R.color.red));
                        }
                    }

                    @Override
                    protected void onFailed(int code, String reason) {
                        ToastUtil.showMsg(reason);
                    }
                });
    }


}
