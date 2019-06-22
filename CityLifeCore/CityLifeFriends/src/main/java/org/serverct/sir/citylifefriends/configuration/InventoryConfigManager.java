package org.serverct.sir.citylifefriends.configuration;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.serverct.sir.citylifecore.data.InventoryGui;
import org.serverct.sir.citylifecore.data.InventoryItem;
import org.serverct.sir.citylifecore.manager.InventoryManager;
import org.serverct.sir.citylifecore.utils.InventoryUtil;
import org.serverct.sir.citylifecore.utils.ItemStackUtil;
import org.serverct.sir.citylifecore.utils.LocaleUtil;
import org.serverct.sir.citylifefriends.CityLifeFriends;
import org.serverct.sir.citylifefriends.util.TimeUtil;

import java.io.File;
import java.util.*;

public class InventoryConfigManager {

    private static InventoryConfigManager instance;

    public static InventoryConfigManager getInstance() {
        if(instance == null) {
            instance = new InventoryConfigManager();
        }
        return instance;
    }

    private LocaleUtil locale = CityLifeFriends.getInstance().getLocale();

    private InventoryManager inventoryManager = CityLifeFriends.getInstance().getCoreApi().getInventoryAPI();
    private InventoryUtil inventoryUtil = CityLifeFriends.getInstance().getCoreApi().getInventoryUtil();

    private File dataFolder = new File(CityLifeFriends.getInstance().getDataFolder() + File.separator + "Guis");
    private File friendsDataFile = new File(dataFolder.getAbsolutePath() + File.separator + "Friends.yml");
    private File applicationsDataFile = new File(dataFolder.getAbsolutePath() + File.separator + "Applications.yml");

    private Map<String, ItemStack> friendsItemStackMap;
    private Map<String, InventoryItem> friendsInventoryItemMap;

