package org.serverct.sir.anohanamarry.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.serverct.sir.anohanamarry.configuration.PlayerData;

public class OnPlayerQuit implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent evt) {
        if(!PlayerData.getInstance().hasDataFile(evt.getPlayer().getName())) {
            PlayerData.getInstance().addNewPlayer(evt.getPlayer().getName());
        } else {
            if(PlayerData.getInstance().isLoverOnline(evt.getPlayer().getName())) {
                PlayerData.getInstance().sendLoverLoginStatusMsg(evt.getPlayer().getName(), false);
            }
        }
    }

}
