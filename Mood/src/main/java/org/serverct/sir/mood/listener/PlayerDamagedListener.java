package org.serverct.sir.mood.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.serverct.sir.mood.MoodChangeType;
import org.serverct.sir.mood.configuration.Config;
import org.serverct.sir.mood.configuration.PlayerData;

public class PlayerDamagedListener implements Listener {

    private Player targetPlayer;
    private int stepping;

    @EventHandler
    public void onPlayerDamaged(EntityDamageByEntityEvent event) {
        if(event.getEntity() instanceof Player) {
            targetPlayer = (Player) event.getEntity();
            stepping = Config.getInstance().getData().getInt("Decrease.Damaged");
            PlayerData.getInstance().addMoodValue(targetPlayer.getName(), stepping, MoodChangeType.DAMAGED, null);
        }
    }
}
