package com.jiec.basketball.entity.response;

import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.jiec.basketball.entity.Category;
import com.jiec.basketball.entity.NewsBean;
import com.jiec.basketball.network.base.CommResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/1/8.
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class NewListResponse extends CommResponse {
    private Category category;

    private List<NewsBean> posts;

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<NewsBean> getPosts() {
        return posts;
    }

    public void setPosts(List<NewsBean> posts) {
        this.posts = posts;
    }
}

