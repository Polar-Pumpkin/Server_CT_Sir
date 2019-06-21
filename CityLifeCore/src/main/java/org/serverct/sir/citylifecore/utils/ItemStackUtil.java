package org.serverct.sir.citylifecore.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.serverct.sir.citylifecore.data.Action;
import org.serverct.sir.citylifecore.data.InventoryItem;
import org.serverct.sir.citylifecore.enums.inventoryitem.ActionType;
import org.serverct.sir.citylifecore.enums.inventoryitem.ClickType;
import org.serverct.sir.citylifecore.enums.inventoryitem.OptionType;
import org.serverct.sir.citylifecore.enums.inventoryitem.PositionType;

import java.util.ArrayList;
import java.util.List;

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

    public static InventoryItem buildInventoryItem(ConfigurationSection section) {
        if(section.isConfigurationSection("ItemStack")) {
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

            List<Action> itemActions = new ArrayList<>();
            if(section.isConfigurationSection("Actions")) {
                itemActions.addAll(buildItemActions(section));
            }

            return new InventoryItem(section.toString(), item, positionList, itemActions, keepOpen, price, point);
        } else {
            return null;
        }
    }

    public static List<Action> buildItemActions(ConfigurationSection section) {
        List<Action> result = new ArrayList<>();
        ConfigurationSection actionSection = section.getConfigurationSection("Actions");

        for(String key : actionSection.getKeys(false)) {
            ConfigurationSection targetSection = actionSection.getConfigurationSection(key);

            String id = section.getName() + "_" + targetSection.getName();
            ClickType clickType = ClickType.valueOf(targetSection.getString("Trigger"));
            String actionConfig = targetSection.getString("Value");

            if(actionConfig.contains(";")) {
                String[] actions = actionConfig.split(";");
                for(String action : actions) {
                    String[] values = action.split(":");
                    result.add(new Action(id, clickType, ActionType.valueOf(values[0]), values[1]));
                }
            } else {
                String[] values = actionConfig.split(":");
                result.add(new Action(id, clickType, ActionType.valueOf(values[0]), values[1]));
            }
        }

        return result;
    }
}
