package org.serverct.sir.soulring.listener;

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
    private ItemStack previousItem;
    private double maxHealth;
    private Map<Attributes, Integer> attributesMap;
    private Map<Attributes, Integer> previousAttributesMap;
    private BukkitRunnable task;

    @EventHandler
    public void onHeld(PlayerItemHeldEvent evt) {
        target = evt.getPlayer();
        targetItem = target.getInventory().getItem(evt.getNewSlot());
        previousItem = target.getInventory().getItem(evt.getPreviousSlot());
        maxHealth= target.getMaxHealth();
        attributesMap = AttributeManager.getInstance().getAttributesFromItem(targetItem);
        previousAttributesMap = AttributeManager.getInstance().getAttributesFromItem(previousItem);

        if(targetItem != null) {
            if(attributesMap.containsKey(Attributes.REGENERATION)) {
                task = new RegenerationTask(target, attributesMap.get(Attributes.REGENERATION));
                tasks.put(target, task);
                task.runTaskTimer(SoulRing.getInstance(), 20, 20);
            } else {
                if(tasks.containsKey(target)) {
                    tasks.get(target).cancel();
                    tasks.remove(target);
                }
            }
            if(previousAttributesMap.containsKey(Attributes.HEALTH)) {
                target.setMaxHealth(maxHealth - previousAttributesMap.get(Attributes.HEALTH));
            }
            if(attributesMap.containsKey(Attributes.HEALTH)) {
                target.setMaxHealth(maxHealth + attributesMap.get(Attributes.HEALTH));
            }
        }
    }
}
