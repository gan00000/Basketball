package com.jiec.basketball.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiec.basketball.R;
import com.jiec.basketball.entity.response.NewsCommentResponse;
import com.jiec.basketball.utils.AppUtil;
import com.jiec.basketball.utils.ImageLoaderUtils;
import com.wangcj.common.widget.PressImageView;
import com.wangcj.common.widget.CircleSImageView;


/**
 *自定义View，用于显示回复信息
 */
public class UserReplyView extends LinearLayout {
	private Context mContext;

	private CircleSImageView ivHead;
	private TextView tvName;
	private TextView tvComment;
	private TextView tvLike;
	private PressImageView ivLike;
	private TextView tvTime;


	public UserReplyView(Context context) {
		super(context);
		this.mContext = context;
		initView();
	}
	
	public UserReplyView(Context context, AttributeSet attrs){
		super(context,attrs);
		this.mContext = context;
		initView();
	}
	
	/**
	 * 初始化自定义控件
	 */
	private void initView() {
		LayoutInflater.from(mContext).inflate(R.layout.item_reply, this, true);
		ivHead = (CircleSImageView)findViewById( R.id.iv_head );
		tvName = (TextView)findViewById( R.id.tv_name );
		tvComment = (TextView)findViewById( R.id.tv_comment );
		tvLike = (TextView)findViewById( R.id.tv_like );
		ivLike = (PressImageView)findViewById( R.id.iv_like );
		tvTime = (TextView)findViewById( R.id.tv_time );
	}

	/**
	 * 設置回復數據
	 * @param replyBean
	 */
	public void setReplyData(NewsCommentResponse.ResultBean.CommentsBean.ReplyBean replyBean){
		if(replyBean == null){
			return;
		}
		ImageLoaderUtils.display(mContext, ivHead, replyBean.getUser_img(),
				R.drawable.img_default_head, R.drawable.img_default_head);
		tvName.setText(replyBean.getComment_author());
		tvComment.setText(replyBean.getComment_content());
		tvTime.setText(AppUtil.getStandardDate(replyBean.getComment_date()));
		tvLike.setText(replyBean.getTotal_like());

		boolean isLike = false;
		if (replyBean.getMy_like() == 0) {
			ivLike.setImageResource(R.drawable.icon_great_normal);
			tvLike.setTextColor(mContext.getResources().getColor(R.color.gray));
		} else {
			isLike = true;
			ivLike.setImageResource(R.drawable.icon_great_pressed);
			tvLike.setTextColor(mContext.getResources().getColor(R.color.red));
		}

	}
	

}
