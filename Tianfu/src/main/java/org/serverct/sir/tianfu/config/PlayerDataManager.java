package org.serverct.sir.tianfu.config;

import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.serverct.sir.tianfu.Tianfu;
import org.serverct.sir.tianfu.data.PlayerData;
import org.serverct.sir.tianfu.enums.MessageType;
import org.serverct.sir.tianfu.enums.TalentType;
import org.serverct.sir.tianfu.util.CommonUtil;
import org.serverct.sir.tianfu.util.LocaleUtil;

import java.io.File;
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
    private LocaleUtil locale = Tianfu.getInstance().getLocale();

    @Getter private Map<String, PlayerData> playerDataMap = new HashMap<>();
    private File dataFolder = new File(Tianfu.getInstance().getDataFolder() + File.separator + "Players");

    public void load() {
        if(!dataFolder.exists()) {
            dataFolder.mkdirs();
            locale.buildMessage(Tianfu.getInstance().getLocaleKey(), MessageType.WARN, "&7未找到玩家数据文件夹, 已自动生成.");
        } else {
            File[] playerDataFiles = dataFolder.listFiles(pathname -> {
                String fileName = pathname.getName();
                return fileName.endsWith(".yml");
            });

            if(playerDataFiles != null && playerDataFiles.length >= 1) {
                for(File dataFile : playerDataFiles) {
                    playerDataMap.put(CommonUtil.getNoExFileName(dataFile.getName()), new PlayerData(dataFile));
                }
                locale.buildMessage(Tianfu.getInstance().getLocaleKey(), MessageType.INFO, "&7共加载 &c" + playerDataMap.size() + " &7个玩家数据.");
            } else {
                locale.buildMessage(Tianfu.getInstance().getLocaleKey(), MessageType.WARN, "&7无玩家数据可供加载.");
            }
        }
    }

    public PlayerData getPlayerData(String playerName) {
        if(!playerDataMap.containsKey(playerName)) {
            createPlayer(playerName);
        }
        return playerDataMap.get(playerName);
    }

    public void addPlayerData(String playerName, TalentType type, int amount) {
        if(!playerDataMap.containsKey(playerName)) {
            createPlayer(playerName);
        }
        PlayerData playerData = playerDataMap.get(playerName);
        Map<TalentType, Integer> levels = playerData.getLevel();

        levels.put(type, levels.get(type) + 1);
        playerData.setLevel(levels);

        playerDataMap.put(playerName, playerData);
        savePlayer(playerName);
    }

    public void savePlayer(String playerName) {
        if(playerDataMap.containsKey(playerName)) {
            File dataFile = new File(dataFolder.getAbsolutePath() + File.separator + playerName + ".yml");
            FileConfiguration data = YamlConfiguration.loadConfiguration(dataFile);
            PlayerData playerData = playerDataMap.get(playerName);
            ConfigurationSection section = data.getConfigurationSection("Talents");

            for(String talentType : section.getKeys(false)) {
                section.set(talentType, playerData.getLevel().get(TalentType.valueOf(talentType.toUpperCase())));
            }

            try {
                data.save(dataFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            createPlayer(playerName);
        }
    }

    public void createPlayer(String playerName) {
        File dataFile = new File(dataFolder.getAbsolutePath() + File.separator + playerName + ".yml");
        FileConfiguration data = YamlConfiguration.loadConfiguration(dataFile);
        ConfigurationSection section = data.createSection("Talents");
        section.set(TalentType.DAMAGE.toString(), 0);
        section.set(TalentType.HEALTHREFILL.toString(), 0);
        section.set(TalentType.REGEN.toString(), 0);
        section.set(TalentType.IMPRISONMENT.toString(), 0);
        section.set(TalentType.LIGHTNING.toString(), 0);

        try {
            data.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        playerDataMap.put(playerName, new PlayerData(dataFile));
    }
}
