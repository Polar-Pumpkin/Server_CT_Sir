package org.serverct.sir.mood.runnable.tasks;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.serverct.sir.mood.MessageType;
import org.serverct.sir.mood.Punishment;
import org.serverct.sir.mood.configuration.Config;
import org.serverct.sir.mood.configuration.Language;
import org.serverct.sir.mood.configuration.PlayerData;
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
            if(targetMoodValue == -1) {
                PlayerData.getInstance().addNewPlayer(player.getName());
                continue;
            }
            for(int limit : Config.getInstance().getPunishmentMap().keySet()) {
                cachePunishmentMap = Config.getInstance().getPunishmentMap().get(limit);
                targetMoodValue = PlayerData.getInstance().getMoodValue(player.getName());

                if(targetMoodValue < limit) {
                    for(Punishment punishment : cachePunishmentMap.values()) {
                        punish(player, punishment);
                    }
                }
            }
        }
    }

    private void punish(Player player, Punishment punishment) {
        switch (punishment.getPunishmentType()) {
            case POTION:
                player.addPotionEffect(punishment.getPotion(), true);
                break;
            case COMMAND:
                player.performCommand(punishment.getCommand());
                break;
            case MESSAGE:
                player.sendMessage(Language.getInstance().buildMessage(MessageType.INFO, punishment.getMessage()));
                break;
            default:
                break;
        }
    }
}
