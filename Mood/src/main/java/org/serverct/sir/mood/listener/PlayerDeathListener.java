package org.serverct.sir.mood.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.serverct.sir.mood.configuration.PlayerData;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        PlayerData.getInstance().addMoodValue(event.getEntity().getName(), 100);
    }
}