    public void loadGuis() {
        if(!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        if(!friendsDataFile.exists()) {
            CityLifeFriends.getInstance().saveResource("Guis/Friends.yml", false);
        }
        inventoryManager.loadInventory("FRIENDS_Friends", YamlConfiguration.loadConfiguration(friendsDataFile), CityLifeFriends.getInstance());
    }

    public InventoryGui applyFriendsToGui(Map<String, Long> friends) {
        locale.debug("  > 执行 applyFriendsToGui 方法.");
        InventoryGui targetInventory = inventoryManager.getLoadedInventory().get("FRIENDS_Friends");
        locale.debug("    > 已获取主界面 InventoryGui 对象.");

        if(!friends.isEmpty()) {
            locale.debug("    > 好友列表非空, 开始根据好友信息构建主菜单.");
            return inventoryUtil.rebuildInventory(applyFriendsToInventory(targetInventory, friends));
        }

        locale.debug("    > 好友列表为空, 返回空主菜单.");
        return targetInventory;
    }

    public int getFriendPlaceholderIndex(InventoryGui inventory) {
        for(InventoryItem item : inventory.getItems()) {
            if(item.getId().equalsIgnoreCase("FriendsPlaceholder")) {
                return inventory.getItems().indexOf(item);
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
        locale.debug("      > 执行 applyFriendsToInventory 方法.");
        InventoryItem friendItem = ItemStackUtil.buildInventoryItem(CityLifeFriends.getInstance().getConfig().getConfigurationSection("FriendItem"), CityLifeFriends.getInstance());
        locale.debug("        > 已根据配置文件信息构建好友显示物品(InventoryItem).");
        List<InventoryItem> guiItems = inventory.getItems();
        locale.debug("        > 已获取空主菜单所有物品(InventoryItem).");

        friendsItemStackMap = constructFriendsItemStack(friendItem, friends);
        locale.debug("        > 已根据好友信息构建好友显示物品(ItemStack).");
        friendsInventoryItemMap = convertFriendsInventoryItem(friendItem, Objects.requireNonNull(friendsItemStackMap));
        locale.debug("        > 已将好友显示物品列表转换为 InventoryItem.");

        if(inventory.containItem("FriendsPlaceholder")) {
            locale.debug("        > Gui 包含好友显示占位符.");
            InventoryItem friendsPlaceholder = getFriendPlaceholder(inventory);
            locale.debug("        > 已获取好友显示占位符 InventoryItem 对象.");
            List<Integer> friendsPlaceholderPositionList = friendsPlaceholder.getPositionList();
            locale.debug("        > 已获取好友显示占位符位置列表: " + friendsPlaceholderPositionList.toString());

            if(friendsPlaceholderPositionList.size() >= friendsItemStackMap.size()) {
                locale.debug("        > 好友占位符位置数量(" + friendsPlaceholderPositionList.size() + ")不小于好友数量(" + friendsItemStackMap.size() + ").");
                locale.debug("        > 开始循环将好友显示物品写入好友显示占位符.");
                for(InventoryItem target : friendsInventoryItemMap.values()) {
                    locale.debug("          > 已获取好友 " + target.getId() + " 的 InventoryItem 对象.");
                    int overwrittenPosition = friendsPlaceholderPositionList.get(0);
                    target.setPositionList(new ArrayList<>(Collections.singletonList(overwrittenPosition)));
                    locale.debug("          > 已设置好友显示物品位置: " + overwrittenPosition);
                    guiItems.add(target);
                    locale.debug("          > 已将好友显示物品添加入主菜单物品列表中.");
                    friendsPlaceholderPositionList.remove(0);
                    locale.debug("          > 已从好友显示占位符位置列表中删除已覆写位置: " + overwrittenPosition);
                }
            }

            friendsPlaceholder.setPositionList(friendsPlaceholderPositionList);
            locale.debug("        > 已更新好友显示物品占位符的位置列表.");
            guiItems.set(getFriendPlaceholderIndex(inventory), friendsPlaceholder);
            locale.debug("        > 已更新主菜单物品列表中的好友显示物品占位符.");
        }
        inventory.setItems(guiItems);
        locale.debug("        > 已更新主菜单的物品列表.");
        locale.debug("      > applyFriendsToInventory 方法执行结束.");
        return inventory;
    }

    private Map<String, ItemStack> constructFriendsItemStack(InventoryItem friendItem, Map<String, Long> friends) {
        locale.debug("          > 执行 constructFriendsItemStack 方法.");
        friendsItemStackMap = new HashMap<>();

        if(friendItem != null) {
            locale.debug("            > 获取到的好友显示物品(InventoryItem)有效.");
            ItemStack friendItemStack = friendItem.getItem();
            locale.debug("            > 已获取好友显示物品(ItemStack).");

            if(friendItemStack != null && friendItemStack.getType() != Material.AIR) {
                locale.debug("            > 好友显示物品(ItemStack)有效.");
                locale.debug("            > 开始根据好友信息构建好友显示物品(ItemStack).");
                for(String friendName : PlayerDataManager.getInstance().convertDataToPlaceholder(friends).keySet()) {
                    String description = TimeUtil.getDescriptionTimeFromTimestamp(friends.get(friendName));

                    friendsItemStackMap.put(friendName, PlayerDataManager.getInstance().replaceFriendItemVariable(friendItemStack, friendName, description));
                    locale.debug("              > 好友 " + friendName + "(添加时间: " + description + ") 构建成功, 已放入 HashMap.");
                }
            }
        }
        locale.debug("          > constructFriendsItemStack 方法执行结束.");
        return friendsItemStackMap;
    }

    private Map<String, InventoryItem> convertFriendsInventoryItem(InventoryItem friendItem, Map<String, ItemStack> friendItemStacks) {
        locale.debug("          > 执行 convertFriendsInventoryItem 方法.");
        friendsInventoryItemMap = new HashMap<>();

        if(friendItem != null) {
            locale.debug("            > 获取到的好友显示物品(InventoryItem)有效.");

            locale.debug("            > 开始根据传入 HashMap 构建好友显示物品(InventoryItem).");
            for(String friendName : friendItemStacks.keySet()) {
                friendsInventoryItemMap.put(
                        friendName,
                        new InventoryItem(
                                friendName,
                                friendItemStacks.get(friendName),
                                new ArrayList<>(),
                                friendItem.getClicks(),
                                friendItem.isKeepOpen(),
                                friendItem.getPrice(),
                                friendItem.getPoint()
                        )
                );
                locale.debug("              > 好友 " + friendName + " 构建成功, 已放入 HashMap.");
            }
        }
        locale.debug("          > convertFriendsInventoryItem 方法执行结束.");
        return friendsInventoryItemMap;
    }

}
