package org.serverct.sir.soulring.runnable;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class RegenerationTask extends BukkitRunnable {

    private Player target;
    private int heal;

    public RegenerationTask(Player player, int heal) {
        this.target = player;
        this.heal = heal;
    }

    @Override
    public void run() {
        if(target.getHealth() < target.getMaxHealth()) {
            target.setHealth(target.getHealth() + (double) heal);
        }
    }
}
