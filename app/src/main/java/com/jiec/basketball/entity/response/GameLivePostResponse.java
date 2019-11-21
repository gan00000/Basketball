package com.jiec.basketball.entity.response;

import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.jiec.basketball.entity.GameLivePost;
import com.jiec.basketball.network.base.CommResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/2/19.
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class GameLivePostResponse extends CommResponse {

    private List<GameLivePost> result;

    public List<GameLivePost> getResult() {
        return result;
    }

    public void setResult(List<GameLivePost> result) {
        this.result = result;
    }
}


