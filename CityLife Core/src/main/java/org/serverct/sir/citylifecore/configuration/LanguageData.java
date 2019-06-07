package org.serverct.sir.citylifecore.configuration;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.serverct.sir.citylifecore.CityLifeCore;
import org.serverct.sir.citylifecore.enums.MessageType;

import java.io.File;

public class LanguageData {

    private static LanguageData instance;

    public static LanguageData getInstance() {
        if(instance == null) {
            instance = new LanguageData();
        }
        return instance;
    }

    private File dataFile = new File(CityLifeCore.getInstance().getDataFolder() + File.separator + "Language.yml");
    @Getter private FileConfiguration data;

    private String pluginPrefix;
    private String typePrefix;
    private ConfigurationSection targetSection;

    public void loadLanguage() {
        if(!dataFile.exists()) {
            CityLifeCore.getInstance().saveResource("Language.yml", false);
            Bukkit.getLogger().info("> 未找到语言文件, 已自动生成.");
        } else {
            Bukkit.getLogger().info("> 已加载语言文件.");
        }
        data = YamlConfiguration.loadConfiguration(dataFile);
    }

    public String getMessage(MessageType type, String section, String path) {
        pluginPrefix = data.getString("Plugin.Prefix");
        typePrefix = data.getString("Plugin." + type.toString());

        if(data.getKeys(false).contains(section)) {
            targetSection = data.getConfigurationSection(section);

            if(targetSection.getString(path) != null) {
                return ChatColor.translateAlternateColorCodes('&', pluginPrefix + typePrefix + targetSection.getString(path));
            }
        }
        return ChatColor.translateAlternateColorCodes('&', "&c&lERROR&7(获取语言信息遇到错误, 请联系管理员解决.)");
    }

    public String buildMessage(MessageType type, String message) {
        pluginPrefix = data.getString("Plugin.Prefix");
        typePrefix = data.getString("Plugin." + type.toString());
        return ChatColor.translateAlternateColorCodes('&', pluginPrefix + typePrefix + message);
    }

}
