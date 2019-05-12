package org.serverct.sir.mood.configuration;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.serverct.sir.mood.MessageType;
import org.serverct.sir.mood.Mood;

import java.io.File;
import java.io.IOException;

public class PlayerData {

    private static PlayerData instance;

    public static PlayerData getInstance() {
        if(instance == null) {
            instance = new PlayerData();
        }
        return instance;
    }

    private File dataFile = new File(Mood.getInstance().getDataFolder() + File.separator + "players.yml");
    @Getter private FileConfiguration data = YamlConfiguration.loadConfiguration(dataFile);

    private int targetMoodValue;
    private Player targetPlayer;

    public void loadPlayerData() {
        if(!dataFile.exists()) {
            try {
                dataFile.createNewFile();
                data = YamlConfiguration.loadConfiguration(dataFile);
                Bukkit.getLogger().info("  > 未找到玩家数据文件, 已自动生成.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Bukkit.getLogger().info("  > 共加载 " + data.getKeys(false).size() + " 个玩家数据.");
    }

    public void addNewPlayer(String playerName) {
        data.set(playerName, 100);
    }

    public void save() {
        try {
            data.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getMoodValue(String playerName) {
        if(data.getKeys(false).contains(playerName)) {
            return data.getInt(playerName);
        }
        return -1;
    }

    public void addMoodValue(String playerName, int value) {
        targetMoodValue = getMoodValue(playerName);
        targetPlayer = Bukkit.getPlayer(playerName);
        if(data.getKeys(false).contains(playerName)) {
            data.set(playerName, targetMoodValue + value);
            if(value > 0) {
                targetPlayer.sendMessage(Language.getInstance().getMessage(MessageType.INFO, "Mood", "Increase").replace("%amount%", String.valueOf(value)));
            } else {
                targetPlayer.sendMessage(Language.getInstance().getMessage(MessageType.INFO, "Mood", "Decrease").replace("%amount%", String.valueOf(Math.abs(value))));
            }
        }
    }

}
