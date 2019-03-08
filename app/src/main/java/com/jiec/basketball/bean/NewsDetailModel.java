package com.jiec.basketball.bean;


import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.jiec.basketball.entity.NewsBean;
import com.jiec.basketball.network.base.CommResponse;

import java.util.List;

@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class NewsDetailModel extends CommResponse {

    public NewsBean post;
    public String previous_url;
    public String next_url;

}
