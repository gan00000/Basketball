package com.jiec.basketball.entity;

import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.jiec.basketball.network.base.CommResponse;

import java.util.List;

/**
 * Created by wangchuangjie on 2018/5/17.
 */

@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class GameList extends CommResponse {

    /**
     * status : ok
     */

    private List<GameInfo> matches;

    public List<GameInfo> getMatches() {
        return matches;
    }

    public void setMatches(List<GameInfo> matches) {
        this.matches = matches;
    }

}
