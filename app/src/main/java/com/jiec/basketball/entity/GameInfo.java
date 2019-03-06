package com.jiec.basketball.entity;

import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by wangchuangjie on 2018/4/21.
 */

@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class GameInfo {

    /**
     * id : 12468
     * scheduleStatus : Final
     * originalDate :
     * originalTime :
     * delayedOrPostponedReason :
     * gametime : 08:30AM
     * date : 2018-05-15
     * time : 08:30PM
     * awayTeam : 12
     * homeTeam : 9
     * location :
     * season_name : 2018POST
     * game_status : 1
     * homeLogo : https://upload.wikimedia.org/wikipedia/en/8/8f/Boston_Celtics.svg
     * homeName : 賽爾提克
     * awayName : 騎士
     * awayLogo : https://upload.wikimedia.org/wikipedia/en/4/4b/Cleveland_Cavaliers_logo.svg
     * homeAbbr :
     * awayAbbr :
     * away_pts : 94
     * home_pts : 107
     * gamedate : 2018-05-16
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
    private String homeAbbr;
    private String awayAbbr;
    private String away_pts;
    private String home_pts;
    private String gamedate;

    private String quarter;

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

    public String getHomeAbbr() {
        return homeAbbr;
    }

    public void setHomeAbbr(String homeAbbr) {
        this.homeAbbr = homeAbbr;
    }

    public String getAwayAbbr() {
        return awayAbbr;
    }

    public void setAwayAbbr(String awayAbbr) {
        this.awayAbbr = awayAbbr;
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

    public String getQuarter() {
        return quarter;
    }

    public void setQuarter(String quarter) {
        this.quarter = quarter;
    }
}
