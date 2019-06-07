package org.serverct.sir.citylifecore.manager;

import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.serverct.sir.citylifecore.data.InventoryGui;
import org.serverct.sir.citylifecore.utils.CommonUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class InventoryManager {

    @Getter private Map<String, InventoryGui> loadedInventory = new HashMap<>();

    public void loadInventory(File dataFile) {
        loadedInventory.put(CommonUtil.getFileNameNoEx(dataFile.getName()), new InventoryGui(YamlConfiguration.loadConfiguration(dataFile)));
    }

}
