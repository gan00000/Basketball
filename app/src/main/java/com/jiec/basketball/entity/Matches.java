package com.jiec.basketball.entity;

import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by wangchuangjie on 2018/5/20.
 */

@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class Matches {
    /**
     * id : 12436
     * scheduleStatus : Final
     * originalDate :
     * originalTime :
     * delayedOrPostponedReason :
     * gametime : 08:00AM
     * date : 2018-04-30
     * time : 08:00PM
     * awayTeam : 7
     * homeTeam : 9
     * location :
     * season_name : 2018POST
     * game_status : 1
     * homeLogo : https://upload.wikimedia.org/wikipedia/en/8/8f/Boston_Celtics.svg
     * homeName : 賽爾提克
     * awayName : 76ers
     * awayLogo : https://upload.wikimedia.org/wikipedia/en/thumb/0/0e/Philadelphia_76ers_logo.svg/200px-Philadelphia_76ers_logo.svg.png
     * away_pts : 101
     * home_pts : 117
     * gamedate : 2018-05-01
     * away_quarter_scores : 22,23,30,26
     * home_quarter_scores : 25,31,31,30
     */

    private String id;
    private String scheduleStatus;
    private String originalDate;
    private String originalTime;
    private String delayedOrPostponedReason;
    private String gametime;
    private String date;
    private String time;
    private String awayTeam;
    private String homeTeam;
    private String location;
    private String season_name;
    private String game_status;
    private String homeLogo;
    private String homeName;
    private String awayName;
    private String awayLogo;
    private String away_pts;
    private String home_pts;
    private String gamedate;
    private String away_quarter_scores;
    private String home_quarter_scores;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getScheduleStatus() {
        return scheduleStatus;
    }

    public void setScheduleStatus(String scheduleStatus) {
        this.scheduleStatus = scheduleStatus;
    }

    public String getOriginalDate() {
        return originalDate;
    }

    public void setOriginalDate(String originalDate) {
        this.originalDate = originalDate;
    }

    public String getOriginalTime() {
        return originalTime;
    }

    public void setOriginalTime(String originalTime) {
        this.originalTime = originalTime;
    }

    public String getDelayedOrPostponedReason() {
        return delayedOrPostponedReason;
    }

    public void setDelayedOrPostponedReason(String delayedOrPostponedReason) {
        this.delayedOrPostponedReason = delayedOrPostponedReason;
    }

    public String getGametime() {
        return gametime;
    }

    public void setGametime(String gametime) {
        this.gametime = gametime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(String awayTeam) {
        this.awayTeam = awayTeam;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSeason_name() {
        return season_name;
    }

    public void setSeason_name(String season_name) {
        this.season_name = season_name;
    }

    public String getGame_status() {
        return game_status;
    }

    public void setGame_status(String game_status) {
        this.game_status = game_status;
    }

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

    public String getGamedate() {
        return gamedate;
    }

    public void setGamedate(String gamedate) {
        this.gamedate = gamedate;
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
}