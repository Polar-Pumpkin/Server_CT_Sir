package org.serverct.sir.hunhuan.configuration;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.serverct.sir.hunhuan.HunHuan;
import org.serverct.sir.hunhuan.data.HunhuanData;
import org.serverct.sir.hunhuan.enums.HunHuanEffect;
import org.serverct.sir.hunhuan.utils.LocaleUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HunhuanManager {

    private LocaleUtil locale;
    private FileConfiguration data;

    @Getter private String header;
    @Getter private String footer;
    @Getter private String display;

    @Getter private Map<String, HunhuanData> hunguList = new HashMap<>();

    private ConfigurationSection hunguSection;
    private ConfigurationSection targetSection;
    private ItemStack item;
    private ItemMeta meta;
    private List<String> lore;
    private List<String> fjLore;
    private List<HunHuanEffect> effects;

    public HunhuanManager(FileConfiguration data) {
        this.data = data;
        this.locale = HunHuan.getInstance().getLocale();
    }

    public void loadHungu() {
        locale.debug("开始加载魂环.");
        header = ChatColor.translateAlternateColorCodes('&', data.getString("Format.Header"));
        locale.debug("魂环镶嵌页眉: " + header);
        footer = ChatColor.translateAlternateColorCodes('&', data.getString("Format.Footer"));
        locale.debug("魂环镶嵌页脚: " + footer);
        display = ChatColor.translateAlternateColorCodes('&', data.getString("Format.Display"));
        locale.debug("魂环镶嵌标签: " + display);
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

            effects = new ArrayList<>();
            for(String temp : targetSection.getStringList("Texiao")) {
                effects.add(HunHuanEffect.valueOf(temp.toUpperCase()));
            }

            HunhuanData hunhuan = new HunhuanData(key, item, fjLore, effects);
            locale.debug("构建魂环: " + hunhuan.toString());
            hunguList.put(key, hunhuan);
        }
    }

    public boolean containHungu(String id) {
        return hunguList.containsKey(id);
    }

    public HunhuanData getHungu(ItemStack item) {
        for(HunhuanData hungu : hunguList.values()) {
            if(item.isSimilar(hungu.getItem())) {
                return hungu;
            }
        }
        return null;
    }

    public HunhuanData getHungu(String displayName) {
        for(HunhuanData hungu : hunguList.values()) {
            if(displayName.equals(hungu.getItem().getItemMeta().getDisplayName())) {
                return hungu;
            }
        }
        return null;
    }

    public boolean isHungu(HunhuanData hungu) {
        return hunguList.containsValue(hungu);
    }

    public boolean isHungu(ItemStack item) {
        return getHungu(item) != null;
    }

    public boolean isHungu(String displayName) {
        return getHungu(displayName) != null;
    }
}
