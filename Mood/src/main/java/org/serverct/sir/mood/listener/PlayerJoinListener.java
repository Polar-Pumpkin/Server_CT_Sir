package org.serverct.sir.mood.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.serverct.sir.mood.configuration.PlayerData;

public class PlayerJoinListener implements Listener {

    private String targetPlayer;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        targetPlayer = event.getPlayer().getName();

        if(PlayerData.getInstance().getData().getKeys(false).contains(targetPlayer)) {
            PlayerData.getInstance().addNewPlayer(targetPlayer);
        }
    }
}
