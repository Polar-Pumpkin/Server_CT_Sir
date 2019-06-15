package org.serverct.sir.citylifefriends.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.serverct.sir.citylifecore.data.Action;
import org.serverct.sir.citylifecore.data.InventoryGui;
import org.serverct.sir.citylifecore.data.InventoryItem;
import org.serverct.sir.citylifecore.manager.InventoryManager;
import org.serverct.sir.citylifecore.utils.InventoryUtil;
import org.serverct.sir.citylifefriends.CityLifeFriends;

public class InventoryInteractListener implements Listener {

    private Inventory clickedInventory;
    private InventoryItem clickedItem;

    private Player user;

    private InventoryManager inventoryManager = CityLifeFriends.getINSTANCE().getCoreApi().getInventoryAPI();
    private InventoryUtil inventoryUtil = CityLifeFriends.getINSTANCE().getCoreApi().getInventoryUtil();

    private InventoryGui friends = inventoryManager.getLoadedInventory().get("FRINEDS_Friends");

    @EventHandler
    public void onPlayerClick(InventoryClickEvent event) {
        clickedInventory = event.getClickedInventory();
        user = (Player) event.getWhoClicked();

        if(clickedInventory.getName().equals(inventoryManager.getNoColorName("FRIENDS_Friends"))) {
            clickedItem = inventoryUtil.getInventoryItem(event.getCurrentItem(), friends.getItems());

            if(clickedItem != null) {
                // TODO Action的具体实现
                for(Action action : clickedItem.getActions()) {
                    if(action.check(event.getClick())) {
                        switch (action.getActionType()) {
                            case MESSAGE:
                            case FORMATEDMSG:

                                break;
                            default:
                                break;
                        }
                    }
                }

                if(clickedItem.isKeepOpen()) {
                    event.setCancelled(true);
                } else {
                    inventoryUtil.closeInventory(event.getWhoClicked(), friends);
                }
            }
        }
    }
}
