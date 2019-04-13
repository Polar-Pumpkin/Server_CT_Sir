package org.serverct.sir.anohanamarry.configuration;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.serverct.sir.anohanamarry.ANOHANAMarry;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemData {

    private static ItemData itemDataClass;

    public static ItemData getInstance() {
        if(itemDataClass == null) {
            itemDataClass = new ItemData();
        }
        return itemDataClass;
    }

    private Map<String, ItemStack> loadedItemMap = new HashMap<>();
    private Map<String, ItemStack> loadedGiftMap = new HashMap<>();

    private File itemFile = new File(ANOHANAMarry.getINSTANCE().getDataFolder() + File.separator + "Items.yml");
    private FileConfiguration itemData = YamlConfiguration.loadConfiguration(itemFile);

    private ItemStack result;
    private ItemMeta resultMeta;
    private List<String> resultLore;

    private ConfigurationSection enchants;

    private ConfigurationSection targetSection;

    private String[] bouquetLore = {
            "&a&m┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄",
            "  &7&o可以用来向你心仪的玩家示爱, 对方同意后即可进入交往关系.",
            "",
            "  &e&l&o>>> &c手持 &7花束按住 &c潜行键 &7并 &c右键 &7目标玩家.",
            "&a&m┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄"
    };

    private String[] ringLore = {
            "&a&m┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄",
            "  &7&o可以用来向你的交往对象求婚, 对方同意后即可结婚.",
            "",
            "  &e&l&o>>> &c手持 &7钻戒按住 &c潜行键 &7并 &c右键 &7目标玩家.",
            "&a&m┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄"
    };

    private String[] certificateLore = {
            "&a&m┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄",
            "  &7&o结婚时获得, 右键撕毁结婚证以离婚.",
            "",
            "  &e&l&o>>> &c手持 &7结婚证按住 &c潜行键 &7并 &c右键&7.",
            "&a&m┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄"
    };

    private String[] sk2Lore = {
            "&a&m┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄",
            "  &7&o送给你的交往对象, 可以提升 &c&o%amount% &7&o点亲密点数.",
            "",
            "  &e&l&o>>> &c手持 &7钻戒按住 &c潜行键 &7并 &c右键 &7目标玩家.",
            "&a&m┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄"
    };

    private String[] exampleItemFlag = {
            "HIDE_ATTRIBUTES",
            "HIDE_ENCHANTS",
            "HIDE_UNBREAKABLE"
    };

    public void loadItemData() {
        if(!itemFile.exists() || !itemData.getKeys(false).contains("Ring") || !itemData.getKeys(false).contains("Bouquet") || !itemData.getKeys(false).contains("Certificate")) {
            createDefaultItem();
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "  &e> &7未找到道具数据文件(或关键道具配置缺失), 已自动生成."));
        }
        loadItems();
    }

    private void createDefaultItem() {
        ConfigurationSection bouquet = itemData.createSection("Bouquet");
        bouquet.set("Display", "&c&l示爱花束");
        bouquet.set("Type", Material.RED_ROSE.toString());
        bouquet.set("Lore", bouquetLore);
        bouquet.set("Enchants.LUCK", 10);
        bouquet.set("ItemFlags", exampleItemFlag);

        ConfigurationSection ring = itemData.createSection("Ring");
        ring.set("Display", "&b&l求婚钻戒");
        ring.set("Type", Material.DIAMOND.toString());
        ring.set("Lore", ringLore);
        ring.set("Enchants.LUCK", 10);
        ring.set("ItemFlags", exampleItemFlag);

        ConfigurationSection certificate = itemData.createSection("Certificate");
        ring.set("Display", "&6&l结婚证");
        ring.set("Type", Material.BOOK.toString());
        ring.set("Lore", certificateLore);
        ring.set("Enchants.LUCK", 10);
        ring.set("ItemFlags", exampleItemFlag);

        ConfigurationSection sk2 = itemData.createSection("SK_II");
        sk2.set("Display", "&d&lSK-II");
        sk2.set("Type", Material.EXP_BOTTLE.toString());
        sk2.set("Lore", sk2Lore);
        sk2.set("LovePoint", 10);
        sk2.set("Enchants.LUCK", 10);
        sk2.set("ItemFlags", exampleItemFlag);

        try {
            itemData.save(itemFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadItems() {
        int amount = 0;
        for(String section : itemData.getKeys(false)) {
            targetSection = itemData.getConfigurationSection(section);

            if(targetSection.getKeys(false).contains("LovePoint")) {
                loadedGiftMap.put(section, buildItem(targetSection));
                // Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "  &a> &7已加载礼物 &c" + section + "&7."));
            } else {
                loadedItemMap.put(section, buildItem(targetSection));
                // Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "  &a> &7已加载道具 &c" + section + "&7."));
            }
            amount++;
        }
        Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "  &a> &7共加载 &c" + String.valueOf(amount) + " &7个道具物品."));
    }

    public ItemStack buildItem(ConfigurationSection section) {
        result = new ItemStack(Material.valueOf(section.getString("Type").toUpperCase()));
        resultMeta = result.getItemMeta();

        resultMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', section.getString("Display")));
        if(section.getKeys(false).contains("LovePoint")) {
            for(String lore : section.getStringList("Lore")) {
                resultLore = new ArrayList<>();
                resultLore.add(ChatColor.translateAlternateColorCodes('&', lore.replace("%amount%", String.valueOf(section.getInt("LovePoint")))));
            }
        } else {
            for(String lore : section.getStringList("Lore")) {
                resultLore = new ArrayList<>();
                resultLore.add(ChatColor.translateAlternateColorCodes('&', lore));
            }
        }

        resultMeta.setLore(resultLore);

        if(section.getKeys(false).contains("Enchants")) {
            enchants = section.getConfigurationSection("Enchants");
            for(String enchant : enchants.getKeys(false)) {
                resultMeta.addEnchant(Enchantment.getByName(enchant), enchants.getInt(enchant), true);
            }
        }
        if(section.getKeys(false).contains("ItemFlags")) {
            for(String itemFlag : section.getStringList("ItemFlags")) {
                resultMeta.addItemFlags(ItemFlag.valueOf(itemFlag));
            }
        }

        result.setItemMeta(resultMeta);
        return result;
    }

    public boolean isGift(String giftKey) {
        return loadedGiftMap.containsKey(giftKey);
    }

    public boolean isGift(ItemStack item) {
        return loadedGiftMap.containsValue(item);
    }

    public boolean isItem(String itemKey) {
        return loadedItemMap.containsKey(itemKey);
    }

    public boolean isItem(ItemStack item) {
        return loadedItemMap.containsValue(item);
    }

    public String getGiftKey(ItemStack item) {
        if(isGift(item)) {
            for (String key : loadedGiftMap.keySet()) {
                if (loadedGiftMap.get(key).isSimilar(item)) {
                    return key;
                }
            }
        }
        return null;
    }

    public String getItemKey(ItemStack item) {
        if(isItem(item)) {
            for (String key : loadedGiftMap.keySet()) {
                if (loadedGiftMap.get(key).isSimilar(item)) {
                    return key;
                }
            }
        }
        return null;
    }

    public int getGiftLoveLevel(String giftKey) {
        if(loadedGiftMap.containsKey(giftKey)) {
            targetSection = itemData.getConfigurationSection(giftKey);
            return targetSection.getInt("LoveLevel");
        } else {
            return 0;
        }
    }
}
