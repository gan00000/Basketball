package com.jiec.basketball.ui.rank;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.core.TableConfig;
import com.bin.david.form.data.CellInfo;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.format.bg.BaseCellBackgroundFormat;
import com.bin.david.form.data.format.bg.ICellBackgroundFormat;
import com.bin.david.form.data.format.draw.TextImageDrawFormat;
import com.bin.david.form.data.format.grid.BaseGridFormat;
import com.bin.david.form.data.style.FontStyle;
import com.bin.david.form.data.style.LineStyle;
import com.bin.david.form.utils.DensityUtils;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.gan.table.KKTextImageDrawFormat;
import com.jiec.basketball.R;
import com.jiec.basketball.entity.EastWestTeamRank;
import com.jiec.basketball.network.GameApi;
import com.jiec.basketball.network.RetrofitClient;
import com.jiec.basketball.utils.svg.GlideApp;
import com.jiec.basketball.utils.svg.SvgSoftwareLayerSetterInBitmap;
import com.wangcj.common.utils.ToastUtil;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *
 */
public class RankEastWestFragment extends Fragment {


    Unbinder unbinder;

    @BindView(R.id.st_team_rank_east)
    SmartTable mStTeamRankEast;
    @BindView(R.id.st_team_rank_west)
    SmartTable mStTeamRankWest;

    private Map<String,Bitmap> bitmapHashMap = new HashMap<>();
    private EastWestTeamRank eastWestTeamRank;


    public static RankEastWestFragment newInstance() {
        RankEastWestFragment fragment = new RankEastWestFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_east_west_rank, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        loadData();

        return view;
    }

    private void loadData() {
        GameApi mService = RetrofitClient.getInstance().create(GameApi.class);
        Observable<EastWestTeamRank> observable = mService.getEastWestRank();
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<EastWestTeamRank>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showMsg(e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(EastWestTeamRank commResponse) {
                        if (commResponse != null && commResponse.getStatus().equals("ok")) {
                            eastWestTeamRank = commResponse;
                            mStTeamRankEast.setData(commResponse.getEastern());
                            mStTeamRankWest.setData(commResponse.getWestern());
                        } else {
                            ToastUtil.showMsg(R.string.network_error);
                        }
                    }
                });
    }

    private void initView() {
        initTableview(mStTeamRankEast);
        initTableview(mStTeamRankWest);
    }

