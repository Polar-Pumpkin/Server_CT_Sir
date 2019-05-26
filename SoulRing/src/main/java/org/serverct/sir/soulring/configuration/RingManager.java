package org.serverct.sir.soulring.configuration;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
//import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.serverct.sir.soulring.Attributes;
import org.serverct.sir.soulring.SoulRing;

import java.io.File;
import java.io.IOException;
import java.util.*;

/*
Display: xxx
Type: EGG
Attribute:
  VAMPIRE_RATE: 10
Enchants:
  - xxx:10
  - xxx:1
ItemFlags:
  - xxx: true
 */

public class RingManager {

    private static RingManager ringManager;

    public static RingManager getRingManager() {
        if(ringManager == null) {
            ringManager = new RingManager();
        }
        return  ringManager;
    }

    private String[] soulRingLore = {
            "&a&m┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄",
            "  &a&l附加属性 &7>",
            "",
            "%AttributesPreview%",
            "",
            "  &a&l用法介绍 &7>",
            "",
            "    &e&l&o> &7需要一个 &c魂环 &7孔位!",
            "    &e&l&o> &7将魂环放在物品栏第 &c9 &7格.",
            "    &e&l&o> &7手持需要附加魂环的物品输入 &d&l/sr absorb &7即可.",
            "&a&m┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄"
    };

    /*private String[] exampleItemFlag = {
            "HIDE_ATTRIBUTES",
            "HIDE_ENCHANTS",
            "HIDE_UNBREAKABLE"
    };*/

    private File ringsDataFolder = new File(SoulRing.getInstance().getDataFolder() + File.separator + "Rings");
    private File ringsFile = new File(ringsDataFolder.getAbsolutePath() + File.separator + "Rings.yml");
    private File settingFile = new File(ringsDataFolder.getAbsolutePath() + File.separator + "Settings.yml");
    private FileConfiguration ringsData = YamlConfiguration.loadConfiguration(ringsFile);
    private FileConfiguration settingData = YamlConfiguration.loadConfiguration(settingFile);
    private Map<String, ItemStack> loadedRingsMap = new HashMap<>();
    private Map<Attributes, Double> ringsAttributes = new HashMap<>();

    private ItemStack result;
    private ItemMeta resultMeta;
    private Set<String> configuredAttributesList;
    private List<String> attributesPreviewLore;
    private List<String> ringLore;
    private List<String> inlayRings;

