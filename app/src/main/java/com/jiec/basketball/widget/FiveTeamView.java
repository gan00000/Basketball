package com.jiec.basketball.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiec.basketball.R;
import com.jiec.basketball.entity.TeamData;
import com.jiec.basketball.utils.ImageLoaderUtils;
import com.jiec.basketball.utils.NumberUtils;
import com.wangcj.common.widget.PressRelativeLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wangchuangjie on 2018/4/14.
 */

public class FiveTeamView extends LinearLayout {
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

    @BindView(R.id.tv_2_value)
    TextView mTv2Value;
    @BindView(R.id.iv_2_head)
    ImageView mIv2Head;
    @BindView(R.id.tv_2_name)
    TextView mTv2Name;

    @BindView(R.id.tv_3_value)
    TextView mTv3Value;
    @BindView(R.id.iv_3_head)
    ImageView mIv3Head;
    @BindView(R.id.tv_3_name)
    TextView mTv3Name;

    @BindView(R.id.tv_4_value)
    TextView mTv4Value;
    @BindView(R.id.iv_4_head)
    ImageView mIv4Head;
    @BindView(R.id.tv_4_name)
    TextView mTv4Name;

    @BindView(R.id.tv_5_value)
    TextView mTv5Value;
    @BindView(R.id.iv_5_head)
    ImageView mIv5Head;
    @BindView(R.id.tv_5_name)
    TextView mTv5Name;


    public FiveTeamView(Context context) {
        super(context);
    }

    public FiveTeamView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        inflate(context, R.layout.layout_five_team, this);


        ButterKnife.bind(this);
    }

    public void setTitle(String title) {
        mTvTitle.setText(title);
    }

    public void setData(List<TeamData.Team> data) {
        if (data != null && data.size() >= 3) {
            mTv1Value.setText(NumberUtils.formatAmount(data.get(0).getPts()));
            ImageLoaderUtils.display(getContext(), mIv1Head, data.get(0).getTeam_logo(),
                    R.drawable.img_default_head, R.drawable.img_default_head);
            mTv1Name.setText(data.get(0).getTeamName());


            mTv2Value.setText(NumberUtils.formatAmount(data.get(1).getPts()));
            ImageLoaderUtils.display(getContext(), mIv2Head, data.get(1).getTeam_logo(),
                    R.drawable.img_default_head, R.drawable.img_default_head);
            mTv2Name.setText(data.get(1).getTeamName());


            mTv3Value.setText(NumberUtils.formatAmount(data.get(2).getPts()));
            ImageLoaderUtils.display(getContext(), mIv3Head, data.get(2).getTeam_logo(),
                    R.drawable.img_default_head, R.drawable.img_default_head);
            mTv3Name.setText(data.get(2).getTeamName());

            mTv4Value.setText(NumberUtils.formatAmount(data.get(3).getPts()));
            ImageLoaderUtils.display(getContext(), mIv4Head, data.get(3).getTeam_logo(),
                    R.drawable.img_default_head, R.drawable.img_default_head);
            mTv4Name.setText(data.get(3).getTeamName());

            mTv5Value.setText(NumberUtils.formatAmount(data.get(4).getPts()));
            ImageLoaderUtils.display(getContext(), mIv5Head, data.get(4).getTeam_logo(),
                    R.drawable.img_default_head, R.drawable.img_default_head);
            mTv5Name.setText(data.get(4).getTeamName());
        } else {
            setVisibility(GONE);
        }
    }

    public void setMoreListener(OnClickListener listener) {
        mLayoutMore.setOnClickListener(listener);
    }

}
