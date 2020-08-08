package com.jiec.basketball.entity;

import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.jiec.basketball.network.base.CommResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by wangchuangjie on 2018/5/20.
 */

@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class GameDataInfo extends CommResponse {
    private List<MatchSummary> match_summary;
    private List<Matches> matches;

    private List<GamePlayerData> matchDetails;

    private HashMap<String, List<GamePlayerData>> matchDetailsMaps = new HashMap<>();

    public List<MatchSummary> getMatch_summary() {
        return match_summary;
    }

    public void setMatch_summary(List<MatchSummary> match_summary) {
        this.match_summary = match_summary;
    }

    public List<Matches> getMatches() {
        return matches;
    }

    public void setMatches(List<Matches> matches) {
        this.matches = matches;
    }

    public List<GamePlayerData> getMatchDetails() {
        return matchDetails;
    }

    public HashMap<String, List<GamePlayerData>> getMatchDetailsMaps() {
        return matchDetailsMaps;
    }

    public void setMatchDetailsMaps(HashMap<String, List<GamePlayerData>> matchDetailsMaps) {
        this.matchDetailsMaps = matchDetailsMaps;
    }

    public void setMatchDetails(List<GamePlayerData> matchDetails) {
        this.matchDetails = matchDetails;

        List<GamePlayerData> matchDetails1 = new ArrayList<>();
        List<GamePlayerData> matchDetails2 = new ArrayList<>();

        if (matchDetails == null || matchDetails.size() == 0) return;

        String match1Id = matchDetails.get(0).getTeamId();

        for (GamePlayerData info : matchDetails) {  //划分主队 客队
            if (info.getTeamId().equals(match1Id)) {
                matchDetails1.add(info);
            } else {
                matchDetails2.add(info);
            }
        }

        matchDetailsMaps.put(match1Id, matchDetails1);
        if (!matchDetails2.isEmpty()) {
            matchDetailsMaps.put(matchDetails2.get(0).getTeamId(), matchDetails2);
        }

    }
}
