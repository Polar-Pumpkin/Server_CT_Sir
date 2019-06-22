package org.serverct.sir.citylifemood.configuration;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.serverct.sir.citylifecore.enums.MessageType;
import org.serverct.sir.citylifecore.utils.LocaleUtil;
import org.serverct.sir.citylifemood.CityLifeMood;
import org.serverct.sir.citylifemood.enums.MoodChangeType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlayerDataManager {

    private static PlayerDataManager instance;

    public static PlayerDataManager getInstance() {
        if(instance == null) {
            instance = new PlayerDataManager();
        }
        return instance;
    }

    private File dataFile = new File(CityLifeMood.getInstance().getDataFolder() + File.separator + "Players.yml");
    @Getter private FileConfiguration data = YamlConfiguration.loadConfiguration(dataFile);

    private LocaleUtil locale = CityLifeMood.getInstance().getLocale();

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
        if(data.getKeys(false).contains(playerName)) {
            data.set(playerName, value);
            Bukkit.getPlayer(playerName).sendMessage(
                    locale.getMessage(MessageType.INFO, "Mood", "Restore")
                            .replace("%amount%", String.valueOf(Math.abs(value)))
                            .replace("%reason%", ChatColor.translateAlternateColorCodes('&', detectMoodChangeReason(MoodChangeType.RESPAWN, null)))
            );
        }
    }

    public void addMoodValue(String playerName, int value, MoodChangeType reasonType, String consumableDisplay) {
        if(data.getKeys(false).contains(playerName)) {
            data.set(playerName, getMoodValue(playerName) + value);
            sendMoodChangeMessage(Bukkit.getPlayer(playerName), value, detectMoodChangeReason(reasonType, consumableDisplay));
        }
    }

    public void addMoodValue(String playerName, int value, String reason) {
        if(data.getKeys(false).contains(playerName)) {
            data.set(playerName, getMoodValue(playerName) + value);
            sendMoodChangeMessage(Bukkit.getPlayer(playerName), value, reason);
        }
    }

    private void sendMoodChangeMessage(Player player, int value, String reason) {
        if(value > 0) {
            player.sendMessage(
                    locale.getMessage(MessageType.INFO, "Mood", "Increase")
                            .replace("%amount%", String.valueOf(value))
                            .replace("%reason%", ChatColor.translateAlternateColorCodes('&', reason))
            );
        } else {
            player.sendMessage(
                    locale.getMessage(MessageType.INFO, "Mood", "Decrease")
                            .replace("%amount%", String.valueOf(Math.abs(value)))
                            .replace("%reason%", ChatColor.translateAlternateColorCodes('&', reason))
            );
        }
    }

    private String detectMoodChangeReason(MoodChangeType reasonType, String consumableDisplay) {
        switch(reasonType) {
            case COMMAND:
                doSthList = new ArrayList<>(locale.getData().getStringList("Mood.Reason.Command"));
                random = new Random();
                return doSthList.get(random.nextInt(doSthList.size()));
            case DAMAGED:
                return locale.getData().getString("Mood.Reason.Damaged");
            case RESPAWN:
                return locale.getData().getString("Mood.Reason.Respawn");
            case CONSUMABLE:
                return locale.getData().getString("Mood.Reason.Consumables").replace("%item%", consumableDisplay);
            case COMMON:
            case SUNNY:
            case RAINY:
            case SNOWY:
                return locale.getData().getString("Mood.Reason.TimerTask." + reasonType.toString());
            default:
                return "(获取心情变动原因遇到未知错误.)";
        }
    }

}
