package org.serverct.sir.mood.configuration;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.serverct.sir.mood.Mood;
import org.serverct.sir.mood.MoodUpdateTaskType;
import org.serverct.sir.mood.Punishment;
import org.serverct.sir.mood.PunishmentType;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Config {

    private static Config instance;

    public static Config getInstance() {
        if(instance == null) {
            instance = new Config();
        }
        return instance;
    }

    private File dataFile = new File(Mood.getInstance().getDataFolder() + File.separator + "config.yml");
    @Getter private FileConfiguration data = YamlConfiguration.loadConfiguration(dataFile);

    @Getter private Map<Integer, Map<Integer, Punishment>> punishmentMap = new HashMap<>();
    private Map<Integer, Punishment> cachePunishmentMap = new HashMap<>();
    private int counter = 1;

    private ConfigurationSection punishmentSection;
    private String punishmentConfigText;
    private String[] punishmentConfigList;
    private Punishment resultPunishment;

    private String[] punishmentData;
    private String[] punishmentDetail;

    private PotionEffectType cachePotionEffectType;
    private int cachePotionEffectLevel;
    private PotionEffect cachePotionEffect;

    public void loadConfig() {
        if(!dataFile.exists()) {
            Mood.getInstance().saveDefaultConfig();
            data = Mood.getInstance().getConfig();
            Bukkit.getLogger().info("  > 未找到配置文件, 已自动生成.");
        }
        loadPunishment();
        Bukkit.getLogger().info("  > 已加载配置文件.");
    }

    private void loadPunishment() {
        punishmentSection = data.getConfigurationSection("Punishment");

        for(String section : punishmentSection.getKeys(false)) {
            punishmentConfigText = punishmentSection.getString(section);

            if(punishmentConfigText.contains(";")) {
                punishmentConfigList = punishmentConfigText.split(";");

                for(String msg : punishmentConfigList) {
                    System.out.println(msg);
                }

                if(!cachePunishmentMap.isEmpty()) {
                    cachePunishmentMap.clear();
                }

                for(String configText : punishmentConfigList) {
                    cachePunishmentMap.put(counter, catchPunishment(configText));
                    counter++;
                }
                punishmentMap.put(Integer.valueOf(section), cachePunishmentMap);
            } else {
                if(!cachePunishmentMap.isEmpty()) {
                    cachePunishmentMap.clear();
                }

                cachePunishmentMap.put(1, catchPunishment(punishmentConfigText));
                punishmentMap.put(Integer.valueOf(section), cachePunishmentMap);
            }
        }
        Bukkit.getLogger().info("  > 共加载 " + punishmentMap.size() + " 个惩罚配置.");
    }

    private Punishment catchPunishment(String configText) {
        punishmentData = configText.split(":");
        punishmentDetail = punishmentData[1].split("\\.");

        switch(PunishmentType.valueOf(punishmentData[0].toUpperCase())) {
            case POTION:
                cachePotionEffectType = PotionEffectType.getByName(punishmentDetail[0].toUpperCase());
                cachePotionEffectLevel = Integer.valueOf(punishmentDetail[1]);
                cachePotionEffect = new PotionEffect(cachePotionEffectType, 10, cachePotionEffectLevel);
                resultPunishment = new Punishment(PunishmentType.POTION, cachePotionEffect);
                return resultPunishment;
            case COMMAND:
                resultPunishment = new Punishment(PunishmentType.COMMAND, punishmentData[1]);
                return resultPunishment;
            default:
                return null;
        }
    }

    public int getMoodDecreasePeriod(MoodUpdateTaskType type) {
        switch (type) {
            case COMMON:
                return data.getInt("Decrease.Common.Period");
            case SUNNY:
                return data.getInt("Decrease.Sunny.Period");
            case RAINY:
                return data.getInt("Decrease.Rainy.Period");
            case SNOWY:
                return data.getInt("Decrease.Snowy.Period");
            default:
                return -1;
        }
    }

    public int getMoodDecreaseStep(MoodUpdateTaskType type) {
        switch (type) {
            case COMMON:
                return data.getInt("Decrease.Common.Step");
            case SUNNY:
                return data.getInt("Decrease.Sunny.Step");
            case RAINY:
                return data.getInt("Decrease.Rainy.Step");
            case SNOWY:
                return data.getInt("Decrease.Snowy.Step");
            default:
                return -1;
        }
    }
}
