package org.serverct.sir.mood.configuration;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.serverct.sir.mood.Consumable;
import org.serverct.sir.mood.ConsumableType;
import org.serverct.sir.mood.Mood;
import org.serverct.sir.mood.utils.ItemStackUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Item {

    private static Item instance;

    public static Item getInstance() {
        if(instance == null) {
            instance = new Item();
        }
        return instance;
    }

    private File dataFile = new File(Mood.getInstance().getDataFolder() + File.separator + "consumables.yml");
    private FileConfiguration data = YamlConfiguration.loadConfiguration(dataFile);

    @Getter private Map<String, Consumable> itemMap = new HashMap<>();

    private int counter;
    private ConfigurationSection targetSection;
    private Consumable targetConsumable;
    private ItemStack savedItem;
    private ItemMeta savedMeta;
    private List<String> savedLore;

    public void loadItem() {
        if(!dataFile.exists()) {
            Mood.getInstance().saveResource("consumables.yml", false);
            data = YamlConfiguration.loadConfiguration(dataFile);
            Bukkit.getLogger().info("  > 未找到消耗品配置文件, 已自动生成.");
        }

        counter = 0;
        for(String section : data.getKeys(false)) {
            targetSection = data.getConfigurationSection(section);
            targetConsumable = new Consumable(ItemStackUtil.buildItem(targetSection), ConsumableType.valueOf(targetSection.getString("Type").toUpperCase()), targetSection.getInt("Value"));
            itemMap.put(section, targetConsumable);
            counter++;
        }

        Bukkit.getLogger().info("  > 共加载 " + counter + " 个消耗品.");
    }

    public void saveItem(String id, ItemStack item, ConsumableType type, int value) {
        savedItem = item;
        savedMeta = item.getItemMeta();
        savedLore = new ArrayList<>();
        targetConsumable = new Consumable(item, type, value);
        itemMap.put(id, targetConsumable);

        for(String lore : savedMeta.getLore()) {
            savedLore.add(lore.replace("§", "&"));
        }

        targetSection = data.createSection(id);
        targetSection.set("Display", savedMeta.getDisplayName().replace("§", "&"));
        targetSection.set("Material", item.getType().getId());
        targetSection.set("Type", type.toString());
        targetSection.set("Lore", savedLore);
        targetSection.set("Value", value);

        try {
            data.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
