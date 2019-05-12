package org.serverct.sir.mood.utils;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemStackUtil {

    public static ItemStack buildItem(ConfigurationSection section) {
        ItemStack result = new ItemStack(section.getInt("Material"));
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
}
