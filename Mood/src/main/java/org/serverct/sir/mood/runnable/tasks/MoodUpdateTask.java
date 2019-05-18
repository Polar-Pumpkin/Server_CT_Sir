package org.serverct.sir.mood.runnable.tasks;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.serverct.sir.mood.MoodChangeType;
import org.serverct.sir.mood.configuration.Config;
import org.serverct.sir.mood.configuration.PlayerData;
import org.serverct.sir.mood.utils.BasicUtil;

import java.util.List;

public class MoodUpdateTask extends BukkitRunnable {

    @Getter private MoodChangeType taskType;
    private List<Player> onlinePlayers;

    private int stepping;
    private Player targetPlayer;

    public MoodUpdateTask(MoodChangeType type) {
        this.taskType = type;
    }
    public MoodUpdateTask(MoodChangeType type, Player player, int step) {
        this.taskType = type;
        this.targetPlayer = player;
        this.stepping = step;
    }

    @Override
    public void run() {
        stepping = Config.getInstance().getMoodDecreaseStep(taskType);

        if(stepping != 0) {
            if(taskType != MoodChangeType.CUSTOM) {
                onlinePlayers = BasicUtil.getOnlinePlayers();

                if(!onlinePlayers.isEmpty()) {
                    for(Player player : onlinePlayers) {
                        switch (taskType) {
                            case COMMON:
                                PlayerData.getInstance().addMoodValue(player.getName(), stepping, taskType, null);
                                break;
                            case SUNNY:
                                if(!player.getWorld().hasStorm()) {
                                    PlayerData.getInstance().addMoodValue(player.getName(), stepping, taskType, null);
                                }
                                break;
                            case RAINY:
                                if(player.getWorld().hasStorm()) {
                                    if(!BasicUtil.isSnowy(player.getLocation())) {
                                        PlayerData.getInstance().addMoodValue(player.getName(), stepping, taskType, null);
                                    }
                                }
                                break;
                            case SNOWY:
                                if(player.getWorld().hasStorm()) {
                                    if(BasicUtil.isSnowy(player.getLocation())) {
                                        PlayerData.getInstance().addMoodValue(player.getName(), stepping,taskType, null);
                                    }
                                }
                                break;
                            default:
                                break;
                        }
                    }
                }
            } else {
                PlayerData.getInstance().addMoodValue(targetPlayer.getName(), stepping, MoodChangeType.CUSTOM, null);
            }
        }
    }
}
