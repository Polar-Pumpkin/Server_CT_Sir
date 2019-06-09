package org.serverct.sir.citylifecore.utils;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.serverct.sir.citylifecore.CityLifeCore;
import org.serverct.sir.citylifecore.data.InventoryItem;
import org.serverct.sir.citylifecore.data.InventoryItemAction;
import org.serverct.sir.citylifecore.enums.inventoryitem.ActionType;
import org.serverct.sir.citylifecore.enums.inventoryitem.ClickType;
import org.serverct.sir.citylifecore.enums.inventoryitem.OptionType;
import org.serverct.sir.citylifecore.enums.inventoryitem.PositionType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ItemStackUtil {
    public static ItemStack buildBasicItem(ConfigurationSection section) {
        ItemStack result;
        String material = section.getString("Material");
        if(material.contains(":")) {
            String[] materialConfig = material.split(":");
            result = new ItemStack(Integer.valueOf(materialConfig[0]), Short.valueOf(materialConfig[1]));
        } else {
            result = new ItemStack(Integer.valueOf(material));
        }

        ItemMeta resultMeta = result.getItemMeta();
        List<String> lores = new ArrayList<>();

        resultMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', section.getString("Display")));
        for(String lore : section.getStringList("Lore")) {
            lores.add(ChatColor.translateAlternateColorCodes('&', lore));
        }

        if(section.contains("Enchants")) {
            ConfigurationSection enchants = section.getConfigurationSection("Enchants");
            for(String enchant : enchants.getKeys(false)) {
                resultMeta.addEnchant(Enchantment.getByName(enchant.toUpperCase()), enchants.getInt(enchant), true);
            }
        }

        resultMeta.setLore(lores);
        result.setItemMeta(resultMeta);
        return result;
    }

    public static InventoryItem buildInventoryItem(ConfigurationSection section) {
        Set<String> keyList = section.getKeys(false);

        if(keyList.contains("ItemStack") && keyList.contains("Position")) {
            ItemStack item = ItemStackUtil.buildBasicItem(section.getConfigurationSection("ItemStack"));
            ConfigurationSection position = section.getConfigurationSection("Position");

            List<Integer> positionList = PositionType.resolutionLocation(position.getString("X"), position.getString("Y"));

            boolean keepOpen = false;
            int price = 0;
            int point = 0;

            if(keyList.contains("Options")) {
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

            List<InventoryItemAction> itemActions = new ArrayList<>();

            if(keyList.contains("Actions")) {
                ConfigurationSection actions = section.getConfigurationSection("Actions");
                for(String key : actions.getKeys(false)) {
                    ConfigurationSection targetAction = actions.getConfigurationSection(key);
                    itemActions.addAll(buildItemActions(targetAction));
                }
            }

            return new InventoryItem(section.toString(), item, positionList, itemActions, keepOpen, price, point);
        } else {
            return null;
        }
    }

    public static List<InventoryItemAction> buildItemActions(ConfigurationSection section) {
        List<InventoryItemAction> result = new ArrayList<>();

        String id = section.getName();
        ClickType clickType = ClickType.valueOf(section.getString("Trigger"));
        String actionConfig = section.getString("Value");

        if(actionConfig.contains(";")) {
            String[] actions = actionConfig.split(";");
            for(String action : actions) {
                String[] values = action.split(":");
                result.add(new InventoryItemAction(id, clickType, ActionType.valueOf(values[0]), values[1]));
            }
        } else {
            String[] values = actionConfig.split(":");
            result.add(new InventoryItemAction(id, clickType, ActionType.valueOf(values[0]), values[1]));
        }

        return result;
    }
}
