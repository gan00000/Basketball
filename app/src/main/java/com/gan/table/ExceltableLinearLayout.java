package com.gan.table;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bin.david.form.utils.DensityUtils;

import java.util.List;

public class ExceltableLinearLayout extends LinearLayout {

    private List<TableTitle> tableTitles;

    private List<List<TableCell>> cellContents;

    private Context mContext;

    private int rowHeight = 10;

    public List<TableTitle> getTableTitles() {
        return tableTitles;
    }

    public void setTableTitles(List<TableTitle> tableTitles) {
        this.tableTitles = tableTitles;
    }

    public List<List<TableCell>> getCellContents() {
        return cellContents;
    }

    public void setCellContents(List<List<TableCell>> cellContents) {
        this.cellContents = cellContents;
    }

    public int getRowHeight() {
        return rowHeight;
    }

    public void setRowHeight(int rowHeight) {
        this.rowHeight = rowHeight;
    }

    public ExceltableLinearLayout(Context context) {
        super(context);
        this.mContext = context;
        initView();
    }

    public ExceltableLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView();
    }

    public ExceltableLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView();
    }

    public void initView(){

        this.setOrientation(VERTICAL);

        if (tableTitles != null && !tableTitles.isEmpty()){

            LinearLayout rowLinearLayout = new LinearLayout(getContext());
            rowLinearLayout.setOrientation(HORIZONTAL);
            rowLinearLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            this.addView(rowLinearLayout);

            for (int i = 0; i < tableTitles.size(); i++) {

                TableTitle mTableTitle = tableTitles.get(i);

                TextView titleView = new TextView(mContext);

                int mCellHeight;

                if (mTableTitle.getHeight() != 0){
                    mCellHeight = mTableTitle.getHeight();
                }else {
                    mCellHeight = rowHeight;
                }
                titleView.setLayoutParams(new LayoutParams(DensityUtils.dp2px(mContext,mTableTitle.getWidth()), DensityUtils.dp2px(mContext,mCellHeight)));
                titleView.setText(mTableTitle.getTitle());
                rowLinearLayout.addView(titleView);

            }

        }

        if (cellContents == null || cellContents.isEmpty()){
            return;
        }

        for(List<TableCell> tableCells :cellContents){

            LinearLayout mRowLinearLayout = createRow(tableCells);
            this.addView(mRowLinearLayout);

        }



    }


    private LinearLayout createRow(List<TableCell> tableCells){//创建每一行


        LinearLayout rowLinearLayout = new LinearLayout(getContext());
        rowLinearLayout.setOrientation(HORIZONTAL);
        rowLinearLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        for (int i = 0; i < tableCells.size(); i++) {

            TableCell tableCell = tableCells.get(i);

            TableTitle mTableTitle = tableTitles.get(i);

            TextView cellTextView = new TextView(mContext);
            cellTextView.setLayoutParams(new LayoutParams(DensityUtils.dp2px(mContext,mTableTitle.getWidth()), DensityUtils.dp2px(mContext,tableCell.getHeight())));
            cellTextView.setText(tableCell.getData());

            rowLinearLayout.addView(cellTextView);

        }

        return rowLinearLayout;

    }


}
