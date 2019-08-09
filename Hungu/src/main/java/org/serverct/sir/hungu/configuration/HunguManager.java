package org.serverct.sir.hungu.configuration;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.serverct.sir.hungu.data.HunguData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HunguManager {

    private FileConfiguration data;

    @Getter private String header;
    @Getter private String footer;
    @Getter private String display;

    @Getter private Map<String, HunguData> hunguList = new HashMap<>();

    private ConfigurationSection hunguSection;
    private ConfigurationSection targetSection;
    private ItemStack item;
    private ItemMeta meta;
    private List<String> lore;
    private List<String> fjLore;

    public HunguManager(FileConfiguration data) {
        this.data = data;
    }

    public void loadHungu() {
        header = ChatColor.translateAlternateColorCodes('&', data.getString("Format.Header"));
        footer = ChatColor.translateAlternateColorCodes('&', data.getString("Format.Footer"));
        display = ChatColor.translateAlternateColorCodes('&', data.getString("Format.Display"));
        hunguSection = data.getConfigurationSection("Hungu");

        for(String key : hunguSection.getKeys(false)) {
            targetSection = hunguSection.getConfigurationSection(key);
            item = new ItemStack(Material.valueOf(targetSection.getString("ID").toUpperCase()));
            meta = item.getItemMeta();
            lore = new ArrayList<>();

            for(String temp : targetSection.getStringList("Lore")) {
                lore.add(ChatColor.translateAlternateColorCodes('&', temp));
            }

            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', targetSection.getString("Display")));
            meta.setLore(lore);
            item.setItemMeta(meta);

            fjLore = new ArrayList<>();
            for(String temp : targetSection.getStringList("fjLore")) {
                fjLore.add(ChatColor.translateAlternateColorCodes('&', temp));
            }

            hunguList.put(key, new HunguData(key, item, fjLore));
        }
    }

    public boolean containHungu(String id) {
        return hunguList.containsKey(id);
    }

    public HunguData getHungu(ItemStack item) {
        for(HunguData hungu : hunguList.values()) {
            if(item.isSimilar(hungu.getItem())) {
                return hungu;
            }
        }
        return null;
    }

    public HunguData getHungu(String displayName) {
        for(HunguData hungu : hunguList.values()) {
            if(displayName.equals(hungu.getItem().getItemMeta().getDisplayName())) {
                return hungu;
            }
        }
        return null;
    }

    public boolean isHungu(HunguData hungu) {
        return hunguList.containsValue(hungu);
    }

    public boolean isHungu(ItemStack item) {
        return getHungu(item) != null;
    }

    public boolean isHungu(String displayName) {
        return getHungu(displayName) != null;
    }
}
