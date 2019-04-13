package org.serverct.sir.anohanamarry.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.serverct.sir.anohanamarry.inventory.InventoryManager;
import org.serverct.sir.anohanamarry.inventory.gui.SexSelector;

public class OnPlayerClickInventory implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent evt) {
        if(InventoryManager.getInstance().getInventoryTitle("SexSelector").equalsIgnoreCase(evt.getClickedInventory().getTitle())) {
            SexSelector.getInstance().click(evt);
        } else if(InventoryManager.getInstance().getInventoryTitle("MainMenu").equalsIgnoreCase(evt.getClickedInventory().getTitle())) {

        } else if(InventoryManager.getInstance().getInventoryTitle("LoveShop").equalsIgnoreCase(evt.getClickedInventory().getTitle())) {

        }
    }
}
