package org.serverct.sir.citylifemood.runnable.tasks;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.serverct.sir.citylifemood.enums.MoodChangeType;
import org.serverct.sir.citylifemood.configuration.ConfigManager;
import org.serverct.sir.citylifemood.configuration.PlayerDataManager;
import org.serverct.sir.citylifemood.utils.CommonUtil;

import java.util.List;

public class MoodUpdateTask extends BukkitRunnable {

    @Getter private MoodChangeType taskType;
    private List<Player> onlinePlayers;

    private int stepping;
    private Player targetPlayer;
    private String reason;

    public MoodUpdateTask(MoodChangeType type) {
        this.taskType = type;
    }
    public MoodUpdateTask(MoodChangeType type, Player player, int step) {
        this.taskType = type;
        this.targetPlayer = player;
        this.stepping = step;
    }
    public MoodUpdateTask(MoodChangeType type, Player player, int step, String reason) {
        this.taskType = type;
        this.targetPlayer = player;
        this.stepping = step;
        this.reason = reason;
    }

    @Override
    public void run() {
        stepping = ConfigManager.getInstance().getMoodDecreaseStep(taskType);

        if(stepping != 0) {
            if(taskType != MoodChangeType.CUSTOM) {
                onlinePlayers = CommonUtil.getOnlinePlayers();

                if(!onlinePlayers.isEmpty()) {
                    for(Player player : onlinePlayers) {
                        switch (taskType) {
                            case COMMON:
                                PlayerDataManager.getInstance().addMoodValue(player.getName(), stepping, taskType, null);
                                break;
                            case SUNNY:
                                if(!player.getWorld().hasStorm()) {
                                    PlayerDataManager.getInstance().addMoodValue(player.getName(), stepping, taskType, null);
                                }
                                break;
                            case RAINY:
                                if(player.getWorld().hasStorm()) {
                                    if(!CommonUtil.isSnowy(player.getLocation())) {
                                        PlayerDataManager.getInstance().addMoodValue(player.getName(), stepping, taskType, null);
                                    }
                                }
                                break;
                            case SNOWY:
                                if(player.getWorld().hasStorm()) {
                                    if(CommonUtil.isSnowy(player.getLocation())) {
                                        PlayerDataManager.getInstance().addMoodValue(player.getName(), stepping,taskType, null);
                                    }
                                }
                                break;
                            case CUSTOM:
                                PlayerDataManager.getInstance().addMoodValue(player.getName(), stepping, reason);
                            default:
                                break;
                        }
                    }
                }
            } else {
                PlayerDataManager.getInstance().addMoodValue(targetPlayer.getName(), stepping, MoodChangeType.CUSTOM, null);
            }
        }
    }
}
