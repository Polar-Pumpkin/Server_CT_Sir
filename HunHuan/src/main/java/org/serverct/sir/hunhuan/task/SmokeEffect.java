package org.serverct.sir.hunhuan.task;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SmokeEffect extends BukkitRunnable {

    private Player user;
    private double degree = 0;
    private int r = 5;
    private double h = 0;
    private Effect effect = Effect.SMOKE;

    public SmokeEffect(Player player) {
        this.user = player;
    }

    @Override
    public void run() {
        if(!user.isOnline()) {
            cancel();
        }
        double radians = Math.toRadians(degree);
        Location playerLocation = user.getLocation();
        Location playEffectLocation1 = playerLocation.clone().add(r * Math.cos(radians), h, r * Math.sin(radians));
        Location playEffectLocation2 = playerLocation.clone().add(-r * Math.cos(radians), h, r * Math.sin(radians));
        Location playEffectLocation3 = playerLocation.clone().add(r * Math.cos(radians), h, -r * Math.sin(radians));
        Location playEffectLocation4 = playerLocation.clone().add(-r * Math.cos(radians), h, -r * Math.sin(radians));

        playerLocation.getWorld().playEffect(playEffectLocation1, effect, 1, 100);
        playerLocation.getWorld().playEffect(playEffectLocation2, effect, 1, 100);
        playerLocation.getWorld().playEffect(playEffectLocation3, effect, 1, 100);
        playerLocation.getWorld().playEffect(playEffectLocation4, effect, 1, 100);

        if (degree >= 360) {
            degree = 0;
        } else {
            degree += 10;
        }

        if(r <= 1) {
            r = 5;
        } else {
            r--;
        }

        if(h >= 0.5) {
            h = 0;
        } else {
            h += 0.1;
        }
    }
}
