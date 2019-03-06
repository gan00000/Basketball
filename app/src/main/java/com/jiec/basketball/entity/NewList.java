package com.jiec.basketball.entity;

import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.List;

/**
 * Created by Administrator on 2017/2/19.
 */

@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class NewList {

    public List<NewsBean> posts;

    public List<NewsBean> getPosts() {
        return posts;
    }

    public void setPosts(List<NewsBean> posts) {
        this.posts = posts;
    }
}
