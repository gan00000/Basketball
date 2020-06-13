package com.jiec.basketball.entity;

import com.bin.david.form.annotation.SmartColumn;
import com.bin.david.form.annotation.SmartTable;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.jiec.basketball.utils.NumberUtils;

/**
 * Created by wangchuangjie on 2018/5/13.
 */

@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
@SmartTable(name = "teamRank")
public class TernBean {
    /**
     * rank : 1
     * teamId : 10
     * division : 1
     * pts : 1070
     * wins : 4
     * losses : 6
     * ptsagainst : 1120
     * gamesplayed : 10
     * season_name : 2018
     * id : 10
     * name : Raptors
     * ch_name : 暴龍
     * city : Toronto
     * ch_city : null
     * logo : null
     * abbreviation :
     * team_logo : https://upload.wikimedia.org/wikipedia/en/thumb/3/36/Toronto_Raptors_logo.svg/200px-Toronto_Raptors_logo.svg.png
     * teamName : 暴龍
     * homewin : 3
     * homelose : 2
     * awaywin : 1
     * awaylose : 4
     */

    @SmartColumn(id = 1, name = "排名", fixed = true)
    private String rank;
    private String teamId;
    private String division;
    private String pts;
//    @SmartColumn(id = 3, name = "勝")
    private String wins;
//    @SmartColumn(id = 4, name = "負")
    private String losses;
    private String ptsagainst;
    private String gamesplayed;
    private String season_name;
    private String id;
    private String name;
    @SmartColumn(id = 2, name = "隊名", fixed = true, width = 60)
    private String ch_name;
    private String city;
    private Object ch_city;
    private Object logo;
    private String abbreviation;
    private String team_logo;
    private String teamName;
    private String homewin;
    private String homelose;
    private String awaywin;
    private String awaylose;
    @SmartColumn(id = 5, name = "勝率")
    private String win_rate;

//    @SmartColumn(id = 6, name = "主場")
    private String home;

//    @SmartColumn(id = 7, name = "賽場")
    private String away;

    @SmartColumn(id = 3, name = "勝-負")
    private String all;

//    @SmartColumn(id = 9, name = "得分")
    private String winPt;

//    @SmartColumn(id = 10, name = "失分")
    private String lostPt;

    @SmartColumn(id = 7, name = "連勝/負")
    private String streakDescription;

    @SmartColumn(id = 6, name = "勝差")
    private String gamesBack;

    public String getStreakDescription() {
        return streakDescription;
    }

    public void setStreakDescription(String streakDescription) {
        this.streakDescription = streakDescription;
    }

    public String getGamesBack() {
        return gamesBack;
    }

    public void setGamesBack(String gamesBack) {
        this.gamesBack = gamesBack;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getPts() {
        return pts;
    }

    public void setPts(String pts) {
        this.pts = pts;
    }

    public String getWins() {
        return wins;
    }

    public void setWins(String wins) {
        this.wins = wins;
    }

    public String getLosses() {
        return losses;
    }

    public void setLosses(String losses) {
        this.losses = losses;
    }

    public String getPtsagainst() {
        return ptsagainst;
    }

    public void setPtsagainst(String ptsagainst) {
        this.ptsagainst = ptsagainst;
    }

    public String getGamesplayed() {
        return gamesplayed;
    }

    public void setGamesplayed(String gamesplayed) {
        this.gamesplayed = gamesplayed;
    }

    public String getSeason_name() {
        return season_name;
    }

    public void setSeason_name(String season_name) {
        this.season_name = season_name;
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

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getHomewin() {
        return homewin;
    }

    public void setHomewin(String homewin) {
        this.homewin = homewin;
    }

    public String getHomelose() {
        return homelose;
    }

    public void setHomelose(String homelose) {
        this.homelose = homelose;
    }

    public String getAwaywin() {
        return awaywin;
    }

    public void setAwaywin(String awaywin) {
        this.awaywin = awaywin;
    }

    public String getAwaylose() {
        return awaylose;
    }

    public void setAwaylose(String awaylose) {
        this.awaylose = awaylose;
    }

    public String getWin_rate() {
        return win_rate;
    }

    public void setWin_rate() {
        this.win_rate = NumberUtils.formatRate(
                100 * Float.parseFloat(wins) / (Float.parseFloat(wins) + Float.parseFloat(losses))) + "%";
    }

    public void setHome() {
        this.home = homewin + "-" + homelose;
    }

    public void setAway() {
        this.away = this.awaywin + "-" + this.awaylose;
    }

    public void setAll() {
        this.all = wins + "-" + losses;
    }

    public void setWinPt() {
        this.winPt = NumberUtils.formatRate(
                Float.parseFloat(pts) / (Integer.parseInt(wins) + Integer.parseInt(losses)));
    }

    public void setLostPt() {
        this.lostPt = NumberUtils.formatRate(
                Float.parseFloat(ptsagainst) / (Integer.parseInt(wins) + Integer.parseInt(losses)));
    }


}
