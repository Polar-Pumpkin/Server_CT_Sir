package org.serverct.sir.citylifefriends.configuration;

import org.bukkit.Material;
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
import java.util.HashMap;
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

    private InventoryItem friendItem;
    private Map<String, ItemStack> resultFriendItemStacks;
    private Map<String, InventoryItem> resultFriendInventoryItem;
    private ItemStack friendItemStack;
    private ItemMeta friendItemMeta;
    private SkullMeta friendSkullMeta;
    private List<Integer> friendItemPositionList;
    private List<Integer> cacheFriendItemPositionList;
    private String targetFriendName;
    private List<Integer> targetFriendPosition;

    private InventoryGui targetInventory;
    private List<InventoryItem> friendItems;

    private List<InventoryItem> inventoryItems;

    public void loadGuis() {
        if(!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        if(!friendsDataFile.exists()) {
            CityLifeFriends.getINSTANCE().saveResource("Guis/Friends.yml", false);
        }
        inventoryManager.loadInventory("FRIENDS_Friends", YamlConfiguration.loadConfiguration(friendsDataFile));
    }

    public InventoryGui appleFriendsToGui(Map<String, Long> friends) {
        targetInventory = inventoryManager.getLoadedInventory().get("FRIENDS_Friends");

        if(!friends.isEmpty()) {
            return inventoryUtil.rebuildInventory(applyFriendsToInventory(targetInventory, friends));
        }

        return targetInventory;
    }

    public int getFriendPlaceholderIndex(InventoryGui inventory) {
        inventoryItems = inventory.getItems();
        for(int index = 0; index <= inventoryItems.size() - 1; index++) {
            if(inventoryItems.get(index).getId().equalsIgnoreCase("FriendsPlaceholder")) {
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

    public InventoryGui applyFriendsToInventory(InventoryGui inventory, Map<String, Long> friends) {
        friendItem = ItemStackUtil.buildInventoryItem(CityLifeFriends.getINSTANCE().getConfig().getConfigurationSection("FriendItem"));
        resultFriendItemStacks = new HashMap<>();

        if(friendItem != null) {
            friendItemStack = friendItem.getItem();

            if(friendItemStack != null && friendItemStack.getType() != Material.AIR) {
                if(friendItemStack.hasItemMeta()) {
                    friendItemMeta = friendItemStack.getItemMeta();

                    for(String friendName : friends.keySet()) {
                        if(friendItemStack.getTypeId() == 397 && friendItemStack.getDurability() == (short) 3) {
                            friendSkullMeta = (SkullMeta) friendItemMeta;

                            friendSkullMeta.setOwner(friendName);
                            friendItemStack.setItemMeta(friendSkullMeta);
                        }

                        friendItemMeta.setDisplayName(friendItemMeta.getDisplayName().replace("%friend%", friendName));
                        friendItemMeta.getLore().replaceAll(s -> s.replace("%friend%", friendName));
                        friendItemMeta.setDisplayName(friendItemMeta.getDisplayName().replace("%addTime%", TimeUtil.getDescriptionTimeFromTimestamp(friends.get(friendName))));
                        friendItemMeta.getLore().replaceAll(s -> s.replace("%addTime%", TimeUtil.getDescriptionTimeFromTimestamp(friends.get(friendName))));

                        friendItemStack.setItemMeta(friendItemMeta);
                        resultFriendItemStacks.put(friendName, friendItemStack);
                    }
                }
            }
        }

        if(getFriendPlaceholderIndex(inventory) >= 0) {
            friendItemPositionList = getFriendPlaceholder(inventory).getPositionList();
            cacheFriendItemPositionList = new ArrayList<>(friendItemPositionList);
            resultFriendInventoryItem = new HashMap<>();

            if(cacheFriendItemPositionList.size() >= resultFriendItemStacks.size()) {
                for(int index = 0; index <= resultFriendItemStacks.size(); index++) {
                    targetFriendName = String.valueOf(resultFriendItemStacks.keySet().toArray()[index]);
                    targetFriendPosition = new ArrayList<>();
                    targetFriendPosition.add(friendItemPositionList.get(index));

                    resultFriendInventoryItem.put(
                            targetFriendName,
                            new InventoryItem(
                                    targetFriendName,
                                    resultFriendItemStacks.get(targetFriendName),
                                    targetFriendPosition,
                                    friendItem.getActions(),
                                    friendItem.isKeepOpen(),
                                    friendItem.getPrice(),
                                    friendItem.getPoint()
                            )
                    );

                    friendItemPositionList.remove(index);
                }

                friendItem.setPositionList(friendItemPositionList);
                friendItems.set(getFriendPlaceholderIndex(inventory), friendItem);
                friendItems = inventory.getItems();
                friendItems.addAll(resultFriendInventoryItem.values());
                inventory.setItems(friendItems);
            }
        }
        return inventory;
    }

}
