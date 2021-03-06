package org.serverct.sir.citylifefriends.configuration;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.serverct.sir.citylifecore.utils.CommonUtil;
import org.serverct.sir.citylifefriends.CityLifeFriends;
import org.serverct.sir.citylifefriends.util.TimeUtil;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerDataManager {

    private static PlayerDataManager instance;

    public static PlayerDataManager getInstance() {
        if(instance == null) {
            instance = new PlayerDataManager();
        }
        return instance;
    }

    @Getter private Map<String, FileConfiguration> loadedPlayers = new HashMap<>();

    private File dataFolder = new File(CityLifeFriends.getInstance().getDataFolder() + File.separator + "Players");

    private File cacheDataFile;

    private FileConfiguration targetPlayerData;
    private ConfigurationSection targetSection;
    private Map<String, Long> targetList;
    private Map<String, String> targetPlaceholders;

    public void loadPlayerData() {
        if(!dataFolder.exists()) {
            dataFolder.mkdirs();
            Bukkit.getLogger().info("  > 未找到玩家数据文件夹, 已自动生成.");
        } else {
            File[] dataFiles = dataFolder.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return pathname.getName().endsWith(".yml");
                }
            });

            if(dataFiles != null && dataFiles.length != 0) {
                for(File dataFile : dataFiles) {
                    loadedPlayers.put(CommonUtil.getFileNameNoEx(dataFile.getName()), YamlConfiguration.loadConfiguration(dataFile));
                }
                Bukkit.getLogger().info("  > 共加载 " + loadedPlayers.size() + " 个玩家数据.");
            } else {
                Bukkit.getLogger().info("  > 无玩家数据可供加载.");
            }
        }
    }

    public void save(String playerName, FileConfiguration data) {
        loadedPlayers.put(playerName, data);

        cacheDataFile = new File(dataFolder.getAbsolutePath() + File.separator + playerName + ".yml");
        try {
            data.save(cacheDataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean hasDataFile(String playerName) {
        File[] dataFiles = dataFolder.listFiles(pathname -> pathname.getName().endsWith(".yml"));

        if(dataFiles != null && dataFiles.length != 0) {
            for(File dataFile : dataFiles) {
                if(CommonUtil.getFileNameNoEx(dataFile.getName()).equals(playerName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void checkData(String playerName) {
        if(!loadedPlayers.containsKey(playerName)) {
            cacheDataFile = new File(dataFolder.getAbsolutePath() + File.separator + playerName + ".yml");
            save(playerName, YamlConfiguration.loadConfiguration(cacheDataFile));
        }
    }

    public void updateData(String playerName) {
        if(hasDataFile(playerName)) {
            cacheDataFile = new File(dataFolder.getAbsolutePath() + File.separator + playerName + ".yml");
            loadedPlayers.put(playerName, YamlConfiguration.loadConfiguration(cacheDataFile));
        }
    }

    public Map<String, Long> getList(String playerName, boolean isFriendList) {
        checkData(playerName);

        targetPlayerData = loadedPlayers.get(playerName);
        targetSection = targetPlayerData.getConfigurationSection(isFriendList ? "Friends" : "Applications");
        targetList = new HashMap<>();

        for(String key : targetSection.getKeys(false)) {
            targetList.put(key, targetSection.getLong(key));
        }
        return targetList;
    }

    public Map<String, String> getFormattedFriendList(String playerName) {
        checkData(playerName);

        targetList = getList(playerName, true);
        targetPlaceholders = new HashMap<>();

        for(String friendName : targetList.keySet()) {
            targetPlaceholders.put(friendName, TimeUtil.getDescriptionTimeFromTimestamp(targetList.get(friendName)));
        }

        return targetPlaceholders;
    }

    public void addPlayerToList(String targetName, String playerName, boolean isFriendList) {
        checkData(targetName);

        targetPlayerData = loadedPlayers.get(targetName);
        targetSection = targetPlayerData.getConfigurationSection(isFriendList ? "Friends" : "Applications");

        targetSection.set(playerName, System.currentTimeMillis());

        save(targetName, targetPlayerData);
    }

    public void removePlayerFromList(String targetName, String playerName, boolean isFriendList) {
        checkData(targetName);

        targetPlayerData = loadedPlayers.get(targetName);
        targetSection = targetPlayerData.getConfigurationSection(isFriendList ? "Friends" : "Applications");

        if(targetSection.contains(playerName)) {
            targetSection.set(playerName, null);

            save(targetName, targetPlayerData);
        }
    }

    public Map<String, String> convertDataToPlaceholder(Map<String, Long> friends) {
        targetPlaceholders = new HashMap<>();

        for(String friendName : friends.keySet()) {
            targetPlaceholders.put(friendName, TimeUtil.getDescriptionTimeFromTimestamp(targetList.get(friendName)));
        }

        return targetPlaceholders;
    }

    public ItemStack replaceFriendItemVariable(ItemStack item, String friendName, String description) {
        ItemMeta meta = item.getItemMeta();
        String displayName = meta.getDisplayName();
        List<String> lore = meta.getLore();
        meta.setDisplayName(displayName.replace("%friend%", friendName));
        meta.setDisplayName(displayName.replace("%addtime%", description));
        lore.replaceAll(s -> s.replace("%friend%", friendName));
        lore.replaceAll(s -> s.replace("%addtime%", description));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
}
