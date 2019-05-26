package org.serverct.sir.citylifemood.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.serverct.sir.citylifemood.enums.MoodChangeType;
import org.serverct.sir.citylifemood.configuration.PlayerDataManager;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        PlayerDataManager.getInstance().addMoodValue(event.getEntity().getName(), 100, MoodChangeType.RESPAWN, null);
    }
}
