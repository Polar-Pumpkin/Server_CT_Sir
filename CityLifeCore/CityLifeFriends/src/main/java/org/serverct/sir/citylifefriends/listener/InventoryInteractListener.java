package org.serverct.sir.citylifefriends.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.serverct.sir.citylifecore.data.InventoryClick;
import org.serverct.sir.citylifecore.data.InventoryGui;
import org.serverct.sir.citylifecore.data.InventoryItem;
import org.serverct.sir.citylifecore.manager.ChatRequestManager;
import org.serverct.sir.citylifecore.manager.InventoryManager;
import org.serverct.sir.citylifecore.utils.InventoryUtil;
import org.serverct.sir.citylifefriends.CityLifeFriends;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryInteractListener implements Listener {

    private Inventory clickedInventory;
    private InventoryItem clickedItem;
    private Map<String, String> placeholder;
    private List<InventoryClick> clickList;

    private Player user;

    private InventoryManager inventoryManager = CityLifeFriends.getInstance().getCoreApi().getInventoryAPI();
    private InventoryUtil inventoryUtil = CityLifeFriends.getInstance().getCoreApi().getInventoryUtil();

    private ChatRequestManager chatRequestManager = CityLifeFriends.getInstance().getCoreApi().getChatRequestAPI();

    private InventoryGui friends = inventoryManager.getLoadedInventory().get("FRINEDS_Friends");

    @EventHandler
    public void onPlayerClick(InventoryClickEvent event) {

        clickedInventory = event.getClickedInventory();
        user = (Player) event.getWhoClicked();

        if(clickedInventory.getName().equals(friends.getInventory().getName())) {
            clickedItem = inventoryUtil.getInventoryItem(event.getCurrentItem(), friends.getItems());
            placeholder = new HashMap<>();

            placeholder.put("friend", clickedItem.getId());

            if(clickedItem != null) {
                clickList = clickedItem.getClicks();
                // TODO Action的具体实现

                for(InventoryClick click : clickList) {
                    if(click.getClickType().getClickType() == event.getClick()) {
                        click.start();
                        break;
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
