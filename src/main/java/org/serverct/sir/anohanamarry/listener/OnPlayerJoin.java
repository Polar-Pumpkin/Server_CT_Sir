package org.serverct.sir.anohanamarry.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.serverct.sir.anohanamarry.configuration.PlayerData;
import org.serverct.sir.anohanamarry.inventory.InventoryManager;
import org.serverct.sir.anohanamarry.inventory.gui.SexSelector;

public class OnPlayerJoin implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent evt) {
        if(!PlayerData.getInstance().hasDataFile(evt.getPlayer().getName())) {
            PlayerData.getInstance().addNewPlayer(evt.getPlayer().getName());
        } else {
            if(PlayerData.getInstance().isLoverOnline(evt.getPlayer().getName())) {
                PlayerData.getInstance().sendLoverLoginStatusMsg(evt.getPlayer().getName(), true);
            }

            PlayerData.getInstance().clearQueue(evt.getPlayer().getName());

            if(PlayerData.getInstance().getSex(evt.getPlayer().getName()).equalsIgnoreCase("Unknown")) {
                InventoryManager.getInstance().openInventory(evt.getPlayer(), SexSelector.getInstance().buildSexSelector());
            }
        }
    }

}
