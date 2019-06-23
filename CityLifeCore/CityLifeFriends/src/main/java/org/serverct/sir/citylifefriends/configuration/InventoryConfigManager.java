package org.serverct.sir.citylifefriends.configuration;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.serverct.sir.citylifecore.data.CLID;
import org.serverct.sir.citylifecore.data.InventoryGui;
import org.serverct.sir.citylifecore.data.InventoryItem;
import org.serverct.sir.citylifecore.manager.InventoryManager;
import org.serverct.sir.citylifecore.utils.InventoryUtil;
import org.serverct.sir.citylifecore.utils.LocaleUtil;
import org.serverct.sir.citylifefriends.CityLifeFriends;
import org.serverct.sir.citylifefriends.util.FriendUtil;

import java.io.File;
import java.util.Map;

public class InventoryConfigManager {

    private static InventoryConfigManager instance;

    public static InventoryConfigManager getInstance() {
        if(instance == null) {
            instance = new InventoryConfigManager();
        }
        return instance;
    }

    private LocaleUtil locale = CityLifeFriends.getInstance().getLocale();
    private Plugin cityLifeFriends = CityLifeFriends.getInstance();
    private InventoryManager inventoryManager = CityLifeFriends.getInstance().getCoreApi().getInventoryAPI();
    private InventoryUtil inventoryUtil = CityLifeFriends.getInstance().getCoreApi().getInventoryUtil();

    private File dataFolder = new File(CityLifeFriends.getInstance().getDataFolder() + File.separator + "Guis");
    private File friendsDataFile = new File(dataFolder.getAbsolutePath() + File.separator + "Friends.yml");
    private FileConfiguration friendsData = YamlConfiguration.loadConfiguration(friendsDataFile);
    private File applicationsDataFile = new File(dataFolder.getAbsolutePath() + File.separator + "Applications.yml");

    public void loadGuis() {
        if(!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        if(!friendsDataFile.exists()) {
            CityLifeFriends.getInstance().saveResource("Guis/Friends.yml", false);
        }
        inventoryManager.loadInventory(new CLID(friendsData.getName(), cityLifeFriends), friendsData, cityLifeFriends);
    }

    public InventoryGui applyFriendsToGui(Map<String, Long> friends) {
        locale.debug("  > 执行 applyFriendsToGui 方法.");
        InventoryGui targetInventory = new InventoryGui(new CLID(friendsData.getName(), cityLifeFriends), friendsData, cityLifeFriends);
        locale.debug("    > 已获取主界面 InventoryGui 对象.");

        if(!friends.isEmpty()) {
            locale.debug("    > 好友列表非空, 开始根据好友信息构建主菜单.");
            return inventoryUtil.rebuildInventory(FriendUtil.getInstance().applyFriendsToInventory(targetInventory, friends));
        }

        locale.debug("    > 好友列表为空, 返回空主菜单.");
        return targetInventory;
    }

    public InventoryItem getFriendPlaceholder(InventoryGui inventory) {
        return inventory.getItem(new CLID("FriendsPlaceholder", inventory));
    }
}
