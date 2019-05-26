package org.serverct.sir.citylifemood.configuration;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.serverct.sir.citylifemood.CityLifeMood;
import org.serverct.sir.citylifemood.enums.MoodChangeType;
import org.serverct.sir.citylifemood.data.Punishment;
import org.serverct.sir.citylifemood.enums.PunishmentType;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ConfigManager {

    private static ConfigManager instance;

    public static ConfigManager getInstance() {
        if(instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    private File dataFile = new File(CityLifeMood.getInstance().getDataFolder() + File.separator + "config.yml");
    @Getter private FileConfiguration data = YamlConfiguration.loadConfiguration(dataFile);

    @Getter private Map<Integer, Map<Integer, Punishment>> punishmentMap = new HashMap<>();
    private Map<Integer, Punishment> cachePunishmentMap;
    private int counter;

    private ConfigurationSection punishmentSection;
    private String punishmentConfigText;
    private String[] punishmentConfigList;
    private PunishmentType punishmentType;
    private Punishment resultPunishment;

    private String[] punishmentData;
    private String[] punishmentDetail;

    private PotionEffectType cachePotionEffectType;
    private int cachePotionEffectLevel;
    private PotionEffect cachePotionEffect;
    private Punishment targetPunishment;

    public void loadConfig() {
        if(!dataFile.exists()) {
            CityLifeMood.getInstance().saveDefaultConfig();
            data = CityLifeMood.getInstance().getConfig();
            Bukkit.getLogger().info("  > 未找到配置文件, 已自动生成.");
        }
        loadPunishment();
        Bukkit.getLogger().info("  > 已加载配置文件.");
    }

    private void loadPunishment() {
        LocaleManager.getInstance().debug("开始加载惩罚配置.");
        punishmentSection = data.getConfigurationSection("Punishment");

        for(String section : punishmentSection.getKeys(false)) {
            LocaleManager.getInstance().debug("> 心情限额: " + section);
            punishmentConfigText = punishmentSection.getString(section);
            LocaleManager.getInstance().debug("  > 源惩罚配置信息: " + punishmentConfigText);

            cachePunishmentMap = new HashMap<>();

            if(punishmentConfigText.contains(";")) {
                LocaleManager.getInstance().debug("  > 检测到惩罚配置信息包含多个惩罚项.");
                punishmentConfigList = punishmentConfigText.split(";");
                counter = 1;
                LocaleManager.getInstance().debug("  > 初始化计数器.");

                for(String configText : punishmentConfigList) {
                    LocaleManager.getInstance().debug("    > 子惩罚项配置信息: " + configText);
                    targetPunishment = catchPunishment(configText);
                    cachePunishmentMap.put(counter, targetPunishment);
                    LocaleManager.getInstance().debug("    > 子惩罚项保存完成: (" + counter + ") " + targetPunishment.info());
                    counter++;
                }
            } else {
                targetPunishment = catchPunishment(punishmentConfigText);
                cachePunishmentMap.put(1, targetPunishment);
                LocaleManager.getInstance().debug("  > 惩罚项保存完成: (单一惩罚项配置) " + targetPunishment.info());
            }
            punishmentMap.put(Integer.valueOf(section), cachePunishmentMap);

            LocaleManager.getInstance().debug("  > 该心情限额下所有惩罚项加载完成, 信息如下.");
            debugCachePunishmentMap(cachePunishmentMap);
        }
        LocaleManager.getInstance().debug("所有惩罚项加载完成, 信息如下.");
        debugPunishmentMap();
        Bukkit.getLogger().info("  > 共加载 " + punishmentMap.size() + " 个惩罚配置.");
    }

    private Punishment catchPunishment(String configText) {
        LocaleManager.getInstance().debug("      > 开始构建惩罚项: " + configText);
        punishmentData = configText.split(":");
        LocaleManager.getInstance().debug("        > 惩罚项类型: " + punishmentData[0] + ", " + "惩罚项值: " + punishmentData[1]);
        punishmentDetail = punishmentData[1].split("\\.");
        punishmentType = PunishmentType.valueOf(punishmentData[0].toUpperCase());
        LocaleManager.getInstance().debug("        > 已识别惩罚项类型: " + punishmentType.getType());

        switch(punishmentType) {
            case POTION:
                cachePotionEffectType = PotionEffectType.getByName(punishmentDetail[0].toUpperCase());
                LocaleManager.getInstance().debug("          > 已识别药水效果类型: " + cachePotionEffectType.getName());
                cachePotionEffectLevel = Integer.valueOf(punishmentDetail[1]);
                LocaleManager.getInstance().debug("          > 已识别药水效果等级: " + punishmentDetail[1]);
                cachePotionEffect = new PotionEffect(cachePotionEffectType, (data.getInt("Delay.Punish") + 1) * 20 * 60, cachePotionEffectLevel);
                LocaleManager.getInstance().debug("        > 构建药水效果成功.");
                resultPunishment = new Punishment(PunishmentType.POTION, cachePotionEffect);
                break;
            case COMMAND:
                resultPunishment = new Punishment(PunishmentType.COMMAND, punishmentData[1]);
                LocaleManager.getInstance().debug("        > 获取命令成功.");
                break;
            case MESSAGE:
                resultPunishment = new Punishment(PunishmentType.MESSAGE, punishmentData[1].replace("<space>", " "));
                LocaleManager.getInstance().debug("        > 获取提示消息成功.");
                break;
            default:
                return null;
        }

        LocaleManager.getInstance().debug("      > 惩罚项构建完成.");
        return resultPunishment;
    }

    public int getMoodDecreasePeriod(MoodChangeType type) {
        switch (type) {
            case COMMON:
                return data.getInt("Decrease.Common.Period") * 20;
            case SUNNY:
                return data.getInt("Decrease.Sunny.Period") * 20;
            case RAINY:
                return data.getInt("Decrease.Rainy.Period") * 20;
            case SNOWY:
                return data.getInt("Decrease.Snowy.Period") * 20;
            default:
                return -1;
        }
    }

    public int getMoodDecreaseStep(MoodChangeType type) {
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
                return 0;
        }
    }

    private void debugPunishmentMap() {
        for(int amount : punishmentMap.keySet()) {
            LocaleManager.getInstance().debug("> 心情限额: " + amount);
            for(int number : punishmentMap.get(amount).keySet()) {
                LocaleManager.getInstance().debug("  > 惩罚项: (" + number + ") " + punishmentMap.get(amount).get(number).info());
            }
        }
    }

    private void debugCachePunishmentMap(Map<Integer, Punishment> cachePunishmentMap) {
        for(int number : cachePunishmentMap.keySet()) {
            LocaleManager.getInstance().debug("    > 惩罚项: (" + number + ") " + cachePunishmentMap.get(number).info());
        }
    }
}
