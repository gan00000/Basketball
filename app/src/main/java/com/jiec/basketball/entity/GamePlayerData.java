package com.jiec.basketball.entity;

import com.bin.david.form.annotation.SmartColumn;
import com.bin.david.form.annotation.SmartTable;
import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by wangchuangjie on 2018/4/22.
 */

@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
@SmartTable(name = "", count = true)
public class GamePlayerData {

    /**
     * gameId : 12469
     * playerId : 20001829
     * teamId : 9
     * fgatt : 10
     * pts : 18
     * fg3ptmade : 0
     * fg3ptatt : 1
     * minseconds : 1872
     * fgmade : 6
     * ast : 1
     * reb : 1
     * ftatt : 8
     * ftmade : 6
     * offreb : 0
     * defreb : 1
     * fouls : 2
     * blk : 1
     * stl : 0
     * blkagainst : 1
     * plusminus : -11
     * id : 20001829
     * 21 : 9
     * lastname : Tatum
     * firstname : J.
     * jerseynumber :
     * position : SF
     * height : 80
     * weight : 205
     * birthdate : 1998-03-03T00:00:00
     * age :
     * birthcity :
     * birthcountry :
     * isrookie :
     * isroster : null
     * isactive : null
     * officialImagesrc : https://s3-us-west-2.amazonaws.com/static.fantasydata.com/headshots/nba/low-res/20001829.png
     * isStarter : 1
     */

    private String gameId;
    private String playerId;
    private String teamId;
    private int fgatt;
    @SmartColumn(id = 4, name = "得分", autoCount = true)
    private int pts;
    private int fg3ptmade;
    private int fg3ptatt;
    @SmartColumn(id = 8, name = "3分")
    private String fg3pt;
    private String minseconds;
    @SmartColumn(id = 3, name = "時間")
    private String seconds;
    private int fgmade;
    @SmartColumn(id = 5, name = "助攻", autoCount = true)
    private int ast;
    @SmartColumn(id = 6, name = "籃板", autoCount = true)
    private int reb;
    @SmartColumn(id = 7, name = "投籃")
    private String shoot;
    private int ftatt;
    private int ftmade;
    @SmartColumn(id = 9, name = "罰球")
    private String ft;
    @SmartColumn(id = 10, name = "前場", autoCount = true)
    private int offreb;
    @SmartColumn(id = 11, name = "後場", autoCount = true)
    private int defreb;
    @SmartColumn(id = 12, name = "犯規", autoCount = true)
    private int fouls;
    @SmartColumn(id = 15, name = "封蓋", autoCount = true)
    private int blk;
    @SmartColumn(id = 13, name = "搶斷", autoCount = true)
    private int stl;
    @SmartColumn(id = 14, name = "失誤", autoCount = true)
    private int blkagainst;
    @SmartColumn(id = 16, name = "+/-")
    private String plusminus;
    private String id;
    private String lastname;
    private String firstname;
    private String jerseynumber;
    @SmartColumn(id = 2, name = "位置")
    private String position;
    private String height;
    private String weight;
    private String birthdate;
    private String age;
    private String birthcity;
    private String birthcountry;
    private String isrookie;
    private String isroster;
    private String isactive;
    private String officialImagesrc;
    private String isStarter;

    @SmartColumn(id = 1, name = "球员", fixed = true, width = 60)
    private String name;


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

    public int getFgatt() {
        return fgatt;
    }

    public void setFgatt(int fgatt) {
        this.fgatt = fgatt;
    }

    public int getPts() {
        return pts;
    }

    public void setPts(int pts) {
        this.pts = pts;
    }

    public int getFg3ptmade() {
        return fg3ptmade;
    }

    public void setFg3ptmade(int fg3ptmade) {
        this.fg3ptmade = fg3ptmade;
        this.fg3pt = this.fg3ptmade + " - " + this.fg3ptatt;
    }

    public int getFg3ptatt() {
        return fg3ptatt;
    }

    public void setFg3ptatt(int fg3ptatt) {
        this.fg3ptatt = fg3ptatt;
        this.fg3pt = this.fg3ptmade + " - " + this.fg3ptatt;
    }

    public String getMinseconds() {
        return minseconds;
    }

    public void setMinseconds(String minseconds) {
        this.minseconds = minseconds;
        int _seconds = Integer.parseInt(minseconds);
        this.seconds = formate(_seconds / 60) + ":" + formate(_seconds % 60);
    }

    private String formate(int num) {
        if (num < 10) {
            return "0" + num;
        }

        return num + "";
    }

    public int getFgmade() {
        return fgmade;
    }

    public void setFgmade(int fgmade) {
        this.fgmade = fgmade;
        this.shoot = fgmade + " - " + fgatt;
    }

    public int getAst() {
        return ast;
    }

    public void setAst(int ast) {
        this.ast = ast;
    }

    public int getReb() {
        return reb;
    }

    public void setReb(int reb) {
        this.reb = reb;
    }

    public int getFtatt() {
        return ftatt;
    }

    public void setFtatt(int ftatt) {
        this.ftatt = ftatt;
    }

    public int getFtmade() {
        return ftmade;
    }

    public void setFtmade(int ftmade) {
        this.ftmade = ftmade;
        this.ft = ftmade + " - " + ftatt;
    }

    public int getOffreb() {
        return offreb;
    }

    public void setOffreb(int offreb) {
        this.offreb = offreb;
    }

    public int getDefreb() {
        return defreb;
    }

    public void setDefreb(int defreb) {
        this.defreb = defreb;
    }

    public int getFouls() {
        return fouls;
    }

    public void setFouls(int fouls) {
        this.fouls = fouls;
    }

    public int getBlk() {
        return blk;
    }

    public void setBlk(int blk) {
        this.blk = blk;
    }

    public int getStl() {
        return stl;
    }

    public void setStl(int stl) {
        this.stl = stl;
    }

    public int getBlkagainst() {
        return blkagainst;
    }

    public void setBlkagainst(int blkagainst) {
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

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
        this.name = getFirst() + "." + this.lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
        this.name = getFirst() + "." + this.lastname;
    }

    private String getFirst() {
        return (firstname != null && firstname.length() > 0) ? firstname.substring(0, 1) : "";
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

    public String getIsroster() {
        return isroster;
    }

    public void setIsroster(String isroster) {
        this.isroster = isroster;
    }

    public String getIsactive() {
        return isactive;
    }

    public void setIsactive(String isactive) {
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

