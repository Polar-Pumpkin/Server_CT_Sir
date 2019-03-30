package org.serverct.sir.soulring.runnable;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class RegenerationTask extends BukkitRunnable {

    private Player target;
    private double heal;

    public RegenerationTask(Player player, double heal) {
        this.target = player;
        this.heal = heal;
    }

    @Override
    public void run() {
        if(target.getHealth() < target.getMaxHealth()) {
            if(target.getHealth() + heal >= target.getHealth()) {
                target.setHealth(target.getHealth());
            } else {
                target.setHealth(target.getHealth() + heal);
            }
        }
    }
}
