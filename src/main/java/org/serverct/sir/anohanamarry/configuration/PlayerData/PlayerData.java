package org.serverct.sir.anohanamarry.configuration.PlayerData;


import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.serverct.sir.anohanamarry.configuration.LanguageData.Language;
import org.serverct.sir.anohanamarry.util.TimeUtils;

import java.util.ArrayList;
import java.util.List;

public class PlayerData {

    @Getter @Setter private String playerName;
    @Getter @Setter private SexType sex;
    @Getter @Setter private StatusType status;
    @Getter @Setter private String lover;
    @Getter @Setter private int loveLevel;
    @Getter @Setter private int lovePoint;
    @Getter @Setter private String marriedTime;
    @Getter @Setter private long marriedTimeStamp;
    @Getter @Setter private Location home;
    @Getter @Setter private List<String> queue;
    @Getter @Setter private boolean shareExp;
    @Getter @Setter private boolean shareHealth;

    private List<String> playerInfoFormat;
    private List<String> playerInfoMsg;

    public PlayerData(String playerName, SexType sex, StatusType status, String lover, int loveLevel, int lovePoint, String marriedTime, long marriedTimeStamp, Location home, List<String> queue, boolean shareExp, boolean shareHealth) {
        this.playerName = playerName;
        this.sex = sex;
        this.status = status;
        this.lover = lover;
        this.loveLevel = loveLevel;
        this.lovePoint = lovePoint;
        this.marriedTime = marriedTime;
        this.marriedTimeStamp = marriedTimeStamp;
        this.home = home;
        this.queue = queue;
        this.shareExp = shareExp;
        this.shareHealth = shareHealth;
    }

    public List<String> getInfo() {
        playerInfoFormat = Language.getInstance().getLanguageData().getStringList("Commands.PlayerInfo");
        playerInfoMsg = new ArrayList<>();
        for(String msg : playerInfoMsg) {
            playerInfoMsg.add(ChatColor.translateAlternateColorCodes(
                    '&',
                    msg
                            .replace("%player%", playerName)
                            .replace("%sex%", SexType.formatSex(sex))
                            .replace("%status%", StatusType.formatStatus(status))
                            .replace("%lover%", lover)
                            .replace("%loveLevel%", String.valueOf(loveLevel))
                            .replace("%lovePoint%", String.valueOf(lovePoint))
                            .replace("%marriedTime_String%", marriedTime)
                            .replace("%marriedTime_Description%", TimeUtils.getDescriptionTimeFromTimestamp(marriedTimeStamp))
                    )
            );
        }
        return playerInfoMsg;
    }

}
