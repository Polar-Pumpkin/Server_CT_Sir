package org.serverct.sir.anohanamarry.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.serverct.sir.anohanamarry.configuration.ItemData;
import org.serverct.sir.anohanamarry.configuration.PlayerData.PlayerData;
import org.serverct.sir.anohanamarry.configuration.PlayerData.PlayerDataManager;

import java.util.List;

public class OnPlayerInteract implements Listener {

    private Player player;
    private ItemStack targetItem;

    @EventHandler
    public void onInteract(PlayerInteractEvent evt) {
        player = evt.getPlayer();
        targetItem = player.getInventory().getItemInMainHand();

        if(player.isSneaking()) {
            if(ItemData.getInstance().isItem(targetItem)) {
                if(ItemData.getInstance().getItemKey(targetItem).equalsIgnoreCase("Certificate")) {
                    PlayerDataManager.getInstance().divorce(player);
                }
            }
        }
    }
}
