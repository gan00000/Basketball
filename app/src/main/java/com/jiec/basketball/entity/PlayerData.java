package com.jiec.basketball.entity;

import com.bin.david.form.annotation.SmartColumn;

/**
 * Created by wangchuangjie on 2018/4/22.
 */

public class PlayerData {

    /**
     * gameId : 12388
     * playerId : 20001661
     * teamId : 7
     * fgatt : 17
     * pts : 24
     * fg3ptmade : 0
     * fg3ptatt : 0
     * minseconds : 2342
     * fgmade : 10
     * ast : 8
     * reb : 8
     * ftatt : 6
     * ftmade : 4
     * offreb : 2
     * defreb : 6
     * fouls : 5
     * blk : 2
     * stl : 0
     * blkagainst : 2
     * plusminus : -9
     * id : 20001661
     * lastname : Simmons
     * firstname : B.
     * jerseynumber : 25
     * position : PG
     * height : 82
     * weight : 230
     * birthdate : 1996-07-20T00:00:00
     * age :
     * birthcity : Melbourne
     * birthcountry :
     * isrookie :
     * isroster : null
     * isactive : null
     * officialImagesrc : https://s3-us-west-2.amazonaws.com/static.fantasydata.com/headshots/nba/low-res/20001661.png
     * isStarter : 1
     */

    private String gameId;
    private String playerId;
    private String teamId;
    private String fgatt;
    private String pts;
    private String fg3ptmade;
    private String fg3ptatt;

    @SmartColumn(id = 3,name = "時間")
    private String minseconds;
    private String fgmade;
    private String ast;
    private String reb;
    @SmartColumn(id = 4, name = "得分")
    private String ftatt;
    private String ftmade;
    private String offreb;
    private String defreb;
    private String fouls;
    private String blk;
    private String stl;
    private String blkagainst;
    private String plusminus;
    private String id;

    @SmartColumn(id = 1,name = "球员")
    private String name;
    private String lastname;
    private String firstname;
    private String jerseynumber;

    @SmartColumn(id = 2,name = "")
    private String position;
    private String height;
    private String weight;
    private String birthdate;
    private String age;
    private String birthcity;
    private String birthcountry;
    private String isrookie;
    private Object isroster;
    private Object isactive;
    private String officialImagesrc;
    private String isStarter;

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getFgatt() {
        return fgatt;
    }

    public void setFgatt(String fgatt) {
        this.fgatt = fgatt;
    }

    public String getPts() {
        return pts;
    }

    public void setPts(String pts) {
        this.pts = pts;
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

    public String getMinseconds() {
        return minseconds;
    }

    public void setMinseconds(String minseconds) {
        this.minseconds = minseconds;
    }

    public String getFgmade() {
        return fgmade;
    }

    public void setFgmade(String fgmade) {
        this.fgmade = fgmade;
    }

    public String getAst() {
        return ast;
    }

    public void setAst(String ast) {
        this.ast = ast;
    }

    public String getReb() {
        return reb;
    }

    public void setReb(String reb) {
        this.reb = reb;
    }

    public String getFtatt() {
        return ftatt;
    }

    public void setFtatt(String ftatt) {
        this.ftatt = ftatt;
    }

    public String getFtmade() {
        return ftmade;
    }

    public void setFtmade(String ftmade) {
        this.ftmade = ftmade;
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

    public String getFouls() {
        return fouls;
    }

    public void setFouls(String fouls) {
        this.fouls = fouls;
    }

    public String getBlk() {
        return blk;
    }

    public void setBlk(String blk) {
        this.blk = blk;
    }

    public String getStl() {
        return stl;
    }

    public void setStl(String stl) {
        this.stl = stl;
    }

    public String getBlkagainst() {
        return blkagainst;
    }

    public void setBlkagainst(String blkagainst) {
        this.blkagainst = blkagainst;
    }

    public String getPlusminus() {
        return plusminus;
    }

    public void setPlusminus(String plusminus) {
        this.plusminus = plusminus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return getFirstname() + "." + getLastname();
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public String getJerseynumber() {
        return jerseynumber;
    }

    public void setJerseynumber(String jerseynumber) {
        this.jerseynumber = jerseynumber;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getBirthcity() {
        return birthcity;
    }

    public void setBirthcity(String birthcity) {
        this.birthcity = birthcity;
    }

    public String getBirthcountry() {
        return birthcountry;
    }

    public void setBirthcountry(String birthcountry) {
        this.birthcountry = birthcountry;
    }

    public String getIsrookie() {
        return isrookie;
    }

    public void setIsrookie(String isrookie) {
        this.isrookie = isrookie;
    }

    public Object getIsroster() {
        return isroster;
    }

    public void setIsroster(Object isroster) {
        this.isroster = isroster;
    }

    public Object getIsactive() {
        return isactive;
    }

    public void setIsactive(Object isactive) {
        this.isactive = isactive;
    }

    public String getOfficialImagesrc() {
        return officialImagesrc;
    }

    public void setOfficialImagesrc(String officialImagesrc) {
        this.officialImagesrc = officialImagesrc;
    }

    public String getIsStarter() {
        return isStarter;
    }

    public void setIsStarter(String isStarter) {
        this.isStarter = isStarter;
    }
}
