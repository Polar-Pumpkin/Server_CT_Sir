package org.serverct.sir.duobao.manager;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.serverct.sir.duobao.Duobao;
import org.serverct.sir.duobao.data.TreasureItem;
import org.serverct.sir.duobao.enums.MessageType;
import org.serverct.sir.duobao.util.LocaleUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ItemManager {

    private static LocaleUtil locale = Duobao.getInstance().getLocale();

    private static ItemManager instance;
    public static ItemManager getInstance() {
        if(instance == null) {
            instance = new ItemManager();
        }
        locale = Duobao.getInstance().getLocale();
        return instance;
    }

    private Map<String, TreasureItem> treasureItemMap = new HashMap<>();

    public void loadItems() {
        ConfigurationSection itemSection = Duobao.getInstance().getConfig().getConfigurationSection("Items");
        for(String key : itemSection.getKeys(false)) {
            treasureItemMap.put(key, new TreasureItem(itemSection.getConfigurationSection(key)));
        }
        Bukkit.getLogger().info(locale.buildMessage(Duobao.getInstance().getLocaleKey(), MessageType.INFO, "&7共加载 &c" + treasureItemMap.size() + " &7个宝藏物品."));
    }

    public Collection<TreasureItem> getTreasures() {
        return treasureItemMap.values();
    }

}
