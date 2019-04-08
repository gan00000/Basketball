package com.jiec.basketball.entity.response;

import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.jiec.basketball.entity.Thumbnail;

/**
 * Created by jiec on 2017/2/26.
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class PostResponse {
    Thumbnail post;

    public Thumbnail getPost() {
        return post;
    }

    public void setPost(Thumbnail post) {
        this.post = post;
    }

    public String getUrl() {
        return post.getThumbnail();
    }
}
