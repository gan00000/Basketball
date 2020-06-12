package com.jiec.basketball.ui.post;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.jiec.basketball.R;
import com.jiec.basketball.base.BaseActivity;
import com.wangcj.common.widget.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 評論跟帖頁面
 */
public class PostReplyActivity extends BaseActivity {

    @BindView(R.id.titleBar)
    TitleBar mTitleBar;

    private String postId;

    public static void show(Context context, String postId) {
        Intent mIntent = new Intent(context, PostReplyActivity.class);
        mIntent.putExtra("postId", postId);
        context.startActivity(mIntent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        ButterKnife.bind(this);
        postId = getIntent().getStringExtra("postId");
        mTitleBar.setTitle("評論回覆");

        PostReplyFragment fragment = (PostReplyFragment) getSupportFragmentManager()
                .findFragmentById(R.id.layout_fragment);

        if (fragment == null) {
            fragment = PostReplyFragment.newInstance(postId);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.layout_fragment, fragment);
            transaction.commit();
        }
    }

}
