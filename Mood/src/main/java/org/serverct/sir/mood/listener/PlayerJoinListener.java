package org.serverct.sir.mood.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.serverct.sir.mood.configuration.PlayerData;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        PlayerData.getInstance().addNewPlayer(event.getPlayer().getName());
    }
}
