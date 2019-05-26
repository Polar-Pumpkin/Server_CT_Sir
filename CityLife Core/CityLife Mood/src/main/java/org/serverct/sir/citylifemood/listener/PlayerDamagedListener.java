package org.serverct.sir.citylifemood.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.serverct.sir.citylifemood.enums.MoodChangeType;
import org.serverct.sir.citylifemood.configuration.ConfigManager;
import org.serverct.sir.citylifemood.configuration.PlayerDataManager;

public class PlayerDamagedListener implements Listener {

    private Player targetPlayer;
    private int stepping;

    @EventHandler
    public void onPlayerDamaged(EntityDamageByEntityEvent event) {
        if(event.getEntity() instanceof Player) {
            targetPlayer = (Player) event.getEntity();
            stepping = ConfigManager.getInstance().getData().getInt("Decrease.Damaged");
            PlayerDataManager.getInstance().addMoodValue(targetPlayer.getName(), stepping, MoodChangeType.DAMAGED, null);
        }
    }
}
