package org.serverct.sir.citylifefriends.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.serverct.sir.citylifecore.data.InventoryGui;
import org.serverct.sir.citylifecore.data.InventoryItem;
import org.serverct.sir.citylifecore.manager.InventoryManager;
import org.serverct.sir.citylifecore.utils.InventoryUtil;
import org.serverct.sir.citylifefriends.CityLifeFriends;

import java.util.HashMap;
import java.util.Map;

public class InventoryInteractListener implements Listener {

    private Inventory clickedInventory;
    private InventoryItem clickedItem;
    private Map<String, String> placeholder;

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
            placeholder = new HashMap<>();

            placeholder.put("friend", clickedItem.getId());

            if(clickedItem != null) {
                // TODO Action的具体实现
                if(clickedItem.hasChatRequestAction()) {

                } else {

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
