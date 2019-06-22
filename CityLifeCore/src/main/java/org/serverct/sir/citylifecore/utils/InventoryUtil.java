package org.serverct.sir.citylifecore.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.serverct.sir.citylifecore.CityLifeCore;
import org.serverct.sir.citylifecore.data.InventoryGui;
import org.serverct.sir.citylifecore.data.InventoryItem;

import java.util.ArrayList;
import java.util.List;

public class InventoryUtil {

    private BukkitRunnable invTask;

    private List<InventoryItem> items;
    private Inventory targetInventory;

    private InventoryItem targetInventoryItem;

    private Player target;

    private int targetItemSlotAmount;
    private int replaceItemAmount;
    private Inventory targetReplacedInventory;

    public Inventory buildInventory(FileConfiguration data, Plugin plugin) {
        targetInventory = Bukkit.createInventory(
                null,
                data.getInt("Setting.Row") * 9,
                ChatColor.translateAlternateColorCodes('&', data.getString("Setting.Title"))
        );
        items = constructInventoryItemList(data.getConfigurationSection("Items"), plugin);

        for(InventoryItem item : items) {
            for(int position : item.getPositionList()) {
                targetInventory.setItem(position, item.getItem());
            }
        }

        return targetInventory;
    }

    public InventoryGui rebuildInventory(InventoryGui gui) {
        targetInventory = gui.getInventory();
        items = gui.getItems();

        for(InventoryItem item : items) {
            for(int position : item.getPositionList()) {
                targetInventory.setItem(position, item.getItem());
            }
        }

        gui.setInventory(targetInventory);
        return gui;
    }

    public List<InventoryItem> constructInventoryItemList(ConfigurationSection section, Plugin plugin) {
        items = new ArrayList<>();

        for(String key : section.getKeys(false)) {
            targetInventoryItem = ItemStackUtil.buildInventoryItem(section.getConfigurationSection(key), plugin);
            items.add(targetInventoryItem);
        }
        return items;
    }

    public InventoryGui replaceMultiInventoryItem(InventoryGui inventory, InventoryItem targetItem, List<InventoryItem> replaceItems) {
        if(inventory.getItems().contains(targetItem)) {
            targetItemSlotAmount = targetItem.getPositionList().size();
            replaceItemAmount = replaceItems.size();
            targetReplacedInventory = inventory.getInventory();
            for(int index = 0; index <= (targetItemSlotAmount >= replaceItemAmount ? replaceItemAmount : targetItemSlotAmount); index++) {
                targetReplacedInventory.setItem(targetItem.getPositionList().get(index), replaceItems.get(index).getItem());
            }

            inventory.setInventory(targetReplacedInventory);
            return inventory;
        }
        return null;
    }

    public InventoryItem getInventoryItem(ItemStack targetItem, List<InventoryItem> items) {
        for(InventoryItem item : items) {
            if(targetItem.isSimilar(item.getItem())) {
                return item;
            }
        }
        return null;
    }

    public void openInventory(HumanEntity humanEntity, InventoryGui inventory){
        closeInventory(humanEntity);
        invTask = new BukkitRunnable() {
            @Override
            public void run() {
                humanEntity.openInventory(inventory.getInventory());
            }
        };
        invTask.runTask(CityLifeCore.getInstance());
        target = (Player) humanEntity;
        target.playSound(target.getLocation(), inventory.getOpenSound(), 1, 1);
    }

    public void closeInventory(HumanEntity humanEntity) {
        invTask = new BukkitRunnable() {
            @Override
            public void run() {
                humanEntity.closeInventory();
            }
        };
        invTask.runTask(CityLifeCore.getInstance());
    }

    public void closeInventory(HumanEntity humanEntity, InventoryGui inventory) {
        if(inventory.getInventory().getViewers().contains(humanEntity)) {
            invTask = new BukkitRunnable() {
                @Override
                public void run() {
                    humanEntity.closeInventory();
                }
            };
            invTask.runTask(CityLifeCore.getInstance());
            target = (Player) humanEntity;
            target.playSound(target.getLocation(), inventory.getCloseSound(), 1, 1);
        }
    }
}
