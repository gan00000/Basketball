package com.jiec.basketball.entity;

import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/2/6.
 */

@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class CustomFields implements Serializable {

    public List<Integer> getViews() {
        return views;
    }

    public void setViews(List<Integer> views) {
        this.views = views;
    }

    private List<Integer> views;
}
