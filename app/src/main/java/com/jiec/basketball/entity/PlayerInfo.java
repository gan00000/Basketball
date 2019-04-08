package com.jiec.basketball.entity;

import com.bin.david.form.annotation.SmartColumn;
import com.bin.david.form.annotation.SmartTable;
import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by wangchuangjie on 2018/4/22.
 */

@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
@SmartTable(name = "player")
public class PlayerInfo {
    /**
     * id : 20000544
     * teamId : 22
     * lastname : Harden
     * firstname : James
     * jerseynumber : 13
     * position : PG
     * height : 77
     * weight : 220
     * birthdate : 1989-08-26T00:00:00
     * age :
     * birthcity : Bellflower
     * birthcountry :
     * isrookie :
     * isroster : null
     * isactive : null
     * officialImagesrc : https://s3-us-west-2.amazonaws.com/static.fantasydata.com/headshots/nba/low-res/20000544.png
     * score : 30.4306
     * name : James Harden
     * teamName : 火箭
     * season_name : 2018
     */

    @SmartColumn(id = 2, name = "位置")
    private String position;
    @SmartColumn(id = 4, name = "height")
    private String height;
    @SmartColumn(id = 5, name = "weight")
    private String weight;
    @SmartColumn(id = 6, name = "birthdate")
    private String birthdate;
    @SmartColumn(id = 7, name = "age")
    private String age;
    private String birthcity;
    private String birthcountry;
    private String isrookie;
    private Object isroster;
    private Object isactive;
    private String officialImagesrc;
    @SmartColumn(id = 3, name = "得分")
    private String score;
    @SmartColumn(id = 1, name = "球员", fixed = true)
    private String name;
    private String teamName;
    private String season_name;

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

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getSeason_name() {
        return season_name;
    }

    public void setSeason_name(String season_name) {
        this.season_name = season_name;
    }
}
