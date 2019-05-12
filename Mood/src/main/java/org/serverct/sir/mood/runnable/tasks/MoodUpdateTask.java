package org.serverct.sir.mood.runnable.tasks;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.serverct.sir.mood.MoodUpdateTaskType;
import org.serverct.sir.mood.configuration.Config;
import org.serverct.sir.mood.configuration.PlayerData;
import org.serverct.sir.mood.utils.BasicUtil;

import java.util.List;

public class MoodUpdateTask extends BukkitRunnable {

    @Getter private MoodUpdateTaskType taskType;
    private List<Player> onlinePlayers;

    private int stepping;
    private Player targetPlayer;

    public MoodUpdateTask(MoodUpdateTaskType type) {
        this.taskType = type;
    }
    public MoodUpdateTask(MoodUpdateTaskType type, Player player, int step) {
        this.taskType = type;
        this.targetPlayer = player;
        this.stepping = step;
    }

    @Override
    public void run() {
        stepping = Config.getInstance().getMoodDecreaseStep(taskType);

        if(taskType != MoodUpdateTaskType.CUSTOM) {
            onlinePlayers = BasicUtil.getOnlinePlayers();
            for(Player player : onlinePlayers) {
                switch (taskType) {
                    case COMMON:
                        PlayerData.getInstance().addMoodValue(player.getName(), stepping);
                        break;
                    case SUNNY:
                        if(!player.getWorld().hasStorm()) {
                            PlayerData.getInstance().addMoodValue(player.getName(), stepping);
                        }
                        break;
                    case RAINY:
                        if(!BasicUtil.isSnowy(player.getLocation())) {
                            PlayerData.getInstance().addMoodValue(player.getName(), stepping);
                        }
                        break;
                    case SNOWY:
                        if(BasicUtil.isSnowy(player.getLocation())) {
                            PlayerData.getInstance().addMoodValue(player.getName(), stepping);
                        }
                        break;
                    default:
                        break;
                }
            }
        } else {
            PlayerData.getInstance().addMoodValue(targetPlayer.getName(), stepping);
        }
    }
}
