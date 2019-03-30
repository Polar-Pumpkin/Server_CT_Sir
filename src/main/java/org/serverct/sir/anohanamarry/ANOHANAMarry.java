package org.serverct.sir.anohanamarry;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.serverct.sir.anohanamarry.command.CommandHandler;
import org.serverct.sir.anohanamarry.configuration.PlayerData;
import org.serverct.sir.anohanamarry.configuration.Language;
import org.serverct.sir.anohanamarry.hook.AMarryExpansion;
import org.serverct.sir.anohanamarry.listener.OnPlayerJoin;
import org.serverct.sir.anohanamarry.listener.OnPlayerQuit;

import java.io.File;

public final class ANOHANAMarry extends JavaPlugin {

    private static ANOHANAMarry INSTANCE;
    public final static String PLUGIN_VERSION = "1.0-SNAPSHOT";
    private static Economy econ = null;

    private File configFile = new File(getDataFolder() + File.separator + "config.yml");

    private String[] enableMsg = {
            "&a&l> &d&m------------------------------",
            "",
            "  &6&lANOHANA Marry &7>>>",
            "",
            "  &7作者: &c&l&oEntityParrot_",
            "  &7版本: &c&l&o" + ANOHANAMarry.PLUGIN_VERSION,
            "",
            "  &a&l正在启动 &7>>>",
            "",
            "&a&l> &d&m------------------------------"
    };

    private String[] reloadMsg = {
            "&e&l> &d&m------------------------------",
            "",
            "  &6&lANOHANA Marry &7>>>",
            "",
            "  &7作者: &c&l&oEntityParrot_",
            "  &7版本: &c&l&o" + ANOHANAMarry.PLUGIN_VERSION,
            "",
            "  &e&l正在重载 &7>>>",
            "",
            "&e&l> &d&m------------------------------"
    };

    public static ANOHANAMarry getINSTANCE() {
        return INSTANCE;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        INSTANCE = this;

        for(String msg : enableMsg) {
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', msg));
        }

        loadConfig();
        loadPlaceholderAPIExpansion();
        loadVault();

        Language.getLanguageClass().loadLanguageData();
        PlayerData.getPlayerDataManager().loadPlayerData();

        Bukkit.getPluginCommand("anohanamarry").setExecutor(new CommandHandler());
        Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "  &a&l> &7注册命令成功."));
        Bukkit.getPluginManager().registerEvents(new OnPlayerJoin(), this);
        Bukkit.getPluginManager().registerEvents(new OnPlayerQuit(), this);
        Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "  &a&l> &7注册事件监听器成功."));

        String enableEndSuffix = "&a&l> &d&m------------------------------";
        Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', enableEndSuffix));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void reloadPlugin() {
        for(String msg : reloadMsg) {
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', msg));
        }

        reloadConfig();
        loadPlaceholderAPIExpansion();
        Language.getLanguageClass().loadLanguageData();
        PlayerData.getPlayerDataManager().loadPlayerData();

        String enableEndSuffix = "&e&l> &d&m------------------------------";
        Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', enableEndSuffix));
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "  &a&l> &7已连接 &d&lVault&7."));
        return econ != null;
    }

    public static Economy getEconomy() {
        return econ;
    }

    private void loadConfig() {
        if(!configFile.exists()){
            saveDefaultConfig();
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "  &a&l> &7未找到配置文件, 已自动生成."));
        } else {
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "  &a&l> &7已加载配置文件."));
        }
    }

    private void loadPlaceholderAPIExpansion() {
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new AMarryExpansion().register();
            getConfig().set("Placeholders", true);
            saveConfig();
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "  &a&l> &7已连接 &d&lPlaceholderAPI&7."));
        } else {
            getConfig().set("Placeholders", false);
            saveConfig();
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "  &a&l> &7未找到 &d&lPlaceholderAPI &7,[变量]模块不可用."));
        }
    }

    private void loadVault() {
        if (!setupEconomy()) {
            getConfig().set("Cost.Enable", false);
            saveConfig();
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "  &a&l> &7未找到 &d&lVault &7,[操作花费]模块不可用."));
        }
    }

}
