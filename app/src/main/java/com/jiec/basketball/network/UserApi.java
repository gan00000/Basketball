package com.jiec.basketball.network;

import com.jiec.basketball.bean.NotifyCounterModel;
import com.jiec.basketball.entity.LoginResult;
import com.jiec.basketball.entity.UserProfile;
import com.jiec.basketball.entity.response.CollectionResponse;
import com.jiec.basketball.entity.response.CommentResponse;
import com.jiec.basketball.entity.response.HistoryResponse;
import com.jiec.basketball.entity.response.LikeResponse;
import com.jiec.basketball.entity.response.NotifyResponse;

import java.util.Map;

import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Jiec on 2019/1/13.
 * 用戶相關業務api接口
 */
public interface UserApi {

    int LOGIN_TYPE_FACEBOOK = 1;
    int LOGIN_TYPE_LINE = 2;

    @FormUrlEncoded
    @POST("/api/app_login/")
    Observable<LoginResult> login(@Field("sns_login") int sns_login, @Field("access_token") String access_token,
                                  @Field("device_token") String device_token, @Field("device_type") int type);


    @GET("/api/get_profile/")
    Observable<UserProfile> getProfile(@Query("token") String token);

    @FormUrlEncoded
    @POST("/api/change_profile/")
    Observable<UserProfile> updateProfile(@Field("token") String token, @Field("email") String email,
                                          @Field("display_name") String display_name, @Field("file") String file);

    @FormUrlEncoded
    @POST("/api/change_profile/")
    Observable<UserProfile> updateProfile(@FieldMap Map<String, String> map);


    @GET("/api/get_saved_post/")
    Observable<CollectionResponse> getCollection(@Query("token") String token, @Query("offset") int offset);

    @GET("/api/get_my_like/")
    Observable<LikeResponse> getLike(@Query("token") String token, @Query("offset") int offset);

    /**保存到歷史記錄*/
    @GET("/api/save_history/")
    Observable<HistoryResponse> saveHistory(@Query("token") String token, @Query("post_id") String newsId);

    /**獲取歷史記錄*/
    @GET("/api/get_history/")
    Observable<HistoryResponse> getHistory(@Query("token") String token, @Query("offset") int offset);

    /**獲取我的評論*/
    @GET("/api/get_my_comment/")
    Observable<CommentResponse> getComments(@Query("token") String token, @Query("offset") int offset);

    /**
     * 获取消息通知列表
     * @param token
     * @param offset
     * @return
     */
    @GET("/api/get_my_notify/")
    Observable<NotifyResponse> getNotify(@Query("token") String token, @Query("offset") int offset);

    /**
     * 获取消息通知 （未读总数）
     * @param token
     * @return
     */
    @GET("/api/get_notify_counter/")
    Observable<NotifyCounterModel> getNotifyCounter(@Query("token") String token);
}
