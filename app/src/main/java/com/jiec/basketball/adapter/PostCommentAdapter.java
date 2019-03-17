package com.jiec.basketball.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import com.jiec.basketball.R;
import com.jiec.basketball.core.BallApplication;
import com.jiec.basketball.core.UserManager;
import com.jiec.basketball.entity.response.NewsCommentResponse;
import com.jiec.basketball.entity.response.NewsCommentResponse.ResultBean.CommentsBean;
import com.jiec.basketball.network.NetSubscriber;
import com.jiec.basketball.network.NetTransformer;
import com.jiec.basketball.network.NewsApi;
import com.jiec.basketball.network.RetrofitClient;
import com.jiec.basketball.network.base.CommResponse;
import com.jiec.basketball.utils.AppUtil;
import com.jiec.basketball.utils.EmptyUtils;
import com.jiec.basketball.utils.ImageLoaderUtils;
import com.jiec.basketball.utils.InputCheckUtils;
import com.jiec.basketball.widget.UserReplyView;
import com.wangcj.common.utils.ToastUtil;
import com.wangcj.common.widget.CircleSImageView;

/**
 * Created by Deng
 * 新聞評論列表适配器
 */

public class PostCommentAdapter extends BaseQuickAdapter<CommentsBean, BaseViewHolder> {

    public PostCommentAdapter(List<CommentsBean> data) {
        super(R.layout.item_news_comment, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, CommentsBean hisBean) {
        CircleSImageView ivHead = baseViewHolder.itemView.findViewById(R.id.iv_head);
        ImageLoaderUtils.display(mContext, ivHead,
                hisBean.getUser_img(),
                R.drawable.img_default_head, R.drawable.img_default_head);
        baseViewHolder.setText(R.id.tv_name, hisBean.getComment_author())
                .setText(R.id.tv_time, AppUtil.getCommentTime(hisBean.getComment_date()))
                .setText(R.id.tv_comment, hisBean.getComment_content())
                .setText(R.id.tv_like, InputCheckUtils.compareIsEqual(hisBean.getTotal_like(), "0") ? "讃" : "（"+hisBean.getTotal_like()+"）")
                .addOnClickListener(R.id.tv_reply);

        ImageView mIvLike = baseViewHolder.itemView.findViewById(R.id.iv_like);
        TextView mTvLike = baseViewHolder.itemView.findViewById(R.id.tv_like);
        if (hisBean.getMy_like() == 0) {
            mIvLike.setImageResource(R.drawable.icon_great_normal);
            mTvLike.setTextColor(mContext.getResources().getColor(R.color.gray));
        } else {
            mIvLike.setImageResource(R.drawable.icon_great_pressed);
            mTvLike.setTextColor(mContext.getResources().getColor(R.color.red));
        }

        mIvLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(BallApplication.userInfo == null){
                    ToastUtil.showMsg("請先登錄");
                    return;
                }
                CommentsBean hisBean = mData.get(baseViewHolder.getAdapterPosition());
                int isLike = hisBean.getMy_like() == 1 ? 0 : 1; //0=取消點讚；1=點讚
                like(hisBean.getPost_id(), hisBean.getComment_id(), isLike, mTvLike, mIvLike);
            }
        });

        LinearLayout llReply = baseViewHolder.itemView.findViewById(R.id.ll_reply);
        /****************設置評論回復View********************/
        int replySize = EmptyUtils.emptyOfObject(hisBean.getReply()) ? 0 : hisBean.getReply().size();
        if (replySize > 0) {
            llReply.removeAllViews();
            List<NewsCommentResponse.ResultBean.CommentsBean.ReplyBean> replyList = hisBean.getReply();
            llReply.setVisibility(View.VISIBLE);
//         lMore.setVisibility(View.VISIBLE);
            for (int i = 0; i < replySize; i++) {
                UserReplyView userReplyView = new UserReplyView(mContext);
                userReplyView.setReplyData(replyList.get(i));
                llReply.addView(userReplyView);
            }
        } else {
            llReply.setVisibility(View.GONE);
        }

    }

    /**
     * 提交点赞
     *
     * @param postId
     * @param commentId
     * @param like
     * @param tvLikeNum
     * @param ivLike
     */
    private void like(String postId, String commentId, final int like, final TextView tvLikeNum, final ImageView ivLike) {
        NewsApi newsApi = RetrofitClient.getInstance().create(NewsApi.class);
        newsApi.likeComment(BallApplication.userInfo.user_token, postId, commentId, like)
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
