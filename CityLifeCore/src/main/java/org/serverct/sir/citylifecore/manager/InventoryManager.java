package org.serverct.sir.citylifecore.manager;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.serverct.sir.citylifecore.data.CLID;
import org.serverct.sir.citylifecore.data.InventoryGui;

import java.util.HashMap;
import java.util.Map;

public class InventoryManager {

    @Getter private Map<CLID, InventoryGui> loadedInventory = new HashMap<>();

    public void loadInventory(CLID id, FileConfiguration data, Plugin plugin) {
        loadedInventory.put(id, new InventoryGui(id, data, plugin));
    }

    public String getNoColorName(String id) {
        if(loadedInventory.containsKey(id)) {
            return ChatColor.stripColor(loadedInventory.get(id).getInventory().getName());
        }
        return "";
    }

    public String getName(String id) {
        if(loadedInventory.containsKey(id)) {
            return loadedInventory.get(id).getInventory().getName();
        }
        return "";
    }

}