    public void loadRings() {
        if(!settingFile.exists()) {
            createDefaultSetting();
            settingData = YamlConfiguration.loadConfiguration(settingFile);
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "  > 未找到魂环设置文件, 已自动生成."));
        } else {
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "  > 已加载魂环设置文件."));
        }
        if(!ringsFile.exists()) {
            createDefaultRings();
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "  > 未找到魂环配置文件, 已自动生成."));
        }

        ringsData = YamlConfiguration.loadConfiguration(ringsFile);

        int ringsAmount = 0;
        for(String key : ringsData.getKeys(false)) {
            loadedRingsMap.put(key, buildRing(ringsData.getConfigurationSection(key)));
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "  > 已构建魂环 " + key + " ."));
            ringsAmount++;
        }
        Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "  > 魂环构建完成, 共加载 " + String.valueOf(ringsAmount) + " 个魂环."));
    }

    private void createDefaultRings() {
        ConfigurationSection exampleRing =  ringsData.createSection("Example");
        exampleRing.set("Display", "&6&l示例魂环");
        exampleRing.set("Type", Material.EGG.toString());
        exampleRing.set("Limit", 3);
        exampleRing.set("Enchants.DAMAGE_ALL", 10);
//        exampleRing.set("ItemFlags", exampleItemFlag);

        ConfigurationSection exampleAttribute = exampleRing.createSection("Attributes");
        exampleAttribute.set("VAMPIRE_RATE", 1);
        exampleAttribute.set("VAMPIRE_PERCENT", 1);
        exampleAttribute.set("CRITICAL_RATE", 1);
        exampleAttribute.set("CRITICAL_DAMAGE", 2);
        exampleAttribute.set("REGENERATION", 3);
        exampleAttribute.set("PHYSICAL_DEFENSE", 1);
        exampleAttribute.set("NAUSEA", 1);
        exampleAttribute.set("BURN", 1);
        exampleAttribute.set("IMPRISONMENT", 1);

        try {
            ringsData.save(ringsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ringsData = YamlConfiguration.loadConfiguration(ringsFile);
    }

    private void createDefaultSetting() {
        settingData.set("RingFormat.Display", "%name%");
        settingData.set("RingFormat.Lore", soulRingLore);
        settingData.set("AttributePreview", "    &d&l> %attribute% &7-> %value%");
        settingData.set("PunchLimit", 3);
        settingData.set("Design.Header", "&2ᚐᚑᚒᚓᚔᚍᚎᚏ &a&l魂环 &2ᚏᚎᚍᚔᚓᚒᚑᚐ");
        settingData.set("Design.Slot.Empty", "&a□ &7空魂环位");
        settingData.set("Design.Slot.Filled", "&a▣ &7已镶嵌: ");

        try {
            settingData.save(settingFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        settingData = YamlConfiguration.loadConfiguration(settingFile);
    }

    public Set<String> getLoadedRings() {
        return loadedRingsMap.keySet();
    }

    public String getEmptySlotDisplay() {
        return ChatColor.translateAlternateColorCodes('&', settingData.getString("Design.Slot.Empty"));
    }

    public String getFilledSlotDisplay() {
        return ChatColor.translateAlternateColorCodes('&', settingData.getString("Design.Slot.Filled"));
    }

    public String getHeader() {
        return ChatColor.translateAlternateColorCodes('&', settingData.getString("Design.Header"));
    }

    public int getLimit(String ringKey) {
        return ringsData.getInt(ringKey + ".Limit");
    }

    public int countRing(ItemStack item, String ringKey) {
        inlayRings = SlotManager.getSlotManager().getInlayRings(item);
        int amount = 0;
        for(String ring : inlayRings) {
            if(ring.equals(ringKey)) {
                amount++;
            }
        }
        return amount;
    }

    public String getRingDisplay(String ringKey) {
        return loadedRingsMap.get(ringKey).getItemMeta().getDisplayName();
    }

    public String getRingDisplay(ItemStack targetItem) {
        if(isRing(targetItem)) {
            return loadedRingsMap.get(getRingKey(targetItem)).getItemMeta().getDisplayName();
        }
        return null;
    }

    private ItemStack buildRing(ConfigurationSection section) {
        result = buildBasicRing(section);
        resultMeta = result.getItemMeta();
        attributesPreviewLore = buildAttributePreviewLore(section);
        ringLore = new ArrayList<>();

        for(String lore : settingData.getStringList("RingFormat.Lore")) {
            ringLore.add(ChatColor.translateAlternateColorCodes('&', lore));
        }

        List<String> currentRingLore = new ArrayList<>(ringLore);

        int loreRow = 0;
        for(String lore : ringLore) {
            if(lore.equalsIgnoreCase("%AttributesPreview%")) {
                int attributePreviewLoreRow = 0;
                for(String attributesPreview : attributesPreviewLore) {
                    if(attributePreviewLoreRow == 0) {
                        currentRingLore.set(loreRow, attributesPreview);
                    } else {
                        currentRingLore.add(loreRow, attributesPreview);
                    }
                    attributePreviewLoreRow++;
                }
            }
            loreRow++;
        }

        resultMeta.setLore(currentRingLore);
        result.setItemMeta(resultMeta);
        return result;
    }

    private ItemStack buildBasicRing(ConfigurationSection section) {
        result = new ItemStack(Material.valueOf(section.getString("Type").toUpperCase()));
        resultMeta = result.getItemMeta();

        resultMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', section.getString("Display")));
        if(section.getKeys(false).contains("Enchants")) {
            try{
                for(String enchant : section.getConfigurationSection("Enchants").getKeys(false)) {
                    resultMeta.addEnchant(Enchantment.getByName(enchant.toUpperCase()), section.getConfigurationSection("Enchants").getInt(enchant), true);
                }
            } catch (Exception e) {}
        }
        /*if(section.getKeys(false).contains("ItemFlags")) {
            try{
                for(String itemFlag : section.getStringList("ItemFlags")) {
                    resultMeta.addItemFlags(ItemFlag.valueOf(itemFlag.toUpperCase()));
                }
            } catch (Exception e) {}
        }*/

        result.setItemMeta(resultMeta);
        return result;
    }

    private List<String> buildAttributePreviewLore(ConfigurationSection section) {
        configuredAttributesList = section.getConfigurationSection("Attributes").getKeys(false);
        attributesPreviewLore = new ArrayList<>();

        for(String attribute : configuredAttributesList) {
            if(AttributeManager.getInstance().getAllAttributes().contains(Attributes.valueOf(attribute))) {
                if(AttributeManager.getInstance().hasEnabled(Attributes.valueOf(attribute))) {
                    attributesPreviewLore.add(
                            ChatColor.translateAlternateColorCodes(
                                    '&',
                                    settingData.getString("AttributePreview")
                                            .replace("%attribute%", AttributeManager.getInstance().getDisplay(Attributes.valueOf(attribute)))
                                            .replace("%value%", AttributeManager.getInstance().getFormattedValue(Attributes.valueOf(attribute), section.getConfigurationSection("Attributes").getInt(attribute)))
                            )
                    );
                } else {
                    Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "  &e> &7构建魂环 &c" + section.toString() + " &7时遇到错误: &c(属性未启用)" + attribute + "&7."));
                }
            } else {
                Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "  &e> &7构建魂环 &c" + section.toString() + " &7时遇到错误: &c(未知属性)" + attribute + "&7."));
            }
        }

        return attributesPreviewLore;
    }

    public boolean isRing(ItemStack item) {
        for(ItemStack loadedRing : loadedRingsMap.values()) {
            if(item.equals(loadedRing)) {
                return true;
            }
        }
        return false;
    }

    public ItemStack getRing(String ringKey) {
        if(loadedRingsMap.get(ringKey) != null) {
            return loadedRingsMap.get(ringKey);
        }
        return null;
    }

    public String getRingKey(String ringDisplay) {
        for(String key : loadedRingsMap.keySet()) {
            if(ringDisplay.equals(loadedRingsMap.get(key).getItemMeta().getDisplayName())) {
                return key;
            }
        }
        return null;
    }

    public String getRingKey(ItemStack targetItem) {
        if(isRing(targetItem)) {
            for(String key : loadedRingsMap.keySet()) {
                if(loadedRingsMap.get(key).equals(targetItem)) {
                    return key;
                }
            }
        }
        return null;
    }

    public Map<Attributes, Double> getRingAttributes(String ringKey) {
        if(!ringsAttributes.isEmpty()) {
            ringsAttributes.clear();
        }
        if(!loadedRingsMap.containsKey(ringKey)) {
            return null;
        }

        ConfigurationSection section = ringsData.getConfigurationSection(ringKey);
        configuredAttributesList = section.getConfigurationSection("Attributes").getKeys(false);

        for(String attribute : configuredAttributesList) {
            if(AttributeManager.getInstance().getAllAttributes().contains(Attributes.valueOf(attribute))) {
                if(AttributeManager.getInstance().hasEnabled(Attributes.valueOf(attribute))) {
                    ringsAttributes.put(Attributes.valueOf(attribute), section.getConfigurationSection("Attributes").getDouble(attribute));
                } else {
                    Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', " &e> &7获取魂环属性 &c" + section.toString() + " &7时遇到错误: &c(属性未启用)" + attribute + "&7."));
                }
            } else {
                Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', " &e> &7获取魂环属性 &c" + section.toString() + " &7时遇到错误: &c(未知属性)" + attribute + "&7."));
            }
        }

        return ringsAttributes;
    }

    public FileConfiguration getSettingData() {
        return settingData;
    }
}
