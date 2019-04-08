package com.jiec.basketball.entity;

import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.List;

/**
 * Created by wangchuangjie on 2018/4/14.
 */

@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class PlayerFirstFive {


    private List<PlayerInfo> pts;
    private List<PlayerInfo> reb;
    private List<PlayerInfo> ast;
    private List<PlayerInfo> stl;
    private List<PlayerInfo> blk;
    private List<PlayerInfo> turnover;

    public List<PlayerInfo> getPts() {
        return pts;
    }

    public void setPts(List<PlayerInfo> pts) {
        this.pts = pts;
    }

    public List<PlayerInfo> getReb() {
        return reb;
    }

    public void setReb(List<PlayerInfo> reb) {
        this.reb = reb;
    }

    public List<PlayerInfo> getAst() {
        return ast;
    }

    public void setAst(List<PlayerInfo> ast) {
        this.ast = ast;
    }

    public List<PlayerInfo> getStl() {
        return stl;
    }

    public void setStl(List<PlayerInfo> stl) {
        this.stl = stl;
    }

    public List<PlayerInfo> getBlk() {
        return blk;
    }

    public void setBlk(List<PlayerInfo> blk) {
        this.blk = blk;
    }

    public List<PlayerInfo> getTurnover() {
        return turnover;
    }

    public void setTurnover(List<PlayerInfo> turnover) {
        this.turnover = turnover;
    }
}
