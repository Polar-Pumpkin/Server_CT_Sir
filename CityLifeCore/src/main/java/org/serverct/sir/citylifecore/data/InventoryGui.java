package org.serverct.sir.citylifecore.data;

import lombok.Data;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.serverct.sir.citylifecore.CityLifeCore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public @Data class InventoryGui{

    private CLID id;
    private Inventory inventory;
    private Plugin plugin;
    private String title;
    private Sound openSound;
    private Sound closeSound;
    private String permission;
    private Map<CLID, InventoryItem> items;

    public InventoryGui(String id, Inventory inventory, Plugin plugin, Map<CLID, InventoryItem> items , Sound openSound, Sound closeSound, String permission) {
        this.id = new CLID(id, plugin);
        this.inventory = inventory;
        this.plugin = plugin;
        this.title = ChatColor.stripColor(inventory.getTitle());
        this.openSound = openSound;
        this.closeSound = closeSound;
        this.permission = permission;
        this.items = items;
    }

    public InventoryGui(String id, FileConfiguration data, Plugin plugin) {
        this.id = new CLID(id, plugin);
        this.inventory = CityLifeCore.getAPI().getInventoryUtil().buildInventory(data, plugin);
        this.plugin = plugin;
        this.title = ChatColor.stripColor(inventory.getTitle());
        this.openSound = Sound.valueOf(data.getString("Setting.Sound.Open").toUpperCase());
        this.closeSound = Sound.valueOf(data.getString("Setting.Sound.Close").toUpperCase());
        this.permission = data.getString("Setting.Permission");
        this.items = CityLifeCore.getAPI().getInventoryUtil().constructInventoryItemList(this.id, data.getConfigurationSection("Items"), plugin);
    }

    public InventoryGui(CLID id, FileConfiguration data, Plugin plugin) {
        this.id = id;
        this.inventory = CityLifeCore.getAPI().getInventoryUtil().buildInventory(data, plugin);
        this.plugin = plugin;
        this.title = ChatColor.stripColor(inventory.getTitle());
        this.openSound = Sound.valueOf(data.getString("Setting.Sound.Open").toUpperCase());
        this.closeSound = Sound.valueOf(data.getString("Setting.Sound.Close").toUpperCase());
        this.permission = data.getString("Setting.Permission");
        this.items = CityLifeCore.getAPI().getInventoryUtil().constructInventoryItemList(this.id, data.getConfigurationSection("Items"), plugin);
    }

    public String[] getInfo() {
        return new String[]{
                "==========[ InventoryGui 界面详细信息 ]==========",
                "  > 主管插件: " + plugin.getName(),
                "  > 标题: " + title,
                "  > 物品数量: " + items.size(),
                "",
                "  > Inventory 信息",
                "    > HashCode: " + inventory.hashCode(),
                "    > 种类: " + inventory.getType(),
                "    > 标题: " + inventory.getTitle(),
                "    > 标识名: " + inventory.getName(),
                "    > 大小: " + inventory.getSize(),
                "",
                "  > 其他设置",
                "    > 权限节点: " + permission,
                "    > 声音设置",
                "      > 开启: " + openSound.toString(),
                "      > 关闭: " + closeSound.toString()
        };
    }

    public List<String> getItemsInfo() {
        List<String> info = new ArrayList<>();
        for(InventoryItem item : items.values()) {
            info.addAll(item.getInfo(false));
        }
        return info;
    }

    public boolean containItem(CLID id) {
        for(CLID itemID : items.keySet()) {
            if(itemID.equals(id)) {
                return true;
            }
        }
        return false;
    }

    public InventoryItem getItem(CLID id) {
        if(containItem(id)) {
            for(InventoryItem item : items.values()) {
                if(item.getId().equals(id)) {
                    return item;
                }
            }
        }
        return null;
    }

    public InventoryGui Xclone() {
        return new InventoryGui(
                this.id.getKey(),
                this.inventory,
                this.plugin,
                this.items,
                this.openSound,
                this.closeSound,
                this.permission
        );
    }
}
