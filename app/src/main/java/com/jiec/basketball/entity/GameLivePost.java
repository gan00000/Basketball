package com.jiec.basketball.entity;

import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.List;

/**
 * Created by wangchuangjie on 2018/4/21.
 */

@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class GameLivePost {

//    {
//        status: "ok",
//                result:- [
//        -{
//                game_id: null,
//            url: "http://app.ballgametime.com/news/188963.html",
//            post_id: "188963",
//            live_url:- [
//        "http://v1.freenbalive.com/live/stream2833121/index.m3u8",
//                "http://v1.freenbalive.com/live/streamhd13776/index.m3u8"
//]
//}
//]
//    }

    private String game_id;
    private String url;
    private String post_id;
    private List<String> live_url;


    public String getGame_id() {
        return game_id;
    }

    public void setGame_id(String game_id) {
        this.game_id = game_id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public List<String> getLive_url() {
        return live_url;
    }

    public void setLive_url(List<String> live_url) {
        this.live_url = live_url;
    }
}
