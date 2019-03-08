package com.jiec.basketball.network;


import com.jiec.basketball.bean.NewsDetailModel;
import com.jiec.basketball.entity.AllPlayerData;
import com.jiec.basketball.entity.AllTeamData;
import com.jiec.basketball.entity.EastWestTeamRank;
import com.jiec.basketball.entity.GameDataInfo;
import com.jiec.basketball.entity.GameList;
import com.jiec.basketball.entity.GameLiveInfo;
import com.jiec.basketball.entity.GameProgress;
import com.jiec.basketball.entity.PlayerFirstFive;
import com.jiec.basketball.entity.TeamData;
import com.jiec.basketball.entity.TopPost;
import com.jiec.basketball.entity.ZoneTeamRank;
import com.jiec.basketball.entity.response.HomeResponse;
import com.jiec.basketball.entity.response.NewListResponse;
import com.jiec.basketball.entity.response.PostResponse;
import com.jiec.basketball.entity.response.TimeResponse;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by jiec on 16/10/15.
 */
public interface GameApi {

    //获取新闻列表
    @GET("/news?json=1")
    Observable<NewListResponse> getNews(@Query("page") int page);

    //获取影片
    @GET("/highlight?json=1")
    Observable<NewListResponse> getFilms(@Query("page") int page);

    //获取战况
    @GET("/live?json=1")
    Observable<NewListResponse> getFightings(@Query("page") int page);

    //获取图片
    @GET("/pics?json=1")
    Observable<NewListResponse> getPictures(@Query("page") int page);

    //获取教学
    @GET("/tech?json=1")
    Observable<NewListResponse> getTeaching(@Query("page") int page);

    @GET("/?json=get_app_index")
    Observable<HomeResponse> getHomePage();


    @GET("/?json=get_post")
    Observable<PostResponse> getPost(@Query("post_id") String id);

    /**
     * 通过新闻id获取新闻详情
     * @param id
     * @return
     */
    @GET("/?json=get_post")
    Observable<NewsDetailModel> getPostDetail(@Query("post_id") String id);

    @GET("?json=get_server_mtime")
    Observable<TimeResponse> getServerTime();

    @FormUrlEncoded
    @POST("/api/index.php")
    Observable<PlayerFirstFive> getPlayerData(@Field("fun") String fun, @Field("params") String params);

    @FormUrlEncoded
    @POST("/api/index.php")
    Observable<TeamData> getTeamData(@Field("fun") String fun);

    @GET("/api/get_top_posts/")
    Observable<TopPost> getTopPosts();

    /**
     * 获取东西部球队排名
     *
     * @return
     */
    @GET("/api/get_east_west_rank/")
    Observable<EastWestTeamRank> getEastWestRank();

    /**
     * 获取赛区排名
     *
     * @return
     */
    @GET("/api/get_zone_rank/")
    Observable<ZoneTeamRank> getZoneRank();

    @GET("/api/get_player_rank_all/")
    Observable<AllPlayerData> getAllPlayerData(@Query("type") String type, @Query("sort_by") String sort);

    @GET("/api/get_team_rank_all/")
    Observable<AllTeamData> getAllTeamData(@Query("type") String type, @Query("sort_by") String sort);

    @GET("/api/get_live_feed/")
    Observable<GameLiveInfo> getGameLiveInfo(@Query("game_id") String gameID);

    @GET("/api/get_match_summary/")
    Observable<GameDataInfo> getGameDataInfo(@Query("game_id") String gameId);

    @GET("/api/get_game_schedule/")
    Observable<GameList> getGameList(@Query("date_from") String date_from, @Query("date_to") String date_to);

    @GET("/api/get_match_progress/")
    Observable<GameProgress> getGameProgress(@Query("game_id") String gameId);


}
