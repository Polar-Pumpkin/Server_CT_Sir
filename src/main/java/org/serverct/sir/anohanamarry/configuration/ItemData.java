package org.serverct.sir.anohanamarry.configuration;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.serverct.sir.anohanamarry.ANOHANAMarry;
import org.serverct.sir.anohanamarry.configuration.LanguageData.Language;
import org.serverct.sir.anohanamarry.configuration.LanguageData.MessageType;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemData {

    private static ItemData itemDataClass;

    public static ItemData getInstance() {
        if(itemDataClass == null) {
            itemDataClass = new ItemData();
        }
        return itemDataClass;
    }

    @Getter private Map<String, ItemStack> loadedItemMap = new HashMap<>();
    @Getter private Map<String, ItemStack> loadedGiftMap = new HashMap<>();

    private File itemFile = new File(ANOHANAMarry.getINSTANCE().getDataFolder() + File.separator + "Items.yml");
    private FileConfiguration itemData = YamlConfiguration.loadConfiguration(itemFile);

    private ItemStack result;
    private ItemMeta resultMeta;
    private List<String> resultLore;

    private ConfigurationSection enchants;

    private ConfigurationSection targetSection;

    private ConfigurationSection newGiftSection;
    private ItemMeta newGiftMeta;
    private List<String> newGiftLore;
    private ConfigurationSection newGiftEnchantSection;
    private Map<Enchantment, Integer> newGiftEnchants;
    private Set<ItemFlag> newGiftItemFlags;
    private List<String> newGiftItemFlagList;

    private ItemStack targetItem;
    private int targetAmount;

    private String regEx = "\\d{1,4}";
    private Pattern pattern = Pattern.compile(regEx);
    private Matcher matcher;

    public void loadItemData() {
        if(!itemFile.exists() || !itemData.getKeys(false).contains("Ring") || !itemData.getKeys(false).contains("Bouquet") || !itemData.getKeys(false).contains("Certificate")) {
            ANOHANAMarry.getINSTANCE().saveResource("Items.yml", true);
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "  &e> &7未找到道具数据文件(或关键道具配置缺失), 已自动生成."));
        }
        loadItems();
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
        for(ItemStack value : loadedItemMap.values()) {
            if(value.isSimilar(item)) {
                return true;
            }
        }
        return false;
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

    public int getGiftLovePoint(String giftKey) {
        if(loadedGiftMap.containsKey(giftKey)) {
            targetSection = itemData.getConfigurationSection(giftKey);
            return targetSection.getInt("LovePoint");
        } else {
            return 0;
        }
    }

    public void saveGift(ItemStack item, String giftKey, int lovePoint) {
        newGiftSection = itemData.createSection(giftKey);
        newGiftMeta = item.getItemMeta();
        newGiftLore = new ArrayList<>();

        newGiftSection.set("Display", newGiftMeta.getDisplayName().replace("§", "&"));
        newGiftSection.set("Type", item.getType().toString());
        for(String lore : newGiftMeta.getLore()) {
            newGiftLore.add(lore.replace("§", "&"));
        }
        newGiftSection.set("Lore", newGiftLore);
        newGiftSection.set("LovePoint", lovePoint);
        if(newGiftMeta.hasEnchants()) {
            newGiftEnchantSection = newGiftSection.createSection("Enchants");
            newGiftEnchants = newGiftMeta.getEnchants();

            for(Enchantment enchantment : newGiftEnchants.keySet()) {
                newGiftEnchantSection.set(enchantment.toString(), newGiftEnchants.get(enchantment));
            }
        }
        newGiftItemFlags = newGiftMeta.getItemFlags();
        if(!newGiftItemFlags.isEmpty()) {
            newGiftItemFlagList = new ArrayList<>();

            for(ItemFlag itemFlag : newGiftItemFlags) {
                newGiftItemFlagList.add(itemFlag.name());
            }
            newGiftSection.set("ItemFlags", newGiftItemFlagList);
        }

        try {
            itemData.save(itemFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        loadedGiftMap.put(giftKey, buildItem(newGiftSection));
    }

    public void removeGift(String giftKey) {
        itemData.set(giftKey, null);
        try {
            itemData.save(itemFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        loadedGiftMap.remove(giftKey);
    }

    public void giveItem(String itemKey, Player target, String amount) {
        if(ItemData.getInstance().getLoadedItemMap().containsKey(itemKey)) {
            matcher = pattern.matcher(amount);

            if(matcher.find()) {
                targetItem = ItemData.getInstance().getLoadedItemMap().get(itemKey);
                targetAmount =Integer.valueOf(amount);
                if(targetAmount <= targetItem.getMaxStackSize()) {
                    targetItem.setAmount(targetAmount);
                    target.getInventory().addItem(targetItem);
                    target.sendMessage(
                            Language.getInstance().getMessage(MessageType.INFO, "Commands.Items.Success.Give")
                                    .replace("%player%", target.getName())
                                    .replace("%amount%", String.valueOf(targetAmount))
                                    .replace("%giftKey%", itemKey)
                                    .replace("%item%", targetItem.getItemMeta().getDisplayName())
                    );
                } else {
                    target.sendMessage(Language.getInstance().getMessage(MessageType.WARN, "Commands.Items.OutOfMaxStackSize"));
                }
            } else {
                target.sendMessage(Language.getInstance().getMessage(MessageType.WARN, "Commands.Items.NotANumber").replace("%param%", "数量"));
            }
        } else if(ItemData.getInstance().getLoadedGiftMap().containsKey(itemKey)) {
            matcher = pattern.matcher(amount);

            if(matcher.find()) {
                targetItem = ItemData.getInstance().getLoadedGiftMap().get(itemKey);
                targetAmount =Integer.valueOf(amount);
                if(targetAmount <= targetItem.getMaxStackSize()) {
                    targetItem.setAmount(targetAmount);
                    target.getInventory().addItem(targetItem);
                    target.sendMessage(
                            Language.getInstance().getMessage(MessageType.INFO, "Commands.Items.Success.Give")
                                    .replace("%player%", target.getName())
                                    .replace("%amount%", String.valueOf(targetAmount))
                                    .replace("%giftKey%", itemKey)
                                    .replace("%item%", targetItem.getItemMeta().getDisplayName())
                    );
                } else {
                    target.sendMessage(Language.getInstance().getMessage(MessageType.WARN, "Commands.Items.OutOfMaxStackSize"));
                }
            } else {
                target.sendMessage(Language.getInstance().getMessage(MessageType.WARN, "Commands.Items.NotANumber").replace("%param%", "数量"));
            }
        } else {
            target.sendMessage(Language.getInstance().getMessage(MessageType.WARN, "Commands.Unknown.Item"));
        }
    }
}
