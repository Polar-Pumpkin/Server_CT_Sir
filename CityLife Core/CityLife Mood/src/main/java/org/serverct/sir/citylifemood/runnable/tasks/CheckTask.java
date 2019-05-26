package org.serverct.sir.citylifemood.runnable.tasks;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.serverct.sir.citylifemood.enums.MessageType;
import org.serverct.sir.citylifemood.data.Punishment;
import org.serverct.sir.citylifemood.configuration.ConfigManager;
import org.serverct.sir.citylifemood.configuration.LocaleManager;
import org.serverct.sir.citylifemood.configuration.PlayerDataManager;
import org.serverct.sir.citylifemood.utils.CommonUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckTask extends BukkitRunnable {

    private List<Player> onlinePlayers;

    private Map<Integer, Punishment> cachePunishmentMap = new HashMap<>();

    private int targetMoodValue;

    @Override
    public void run() {
        onlinePlayers = CommonUtil.getOnlinePlayers();
        for(Player player : onlinePlayers) {
            if(targetMoodValue == -1) {
                PlayerDataManager.getInstance().addNewPlayer(player.getName());
                continue;
            }
            for(int limit : ConfigManager.getInstance().getPunishmentMap().keySet()) {
                cachePunishmentMap = ConfigManager.getInstance().getPunishmentMap().get(limit);
                targetMoodValue = PlayerDataManager.getInstance().getMoodValue(player.getName());

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
                player.sendMessage(LocaleManager.getInstance().buildMessage(MessageType.INFO, punishment.getMessage()));
                break;
            default:
                break;
        }
    }
}
