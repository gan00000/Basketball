package com.jiec.basketball.network;

import com.jiec.basketball.entity.response.NewsCommentResponse;
import com.jiec.basketball.network.base.CommResponse;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Jiec on 2019/1/13.
 */
public interface NewsApi {

    //点赞
    @FormUrlEncoded
    @POST("/api/save_post_like/")
    Observable<CommResponse> like(@Field("token") String token,
                                  @Field("post_id") String post_id,
                                  @Field("like") int like);

    //点赞評論
    @FormUrlEncoded
    @POST("/api/save_comment_like/")
    Observable<CommResponse> likeComment(@Field("token") String token,
                                         @Field("post_id") String post_id,
                                         @Field("comment_id") String comment_id,
                                         @Field("like") int like);

    //收藏
    @FormUrlEncoded
    @POST("/api/save_post/")
    Observable<CommResponse> collect(@Field("token") String token, @Field("post_id") String id);

    //取消收藏
    @FormUrlEncoded
    @POST("/api/delete_saved_post/")
    Observable<CommResponse> cancleCollect(@Field("token") String token, @Field("post_id") String id);

    //发布留言
    @FormUrlEncoded
    @POST("/api/save_post_comments/")
    Observable<CommResponse> comment(@Field("token") String token, @Field("post_id") String id,
                                     @Field("comment_txt") String comment_txt, @Field("reply_comment_id") String reply_comment_id);

    /**
     * 獲取所有新聞評論
     * @param token
     * @param post_id
     * @param offset
     * @return
     */
    @GET("/api/get_post_comments/")
    Observable<NewsCommentResponse> getNewsCommnet(@Query("token") String token, @Query("post_id") String post_id, @Query("offset") int offset);

    /**
     * 獲取所有新聞評論
     * @param token
     * @param post_id
     * @param offset
     * @return
     */
    @GET("/api/get_post_hot_comments/")
    Observable<NewsCommentResponse> getHotCommnet(@Query("token") String token, @Query("post_id") String post_id, @Query("offset") int offset);
}
