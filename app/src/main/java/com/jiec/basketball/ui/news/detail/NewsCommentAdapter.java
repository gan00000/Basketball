package com.jiec.basketball.ui.news.detail;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiec.basketball.R;
import com.jiec.basketball.adapter.MyCommentAdapter;
import com.jiec.basketball.core.BallApplication;
import com.jiec.basketball.core.UserManager;
import com.jiec.basketball.entity.response.NewsCommentResponse;
import com.jiec.basketball.network.NetSubscriber;
import com.jiec.basketball.network.NetTransformer;
import com.jiec.basketball.network.NewsApi;
import com.jiec.basketball.network.RetrofitClient;
import com.jiec.basketball.network.base.CommResponse;
import com.jiec.basketball.utils.AppUtil;
import com.jiec.basketball.utils.ImageLoaderUtils;
import com.jiec.basketball.widget.UserReplyView;
import com.wangcj.common.utils.ToastUtil;
import com.wangcj.common.widget.CircleSImageView;

import java.util.ArrayList;
import java.util.List;


/**
 * Description : 新闻详情评论列表适配器
 * Author : jiec
 * Date   : 17-1-6
 */
public class NewsCommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    private List<NewsCommentResponse.ResultBean.CommentsBean> mData = new ArrayList<>();

    private boolean mShowFooter = true;

    private Context mContext;
    private ItemChildClickListener itemChildClickListener;

    public NewsCommentAdapter(Context context) {
        mContext = context.getApplicationContext();
    }

    public void addData(List<NewsCommentResponse.ResultBean.CommentsBean> data) {
        this.mData = data;
        this.notifyDataSetChanged();
    }

    public void setItemChildClickListener(ItemChildClickListener itemChildClickListener) {
        this.itemChildClickListener = itemChildClickListener;
    }

    public interface ItemChildClickListener{
        void onItemChildClick(View view, int position);
    }

    /**
     * 添加一个Item数据
     * @param commentsBean
     */
    public void addItemData(NewsCommentResponse.ResultBean.CommentsBean commentsBean){
        this.mData.add(commentsBean);
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为footerView
        if (!mShowFooter) {
            return TYPE_ITEM;
        }

        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_news_comment, parent, false);
            ItemViewHolder vh = new ItemViewHolder(v);
            return vh;
        } else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.footer, null);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            if (this.mShowFooter) {
                view.setVisibility(View.VISIBLE);
            } else {
                view.setVisibility(View.GONE);
            }

            return new FooterViewHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            final NewsCommentResponse.ResultBean.CommentsBean news = mData.get(position);
            if (news == null) {
                return;
            }

            ImageLoaderUtils.display(mContext, ((ItemViewHolder) holder).ivHead,
                    news.getUser_img(),
                    R.drawable.img_default_head, R.drawable.img_default_head);
            ((ItemViewHolder) holder).mTvName.setText(news.getComment_author());
            ((ItemViewHolder) holder).mTvComment.setText(news.getComment_content());
            ((ItemViewHolder) holder).mTvTime.setText(AppUtil.getStandardDate(news.getComment_date()));
            ((ItemViewHolder) holder).mTvLike.setText(news.getTotal_like());
            ((ItemViewHolder) holder).tvReplyNum.setText(news.getTotal_reply()+"回復");

            boolean isLike = false;
            if (news.getMy_like() == 0) {
                ((ItemViewHolder) holder).mIvLike.setImageResource(R.drawable.icon_great_normal);
                ((ItemViewHolder) holder).mTvLike.setTextColor(mContext.getResources().getColor(R.color.gray));
            } else {
                isLike = true;
                ((ItemViewHolder) holder).mIvLike.setImageResource(R.drawable.icon_great_pressed);
                ((ItemViewHolder) holder).mTvLike.setTextColor(mContext.getResources().getColor(R.color.red));
            }

            final int _isLike = isLike ? 0 : 1;
            ((ItemViewHolder) holder).mIvLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    like(news.getPost_id(), news.getComment_id(), _isLike,
                            ((ItemViewHolder) holder).mTvLike, ((ItemViewHolder) holder).mIvLike);
                }
            });

            ((ItemViewHolder) holder).tvReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemChildClickListener.onItemChildClick(view, position);
                }
            });

            /****************設置評論回復View********************/
            int replySize = Integer.parseInt(news.getTotal_reply());
            if(replySize > 0){
                ((ItemViewHolder) holder).llReply.removeAllViews();
                List<NewsCommentResponse.ResultBean.CommentsBean.ReplyBean> replyList = news.getReply();
                ((ItemViewHolder) holder).llReply.setVisibility(View.VISIBLE);
//                ((ItemViewHolder) holder).llMore.setVisibility(View.VISIBLE);
                for (int i = 0; i < replySize; i++) {
                    UserReplyView userReplyView = new UserReplyView(mContext);
                    userReplyView.setReplyData(replyList.get(i));
                    ((ItemViewHolder) holder).llReply.addView(userReplyView);
                }
            }else {
                ((ItemViewHolder) holder).llReply.setVisibility(View.GONE);
            }

        }
    }

    @Override
    public int getItemCount() {
        int begin = mShowFooter ? 1 : 0;
        if (mData == null) {
            return begin;
        }

        int sumSize = mData.size() + begin;

        return sumSize;
    }

    public NewsCommentResponse.ResultBean.CommentsBean getItem(int position) {
        return mData == null ? null : mData.get(position);
    }

    public void isShowFooter(boolean showFooter) {
        this.mShowFooter = showFooter;
    }

    public boolean isShowFooter() {
        return this.mShowFooter;
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {
        public FooterViewHolder(View view) {
            super(view);
        }

    }

    private static class ItemViewHolder extends RecyclerView.ViewHolder {

        private CircleSImageView ivHead;
        private TextView mTvName;
        private TextView mTvComment;
        private ImageView mIvLike;
        private TextView mTvLike;
        private TextView mTvTime;
        private TextView tvReply;
        private TextView tvReplyNum;
        private LinearLayout llReply;
        private LinearLayout llMore;


        public ItemViewHolder(View v) {
            super(v);
            ivHead = (CircleSImageView)v.findViewById( R.id.iv_head );
            mTvName = v.findViewById(R.id.tv_name);
            mTvComment = v.findViewById(R.id.tv_comment);
            mIvLike = v.findViewById(R.id.iv_like);
            mTvLike = v.findViewById(R.id.tv_like);
            mTvTime = v.findViewById(R.id.tv_time);
            tvReply = v.findViewById(R.id.tv_reply);
            tvReplyNum = v.findViewById(R.id.tv_reply_num);
            llReply = v.findViewById(R.id.ll_reply);
            llMore = v.findViewById(R.id.more_comment_layout);
        }
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
