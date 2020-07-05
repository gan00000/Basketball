package com.jiec.basketball.ui.game.detail;

public class PlayerMatchInfo {

    private String topInfo;
    private String BottomInfo;

    public PlayerMatchInfo(String topInfo, String bottomInfo) {
        this.topInfo = topInfo;
        BottomInfo = bottomInfo;
    }

    public String getTopInfo() {
        return topInfo;
    }

    public void setTopInfo(String topInfo) {
        this.topInfo = topInfo;
    }

    public String getBottomInfo() {
        return BottomInfo;
    }

    public void setBottomInfo(String bottomInfo) {
        BottomInfo = bottomInfo;
    }
}
