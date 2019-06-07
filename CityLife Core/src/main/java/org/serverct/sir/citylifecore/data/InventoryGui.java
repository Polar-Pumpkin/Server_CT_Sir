package org.serverct.sir.citylifecore.data;

import lombok.Data;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;
import org.serverct.sir.citylifecore.CityLifeCore;

import java.util.List;

public @Data class InventoryGui {

    private Inventory inventory;
    private String title;
    private Sound openSound;
    private Sound closeSound;
    private String permission;
    private List<InventoryItem> items;

    public InventoryGui(Inventory inventory, List<InventoryItem> items , Sound openSound, Sound closeSound, String permission) {
        this.inventory = inventory;
        this.title = ChatColor.stripColor(inventory.getTitle());
        this.openSound = openSound;
        this.closeSound = closeSound;
        this.permission = permission;
        this.items = items;
    }

    public InventoryGui(FileConfiguration data) {
        this.inventory = CityLifeCore.getAPI().getInventoryUtil().buildInventory(data);
        this.title = ChatColor.stripColor(inventory.getTitle());
        this.openSound = Sound.valueOf(data.getString("Setting.Sound.Open").toUpperCase());
        this.closeSound = Sound.valueOf(data.getString("Setting.Sound.Close").toUpperCase());
        this.permission = data.getString("Setting.Permission");
        this.items = CityLifeCore.getAPI().getInventoryUtil().constructInventoryItemList(data.getConfigurationSection("Items"));
    }

}
