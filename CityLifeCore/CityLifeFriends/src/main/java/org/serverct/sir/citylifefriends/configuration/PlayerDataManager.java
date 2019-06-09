package org.serverct.sir.citylifefriends.configuration;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.serverct.sir.citylifecore.utils.CommonUtil;
import org.serverct.sir.citylifefriends.CityLifeFriends;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.HashMap;
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

    private File dataFolder = new File(CityLifeFriends.getINSTANCE().getDataFolder() + File.separator + "Players");

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
        try {
            data.save(new File(dataFolder.getAbsolutePath() + File.separator + playerName + ".yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
