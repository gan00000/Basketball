package com.jiec.basketball.ui.search;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gan.ctools.tool.SPUtil;
import com.jiec.basketball.R;
import com.jiec.basketball.base.BaseUIActivity;
import com.wangcj.common.utils.ToastUtil;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity  extends BaseUIActivity {

    private static final String FILE_NAME_SEARCH_HISTORY = "FILE_NAME_SEARCH_HISTORY.xml";
    private static final String FILE_NAME_SEARCH_HISTORY_KEY = "FILE_NAME_SEARCH_HISTORY_KEY";
    private static final String FILE_NAME_SEARCH_HISTORY_SPLITE = "&&&";
    @BindView(R.id.search_back_btn)
    View backView;
    @BindView(R.id.search_key_et)
    EditText search_key_et;

    @BindView(R.id.search_key_delete_v)
    View search_key_delete_v;

    @BindView(R.id.search_ok_btn)
    TextView search_ok_btn;

    @BindView(R.id.search_content_layout)
    View search_content_layout;

    @BindView(R.id.search_setting_layout)
    View search_setting_layout;

    @BindView(R.id.search_result_fragment_layout)
    View search_result_fragment_layout;

    @BindView(R.id.history_search_rv)
    RecyclerView history_search_rv;

    private FragmentManager fragmentManager;
    private List<String> searchHistoryData = new ArrayList<>();
    private CommonAdapter mCommonAdapter;
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        refreshSearchHistoryData();
        setSearchSettingVisable(true);

        backView.setOnClickListener(v -> finish());

        search_key_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        search_key_delete_v.setVisibility(View.GONE);
        search_key_delete_v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_key_et.setText("");
                setSearchSettingVisable(true);
            }
        });

        fragmentManager = getSupportFragmentManager();
        search_ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String searchText = search_key_et.getEditableText().toString();
                if (StringUtils.isEmpty(searchText)){
                    ToastUtil.showMsg("請輸入要搜索的內容");
                    return;
                }

                addSearchHistoryData(searchText);

                setSearchSettingVisable(false);
                fragmentManager.beginTransaction().replace(search_result_fragment_layout.getId(),SearchResultListFragment.newInstance(searchText)).commit();
            }
        });

        search_key_et.addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (StringUtils.isEmpty(s)){
                    search_key_delete_v.setVisibility(View.GONE);//輸入框沒有文字時隱藏刪除按鈕
                    setSearchSettingVisable(true);
                }else {
                    search_key_delete_v.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        initSearchRecyclerView();
        showSoftInputFromWindow(this, search_key_et);
    }

    /**
     * 显示历史记录与搜索结果页面切换
     * @param visible
     */
    private void setSearchSettingVisable(Boolean visible) {
        if (visible){
            search_content_layout.setVisibility(View.GONE);
            search_setting_layout.setVisibility(View.VISIBLE);
        }else {
            search_content_layout.setVisibility(View.VISIBLE);
            search_setting_layout.setVisibility(View.GONE);
        }
    }

    private void addSearchHistoryData(String searchText) {//重新保存搜索歷史數據

        if (StringUtils.isEmpty(searchText)){
            return;
        }
        String searchHistoryString = SPUtil.getSimpleString(SearchActivity.this,FILE_NAME_SEARCH_HISTORY,FILE_NAME_SEARCH_HISTORY_KEY);

        if (searchHistoryString.contains(searchText)){
            return;
        }
//        if (StringUtils.isEmpty(searchHistoryString)){
//            searchHistoryString = searchText ;
//        }else{
//            searchHistoryString = searchText + FILE_NAME_SEARCH_HISTORY_SPLITE;
//        }
        searchHistoryString = searchText + FILE_NAME_SEARCH_HISTORY_SPLITE + searchHistoryString;
        SPUtil.saveSimpleInfo(SearchActivity.this,FILE_NAME_SEARCH_HISTORY,FILE_NAME_SEARCH_HISTORY_KEY, searchHistoryString);

        refreshSearchHistoryData();

        if (mHeaderAndFooterWrapper != null){
            mHeaderAndFooterWrapper.notifyDataSetChanged();
        }
    }


    private void deleteOneSearchHistoryData(String searchText) {//刪除某一個歷史記錄

        if (StringUtils.isEmpty(searchText)){
            return;
        }
        String searchHistoryString = SPUtil.getSimpleString(SearchActivity.this,FILE_NAME_SEARCH_HISTORY,FILE_NAME_SEARCH_HISTORY_KEY);

        if (!searchHistoryString.contains(searchText)){
            return;
        }
        searchHistoryString = searchHistoryString.replace(searchText + FILE_NAME_SEARCH_HISTORY_SPLITE,"");

        SPUtil.saveSimpleInfo(SearchActivity.this,FILE_NAME_SEARCH_HISTORY,FILE_NAME_SEARCH_HISTORY_KEY, searchHistoryString);

        refreshSearchHistoryData();

        if (mHeaderAndFooterWrapper != null){
            mHeaderAndFooterWrapper.notifyDataSetChanged();
        }
    }

    private void clearSearchHistoryData() {//清除搜索歷史數據

        SPUtil.saveSimpleInfo(SearchActivity.this,FILE_NAME_SEARCH_HISTORY,FILE_NAME_SEARCH_HISTORY_KEY, "");

        refreshSearchHistoryData();

        if (mHeaderAndFooterWrapper != null){
            mHeaderAndFooterWrapper.notifyDataSetChanged();
        }
    }

    private void refreshSearchHistoryData() {//刷新 history_search_rv 的data
        String searchHistoryString = SPUtil.getSimpleString(this,FILE_NAME_SEARCH_HISTORY,FILE_NAME_SEARCH_HISTORY_KEY);
        searchHistoryData.clear();
        if (StringUtils.isNotEmpty(searchHistoryString)){
            String[] ss = searchHistoryString.split(FILE_NAME_SEARCH_HISTORY_SPLITE);
            for (int i = 0; i < ss.length; i++) {
                searchHistoryData.add(ss[i]);
            }
        }
    }

    private void initSearchRecyclerView() {

        history_search_rv.setLayoutManager(new LinearLayoutManager(this));
//        history_search_rv.setNestedScrollingEnabled(true);

        mCommonAdapter = new CommonAdapter<String>(this,R.layout.search_history_item, searchHistoryData){

            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                holder.setText(R.id.search_history_data_tv, s);
            }

            @Override
            public void onBindViewHolder(ViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);

                holder.getView(R.id.search_history_delete_ll).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (position + 1 > searchHistoryData.size()){
                            return;
                        }
                        String mm = searchHistoryData.get(position);
                        deleteOneSearchHistoryData(mm);
                    }
                });
            }
        };

        mCommonAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder viewHolder, int i) { //i这里从1开始
                if (i > searchHistoryData.size()){
                    return;
                }
                search_key_et.setText(searchHistoryData.get(i - 1));
                search_ok_btn.performClick();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder viewHolder, int i) {
                return false;
            }
        });

        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(mCommonAdapter);

        View headerView = getLayoutInflater().inflate(R.layout.search_history_item_header,null,false);
        headerView.findViewById(R.id.search_history_item_header_delete_all).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSearchHistoryData();
            }
        });
        mHeaderAndFooterWrapper.addHeaderView(headerView);
        history_search_rv.setAdapter(mHeaderAndFooterWrapper);
        mHeaderAndFooterWrapper.notifyDataSetChanged();

    }

    /**
     * EditText获取焦点并显示软键盘
     */
    public static void showSoftInputFromWindow(Activity activity, EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        //显示软键盘
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        //如果上面的代码没有弹出软键盘 可以使用下面另一种方式
        //InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        // imm.showSoftInput(editText, 0);
    }
}
