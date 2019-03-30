package org.serverct.sir.soulring.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class OnPlayerHeld implements Listener {

    private Map<Player, Integer> tasks = new HashMap<>();
    private Player target;
    private ItemStack targetItem;

    @EventHandler
    public void onHeld(PlayerItemHeldEvent evt) {
        target = evt.getPlayer();
        targetItem = target.getItemInHand();

        if(targetItem != null || targetItem.getType() != Material.AIR) {

        }
    }
}
