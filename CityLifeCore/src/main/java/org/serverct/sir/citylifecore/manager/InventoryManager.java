package org.serverct.sir.citylifecore.manager;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.serverct.sir.citylifecore.data.InventoryGui;
import org.serverct.sir.citylifecore.utils.CommonUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class InventoryManager {

    @Getter private Map<String, InventoryGui> loadedInventory = new HashMap<>();

    public void loadInventory(String id, FileConfiguration data) {
        loadedInventory.put(id, new InventoryGui(data));
    }

}
