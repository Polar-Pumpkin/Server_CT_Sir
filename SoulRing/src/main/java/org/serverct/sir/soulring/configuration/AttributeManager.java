package org.serverct.sir.soulring.configuration;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.serverct.sir.soulring.Attributes;
import org.serverct.sir.soulring.SoulRing;

import java.util.*;

public class AttributeManager {

    private static AttributeManager attributeManager;

    public static AttributeManager getInstance() {
        if(attributeManager == null) {
            attributeManager = new AttributeManager();
        }
        return attributeManager;
    }

    private Map<Attributes, String> attributesDisplayMap = new HashMap<>();
    private Map<Attributes, ChatColor> attributesColorMap = new HashMap<>();

    private Map<Attributes, Integer> attributesOnItem = new HashMap<>();
    private Map<Attributes, Integer> cacheAttributesMap = new HashMap<>();

    private Map<Attributes, Integer> attributesOnArmor = new HashMap<>();
    private Map<Attributes, Integer> attributesOnHand = new HashMap<>();
    private Map<Attributes, Integer> attributesOnPlayer = new HashMap<>();

    private List<String> inlayRings;
    private ItemStack[] armors;

    public void loadAttributes() {
        FileConfiguration configData = SoulRing.getInstance().getConfig();
        ConfigurationSection attributesSection = configData.getConfigurationSection("Attributes");

        int attributeAmount = 0;
        for(String section : attributesSection.getKeys(false)) {
            if(getAllAttributes().contains(Attributes.valueOf(section))) {
                if(attributesSection.getConfigurationSection(section).getBoolean("Enable")) {
                    attributesDisplayMap.put(Attributes.valueOf(section), attributesSection.getConfigurationSection(section).getString("Display"));
                    attributesColorMap.put(Attributes.valueOf(section), ChatColor.valueOf(attributesSection.getConfigurationSection(section).getString("Color").toUpperCase()));
                    Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "  > 属性 " + section + " 已加载."));
                } else {
                    Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "  > 属性 " + section + " 已禁用."));
                }
            } else {
                Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "  > 发现未知属性 " + section + "."));
            }
            attributeAmount++;
        }
        Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "  > 属性加载完成, 共加载 " + attributeAmount + " 个属性."));
    }

    public String getDisplay(Attributes attribute) {
        return attributesColorMap.get(attribute) + attributesDisplayMap.get(attribute);
    }

    public ChatColor getColor(Attributes attribute) {
        return attributesColorMap.get(attribute);
    }

    public int getDuration(Attributes attribute) {
        switch (attribute) {
            case NAUSEA:
            case IMPRISONMENT:
                return SoulRing.getInstance().getConfig().getInt("Attributes." + attribute + ".Duration") * 20;
            default:
                return -1;
        }
    }

    private Attributes getKey(String display) {
        for(Attributes attribute : attributesDisplayMap.keySet()) {
            if(attributesDisplayMap.get(attribute).equalsIgnoreCase(display)) {
                return attribute;
            }
        }
        return null;
    }

    public List<Attributes> getAllAttributes() {
        return new ArrayList<>(Arrays.asList(Attributes.values()));
    }

    public String getFormattedValue(Attributes attribute, int value) {
        if(value >= 0) {
            if(isPercentValue(attribute)) {
                return ChatColor.GREEN + "+" + value + "%";
            } else if(attribute == Attributes.CRITICAL_DAMAGE) {
                return ChatColor.GREEN + "+" + value + "x";
            } else if(attribute == Attributes.REGENERATION) {
                return ChatColor.GREEN + "+" + value + "/s";
            } else {
                return ChatColor.GREEN + "+" + value;
            }
        } else {
            if(isPercentValue(attribute)) {
                return ChatColor.RED + String.valueOf(value) + "%";
            } else if(attribute == Attributes.CRITICAL_DAMAGE) {
                return ChatColor.RED + String.valueOf(value) + "x";
            } else if(attribute == Attributes.REGENERATION) {
                return ChatColor.RED + String.valueOf(value) + "/s";
            } else {
                return ChatColor.RED + String.valueOf(value);
            }
        }
    }

    public Map<Attributes, Integer> getAttributesFromItem(ItemStack item) {
        if(!attributesOnItem.isEmpty()) {
            attributesOnItem.clear();
        }
        if(item != null || item.getType() != Material.AIR) {
            if(item.hasItemMeta()) {
                if(SlotManager.getSlotManager().containSlot(item)) {
                    inlayRings = SlotManager.getSlotManager().getInlayRings(item);
                    if (!inlayRings.isEmpty()) {
                        for (String key : inlayRings) {
                            cacheAttributesMap = RingManager.getRingManager().getRingAttributes(key);
                            for (Attributes attribute : cacheAttributesMap.keySet()) {
                                if (attributesOnItem.containsKey(attribute)) {
                                    attributesOnItem.put(attribute, attributesOnItem.get(attribute) + cacheAttributesMap.get(attribute));
                                } else {
                                    attributesOnItem.put(attribute, cacheAttributesMap.get(attribute));
                                }
                            }
                        }
                    }
                }
            }
        }

        return attributesOnItem;
    }

    public Map<Attributes, Integer> getAttributesFromPlayer(Player player) {
        if(!attributesOnPlayer.isEmpty()) {
            attributesOnPlayer.clear();
        }
        if(!attributesOnHand.isEmpty()) {
            attributesOnHand.clear();
        }
        if(!attributesOnArmor.isEmpty()) {
            attributesOnArmor.clear();
        }

        attributesOnHand = getAttributesFromItem(player.getItemInHand());
        armors = player.getInventory().getArmorContents();

        attributesOnPlayer.putAll(attributesOnHand);

        for(ItemStack armor : armors) {
            if(armor != null || armor.getType() != Material.AIR) {
                if(armor.hasItemMeta()) {
                    attributesOnArmor = getAttributesFromItem(armor);
                    for (Attributes attribute : attributesOnArmor.keySet()) {
                        if (attributesOnPlayer.containsKey(attribute)) {
                            attributesOnPlayer.put(attribute, attributesOnPlayer.get(attribute) + attributesOnArmor.get(attribute));
                        } else {
                            attributesOnPlayer.put(attribute, attributesOnArmor.get(attribute));
                        }
                    }
                }
            }
        }

        return attributesOnPlayer;
    }

    /*
    public List<Attributes> getAttributesOwned(String[] lore) {
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
    */

    public boolean hasEnabled(Attributes attribute) {
        return attributesDisplayMap.containsKey(attribute);
    }

    public boolean isPercentValue(Attributes attribute) {
        switch (attribute) {
            case VAMPIRE_RATE:
            case VAMPIRE_PERCENT:
            case CRITICAL_RATE:
            case NAUSEA:
            case BURN:
            case IMPRISONMENT:
                return true;
            default:
                return false;
        }
    }
}
