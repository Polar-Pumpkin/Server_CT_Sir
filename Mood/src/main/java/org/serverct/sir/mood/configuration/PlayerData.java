package org.serverct.sir.mood.configuration;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.serverct.sir.mood.MessageType;
import org.serverct.sir.mood.Mood;
import org.serverct.sir.mood.MoodChangeType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    private String targetReason;

    private Random random;
    private List<String> doSthList;

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

    public void setMoodValue(String playerName, int value) {
        data.set(playerName, value);
    }

    public void addMoodValue(String playerName, int value, MoodChangeType reasonType, String consumableDisplay) {
        targetMoodValue = getMoodValue(playerName);
        targetPlayer = Bukkit.getPlayer(playerName);

        switch(reasonType) {
            case COMMAND:
                doSthList = new ArrayList<>(Language.getInstance().getData().getStringList("Mood.Reason.Command"));
                random = new Random();
                targetReason = doSthList.get(random.nextInt(doSthList.size() + 1));
                break;
            case DAMAGED:
                targetReason = Language.getInstance().getData().getString("Mood.Reason.Damaged");
                break;
            case RESPAWN:
                targetReason = Language.getInstance().getData().getString("Mood.Reason.Respawn");
                break;
            case CONSUMABLE:
                targetReason = Language.getInstance().getData().getString("Mood.Reason.Consumables").replace("%item%", consumableDisplay);
                break;
            case COMMON:
            case SUNNY:
            case RAINY:
            case SNOWY:
                targetReason = Language.getInstance().getData().getString("Mood.Reason.TimerTask." + reasonType.toString());
                break;
            default:
                targetReason = "(获取心情变动原因遇到未知错误.)";
                break;
        }

        if(data.getKeys(false).contains(playerName)) {
            data.set(playerName, targetMoodValue + value);
            if(value > 0) {
                targetPlayer.sendMessage(
                        Language.getInstance().getMessage(MessageType.INFO, "Mood", "Increase")
                                .replace("%amount%", String.valueOf(value))
                                .replace("%reason%", ChatColor.translateAlternateColorCodes('&', targetReason))
                );
            } else {
                targetPlayer.sendMessage(
                        Language.getInstance().getMessage(MessageType.INFO, "Mood", "Decrease")
                                .replace("%amount%", String.valueOf(Math.abs(value)))
                                .replace("%reason%", ChatColor.translateAlternateColorCodes('&', targetReason))
                );
            }
        }
    }

}
