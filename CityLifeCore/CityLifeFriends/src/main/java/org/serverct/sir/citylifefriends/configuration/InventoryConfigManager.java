package org.serverct.sir.citylifefriends.configuration;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.serverct.sir.citylifecore.data.InventoryGui;
import org.serverct.sir.citylifecore.data.InventoryItem;
import org.serverct.sir.citylifecore.manager.InventoryManager;
import org.serverct.sir.citylifecore.utils.InventoryUtil;
import org.serverct.sir.citylifecore.utils.ItemStackUtil;
import org.serverct.sir.citylifefriends.CityLifeFriends;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class InventoryConfigManager {

    private static InventoryConfigManager instance;

    public static InventoryConfigManager getInstance() {
        if(instance == null) {
            instance = new InventoryConfigManager();
        }
        return instance;
    }

    private InventoryManager inventoryManager = CityLifeFriends.getINSTANCE().getCoreApi().getInventoryAPI();
    private InventoryUtil inventoryUtil = CityLifeFriends.getINSTANCE().getCoreApi().getInventoryUtil();

    private File dataFolder = new File(CityLifeFriends.getINSTANCE().getDataFolder() + File.separator + "Guis");
    private File friendsDataFile = new File(dataFolder.getAbsolutePath() + File.separator + "Friends.yml");
    private File applicationsDataFile = new File(dataFolder.getAbsolutePath() + File.separator + "Applications.yml");

    private InventoryItem targetFriendItem;
    private ItemStack targetItem;
    private ItemMeta targetItemMeta;
    private SkullMeta targetSkullMeta;

    private InventoryGui targetInventory;
    private InventoryGui resultInventory;
    private List<InventoryItem> friendItems;

    public void loadGuis() {
        inventoryManager.loadInventory("FRIENDS_Friends", YamlConfiguration.loadConfiguration(friendsDataFile));
    }

    public InventoryGui appleFriendsToGui(List<String> friends) {
        targetInventory = inventoryManager.getLoadedInventory().get("FRIENDS_Friends");
        friendItems = new ArrayList<>();

        for(String friendName : friends) {
            friendItems.add(applyFriendsToItem(friendName));
        }

        if(getFriendPlaceholderIndex(targetInventory) >= 0) {
            resultInventory = inventoryUtil.replaceMultiInventoryItem(targetInventory, getFriendPlaceholder(targetInventory), friendItems);
        }

        return resultInventory;
    }

    public int getFriendPlaceholderIndex(InventoryGui inventory) {
        for(int index = 0; index <= inventory.getItems().size(); index++) {
            if(inventory.getItems().get(index).getId().equalsIgnoreCase("FriendsPlaceholder")) {
                return index;
            }
        }
        return -1;
    }

    public InventoryItem getFriendPlaceholder(InventoryGui inventory) {
        for(InventoryItem item : inventory.getItems()) {
            if(item.getId().equalsIgnoreCase("FriendsPlaceholder")) {
                return item;
            }
        }
        return null;
    }

    public InventoryItem applyFriendsToItem(String friendName) {
        targetFriendItem = ItemStackUtil.buildInventoryItem(CityLifeFriends.getINSTANCE().getConfig().getConfigurationSection("FriendItem"));
        targetItem = targetFriendItem.getItem();

        if(targetItem.hasItemMeta()) {
            targetItemMeta = targetItem.getItemMeta();

            if(targetItem.getTypeId() == 397 && targetItem.getDurability() == (short) 3) {
                targetSkullMeta = (SkullMeta) targetItemMeta;

                targetSkullMeta.setOwner(friendName);
                targetItem.setItemMeta(targetSkullMeta);
            }

            targetItemMeta.setDisplayName(targetItemMeta.getDisplayName().replace("%friend%", friendName));
            targetItemMeta.getLore().replaceAll(s -> s.replace("%friend%", friendName));

            targetItem.setItemMeta(targetItemMeta);
        }

        targetFriendItem.setItem(targetItem);
        return targetFriendItem;
    }

}
