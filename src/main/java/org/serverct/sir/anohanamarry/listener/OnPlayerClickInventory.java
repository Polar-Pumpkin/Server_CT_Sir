package org.serverct.sir.anohanamarry.listener;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.serverct.sir.anohanamarry.inventory.InventoryManager;
import org.serverct.sir.anohanamarry.inventory.gui.SexSelector;

public class OnPlayerClickInventory implements Listener {

    private String targetInventoryTitle;

    @EventHandler
    public void onClick(InventoryClickEvent evt) {
        targetInventoryTitle = ChatColor.stripColor(evt.getClickedInventory().getTitle());

        if(targetInventoryTitle != null) {
            if(InventoryManager.getInstance().getInventoryTitle("SexSelector").equalsIgnoreCase(targetInventoryTitle)) {
                SexSelector.getInstance().click(evt);
            } else if(InventoryManager.getInstance().getInventoryTitle("MainMenu").equalsIgnoreCase(targetInventoryTitle)) {

            } else if(InventoryManager.getInstance().getInventoryTitle("LoveShop").equalsIgnoreCase(targetInventoryTitle)) {

            }
        }

    }
}
