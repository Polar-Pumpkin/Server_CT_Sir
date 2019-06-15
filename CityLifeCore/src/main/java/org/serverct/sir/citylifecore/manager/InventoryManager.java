package org.serverct.sir.citylifecore.manager;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.serverct.sir.citylifecore.data.InventoryGui;

import java.util.HashMap;
import java.util.Map;

public class InventoryManager {

    @Getter private Map<String, InventoryGui> loadedInventory = new HashMap<>();

    public void loadInventory(String id, FileConfiguration data) {
        loadedInventory.put(id, new InventoryGui(data));
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
