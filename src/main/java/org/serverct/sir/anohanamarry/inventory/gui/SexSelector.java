package org.serverct.sir.anohanamarry.inventory.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.serverct.sir.anohanamarry.ANOHANAMarry;
import org.serverct.sir.anohanamarry.hook.AMarryEconomy;
import org.serverct.sir.anohanamarry.inventory.InventoryManager;
import org.serverct.sir.anohanamarry.inventory.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class SexSelector {

    private static SexSelector sexSelector;

    public static SexSelector getInstance() {
        if(sexSelector == null) {
            sexSelector = new SexSelector();
        }
        return sexSelector;
    }

    private List<MenuItem> items = new ArrayList<>();

    private FileConfiguration sexSelectorData;
    private Inventory sexSelectorInv;
    private ConfigurationSection itemSection;

    private MenuItem buildedMenuItem;

    private MenuItem targetMenuItem;
    private Player targetPlayer;

    public Inventory buildSexSelector() {
        sexSelectorData = InventoryManager.getInstance().getGuiInventoryMap().get("SexSelector");
        sexSelectorInv = Bukkit.createInventory(null, sexSelectorData.getInt("Menu-Setting.Row") * 9, InventoryManager.getInstance().getInventoryTitle("SexSelector"));
        if(!items.isEmpty()) {
            items.clear();
        }

        itemSection = sexSelectorData.getConfigurationSection("Items");
        for(String key : itemSection.getKeys(false)) {
            buildedMenuItem = InventoryManager.getInstance().buildMenuItem(itemSection.getConfigurationSection(key));
            buildedMenuItem.info();
            items.add(buildedMenuItem);

        }
        System.out.println(items);

        for(MenuItem item : items) {
            for(int position : item.getPositionList()) {
                sexSelectorInv.setItem(position, item.getItem());
            }
        }

        return sexSelectorInv;
    }

    public void click(InventoryClickEvent evt) {
        targetMenuItem = InventoryManager.getInstance().getMenuItem(evt.getCurrentItem(), items);
        if(evt.getWhoClicked() instanceof  Player) {
            targetPlayer = (Player) evt.getWhoClicked();

            if(targetMenuItem != null) {
                if(targetMenuItem.getPrice() != 0) {
                    if (!AMarryEconomy.getAMarryEconomyUtil().cost(Bukkit.getOfflinePlayer(targetPlayer.getName()), targetMenuItem.getPrice())) {
                        return;
                    }
                }

                if(!targetMenuItem.getCommand().equalsIgnoreCase("") && targetMenuItem != null) {
                    targetPlayer.chat("/" + targetMenuItem.getCommand());
                }

                evt.setCancelled(true);
                if(!targetMenuItem.isKeepOpen()) {
                    InventoryManager.getInstance().closeInventory(evt.getWhoClicked());
                }
            }
        } else {
            InventoryManager.getInstance().closeInventory(evt.getWhoClicked());
        }
    }

}
