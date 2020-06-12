package com.gan.video.view;

import android.content.Context;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.gan.video.model.SwitchVideoModel;
import com.jiec.basketball.R;

import java.util.List;

public class SwitchVideoTypeView extends FrameLayout {

    private Context mContext;

    private ListView listView = null;

    private ArrayAdapter<SwitchVideoModel> adapter = null;

    private OnListItemClickListener onItemClickListener;

    private List<SwitchVideoModel> data;

    public SwitchVideoTypeView(@NonNull Context context) {
        super(context);
    }

    public SwitchVideoTypeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SwitchVideoTypeView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SwitchVideoTypeView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public interface OnListItemClickListener {
        void onItemClick(int position);
    }

//    public SwitchVideoTypeView(Context context) {
//        super(context, R.style.dialog_style);
//        this.mContext = context;
//    }



    public void initList(List<SwitchVideoModel> data, OnListItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        this.data = data;

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.switch_video_dialog, null);
        listView = (ListView) view.findViewById(R.id.switch_dialog_list);
        addView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        adapter = new ArrayAdapter<>(mContext, R.layout.switch_video_dialog_item, data);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener());

    }

    private class OnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            onItemClickListener.onItemClick(position);
        }
    }


}