package org.serverct.sir.soulring.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.serverct.sir.soulring.Attributes;
import org.serverct.sir.soulring.SoulRing;
import org.serverct.sir.soulring.configuration.AttributeManager;
import org.serverct.sir.soulring.runnable.RegenerationTask;

import java.util.HashMap;
import java.util.Map;

public class OnPlayerHeld implements Listener {

    private Map<Player, BukkitRunnable> tasks = new HashMap<>();

    private Player target;
    private ItemStack targetItem;
    private Map<Attributes, Double> attributesMap;
    private BukkitRunnable task;

    @EventHandler
    public void onHeld(PlayerItemHeldEvent evt) {
        target = evt.getPlayer();
        targetItem = target.getItemInHand();
        attributesMap = AttributeManager.getInstance().getAttributesFromItem(targetItem);

        if(targetItem != null) {
            if(attributesMap.keySet().contains(Attributes.REGENERATION)) {
                task = new RegenerationTask(target, attributesMap.get(Attributes.REGENERATION));
                tasks.put(target, task);
                task.runTaskTimer(SoulRing.getInstance(), 20, 20);
            } else {
                if(tasks.keySet().contains(target)) {
                    tasks.get(target).cancel();
                    tasks.remove(target);
                }
            }
        }
    }
}
