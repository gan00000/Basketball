package com.jiec.basketball.entity;

import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by wangchuangjie on 2018/5/20.
 */

@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class MatchSummary {
    /**
     * homeLogo : https://upload.wikimedia.org/wikipedia/en/8/8f/Boston_Celtics.svg
     * homeName : 賽爾提克
     * awayName : 76ers
     * awayLogo : https://upload.wikimedia.org/wikipedia/en/thumb/0/0e/Philadelphia_76ers_logo.svg/200px-Philadelphia_76ers_logo.svg.png
     * homeTeam : 9
     * awayTeam : 7
     * date : 2018-04-30
     * scheduleStatus : Final
     * away_pts : 101
     * home_pts : 117
     * away_quarter_scores : 22,23,30,26
     * home_quarter_scores : 25,31,31,30
     * game_status : 1
     * quarter : 4
     * minutes : 1
     * seconds : 18
     * time : 01:18
     */

    private String homeLogo;
    private String homeName;
    private String awayName;
    private String awayLogo;
    private String homeTeam;
    private String awayTeam;
    private String date;
    private String scheduleStatus;
    private String away_pts;
    private String home_pts;
    private String away_quarter_scores;
    private String home_quarter_scores;
    private String game_status;
    private String quarter;
    private String minutes;
    private String seconds;
    private String time;

    public String getHomeLogo() {
        return homeLogo;
    }

    public void setHomeLogo(String homeLogo) {
        this.homeLogo = homeLogo;
    }

    public String getHomeName() {
        return homeName;
    }

    public void setHomeName(String homeName) {
        this.homeName = homeName;
    }

    public String getAwayName() {
        return awayName;
    }

    public void setAwayName(String awayName) {
        this.awayName = awayName;
    }

    public String getAwayLogo() {
        return awayLogo;
    }

    public void setAwayLogo(String awayLogo) {
        this.awayLogo = awayLogo;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(String awayTeam) {
        this.awayTeam = awayTeam;
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

    public String getAway_pts() {
        return away_pts;
    }

    public void setAway_pts(String away_pts) {
        this.away_pts = away_pts;
    }

    public String getHome_pts() {
        return home_pts;
    }

    public void setHome_pts(String home_pts) {
        this.home_pts = home_pts;
    }

    public String getAway_quarter_scores() {
        return away_quarter_scores;
    }

    public void setAway_quarter_scores(String away_quarter_scores) {
        this.away_quarter_scores = away_quarter_scores;
    }

    public String getHome_quarter_scores() {
        return home_quarter_scores;
    }

    public void setHome_quarter_scores(String home_quarter_scores) {
        this.home_quarter_scores = home_quarter_scores;
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

