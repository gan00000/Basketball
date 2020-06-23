package com.jiec.basketball.entity.response;

import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.jiec.basketball.network.base.CommResponse;

/**
 * Created by Administrator on 2017/1/8.
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class GameLikeResponse extends CommResponse {

    GameLike team_like;

    public GameLike getTeam_like() {
        return team_like;
    }

    public void setTeam_like(GameLike team_like) {
        this.team_like = team_like;
    }

    @JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
    public static class GameLike {
        private String homeTeamLike = "0";

        private String awayTeamLike = "0";


        public String getHomeTeamLike() {
            return homeTeamLike;
        }

        public void setHomeTeamLike(String homeTeamLike) {
            this.homeTeamLike = homeTeamLike;
        }

        public String getAwayTeamLike() {
            return awayTeamLike;
        }

        public void setAwayTeamLike(String awayTeamLike) {
            this.awayTeamLike = awayTeamLike;
        }
    }
}

