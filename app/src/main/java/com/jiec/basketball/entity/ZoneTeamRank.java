package com.jiec.basketball.entity;

import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by wangchuangjie on 2018/5/12.
 */

@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class ZoneTeamRank {


    /**
     * status : ok
     * Eastern : [{"rank":"1","teamId":"10","division":"1","pts":"1070","wins":"4","losses":"6","ptsagainst":"1120","gamesplayed":"10","season_name":"2018","id":"10","name":"Raptors","ch_name":"暴龍","city":"Toronto","ch_city":null,"logo":null,"abbreviation":"","team_logo":"https://upload.wikimedia.org/wikipedia/en/thumb/3/36/Toronto_Raptors_logo.svg/200px-Toronto_Raptors_logo.svg.png","teamName":"暴龍","homewin":"3","homelose":"2","awaywin":"1","awaylose":"4"},{"rank":"1","teamId":"9","division":"1","pts":"1248","wins":"8","losses":"4","ptsagainst":"1236","gamesplayed":"12","season_name":"2018","id":"9","name":"Celtics","ch_name":"賽爾提克","city":"Boston","ch_city":null,"logo":null,"abbreviation":"","team_logo":"https://upload.wikimedia.org/wikipedia/en/8/8f/Boston_Celtics.svg","teamName":"賽爾提克","homewin":"7","homelose":"14","awaywin":"1","awaylose":"4"},{"rank":"1","teamId":"7","division":"1","pts":"1090","wins":"5","losses":"5","ptsagainst":"1050","gamesplayed":"10","season_name":"2018","id":"7","name":"76ers","ch_name":"76ers","city":"Philadelphia","ch_city":null,"logo":null,"abbreviation":"","team_logo":"https://upload.wikimedia.org/wikipedia/en/thumb/0/0e/Philadelphia_76ers_logo.svg/200px-Philadelphia_76ers_logo.svg.png","teamName":"76ers","homewin":"3","homelose":"2","awaywin":"2","awaylose":"3"},{"rank":"2","teamId":"12","division":"1","pts":"1133","wins":"8","losses":"3","ptsagainst":"1122","gamesplayed":"11","season_name":"2018","id":"12","name":"Cavaliers","ch_name":"騎士","city":"Cleveland","ch_city":null,"logo":null,"abbreviation":"","team_logo":"https://upload.wikimedia.org/wikipedia/en/4/4b/Cleveland_Cavaliers_logo.svg","teamName":"騎士","homewin":"5","homelose":"1","awaywin":"3","awaylose":"2"},{"rank":"2","teamId":"13","division":"1","pts":"707","wins":"3","losses":"4","ptsagainst":"665","gamesplayed":"7","season_name":"2018","id":"13","name":"Pacers","ch_name":"遛马","city":"Indiana","ch_city":null,"logo":null,"abbreviation":"","team_logo":"https://upload.wikimedia.org/wikipedia/en/1/1b/Indiana_Pacers.svg","teamName":"遛马","homewin":"2","homelose":"1","awaywin":"1","awaylose":"3"},{"rank":"2","teamId":"15","division":"1","pts":"714","wins":"3","losses":"4","ptsagainst":"714","gamesplayed":"7","season_name":"2018","id":"15","name":"Bucks","ch_name":"雄鹿","city":"Milwaukee","ch_city":null,"logo":null,"abbreviation":"","team_logo":"https://upload.wikimedia.org/wikipedia/en/4/4a/Milwaukee_Bucks_logo.svg","teamName":"雄鹿","homewin":"3","homelose":"16","awaywin":"19","awaylose":"4"},{"rank":"3","teamId":"1","division":"1","pts":"642","wins":"2","losses":"4","ptsagainst":"654","gamesplayed":"6","season_name":"2018","id":"1","name":"Wizards","ch_name":"巫師","city":"Washington","ch_city":null,"logo":null,"abbreviation":"","team_logo":"https://upload.wikimedia.org/wikipedia/en/0/02/Washington_Wizards_logo.svg","teamName":"巫師","homewin":"2","homelose":"1","awaywin":"20","awaylose":"3"},{"rank":"3","teamId":"4","division":"1","pts":"515","wins":"1","losses":"4","ptsagainst":"570","gamesplayed":"5","season_name":"2018","id":"4","name":"Heat","ch_name":"熱火","city":"Miami","ch_city":null,"logo":null,"abbreviation":"","team_logo":"https://upload.wikimedia.org/wikipedia/en/f/fb/Miami_Heat_logo.svg","teamName":"熱火","homewin":"26","homelose":"2","awaywin":"1","awaylose":"2"}]
     * Western : [{"rank":"4","teamId":"19","division":"2","pts":"5618","wins":"49","losses":"4","ptsagainst":"6095","gamesplayed":"53","season_name":"2018","id":"19","name":"Trail Blazers","ch_name":"拓荒者","city":"Portland","ch_city":null,"logo":null,"abbreviation":"","team_logo":"https://upload.wikimedia.org/wikipedia/en/thumb/2/21/Portland_Trail_Blazers_logo.svg/260px-Portland_Trail_Blazers_logo.svg.png","teamName":"拓荒者","homewin":"28","homelose":"2","awaywin":"21","awaylose":"2"},{"rank":"4","teamId":"18","division":"2","pts":"606","wins":"2","losses":"4","ptsagainst":"636","gamesplayed":"6","season_name":"2018","id":"18","name":"Thunder","ch_name":"雷霆","city":"Oklahoma City","ch_city":null,"logo":null,"abbreviation":"","team_logo":"https://upload.wikimedia.org/wikipedia/en/5/5d/Oklahoma_City_Thunder.svg","teamName":"雷霆","homewin":"2","homelose":"1","awaywin":"21","awaylose":"3"},{"rank":"4","teamId":"17","division":"2","pts":"1122","wins":"5","losses":"6","ptsagainst":"1155","gamesplayed":"11","season_name":"2018","id":"17","name":"Jazz","ch_name":"爵士","city":"Utah","ch_city":null,"logo":null,"abbreviation":"","team_logo":"https://upload.wikimedia.org/wikipedia/en/0/04/Utah_Jazz_logo_%282016%29.svg","teamName":"爵士","homewin":"3","homelose":"2","awaywin":"2","awaylose":"4"},{"rank":"4","teamId":"16","division":"2","pts":"510","wins":"1","losses":"4","ptsagainst":"550","gamesplayed":"5","season_name":"2018","id":"16","name":"Timberwolves","ch_name":"灰狼","city":"Minnesota","ch_city":null,"logo":null,"abbreviation":"","team_logo":"https://upload.wikimedia.org/wikipedia/en/thumb/c/c2/Minnesota_Timberwolves_logo.svg/200px-Minnesota_Timberwolves_logo.svg.png","teamName":"灰狼","homewin":"1","homelose":"1","awaywin":"17","awaylose":"3"},{"rank":"5","teamId":"26","division":"2","pts":"1100","wins":"8","losses":"2","ptsagainst":"1020","gamesplayed":"10","season_name":"2018","id":"26","name":"Warriors","ch_name":"勇士","city":"Golden State","ch_city":null,"logo":null,"abbreviation":"","team_logo":"https://upload.wikimedia.org/wikipedia/en/0/01/Golden_State_Warriors_logo.svg","teamName":"勇士","homewin":"6","homelose":"12","awaywin":"2","awaylose":"2"},{"rank":"6","teamId":"22","division":"2","pts":"1100","wins":"8","losses":"2","ptsagainst":"1000","gamesplayed":"10","season_name":"2018","id":"22","name":"Rockets","ch_name":"火箭","city":"Houston","ch_city":null,"logo":null,"abbreviation":"","team_logo":"https://upload.wikimedia.org/wikipedia/en/2/28/Houston_Rockets.svg","teamName":"火箭","homewin":"5","homelose":"1","awaywin":"3","awaylose":"1"},{"rank":"6","teamId":"24","division":"2","pts":"485","wins":"1","losses":"4","ptsagainst":"530","gamesplayed":"5","season_name":"2018","id":"24","name":"Spurs","ch_name":"馬刺","city":"San Antonio","ch_city":null,"logo":null,"abbreviation":"","team_logo":"https://upload.wikimedia.org/wikipedia/en/a/a2/San_Antonio_Spurs.svg","teamName":"馬刺","homewin":"1","homelose":"1","awaywin":"14","awaylose":"3"},{"rank":"6","teamId":"23","division":"2","pts":"990","wins":"5","losses":"4","ptsagainst":"999","gamesplayed":"9","season_name":"2018","id":"23","name":"Pelicans","ch_name":"鵜鶘","city":"New Orleans","ch_city":null,"logo":null,"abbreviation":"","team_logo":"https://upload.wikimedia.org/wikipedia/en/0/0d/New_Orleans_Pelicans_logo.svg","teamName":"鵜鶘","homewin":"3","homelose":"1","awaywin":"2","awaylose":"3"}]
     */

    private String status;
    private List<ZoneTernBean> EasternAtlantic;
    private List<ZoneTernBean> EasternCentral;
    private List<ZoneTernBean> EasternSoutheast;
    private List<ZoneTernBean> WesternNorthwest;
    private List<ZoneTernBean> WesternPacific;
    private List<ZoneTernBean> WesternSouthwest;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ZoneTernBean> getEasternAtlantic() {
        return EasternAtlantic;
    }

    public void setEasternAtlantic(List<ZoneTernBean> easternAtlantic) {
        EasternAtlantic = easternAtlantic;
        processData(easternAtlantic);
    }

    public List<ZoneTernBean> getEasternCentral() {
        return EasternCentral;
    }

    public void setEasternCentral(List<ZoneTernBean> easternCentral) {
        EasternCentral = easternCentral;
        processData(easternCentral);
    }

    public List<ZoneTernBean> getEasternSoutheast() {
        return EasternSoutheast;
    }

    public void setEasternSoutheast(List<ZoneTernBean> easternSoutheast) {
        EasternSoutheast = easternSoutheast;
        processData(easternSoutheast);
    }

    public List<ZoneTernBean> getWesternNorthwest() {
        return WesternNorthwest;
    }

    public void setWesternNorthwest(List<ZoneTernBean> westernNorthwest) {
        WesternNorthwest = westernNorthwest;
        processData(westernNorthwest);
    }

    public List<ZoneTernBean> getWesternPacific() {
        return WesternPacific;
    }

    public void setWesternPacific(List<ZoneTernBean> westernPacific) {
        WesternPacific = westernPacific;
        processData(westernPacific);
    }

    public List<ZoneTernBean> getWesternSouthwest() {
        return WesternSouthwest;
    }

    public void setWesternSouthwest(List<ZoneTernBean> westernSouthwest) {
        WesternSouthwest = westernSouthwest;
        processData(westernSouthwest);
    }

    private void processData(List<ZoneTernBean> tern) {
        for (ZoneTernBean b : tern) {
            b.setWin_rate();
            b.setAway();
            b.setHome();
            b.setAll();
            b.setWinPt();
            b.setLostPt();
        }

        Collections.sort(tern, new Comparator<ZoneTernBean>() {
            @Override
            public int compare(ZoneTernBean o1, ZoneTernBean o2) {
                if (Float.parseFloat(o1.getWin_rate().substring(0, o1.getWin_rate().length() - 1))
                        < Float.parseFloat(o2.getWin_rate().substring(0, o2.getWin_rate().length() - 1)))
                    return 1;
                else
                    return -1;
            }
        });

        for (int i = 0; i < tern.size(); i++) {
            tern.get(i).setRank(String.valueOf(i + 1));
        }
    }
}
