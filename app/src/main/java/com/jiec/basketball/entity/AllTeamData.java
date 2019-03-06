package com.jiec.basketball.entity;

import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.jiec.basketball.network.base.CommResponse;

import java.util.List;

/**
 * Created by wangchuangjie on 2018/5/13.
 */

@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class AllTeamData extends CommResponse {

    /**
     * status : ok
     * teams_rank : [{"teamName":"勇士","team_logo":"https://upload.wikimedia.org/wikipedia/en/0/01/Golden_State_Warriors_logo.svg","pts":"1103","reb":"485","ast":"289","stl":"80","blk":"54","fouls":"188","fgmade":"414","fgatt":"883","fg3ptmade":"98","fg3ptatt":"298","ftmade":"177","ftatt":"217","matches":"10","type_avg":"110.3000"},{"teamName":"鵜鶘","team_logo":"https://upload.wikimedia.org/wikipedia/en/0/0d/New_Orleans_Pelicans_logo.svg","pts":"990","reb":"411","ast":"242","stl":"73","blk":"47","fouls":"174","fgmade":"386","fgatt":"809","fg3ptmade":"89","fg3ptatt":"243","ftmade":"129","ftatt":"165","matches":"9","type_avg":"110.0000"},{"teamName":"火箭","team_logo":"https://upload.wikimedia.org/wikipedia/en/2/28/Houston_Rockets.svg","pts":"1095","reb":"438","ast":"206","stl":"84","blk":"67","fouls":"188","fgmade":"390","fgatt":"874","fg3ptmade":"141","fg3ptatt":"399","ftmade":"174","ftatt":"219","matches":"10","type_avg":"109.5000"},{"teamName":"76ers","team_logo":"https://upload.wikimedia.org/wikipedia/en/thumb/0/0e/Philadelphia_76ers_logo.svg/200px-Philadelphia_76ers_logo.svg.png","pts":"1088","reb":"495","ast":"258","stl":"78","blk":"43","fouls":"228","fgmade":"387","fgatt":"885","fg3ptmade":"99","fg3ptatt":"293","ftmade":"215","ftatt":"283","matches":"10","type_avg":"108.8000"},{"teamName":"暴龍","team_logo":"https://upload.wikimedia.org/wikipedia/en/thumb/3/36/Toronto_Raptors_logo.svg/200px-Toronto_Raptors_logo.svg.png","pts":"1073","reb":"401","ast":"218","stl":"57","blk":"61","fouls":"226","fgmade":"389","fgatt":"823","fg3ptmade":"104","fg3ptatt":"267","ftmade":"191","ftatt":"239","matches":"10","type_avg":"107.3000"},{"teamName":"巫師","team_logo":"https://upload.wikimedia.org/wikipedia/en/0/02/Washington_Wizards_logo.svg","pts":"643","reb":"240","ast":"140","stl":"47","blk":"31","fouls":"126","fgmade":"238","fgatt":"511","fg3ptmade":"48","fg3ptatt":"131","ftmade":"119","ftatt":"140","matches":"6","type_avg":"107.1667"},{"teamName":"拓荒者","team_logo":"https://upload.wikimedia.org/wikipedia/en/thumb/2/21/Portland_Trail_Blazers_logo.svg/260px-Portland_Trail_Blazers_logo.svg.png","pts":"422","reb":"174","ast":"76","stl":"33","blk":"15","fouls":"82","fgmade":"165","fgatt":"364","fg3ptmade":"43","fg3ptatt":"129","ftmade":"49","ftatt":"63","matches":"4","type_avg":"105.5000"},{"teamName":"賽爾提克","team_logo":"https://upload.wikimedia.org/wikipedia/en/8/8f/Boston_Celtics.svg","pts":"1249","reb":"510","ast":"254","stl":"82","blk":"57","fouls":"245","fgmade":"447","fgatt":"1003","fg3ptmade":"131","fg3ptatt":"364","ftmade":"224","ftatt":"294","matches":"12","type_avg":"104.0833"},{"teamName":"騎士","team_logo":"https://upload.wikimedia.org/wikipedia/en/4/4b/Cleveland_Cavaliers_logo.svg","pts":"1138","reb":"422","ast":"217","stl":"72","blk":"42","fouls":"219","fgmade":"412","fgatt":"883","fg3ptmade":"119","fg3ptatt":"339","ftmade":"195","ftatt":"256","matches":"11","type_avg":"103.4545"},{"teamName":"熱火","team_logo":"https://upload.wikimedia.org/wikipedia/en/f/fb/Miami_Heat_logo.svg","pts":"517","reb":"206","ast":"119","stl":"44","blk":"32","fouls":"146","fgmade":"183","fgatt":"415","fg3ptmade":"54","fg3ptatt":"138","ftmade":"97","ftatt":"143","matches":"5","type_avg":"103.4000"},{"teamName":"爵士","team_logo":"https://upload.wikimedia.org/wikipedia/en/0/04/Utah_Jazz_logo_%282016%29.svg","pts":"1126","reb":"495","ast":"223","stl":"83","blk":"55","fouls":"226","fgmade":"415","fgatt":"911","fg3ptmade":"118","fg3ptatt":"329","ftmade":"178","ftatt":"259","matches":"11","type_avg":"102.3636"},{"teamName":"雄鹿","team_logo":"https://upload.wikimedia.org/wikipedia/en/4/4a/Milwaukee_Bucks_logo.svg","pts":"713","reb":"270","ast":"154","stl":"46","blk":"48","fouls":"160","fgmade":"274","fgatt":"548","fg3ptmade":"67","fg3ptatt":"180","ftmade":"98","ftatt":"139","matches":"7","type_avg":"101.8571"},{"teamName":"灰狼","team_logo":"https://upload.wikimedia.org/wikipedia/en/thumb/c/c2/Minnesota_Timberwolves_logo.svg/200px-Minnesota_Timberwolves_logo.svg.png","pts":"508","reb":"226","ast":"112","stl":"25","blk":"16","fouls":"91","fgmade":"190","fgatt":"413","fg3ptmade":"45","fg3ptatt":"109","ftmade":"83","ftatt":"114","matches":"5","type_avg":"101.6000"},{"teamName":"雷霆","team_logo":"https://upload.wikimedia.org/wikipedia/en/5/5d/Oklahoma_City_Thunder.svg","pts":"607","reb":"264","ast":"98","stl":"53","blk":"25","fouls":"130","fgmade":"223","fgatt":"519","fg3ptmade":"63","fg3ptatt":"173","ftmade":"98","ftatt":"127","matches":"6","type_avg":"101.1667"},{"teamName":"遛马","team_logo":"https://upload.wikimedia.org/wikipedia/en/1/1b/Indiana_Pacers.svg","pts":"704","reb":"280","ast":"148","stl":"56","blk":"19","fouls":"153","fgmade":"270","fgatt":"561","fg3ptmade":"68","fg3ptatt":"190","ftmade":"96","ftatt":"135","matches":"7","type_avg":"100.5714"},{"teamName":"馬刺","team_logo":"https://upload.wikimedia.org/wikipedia/en/a/a2/San_Antonio_Spurs.svg","pts":"484","reb":"188","ast":"95","stl":"38","blk":"15","fouls":"102","fgmade":"173","fgatt":"419","fg3ptmade":"42","fg3ptatt":"141","ftmade":"96","ftatt":"115","matches":"5","type_avg":"96.8000"}]
     */

    private List<TeamsRankBean> teams_rank;

    public List<TeamsRankBean> getTeams_rank() {
        return teams_rank;
    }

    public void setTeams_rank(List<TeamsRankBean> teams_rank) {
        this.teams_rank = teams_rank;
    }

    @JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
    public static class TeamsRankBean {
        /**
         * teamName : 勇士
         * team_logo : https://upload.wikimedia.org/wikipedia/en/0/01/Golden_State_Warriors_logo.svg
         * pts : 1103
         * reb : 485
         * ast : 289
         * stl : 80
         * blk : 54
         * fouls : 188
         * fgmade : 414
         * fgatt : 883
         * fg3ptmade : 98
         * fg3ptatt : 298
         * ftmade : 177
         * ftatt : 217
         * matches : 10
         * type_avg : 110.3000
         */

        private String teamName;
        private String team_logo;
        private String pts;
        private String reb;
        private String ast;
        private String stl;
        private String blk;
        private String fouls;
        private String fgmade;
        private String fgatt;
        private String fg3ptmade;
        private String fg3ptatt;
        private String ftmade;
        private String ftatt;
        private String matches;
        private String type_avg;

        public String getTeamName() {
            return teamName;
        }

        public void setTeamName(String teamName) {
            this.teamName = teamName;
        }

        public String getTeam_logo() {
            return team_logo;
        }

        public void setTeam_logo(String team_logo) {
            this.team_logo = team_logo;
        }

        public String getPts() {
            return pts;
        }

        public void setPts(String pts) {
            this.pts = pts;
        }

        public String getReb() {
            return reb;
        }

        public void setReb(String reb) {
            this.reb = reb;
        }

        public String getAst() {
            return ast;
        }

        public void setAst(String ast) {
            this.ast = ast;
        }

        public String getStl() {
            return stl;
        }

        public void setStl(String stl) {
            this.stl = stl;
        }

        public String getBlk() {
            return blk;
        }

        public void setBlk(String blk) {
            this.blk = blk;
        }

        public String getFouls() {
            return fouls;
        }

        public void setFouls(String fouls) {
            this.fouls = fouls;
        }

        public String getFgmade() {
            return fgmade;
        }

        public void setFgmade(String fgmade) {
            this.fgmade = fgmade;
        }

        public String getFgatt() {
            return fgatt;
        }

        public void setFgatt(String fgatt) {
            this.fgatt = fgatt;
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

        public String getFtmade() {
            return ftmade;
        }

        public void setFtmade(String ftmade) {
            this.ftmade = ftmade;
        }

        public String getFtatt() {
            return ftatt;
        }

        public void setFtatt(String ftatt) {
            this.ftatt = ftatt;
        }

        public String getMatches() {
            return matches;
        }

        public void setMatches(String matches) {
            this.matches = matches;
        }

        public String getType_avg() {
            return type_avg;
        }

        public void setType_avg(String type_avg) {
            this.type_avg = type_avg;
        }
    }
}
