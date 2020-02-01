package org.serverct.sir.guajichi.utils;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocaleUtil {

    public static final String TOOL_VERSION = "v1.4";

    private String toolPrefix = "&7[&b&lEP's &aLocale Tool&7] ";
    private String toolINFO = "&a&l> ";
    private String toolWARN = "&e&l> ";
    private String toolERROR = "&c&l> ";
    private String toolDEBUG = "&d&l> ";

    public static MessageType INFO = MessageType.INFO;
    public static MessageType WARN = MessageType.WARN;
    public static MessageType ERROR = MessageType.ERROR;
    public static MessageType DEBUG = MessageType.DEBUG;

    @Getter @Setter private String defaultLocaleKey = "Chinese";
    private Plugin plugin;
    private File dataFolder;
    @Getter private Map<String, FileConfiguration> locales = new HashMap<>();

    public LocaleUtil(Plugin plugin) {
        this.plugin = plugin;
        dataFolder = new File(plugin.getDataFolder() + File.separator + "Locales");
        init();
    }

    public void init() {
        if(!dataFolder.exists()) {
            dataFolder.mkdirs();
            plugin.saveResource("Locales/" + defaultLocaleKey + ".yml", false);
            log("&7Language folder not found, has been automatically generated.", MessageType.WARN, true);
        } else {
            if(dataFolder.listFiles() == null || dataFolder.listFiles().length == 0) {
                plugin.saveResource("Locales/" + defaultLocaleKey + ".yml", false);
                log("&7Default language file not found, has been automatically generated.", MessageType.WARN, true);
            }
        }
        load();
    }

    private void load() {
        File[] localeDataFiles = dataFolder.listFiles(pathname -> {
            String fileName = pathname.getName();
            return fileName.endsWith(".yml");
        });

        if(localeDataFiles != null) {
            for(File dataFile : localeDataFiles) {
                locales.put(CommonUtil.getNoExFileName(dataFile.getName()), YamlConfiguration.loadConfiguration(dataFile));
            }
        }
        log("&7Loading language data successful, " + locales.size() + " locale(s) loaded in total.", MessageType.INFO, true);
    }

    public void debug(String message, boolean viaTool) {
        if(viaTool) {
            Bukkit.getLogger().info(color(toolPrefix + toolDEBUG + message));
        } else {
            if(plugin.getConfig().getBoolean("Debug")) {
                Bukkit.getLogger().info(buildMessage(null, MessageType.DEBUG, message));
            }
        }
    }

    public void log(String message, MessageType type, boolean viaTool) {
        if(type != MessageType.DEBUG) {
            String result;
            String level;

            switch (type) {
                case INFO:
                default:
                    level = toolINFO;
                case WARN:
                    level = toolWARN;
                case ERROR:
                    level = toolERROR;
            }

            if(viaTool) {
                result = color(toolPrefix + level + message);
            } else {
                result = buildMessage(null, type, message);
            }

            Bukkit.getLogger().info(result);
        } else {
            debug(message, viaTool);
        }
    }

    public String getMessage(String key, MessageType type, String section, String path) {
        FileConfiguration data = locales.containsKey(key) ? locales.get(key) : locales.get(defaultLocaleKey);

        if(data.getKeys(false).contains(section)) {
            String message = data.getConfigurationSection(section).getString(path);
            if(message != null && !message.equalsIgnoreCase("")) {
                return buildMessage(key, type, message);
            }
        }

        log("&7Encountered an error when try to get a message."
                + "Key: &c" + key
                + "&7, Type: &c" + type.toString()
                + "&7, Section: &c" + section
                + "&7, Path: &c" + path
                + "&7.", MessageType.ERROR, true);
        return color("&c&lERROR&7(Encountered language information encountered an error, please contact the administrator to resolve.)");
    }

    public String buildMessage(String key, MessageType type, String message) {
        FileConfiguration data = locales.containsKey(key) ? locales.get(key) : locales.get(defaultLocaleKey);
        String pluginPrefix = type != MessageType.DEBUG ? data.getString("Plugin.Prefix") : "&9[&d" + plugin.getName() + "&9]&7(&d&lDEBUG&7) ";
        String typePrefix = type != MessageType.DEBUG ? data.getString("Plugin." + type.toString()) : "&d&l>> ";

        return color(pluginPrefix + typePrefix + message);
    }

    public List<String> getHelp(String key) {
        if(locales.containsKey(key)) {
            return locales.get(key).getStringList("Plugin.HelpMessage");
        }
        return locales.get(defaultLocaleKey).getStringList("Plugin.HelpMessage");
    }

    public String getLocation(Location loc) {
        return color("&7(&c" + loc.getX() + "&7, &c" + loc.getY() + "&7, &c" + loc.getZ() + "&7)");
    }

    public String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}

enum MessageType {
    INFO, WARN, ERROR, DEBUG
}
