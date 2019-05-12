package org.serverct.sir.mood.runnable.tasks;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.serverct.sir.mood.Punishment;
import org.serverct.sir.mood.configuration.Config;
import org.serverct.sir.mood.configuration.PlayerData;
import org.serverct.sir.mood.runnable.PunishTaskManager;
import org.serverct.sir.mood.utils.BasicUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckTask extends BukkitRunnable {

    private List<Player> onlinePlayers;

    private Map<Integer, Punishment> cachePunishmentMap = new HashMap<>();

    private int targetMoodValue;

    @Override
    public void run() {
        onlinePlayers = BasicUtil.getOnlinePlayers();
        for(Player player : onlinePlayers) {
            for(int limit : Config.getInstance().getPunishmentMap().keySet()) {
                cachePunishmentMap = Config.getInstance().getPunishmentMap().get(limit);
                targetMoodValue = PlayerData.getInstance().getMoodValue(player.getName());

                if(targetMoodValue == -1) {
                    PlayerData.getInstance().addNewPlayer(player.getName());
                } else if(targetMoodValue < limit) {
                    for(Punishment punishment : cachePunishmentMap.values()) {
                        PunishTaskManager.getInstance().startPunish(player, punishment);
                    }
                } else {
                    for(Punishment punishment : cachePunishmentMap.values()) {
                        PunishTaskManager.getInstance().cancelPunish(player, punishment);
                    }
                }
            }
        }
    }
}
