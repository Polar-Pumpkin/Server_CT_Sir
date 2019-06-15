package org.serverct.sir.citylifefriends.configuration;

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
import org.serverct.sir.citylifefriends.util.TimeUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        if(!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        if(!friendsDataFile.exists()) {
            CityLifeFriends.getINSTANCE().saveResource("Guis/Friends.yml", false);
        }
        inventoryManager.loadInventory("FRIENDS_Friends", YamlConfiguration.loadConfiguration(friendsDataFile));
    }

    public void isClickedFriend() {

    }

    public InventoryGui appleFriendsToGui(Map<String, Long> friends) {
        targetInventory = inventoryManager.getLoadedInventory().get("FRIENDS_Friends");

        if(!friends.isEmpty()) {
            friendItems = new ArrayList<>();

            for(String friendName : friends.keySet()) {
                friendItems.add(applyFriendsToItem(friendName, TimeUtil.getDescriptionTimeFromTimestamp(friends.get(friendName))));
            }

            if(getFriendPlaceholderIndex(targetInventory) >= 0) {
                resultInventory = inventoryUtil.replaceMultiInventoryItem(targetInventory, getFriendPlaceholder(targetInventory), friendItems);
            }

            return resultInventory;
        }

        return targetInventory;
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

    public InventoryItem applyFriendsToItem(String friendName, String addTime) {
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
            targetItemMeta.setDisplayName(targetItemMeta.getDisplayName().replace("%addTime%", addTime));
            targetItemMeta.getLore().replaceAll(s -> s.replace("%addTime%", addTime));

            targetItem.setItemMeta(targetItemMeta);
        }

        targetFriendItem.setItem(targetItem);
        return targetFriendItem;
    }

}
