package org.serverct.sir.anohanamarry.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.serverct.sir.anohanamarry.configuration.PlayerData;

public class OnPlayerQuit implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent evt) {
        if(!PlayerData.getPlayerDataManager().hasDataFile(evt.getPlayer().getName())) {
            PlayerData.getPlayerDataManager().addNewPlayer(evt.getPlayer().getName());
        } else {
            if(PlayerData.getPlayerDataManager().isLoverOnline(evt.getPlayer().getName())) {
                PlayerData.getPlayerDataManager().sendLoverLoginStatusMsg(evt.getPlayer().getName(), false);
            }
        }
    }

}
