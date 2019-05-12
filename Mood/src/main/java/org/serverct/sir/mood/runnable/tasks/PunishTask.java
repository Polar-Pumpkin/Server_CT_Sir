package org.serverct.sir.mood.runnable.tasks;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.serverct.sir.mood.Punishment;

public class PunishTask extends BukkitRunnable {

    @Getter private Player targetPlayer;
    @Getter private Punishment targetPunishment;

    public PunishTask(Player player, Punishment punishment) {
        this.targetPlayer = player;
        this.targetPunishment = punishment;
    }

    @Override
    public void run() {
        switch(targetPunishment.getPunishmentType()) {
            case POTION:
                targetPlayer.addPotionEffect(targetPunishment.getPotion(), true);
                break;
            case COMMAND:
                targetPlayer.performCommand(targetPunishment.getCommand());
                break;
            default:
                break;
        }
    }
}
