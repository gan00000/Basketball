package com.gan.video.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.gan.video.model.SwitchVideoModel;
import com.jiec.basketball.R;

import java.util.List;

public class SwitchVideoTypeDialog extends Dialog {

    private Context mContext;

    private ListView listView = null;

    private ArrayAdapter<SwitchVideoModel> adapter = null;

    private OnListItemClickListener onItemClickListener;

    private List<SwitchVideoModel> data;

    private int mHeight;
    private int mWidth;
    private int[] position;

    public interface OnListItemClickListener {
        void onItemClick(int position);
    }

    public SwitchVideoTypeDialog(Context context) {
        super(context, R.style.dialog_style);
        this.mContext = context;
    }

    public SwitchVideoTypeDialog(Context context, int mWidth, int mHeight) {
        super(context, R.style.dialog_style);
        this.mContext = context;

        this.mWidth = mWidth;
        this.mHeight = mHeight;
    }

    public SwitchVideoTypeDialog(Context context, int[] position, int mWidth, int mHeight) {
        super(context, R.style.dialog_style);
        this.mContext = context;

        this.position = position;
        this.mWidth = mWidth;
        this.mHeight = mHeight;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void initList(List<SwitchVideoModel> data, OnListItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        this.data = data;

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.switch_video_dialog, null);
        listView = (ListView) view.findViewById(R.id.switch_dialog_list);
        setContentView(view);
        adapter = new ArrayAdapter<>(mContext, R.layout.switch_video_dialog_item, data);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener());

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();

        dialogWindow.setGravity(Gravity.RIGHT | Gravity.TOP);

        int height = 0;
        int resourceId = getContext().getApplicationContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            height = getContext().getApplicationContext().getResources().getDimensionPixelSize(resourceId);
        }

        lp.width = this.mWidth;
        lp.height = this.mHeight;
//        lp.x = this.position[0];
        lp.y = this.position[1] - height;

//        if (this.position != null){
//
//        }else if (this.mWidth != 0 && this.mHeight != 0){
//        }else {
//            DisplayMetrics d = mContext.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
//            lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.6
//        }
        dialogWindow.setAttributes(lp);
    }

    private class OnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            dismiss();
            onItemClickListener.onItemClick(position);
        }
    }


}