package com.jiec.basketball.entity;

import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.jiec.basketball.network.base.CommResponse;

import java.util.List;

/**
 * Created by wangchuangjie on 2018/5/13.
 */

@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class GameLiveInfo extends CommResponse {

    private List<MatchSummary> match_summary;
    private List<Matches> matches;
    private List<List<LiveFeedBean>> live_feed;

    public List<MatchSummary> getMatch_summary() {
        return match_summary;
    }

    public void setMatch_summary(List<MatchSummary> match_summary) {
        this.match_summary = match_summary;
    }

    public List<Matches> getMatches() {
        return matches;
    }

    public void setMatches(List<Matches> matches) {
        this.matches = matches;
    }

    public List<List<LiveFeedBean>> getLive_feed() {
        return live_feed;
    }

    public void setLive_feed(List<List<LiveFeedBean>> live_feed) {
        this.live_feed = live_feed;
    }


    @JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
    public static class LiveFeedBean {
        /**
         * id : 22262065
         * game_id : 12436
         * quarter : 1
         * minutes : 0
         * seconds : 0
         * event_type : Period
         * team_abbr : null
         * pts : 22 - 25
         * description : 第一節結束
         * teamName : null
         * awayPts : 22
         * homePts : 25
         * type : Period
         * time : 00:00
         */

        private String id;
        private String game_id;
        private String quarter;
        private String minutes;
        private String seconds;
        private String event_type;
        private String team_abbr;
        private String pts;
        private String description;
        private String teamName;
        private String awayPts;
        private String homePts;
        private String type;
        private String time;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getGame_id() {
            return game_id;
        }

        public void setGame_id(String game_id) {
            this.game_id = game_id;
        }

        public String getQuarter() {
            return quarter;
        }

        public void setQuarter(String quarter) {
            this.quarter = quarter;
        }

        public String getMinutes() {
            return minutes;
        }

        public void setMinutes(String minutes) {
            this.minutes = minutes;
        }

        public String getSeconds() {
            return seconds;
        }

        public void setSeconds(String seconds) {
            this.seconds = seconds;
        }

        public String getEvent_type() {
            return event_type;
        }

        public void setEvent_type(String event_type) {
            this.event_type = event_type;
        }

        public String getTeam_abbr() {
            return team_abbr;
        }

        public void setTeam_abbr(String team_abbr) {
            this.team_abbr = team_abbr;
        }

        public String getPts() {
            return pts;
        }

        public void setPts(String pts) {
            this.pts = pts;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getTeamName() {
            return teamName;
        }

        public void setTeamName(String teamName) {
            this.teamName = teamName;
        }

        public String getAwayPts() {
            return awayPts;
        }

        public void setAwayPts(String awayPts) {
            this.awayPts = awayPts;
        }

        public String getHomePts() {
            return homePts;
        }

        public void setHomePts(String homePts) {
            this.homePts = homePts;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
