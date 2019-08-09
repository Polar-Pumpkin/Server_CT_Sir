package org.serverct.sir.hungu.utils;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.serverct.sir.hungu.Hungu;
import org.serverct.sir.hungu.enums.MessageType;

import java.io.File;

public class LocaleUtil {

    private Plugin plugin;
    private File dataFile;
    @Getter private FileConfiguration data;

    private String pluginPrefix;
    private String typePrefix;
    private ConfigurationSection targetSection;

    public LocaleUtil(Plugin plugin) {
        this.plugin = plugin;
        //dataFile = new File(plugin.getDataFolder() + File.separator + "Locale.yml");
        dataFile = new File(plugin.getDataFolder(), "Locale.yml");
        init();
    }

    public void init() {
        if(!dataFile.exists()) {
            plugin.saveResource("Locale.yml", false);
            //Bukkit.getLogger().info("> 未找到语言文件, 已自动生成.");
        } else {
            //Bukkit.getLogger().info("> 已加载语言文件.");
        }
        System.out.println("DataFile: " +  dataFile);
        data = YamlConfiguration.loadConfiguration(dataFile);
    }

    public void debug(String message) {
        if(Hungu.getInstance().checkDebugMode()) {
            Bukkit.getLogger().info(ChatColor.stripColor(buildMessage(MessageType.DEBUG, message)));
        }
    }

    public String getMessage(MessageType type, String section, String path) {
        if(data.getKeys(false).contains(section)) {
            targetSection = data.getConfigurationSection(section);

            if(targetSection.getString(path) != null) {
                return buildMessage(type, targetSection.getString(path));
            }
        }

        return ChatColor.translateAlternateColorCodes('&', "&c&lERROR&7(获取语言信息遇到错误, 请联系管理员解决.)");
    }

    public String buildMessage(MessageType type, String message) {
        if(type != MessageType.DEBUG) {
            pluginPrefix = data.getString("Plugin.Prefix");
            typePrefix = data.getString("Plugin." + type.toString());
        } else {
            pluginPrefix = "&9[&d" + plugin.getName() + "&9]&7(&d&lDEBUG&7) ";
            typePrefix = "&d&l>> ";
        }

        return ChatColor.translateAlternateColorCodes('&', pluginPrefix + typePrefix + message);
    }
}
