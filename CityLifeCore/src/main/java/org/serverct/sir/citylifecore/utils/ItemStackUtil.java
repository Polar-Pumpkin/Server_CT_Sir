package org.serverct.sir.citylifecore.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.serverct.sir.citylifecore.data.Action;
import org.serverct.sir.citylifecore.data.CLID;
import org.serverct.sir.citylifecore.data.InventoryClick;
import org.serverct.sir.citylifecore.data.InventoryItem;
import org.serverct.sir.citylifecore.enums.inventoryitem.ActionType;
import org.serverct.sir.citylifecore.enums.inventoryitem.ClickType;
import org.serverct.sir.citylifecore.enums.inventoryitem.OptionType;
import org.serverct.sir.citylifecore.enums.inventoryitem.PositionType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ItemStackUtil {
    public static ItemStack buildBasicItem(ConfigurationSection section) {
        ItemStack result;
        String material = section.getString("Material");
        if(material.contains(":")) {
            String[] materialConfig = material.split(":");
            if(CommonUtil.isInteger(materialConfig[1])) {
                if(CommonUtil.isInteger(materialConfig[0])) {
                    result = new ItemStack(Integer.valueOf(materialConfig[0]), Short.valueOf(materialConfig[1]));
                } else {
                    result = new ItemStack(Material.valueOf(materialConfig[0].toUpperCase()), Short.valueOf(materialConfig[1]));
                }
            } else {
                return null;
            }
        } else {
            if(CommonUtil.isInteger(material)) {
                result = new ItemStack(Integer.valueOf(material));
            } else {
                result = new ItemStack(Material.valueOf(material.toUpperCase()));
            }
        }

        if(result.getType() != Material.AIR) {
            ItemMeta resultMeta = result.getItemMeta();

            resultMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', section.getString("Display")));

            if(section.contains("Enchants")) {
                ConfigurationSection enchants = section.getConfigurationSection("Enchants");
                for(String enchant : enchants.getKeys(false)) {
                    resultMeta.addEnchant(Enchantment.getByName(enchant.toUpperCase()), enchants.getInt(enchant), true);
                }
            }
            if(section.contains("Lore")) {
                List<String> lores = new ArrayList<>();

                for(String lore : section.getStringList("Lore")) {
                    lores.add(ChatColor.translateAlternateColorCodes('&', lore));
                }

                resultMeta.setLore(lores);
            }

            result.setItemMeta(resultMeta);
        }
        return result;
    }

    public static InventoryItem buildInventoryItem(CLID inventoryID, ConfigurationSection section, Plugin plugin) {
        if(section.isConfigurationSection("ItemStack")) {
            CLID itemID = new CLID(section.getName(), inventoryID);
            ItemStack item = ItemStackUtil.buildBasicItem(section.getConfigurationSection("ItemStack"));
            List<Integer> positionList = new ArrayList<>();

            if(section.isConfigurationSection("Position")) {
                ConfigurationSection position = section.getConfigurationSection("Position");
                positionList = PositionType.resolutionLocation(position.getString("X"), position.getString("Y"));
            }

            boolean keepOpen = false;
            int price = 0;
            int point = 0;
            if(section.isConfigurationSection("Options")) {
                ConfigurationSection options = section.getConfigurationSection("Options");
                for(String key : options.getKeys(false)) {
                    switch(OptionType.valueOf(key.toUpperCase())) {
                        case KEEPOPEN:
                            keepOpen = options.getBoolean(key);
                            break;
                        case PRICE:
                            price = options.getInt(key);
                            break;
                        case POINT:
                            point = options.getInt(key);
                            break;
                        default:
                            break;
                    }
                }
            }

            List<InventoryClick> itemClicks = new ArrayList<>();
            if(section.isConfigurationSection("Actions")) {
                itemClicks.addAll(buildItemClicks(itemID, section, plugin));
            }

            return new InventoryItem(itemID, item, positionList, itemClicks, keepOpen, price, point);
        } else {
            return null;
        }
    }

    public static List<InventoryClick> buildItemClicks(CLID itemID, ConfigurationSection section, Plugin plugin) {
        List<InventoryClick> result = new ArrayList<>();
        ConfigurationSection actionSection = section.getConfigurationSection("Actions");

        for(String key : actionSection.getKeys(false)) {
            CLID clickID = new CLID(key, itemID);
            ConfigurationSection targetSection = actionSection.getConfigurationSection(key);
            result.add(
                    new InventoryClick(
                            clickID,
                            plugin,
                            ClickType.valueOf(targetSection.getString("Trigger").toUpperCase()),
                            buildItemActions(clickID, targetSection, plugin)
                    )
            );
        }

        return result;
    }

    public static List<Action> buildItemActions(CLID clickID, ConfigurationSection section, Plugin plugin) {
        List<Action> result = new ArrayList<>();
        CLID actionID;

        String actionConfig = section.getString("Value");

        if(actionConfig.contains(";")) {
            String[] actions = actionConfig.split(";");
            for(String action : actions) {
                String[] values = action.split(":");
                actionID = new CLID(values[0], clickID);
                result.add(new Action(actionID, plugin, ActionType.valueOf(values[0]), values[1]));
            }
        } else {
            String[] values = actionConfig.split(":");
            actionID = new CLID(values[0], clickID);
            result.add(new Action(actionID, plugin, ActionType.valueOf(values[0]), values[1]));
        }

        return result;
    }

    public static ItemStack replaceVariable(ItemStack item, Map<String, String> placeholders) {
        ItemMeta meta = item.getItemMeta();
        for(String key : placeholders.keySet()) {
            meta.setDisplayName(meta.getDisplayName().replace("%" + key + "%", placeholders.get(key)));
            meta.getLore().replaceAll(s -> s.replace("%" + key + "%", placeholders.get(key)));
        }
        item.setItemMeta(meta);
        return item;
    }
}
