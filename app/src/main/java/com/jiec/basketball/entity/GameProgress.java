package com.jiec.basketball.entity;

import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.jiec.basketball.network.base.CommResponse;

/**
 * Created by wangchuangjie on 2018/5/26.
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class GameProgress extends CommResponse {

    /**
     * status : ok
     * match_progress : {"game_id":"12436","date":"2018-04-30","scheduleStatus":"Final","game_status":"1","quarter":"4","minutes":"1","seconds":"18","time":"01:18"}
     */

    private MatchProgress match_progress;

    public MatchProgress getMatch_progress() {
        return match_progress;
    }

    public void setMatch_progress(MatchProgress match_progress) {
        this.match_progress = match_progress;
    }

    @JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
    public static class MatchProgress {
        /**
         * game_id : 12436
         * date : 2018-04-30
         * scheduleStatus : Final
         * game_status : 1
         * quarter : 4
         * minutes : 1
         * seconds : 18
         * time : 01:18
         */

        private String game_id;
        private String date;
        private String scheduleStatus;
        private String game_status;
        private String quarter;
        private String minutes;
        private String seconds;
        private String time;

        public String getGame_id() {
            return game_id;
        }

        public void setGame_id(String game_id) {
            this.game_id = game_id;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getScheduleStatus() {
            return scheduleStatus;
        }

        public void setScheduleStatus(String scheduleStatus) {
            this.scheduleStatus = scheduleStatus;
        }

        public String getGame_status() {
            return game_status;
        }

        public void setGame_status(String game_status) {
            this.game_status = game_status;
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

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
