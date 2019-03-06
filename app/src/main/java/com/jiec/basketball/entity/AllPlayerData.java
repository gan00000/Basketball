package com.jiec.basketball.entity;

import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.jiec.basketball.network.base.CommResponse;

import java.util.List;

/**
 * Created by wangchuangjie on 2018/5/13.
 */

@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class AllPlayerData extends CommResponse {

    /**
     * status : ok
     * players_rank : [{"lastname":"Davis","firstname":"Anthony","teamName":"鵜鶘","pts":"271","fgmade":"106","fgatt":"205","fg3ptmade":"6","fg3ptatt":"22","ftmade":"53","ftatt":"64","minseconds":"21488","matches":"9","reb":"123","offreb":"26","defreb":"97","ast":"15","fouls":"30","stl":"18","blk":"22","turnover":"23","type_avg":"13.6667"}]
     */

    private List<PlayersRankBean> players_rank;

    public List<PlayersRankBean> getPlayers_rank() {
        return players_rank;
    }

    public void setPlayers_rank(List<PlayersRankBean> players_rank) {
        this.players_rank = players_rank;
    }

    @JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
    public static class PlayersRankBean {
        /**
         * lastname : Davis
         * firstname : Anthony
         * teamName : 鵜鶘
         * pts : 271
         * fgmade : 106
         * fgatt : 205
         * fg3ptmade : 6
         * fg3ptatt : 22
         * ftmade : 53
         * ftatt : 64
         * minseconds : 21488
         * matches : 9
         * reb : 123
         * offreb : 26
         * defreb : 97
         * ast : 15
         * fouls : 30
         * stl : 18
         * blk : 22
         * turnover : 23
         * type_avg : 13.6667
         */

        private String lastname;
        private String firstname;
        private String teamName;
        private String pts;
        private String fgmade;
        private String fgatt;
        private String fg3ptmade;
        private String fg3ptatt;
        private String ftmade;
        private String ftatt;
        private String minseconds;
        private String matches;
        private String reb;
        private String offreb;
        private String defreb;
        private String ast;
        private String fouls;
        private String stl;
        private String blk;
        private String turnover;
        private String type_avg;

        public String getLastname() {
            return lastname;
        }

        public void setLastname(String lastname) {
            this.lastname = lastname;
        }

        public String getFirstname() {
            return firstname;
        }

        public void setFirstname(String firstname) {
            this.firstname = firstname;
        }

        public String getTeamName() {
            return teamName;
        }

        public void setTeamName(String teamName) {
            this.teamName = teamName;
        }

        public String getPts() {
            return pts;
        }

        public void setPts(String pts) {
            this.pts = pts;
        }

        public String getFgmade() {
            return fgmade;
        }

        public void setFgmade(String fgmade) {
            this.fgmade = fgmade;
        }

        public String getFgatt() {
            return fgatt;
        }

        public void setFgatt(String fgatt) {
            this.fgatt = fgatt;
        }

        public String getFg3ptmade() {
            return fg3ptmade;
        }

        public void setFg3ptmade(String fg3ptmade) {
            this.fg3ptmade = fg3ptmade;
        }

        public String getFg3ptatt() {
            return fg3ptatt;
        }

        public void setFg3ptatt(String fg3ptatt) {
            this.fg3ptatt = fg3ptatt;
        }

        public String getFtmade() {
            return ftmade;
        }

        public void setFtmade(String ftmade) {
            this.ftmade = ftmade;
        }

        public String getFtatt() {
            return ftatt;
        }

        public void setFtatt(String ftatt) {
            this.ftatt = ftatt;
        }

        public String getMinseconds() {
            return minseconds;
        }

        public void setMinseconds(String minseconds) {
            this.minseconds = minseconds;
        }

        public String getMatches() {
            return matches;
        }

        public void setMatches(String matches) {
            this.matches = matches;
        }

        public String getReb() {
            return reb;
        }

        public void setReb(String reb) {
            this.reb = reb;
        }

        public String getOffreb() {
            return offreb;
        }

        public void setOffreb(String offreb) {
            this.offreb = offreb;
        }

        public String getDefreb() {
            return defreb;
        }

        public void setDefreb(String defreb) {
            this.defreb = defreb;
        }

        public String getAst() {
            return ast;
        }

        public void setAst(String ast) {
            this.ast = ast;
        }

        public String getFouls() {
            return fouls;
        }

        public void setFouls(String fouls) {
            this.fouls = fouls;
        }

        public String getStl() {
            return stl;
        }

        public void setStl(String stl) {
            this.stl = stl;
        }

        public String getBlk() {
            return blk;
        }

        public void setBlk(String blk) {
            this.blk = blk;
        }

        public String getTurnover() {
            return turnover;
        }

        public void setTurnover(String turnover) {
            this.turnover = turnover;
        }

        public String getType_avg() {
            return type_avg;
        }

        public void setType_avg(String type_avg) {
            this.type_avg = type_avg;
        }
    }
}
