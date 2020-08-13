package com.jiec.basketball.ui.game.detail;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.jiec.basketball.R;
import com.jiec.basketball.base.BaseUIActivity;
import com.jiec.basketball.entity.GameDataInfo;
import com.jiec.basketball.entity.GamePlayerData;
import com.jiec.basketball.entity.MatchSummary;
import com.jiec.basketball.network.GameApi;
import com.jiec.basketball.network.NetSubscriber;
import com.jiec.basketball.network.NetTransformer;
import com.jiec.basketball.network.RetrofitClient;
import com.jiec.basketball.ui.dialog.ShareUrlDialog;
import com.jiec.basketball.utils.ImageLoaderUtils;
import com.wangcj.common.utils.LogUtil;
import com.wangcj.common.widget.PressImageView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayerMatchSummaryActivity extends BaseUIActivity {

    private static final String TAG = "SummaryActivity";


    @BindView(R.id.player_match_summary_name_tv)
    TextView playerNameTv;

    @BindView(R.id.player_match_summary_teamname_tv)
    TextView playerTeamnameTv;

    @BindView(R.id.player_match_summary_info_label)
    TextView matchInfoTv;

    @BindView(R.id.iv_share)
    PressImageView ivShare;

    @BindView(R.id.player_match_summary_icon_iv)
    com.wangcj.common.widget.CircleSImageView playerIconCircleSImageView;

    @BindView(R.id.player_match_summary_quarter_tab)
    CommonTabLayout mTabLayout;

    @BindView(R.id.player_match_summary_info_rv)
    RecyclerView mRecyclerView;

    @BindView(R.id.player_match_summary_ll)
    View player_match_summary_ll;

    CommonAdapter mCommonAdapter;

    List<PlayerMatchInfo> playerMatchInfos_quarter_1 = new ArrayList<>();
    List<PlayerMatchInfo> playerMatchInfos_quarter_2 = new ArrayList<>();
    List<PlayerMatchInfo> playerMatchInfos_quarter_3 = new ArrayList<>();
    List<PlayerMatchInfo> playerMatchInfos_quarter_4 = new ArrayList<>();
    List<PlayerMatchInfo> playerMatchInfos_quarter_ot = new ArrayList<>();//加时
    List<PlayerMatchInfo> playerMatchInfos_quarter_all = new ArrayList<>();//全场

    List<PlayerMatchInfo> selectMatchInfos = new ArrayList<>();//當前顯示的數據

    String TITLES[] = {"1ST","2ND","3RD","4TH","全場"};
    String TITLES_OT[] = {"1ST","2ND","3RD","4TH","加時","全場"};

    MatchSummary matchSummary;
    Boolean isHomeTeam;

    public static void startActivity(MatchSummary matchSummary, boolean isHomeTeam, Activity activity, String gameId, String teamId, String playerId){
        Intent mIntent = new Intent(activity, PlayerMatchSummaryActivity.class);
        mIntent.putExtra("gameId",gameId);
        mIntent.putExtra("teamId",teamId);
        mIntent.putExtra("playerId",playerId);
        mIntent.putExtra("isHomeTeam",isHomeTeam);
        mIntent.putExtra("matchSummary",matchSummary);
        activity.startActivity(mIntent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_match_summary);
        ButterKnife.bind(this);

        String gameId = getIntent().getStringExtra("gameId");
        String teamId = getIntent().getStringExtra("teamId");
        String playerId = getIntent().getStringExtra("playerId");
        isHomeTeam = getIntent().getBooleanExtra("isHomeTeam",false);
        matchSummary = (MatchSummary) getIntent().getSerializableExtra("matchSummary");

        //測試數據
//        gameId = "3506275";
//        teamId = "10136";
//        playerId = "10723";

        getPlayerGameData(gameId,teamId,playerId);//获取球员数据

        setTeamName("");
        String status = "已結束";
        if (matchSummary.getScheduleStatus().equals("Final")) {
            status = "已結束";
        } else if (matchSummary.getScheduleStatus().equals("InProgress")) {
            status = "進行中";
        } else {
            status = "未開始";
        }
        matchInfoTv.setText(matchSummary.getDate() + "\n" + status + "\n" +
                matchSummary.getHomeName() + matchSummary.getHome_pts() +
                " - " +
                matchSummary.getAway_pts() + matchSummary.getAwayName());

        initTabLayout();

        mRecyclerView.setLayoutManager(new GridLayoutManager(this,4));

       mCommonAdapter = new CommonAdapter<PlayerMatchInfo>(this,R.layout.item_player_match_info,this.selectMatchInfos) {

            @Override
            protected void convert(ViewHolder holder, PlayerMatchInfo playerMatchInfo, int position) {
                holder.setText(R.id.item_player_match_info_result, playerMatchInfo.getTopInfo());
                holder.setText(R.id.item_player_match_info_category, playerMatchInfo.getBottomInfo());
            }
        };

        mRecyclerView.setAdapter(mCommonAdapter);
        mCommonAdapter.notifyDataSetChanged();

        ivShare.setOnClickListener(v -> {
            Bitmap mBitmap = createBitmapFormView(player_match_summary_ll);
            if (mBitmap != null){
//                playerIconCircleSImageView.setImageBitmap(mBitmap);
                ShareUrlDialog dialog = new ShareUrlDialog(PlayerMatchSummaryActivity.this, "");
                dialog.setShareLocalPhoto(mBitmap);
                dialog.show();

            }
        });

        mTabLayout.setCurrentTab(TITLES.length - 1);
    }

    private void setTeamName(String jerseynumber) {
        if (matchSummary != null){
            if (isHomeTeam){
                playerTeamnameTv.setText(matchSummary.getHomeName() + " " + jerseynumber + "號");
            }else {
                playerTeamnameTv.setText(matchSummary.getAwayName() + " " + jerseynumber + "號");
            }
        }
    }

    private void initTabLayout() {
        ArrayList<CustomTabEntity> tabEntities = new ArrayList<>();

        for (int i = 0; i < TITLES.length; i++) {
            tabEntities.add(createTabEntity(TITLES[i], 0, 0));
        }

        mTabLayout.setTabData(tabEntities);

        mTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {

                Log.d(TAG, "onTabSelect position" + position);
                selectMatchInfos.clear();
                switch (position){
                    case 0:
                        selectMatchInfos.addAll(playerMatchInfos_quarter_1);
                        break;
                    case 1:
                        selectMatchInfos.addAll(playerMatchInfos_quarter_2);
                        break;
                    case 2:
                        selectMatchInfos.addAll(playerMatchInfos_quarter_3);
                        break;
                    case 3:
                        selectMatchInfos.addAll(playerMatchInfos_quarter_4);
                        break;
                    case 4:
                        selectMatchInfos.addAll(playerMatchInfos_quarter_all);
                        break;
                        default:
                }

                if (mCommonAdapter != null){
                    mCommonAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onTabReselect(int position) {
                Log.d(TAG, "onTabReselect position" + position);
            }
        });
    }

    private void getPlayerGameData(String gameId, String teamId,String playerId) {

        showLoading();
        GameApi gameApi = RetrofitClient.getInstance().create(GameApi.class);
        gameApi.getPlayerGameDataInfo(gameId,teamId,playerId)
//                .compose(mView.getBindToLifecycle())
            .compose(new NetTransformer())
            .subscribe(new NetSubscriber<GameDataInfo>() {
                @Override
                protected void onSuccess(GameDataInfo result) {

                    hideLoading();
                    if (result.getStatus().equals("ok") && result.getMatchDetails() != null && result.getMatchDetails().size() > 0) {
                        List<GamePlayerData> mGamePlayerDataList = result.getMatchDetails();

                        for (int i = 0; i < mGamePlayerDataList.size(); i++) {
                            GamePlayerData gamePlayerData = mGamePlayerDataList.get(i);
                            if (gamePlayerData.getQuarter().equals("1")){

                                playerNameTv.setText(gamePlayerData.getPlName());//設置球員名字
                                ImageLoaderUtils.display(getApplicationContext(),playerIconCircleSImageView,gamePlayerData.getOfficialImagesrc());//設置球員頭像
                                setTeamName(gamePlayerData.getJerseynumber());

                                changeToRvData(playerMatchInfos_quarter_1,gamePlayerData);
//                                selectMatchInfos.addAll(playerMatchInfos_quarter_1);//默認顯示第一個
//                                mCommonAdapter.notifyDataSetChanged();
//                                mTabLayout.setCurrentTab(0);


                            }else if (gamePlayerData.getQuarter().equals("2")){
                                changeToRvData(playerMatchInfos_quarter_2,gamePlayerData);
                            }else if (gamePlayerData.getQuarter().equals("3")){
                                changeToRvData(playerMatchInfos_quarter_3,gamePlayerData);
                            }else if (gamePlayerData.getQuarter().equals("4")){
                                changeToRvData(playerMatchInfos_quarter_4,gamePlayerData);
                            }else if (gamePlayerData.getQuarter().equals("OT")){
                                changeToRvData(playerMatchInfos_quarter_ot,gamePlayerData);
                            }
                        }


                        setGamePlayerDataAll(playerMatchInfos_quarter_all);
                        if (mCommonAdapter != null){
                            mCommonAdapter.notifyDataSetChanged();
                        }
                    }

                    selectMatchInfos.addAll(playerMatchInfos_quarter_all);//默認顯示全場
                    mTabLayout.setCurrentTab(TITLES.length - 1);
                }

                @Override
                protected void onFailed(int code, String reason) {
                    LogUtil.e(reason);
                    hideLoading();
                }
            });
    }

   private int pts,ast,reb,offreb,defreb,fouls,stl,blkgainst,blk, fgmade,fgatt, fg3ptmade,fg3ptatt, ftmade,ftatt;

    private void changeToRvData(List<PlayerMatchInfo> playerMatchInfos, GamePlayerData gamePlayerData) {
        pts += gamePlayerData.getPts();
        ast += gamePlayerData.getAst();
        reb += gamePlayerData.getReb();
        offreb += gamePlayerData.getOffreb();
        defreb += gamePlayerData.getDefreb();
        fouls += gamePlayerData.getFouls();
        stl += gamePlayerData.getStl();
        blkgainst += gamePlayerData.getBlkagainst();
        blk += gamePlayerData.getBlk();

        //投籃
        fgmade += gamePlayerData.getFgmade();
        fgatt += gamePlayerData.getFgatt();

        //3分
        fg3ptmade += gamePlayerData.getFg3ptmade();
        fg3ptatt += gamePlayerData.getFg3ptatt();

        //罰球
        ftmade += gamePlayerData.getFtmade();
        ftatt += gamePlayerData.getFtatt();


        playerMatchInfos.add(new PlayerMatchInfo(""+ gamePlayerData.getPts(),"得分"));
        playerMatchInfos.add(new PlayerMatchInfo(""+gamePlayerData.getAst(),"助攻"));
        playerMatchInfos.add(new PlayerMatchInfo(""+gamePlayerData.getReb(),"籃板"));
        playerMatchInfos.add(new PlayerMatchInfo(""+gamePlayerData.getShoot(),"投籃"));
        playerMatchInfos.add(new PlayerMatchInfo(""+gamePlayerData.getFg3pt(),"3分"));
        playerMatchInfos.add(new PlayerMatchInfo(""+gamePlayerData.getFt(),"罰球"));
        playerMatchInfos.add(new PlayerMatchInfo(""+gamePlayerData.getOffreb(),"前板"));
        playerMatchInfos.add(new PlayerMatchInfo(""+gamePlayerData.getDefreb(),"後板"));
        playerMatchInfos.add(new PlayerMatchInfo(""+gamePlayerData.getFouls(),"犯規"));
        playerMatchInfos.add(new PlayerMatchInfo(""+gamePlayerData.getStl(),"抄截"));
        playerMatchInfos.add(new PlayerMatchInfo(""+gamePlayerData.getBlkagainst(),"失誤"));
        playerMatchInfos.add(new PlayerMatchInfo(""+gamePlayerData.getBlk(),"封蓋"));
    }

    private void setGamePlayerDataAll(List<PlayerMatchInfo> playerMatchInfos){//設置全場數據

        playerMatchInfos.add(new PlayerMatchInfo(""+ pts,"得分"));
        playerMatchInfos.add(new PlayerMatchInfo(""+ast,"助攻"));
        playerMatchInfos.add(new PlayerMatchInfo(""+reb,"籃板"));
        playerMatchInfos.add(new PlayerMatchInfo(fgmade+" - "+ fgatt,"投籃"));
        playerMatchInfos.add(new PlayerMatchInfo(fg3ptmade+" - "+ fg3ptatt,"3分"));
        playerMatchInfos.add(new PlayerMatchInfo(ftmade+" - "+ ftatt,"罰球"));
        playerMatchInfos.add(new PlayerMatchInfo(offreb+"","前板"));
        playerMatchInfos.add(new PlayerMatchInfo(""+defreb,"後板"));
        playerMatchInfos.add(new PlayerMatchInfo(""+fouls,"犯規"));
        playerMatchInfos.add(new PlayerMatchInfo(""+stl,"抄截"));
        playerMatchInfos.add(new PlayerMatchInfo(""+blkgainst,"失誤"));
        playerMatchInfos.add(new PlayerMatchInfo(""+blk,"封蓋"));
    }

    /**
     * 默认tab图标
     *
     * @param title
     * @param selectIcon
     * @param unSelectIconId
     * @return
     */
    private CustomTabEntity createTabEntity(final String title, final int selectIcon, final int unSelectIconId) {

        return new CustomTabEntity() {
            @Override
            public String getTabTitle() {
                return title;
            }

            @Override
            public void setSelectedIcon(ImageView view) {

            }

            @Override
            public void setUnSelectedIcon(ImageView view) {
            }

        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private Bitmap createBitmapFormView(View view) {
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }

}
