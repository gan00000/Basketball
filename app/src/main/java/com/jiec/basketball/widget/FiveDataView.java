package com.jiec.basketball.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiec.basketball.R;
import com.jiec.basketball.entity.PlayerInfo;
import com.jiec.basketball.utils.AppUtil;
import com.jiec.basketball.utils.ImageLoaderUtils;
import com.jiec.basketball.utils.NumberUtils;
import com.wangcj.common.widget.PressRelativeLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wangchuangjie on 2018/4/14.
 */

public class FiveDataView extends LinearLayout {
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.iv_more)
    ImageView mIvMore;
    @BindView(R.id.layout_more)
    PressRelativeLayout mLayoutMore;
    @BindView(R.id.tv_1_value)
    TextView mTv1Value;
    @BindView(R.id.iv_1_head)
    ImageView mIv1Head;
    @BindView(R.id.tv_1_name)
    TextView mTv1Name;
    @BindView(R.id.tv_1_other)
    TextView mTv1Other;
    @BindView(R.id.tv_2_value)
    TextView mTv2Value;
    @BindView(R.id.iv_2_head)
    ImageView mIv2Head;
    @BindView(R.id.tv_2_name)
    TextView mTv2Name;
    @BindView(R.id.tv_2_other)
    TextView mTv2Other;
    @BindView(R.id.tv_3_value)
    TextView mTv3Value;
    @BindView(R.id.iv_3_head)
    ImageView mIv3Head;
    @BindView(R.id.tv_3_name)
    TextView mTv3Name;
    @BindView(R.id.tv_3_other)
    TextView mTv3Other;
    @BindView(R.id.tv_4_value)
    TextView mTv4Value;
    @BindView(R.id.iv_4_head)
    ImageView mIv4Head;
    @BindView(R.id.tv_4_name)
    TextView mTv4Name;
    @BindView(R.id.tv_4_other)
    TextView mTv4Other;
    @BindView(R.id.tv_5_value)
    TextView mTv5Value;
    @BindView(R.id.iv_5_head)
    ImageView mIv5Head;
    @BindView(R.id.tv_5_name)
    TextView mTv5Name;
    @BindView(R.id.tv_5_other)
    TextView mTv5Other;

    public FiveDataView(Context context) {
        super(context);
    }

    public FiveDataView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        inflate(context, R.layout.item_five_data, this);


        ButterKnife.bind(this);
    }

    public void setTitle(String title) {
        mTvTitle.setText(title);
    }

    public void setData(List<PlayerInfo> data) {
        if (data != null && data.size() >= 3) {
            mTv1Value.setText(NumberUtils.formatAmount(data.get(0).getScore()));
            ImageLoaderUtils.display(getContext(), mIv1Head, data.get(0).getOfficialImagesrc(),
                    R.drawable.img_default_head, R.drawable.img_default_head);
            mTv1Name.setText(AppUtil.name(data.get(0).getName()));
            mTv1Other.setText(data.get(0).getTeamName());


            mTv2Value.setText(NumberUtils.formatAmount(data.get(1).getScore()));
            ImageLoaderUtils.display(getContext(), mIv2Head, data.get(1).getOfficialImagesrc(),
                    R.drawable.img_default_head, R.drawable.img_default_head);
            mTv2Name.setText(AppUtil.name(data.get(1).getName()));
            mTv2Other.setText(data.get(1).getTeamName());


            mTv3Value.setText(NumberUtils.formatAmount(data.get(2).getScore()));
            ImageLoaderUtils.display(getContext(), mIv3Head, data.get(2).getOfficialImagesrc(),
                    R.drawable.img_default_head, R.drawable.img_default_head);
            mTv3Name.setText(AppUtil.name(data.get(2).getName()));
            mTv3Other.setText(data.get(2).getTeamName());

            mTv4Value.setText(NumberUtils.formatAmount(data.get(3).getScore()));
            ImageLoaderUtils.display(getContext(), mIv4Head, data.get(3).getOfficialImagesrc(),
                    R.drawable.img_default_head, R.drawable.img_default_head);
            mTv4Name.setText(AppUtil.name(data.get(3).getName()));
            mTv4Other.setText(data.get(3).getTeamName());

            mTv5Value.setText(NumberUtils.formatAmount(data.get(4).getScore()));
            ImageLoaderUtils.display(getContext(), mIv5Head, data.get(4).getOfficialImagesrc(),
                    R.drawable.img_default_head, R.drawable.img_default_head);
            mTv5Name.setText(AppUtil.name(data.get(4).getName()));
            mTv5Other.setText(data.get(4).getTeamName());
        } else {
            setVisibility(GONE);
        }
    }

    public void setMoreListener(OnClickListener listener) {
        mLayoutMore.setOnClickListener(listener);
    }

}