//    KKTextImageDrawFormat mTextImageDrawFormat;

    private void initTableview(SmartTable smartTable) {
        FontStyle.setDefaultTextSize(DensityUtils.sp2px(getContext(), 15));

        smartTable.getConfig().setShowXSequence(false);
        smartTable.getConfig().setShowYSequence(false);
        smartTable.getConfig().setShowTableTitle(false);
        smartTable.getConfig().setHorizontalPadding(30);
        smartTable.getConfig().setColumnTitleHorizontalPadding(10);

        smartTable.getConfig().setContentGridStyle(new LineStyle() {
            @Override
            public LineStyle setColor(int color) {
                return super.setColor(ContextCompat.getColor(getActivity(), R.color.black));
            }
        });

        int imgSize = DensityUtils.dp2px(this.getContext(),16);
        int pading = DensityUtils.dp2px(this.getContext(),4);
        final KKTextImageDrawFormat mTextImageDrawFormat = new KKTextImageDrawFormat<String>(imgSize,imgSize,TextImageDrawFormat.LEFT, pading) {

            @Override
            protected String getFirstString(CellInfo<String> cellInfo, Paint paint) {
                int row = cellInfo.row;

                if (cellInfo.row < 8){
                    paint.setColor(getContext().getResources().getColor(R.color.fd7f23));
                    paint.setTypeface(Typeface.DEFAULT_BOLD);
                }
                paint.setTextAlign(Paint.Align.CENTER);
                if (smartTable == mStTeamRankEast){
                    return eastWestTeamRank.getEastern().get(row).getRank();
                }
                return eastWestTeamRank.getWestern().get(row).getRank();
            }

            @Override
            protected Bitmap getBitmap(String s, String value, int position) { //position 行的行数

                if (bitmapHashMap.get(value) != null){
                    return bitmapHashMap.get(value);
                }
                if (eastWestTeamRank != null){

//                    if (eastWestTeamRank.getEastern() != null && !eastWestTeamRank.getEastern().isEmpty()){
//                        for (TernBean ternBean: eastWestTeamRank.getEastern()) {
//                            if (ternBean.getCh_name().equals(value)){
//                                logoUrl = ternBean.getTeam_logo();
//                                break;
//                            }
//                        }
//                    }
//
//                    if (StringUtils.isEmpty(logoUrl)){
//
//                        if (eastWestTeamRank.getWestern() != null && !eastWestTeamRank.getWestern().isEmpty()){
//                            for (TernBean ternBean: eastWestTeamRank.getWestern()) {
//                                if (ternBean.getCh_name().equals(value)){
//                                    logoUrl = ternBean.getTeam_logo();
//                                    break;
//                                }
//                            }
//                        }
//
//                    }

                    String logoUrl = "";
                    if (smartTable == mStTeamRankEast){
                        logoUrl =eastWestTeamRank.getEastern().get(position).getTeam_logo();
                    }else {
                        logoUrl =eastWestTeamRank.getWestern().get(position).getTeam_logo();
                    }

                    if(StringUtils.isNotEmpty(logoUrl)) {

                        RequestBuilder<PictureDrawable> requestBuilder = GlideApp.with(RankEastWestFragment.this)
                                .as(PictureDrawable.class)
                                .listener(new SvgSoftwareLayerSetterInBitmap());

                        Uri uri = Uri.parse(logoUrl);
                        requestBuilder.load(uri).into(new SimpleTarget<PictureDrawable>() {
                            @Override
                            public void onResourceReady(@NonNull PictureDrawable pictureDrawable, @Nullable Transition<? super PictureDrawable> transition) {

                                Bitmap bitmap = Bitmap.createBitmap(pictureDrawable.getIntrinsicWidth(), pictureDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                                Canvas canvas = new Canvas(bitmap);
                                canvas.drawPicture(pictureDrawable.getPicture());

                                bitmapHashMap.put(value, bitmap);
                                smartTable.invalidate();
                            }
                        });
//                        Glide.with(RankEastWestFragment.this).asBitmap().load(logoUrl)
//                                .apply(bitmapTransform(new CenterCrop())).into(new SimpleTarget<Bitmap>() {
//                            @Override
//                            public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
//                                bitmapHashMap.put(value, bitmap);
//                                smartTable.invalidate();
//                            }
//                        });
                    }

                }
                return bitmapHashMap.get(value);
            }
        };


        smartTable.getConfig().setTableTitleStyle(new FontStyle(getContext(), 18,
                getResources().getColor(R.color.black)));
        ICellBackgroundFormat<CellInfo> backgroundFormat = new BaseCellBackgroundFormat<CellInfo>() {
            @Override
            public int getBackGroundColor(CellInfo cellInfo) {

                if (cellInfo.col== 0){
                    cellInfo.column.setDrawFormat(mTextImageDrawFormat);
                }
                if (cellInfo.row % 2 == 0) {
                    //return ContextCompat.getColor(getActivity(), R.color.gray_light);
                    return TableConfig.INVALID_COLOR;
                } else {
                    return TableConfig.INVALID_COLOR;
                }
//                if (cellInfo.col == 1) {
//                    return ContextCompat.getColor(getActivity(), R.color.gray_light);
//                }else if (cellInfo.col == 2) {
//                    return ContextCompat.getColor(getActivity(), R.color.blue);
//                }else {
//                    return ContextCompat.getColor(getActivity(), R.color.red);
//                }
            }
        };


        smartTable.getConfig().setContentCellBackgroundFormat(backgroundFormat);


        smartTable.getConfig().setTableGridFormat(new BaseGridFormat() {
            @Override
            protected boolean isShowColumnTitleVerticalLine(int col, Column column) {
                return false;
            }

            @Override
            protected boolean isShowVerticalLine(int col, int row, CellInfo cellInfo) {
                return false;
            }

            @Override
            protected boolean isShowHorizontalLine(int col, int row, CellInfo cellInfo) {
                return true;
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
