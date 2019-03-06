package com.jiec.basketball.entity;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.List;

/**
 * Created by wangchuangjie on 2018/4/14.
 */

@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class TeamData {

    private List<Team> pts;
    private List<Team> reb;
    private List<Team> ast;
    private List<Team> stl;
    private List<Team> blk;
    private List<Team> turnover;

    public List<Team> getPts() {
        return pts;
    }

    public void setPts(List<Team> pts) {
        this.pts = pts;
    }

    public List<Team> getReb() {
        return reb;
    }

    public void setReb(List<Team> reb) {
        this.reb = reb;
    }

    public List<Team> getAst() {
        return ast;
    }

    public void setAst(List<Team> ast) {
        this.ast = ast;
    }

    public List<Team> getStl() {
        return stl;
    }

    public void setStl(List<Team> stl) {
        this.stl = stl;
    }

    public List<Team> getBlk() {
        return blk;
    }

    public void setBlk(List<Team> blk) {
        this.blk = blk;
    }

    public List<Team> getTurnover() {
        return turnover;
    }

    public void setTurnover(List<Team> turnover) {
        this.turnover = turnover;
    }

    @JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
    public static class Team {

        /**
         * Games : 82
         * id : 7
         * name : 76ers
         * ch_name : 76ers
         * city : Philadelphia
         * ch_city : null
         * logo : null
         * abbreviation :
         * team_logo : https://upload.wikimedia.org/wikipedia/en/thumb/0/0e/Philadelphia_76ers_logo.svg/200px-Philadelphia_76ers_logo.svg.png
         * score : 1302
         * TeamID : 7
         * teamName : 76ers
         * matches : 82
         * pts : 15.8780
         * season_name : 2018
         */

        private String Games;
        private String id;
        private String name;
        private String ch_name;
        private String city;
        private Object ch_city;
        private Object logo;
        private String abbreviation;
        private String team_logo;
        private String score;
        private String TeamID;
        private String teamName;
        private String matches;
        private String pts;

        private String season_name;

        public String getGames() {
            return Games;
        }

        public void setGames(String Games) {
            this.Games = Games;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCh_name() {
            return ch_name;
        }

        public void setCh_name(String ch_name) {
            this.ch_name = ch_name;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public Object getCh_city() {
            return ch_city;
        }

        public void setCh_city(Object ch_city) {
            this.ch_city = ch_city;
        }

        public Object getLogo() {
            return logo;
        }

        public void setLogo(Object logo) {
            this.logo = logo;
        }

        public String getAbbreviation() {
            return abbreviation;
        }

        public void setAbbreviation(String abbreviation) {
            this.abbreviation = abbreviation;
        }

        public String getTeam_logo() {
            return team_logo;
        }

        public void setTeam_logo(String team_logo) {
            this.team_logo = team_logo;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getTeamID() {
            return TeamID;
        }

        public void setTeamID(String TeamID) {
            this.TeamID = TeamID;
        }

        public String getTeamName() {
            return teamName;
        }

        public void setTeamName(String teamName) {
            this.teamName = teamName;
        }

        public String getMatches() {
            return matches;
        }

        public void setMatches(String matches) {
            this.matches = matches;
        }

        public String getPts() {
            return pts;
        }

        public void setPts(String pts) {
            this.pts = pts;
        }

        public String getSeason_name() {
            return season_name;
        }

        public void setSeason_name(String season_name) {
            this.season_name = season_name;
        }
    }
}
