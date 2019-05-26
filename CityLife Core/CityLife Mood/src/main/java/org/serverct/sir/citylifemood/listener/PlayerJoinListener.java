package org.serverct.sir.citylifemood.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.serverct.sir.citylifemood.configuration.PlayerDataManager;

public class PlayerJoinListener implements Listener {

    private String targetPlayer;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        targetPlayer = event.getPlayer().getName();

        if(PlayerDataManager.getInstance().getData().getKeys(false).contains(targetPlayer)) {
            PlayerDataManager.getInstance().addNewPlayer(targetPlayer);
        }
    }
}
