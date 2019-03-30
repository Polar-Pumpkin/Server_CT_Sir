package org.serverct.sir.soulring;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Attributes {
    VAMPIRE_RATE, VAMPIRE_PERCENT, CRITICAL_RATE, CRITICAL_DAMAGE, REGENERATION, PHYSICAL_DEFENSE, NAUSEA, BURN, IMPRISONMENT;

    private static Map<Attributes, String> attributesDisplayMap = new HashMap<>();
    private static Map<Attributes, ChatColor> attributesColorMap = new HashMap<>();

    public static void loadAttributes() {
        FileConfiguration configData = SoulRing.getInstance().getConfig();
        ConfigurationSection attributesSection = configData.getConfigurationSection("Attributes");
        int attributeAmount = 0;
        for(String section : attributesSection.getKeys(false)) {
            if(getAttributesList().contains(Attributes.valueOf(section))) {
                if(attributesSection.getConfigurationSection(section).getBoolean("Enable")) {
                    attributesDisplayMap.put(Attributes.valueOf(section), attributesSection.getConfigurationSection(section).getString("Display"));
                    attributesColorMap.put(Attributes.valueOf(section), ChatColor.valueOf(attributesSection.getConfigurationSection(section).getString("Color").toUpperCase()));
                    Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "  &a> &7属性 &c" + section + " &7已加载."));
                } else {
                    Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "  &e> &7属性 &c" + section + " &7已禁用."));
                }
            } else {
                Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "  &c> &7发现未知属性 &c" + section + "&7."));
            }
            attributeAmount++;
        }
        Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "  &a> &7属性加载完成, 共加载 &c" + String.valueOf(attributeAmount) + " &7个属性."));
    }

    public static String getAttributeDisplay(Attributes attribute) {
        return attributesColorMap.get(attribute) + attributesDisplayMap.get(attribute);
    }

    public static ChatColor getAttributeColor(Attributes attribute) {
        return attributesColorMap.get(attribute);
    }

    public static int getDuration(Attributes attribute) {
        switch (attribute) {
            case NAUSEA:
            case IMPRISONMENT:
                return SoulRing.getInstance().getConfig().getInt("Attributes." + attribute + ".Duration") * 20;
            default:
                return -1;
        }
    }

    public static String getAttributeAmount(Attributes attribute, int value) {
        if(value >= 0) {
            if(isPercentAttribute(attribute)) {
                return ChatColor.GREEN + "+" + String.valueOf(value * 100) + "%";
            } else if(attribute == Attributes.CRITICAL_DAMAGE) {
                return ChatColor.GREEN + "+" + String.valueOf(value) + "x";
            } else if(attribute == Attributes.REGENERATION) {
                return ChatColor.GREEN + "+" + String.valueOf(value) + "/s";
            } else {
                return ChatColor.GREEN + "+" + String.valueOf(value);
            }
        } else {
            if(isPercentAttribute(attribute)) {
                return ChatColor.RED + String.valueOf(value * 100) + "%";
            } else if(attribute == Attributes.CRITICAL_DAMAGE) {
                return ChatColor.RED + String.valueOf(value) + "x";
            } else if(attribute == Attributes.REGENERATION) {
                return ChatColor.RED + String.valueOf(value) + "/s";
            } else {
                return ChatColor.RED + String.valueOf(value);
            }
        }
    }

    public static List<Attributes> getAttributesOwned(String[] lore) {
        List<String> loreWithoutColor = new ArrayList<>();
        List<Attributes> result = new ArrayList<>();
        for(String loreString : lore) {
            loreWithoutColor.add(ChatColor.stripColor(loreString));
        }
        for(String loreString : loreWithoutColor) {
            for(String attributeDisplay : attributesDisplayMap.values()) {
                if(loreString.contains(attributeDisplay) && loreString.contains(SoulRing.getInstance().getConfig().getString("Validator"))) {
                    result.add(getKey(attributeDisplay));
                }
            }
        }
        return result;
    }

    public static boolean isEnabled(Attributes attribute) {
        return attributesDisplayMap.containsKey(attribute);
    }

    public static boolean isPercentAttribute(Attributes attribute) {
        switch (attribute) {
            case VAMPIRE_RATE:
            case VAMPIRE_PERCENT:
            case CRITICAL_RATE:
            case PHYSICAL_DEFENSE:
            case NAUSEA:
            case BURN:
            case IMPRISONMENT:
                return true;
            default:
                return false;
        }
    }

    public static List<Attributes> getAttributesList() {
        List<Attributes> result = new ArrayList<>();
        for (Attributes attribute : Attributes.values()) {
            result.add(attribute);
        }
        return result;
    }

    private static Attributes getKey(String display) {
        for(Attributes attribute : attributesDisplayMap.keySet()) {
            if(attributesDisplayMap.get(attribute).equalsIgnoreCase(display)) {
                return attribute;
            }
        }
        return null;
    }
}
