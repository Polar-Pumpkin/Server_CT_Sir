package org.serverct.sir.anohanamarry;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.serverct.sir.anohanamarry.command.CommandHandler;
import org.serverct.sir.anohanamarry.configuration.ItemData;
import org.serverct.sir.anohanamarry.configuration.PlayerData.PlayerDataManager;
import org.serverct.sir.anohanamarry.configuration.LanguageData.Language;
import org.serverct.sir.anohanamarry.hook.AMarryExpansion;
import org.serverct.sir.anohanamarry.inventory.InventoryManager;
import org.serverct.sir.anohanamarry.listener.*;

import java.io.File;

public final class ANOHANAMarry extends JavaPlugin {

    private static ANOHANAMarry INSTANCE;
    public final static String PLUGIN_VERSION = "1.0-SNAPSHOT";
    private static Economy econ = null;

    private File configFile = new File(getDataFolder() + File.separator + "config.yml");

    private String[] enableMsg = {
            "&d--------------------------------------------------",
            "",
            "  &6ANOHANA Marry &7>>>",
            "",
            "  &7作者: &cEntityParrot_",
            "  &7版本: &c" + ANOHANAMarry.PLUGIN_VERSION,
            "",
            "  &a正在启动 &7>>>",
            "",
            "&d--------------------------------------------------"
    };

    private String[] reloadMsg = {
            "&d--------------------------------------------------",
            "",
            "  &6ANOHANA Marry &7>>>",
            "",
            "  &7作者: &cEntityParrot_",
            "  &7版本: &c" + ANOHANAMarry.PLUGIN_VERSION,
            "",
            "  &e正在重载 &7>>>",
            "",
            "&d--------------------------------------------------"
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

        Language.getInstance().loadLanguageData();
        PlayerDataManager.getInstance().loadPlayerDatas();
        ItemData.getInstance().loadItemData();
        InventoryManager.getInstance().loadGuiData();

        Bukkit.getPluginCommand("anohanamarry").setExecutor(new CommandHandler());
        Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "  &a&l> &7注册命令成功."));
        Bukkit.getPluginManager().registerEvents(new OnPlayerJoin(), this);
        Bukkit.getPluginManager().registerEvents(new OnPlayerQuit(), this);
        Bukkit.getPluginManager().registerEvents(new OnPlayerClickInventory(), this);
        Bukkit.getPluginManager().registerEvents(new OnPlayerInteractEntity(), this);
        Bukkit.getPluginManager().registerEvents(new OnPlayerInteract(), this);
        Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "  &a&l> &7注册事件监听器成功."));

        String enableEndSuffix = "&d--------------------------------------------------";
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
        Language.getInstance().loadLanguageData();
        PlayerDataManager.getInstance().loadPlayerDatas();
        ItemData.getInstance().loadItemData();
        InventoryManager.getInstance().loadGuiData();

        String enableEndSuffix = "&d--------------------------------------------------";
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
        Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "  &a> &7已连接 &d&lVault&7."));
        return econ != null;
    }

    public static Economy getEconomy() {
        return econ;
    }

    private void loadConfig() {
        if(!configFile.exists()){
            saveDefaultConfig();
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "  &e> &7未找到配置文件, 已自动生成."));
        } else {
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "  &a> &7已加载配置文件."));
        }
    }

    private void loadPlaceholderAPIExpansion() {
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new AMarryExpansion().register();
            getConfig().set("Placeholders", true);
            saveConfig();
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "  &a> &7已连接 &d&lPlaceholderAPI&7."));
        } else {
            getConfig().set("Placeholders", false);
            saveConfig();
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "  &e> &7未找到 &d&lPlaceholderAPI&7, 变量模块不可用."));
        }
    }

    private void loadVault() {
        if (!setupEconomy()) {
            getConfig().set("Cost.Enable", false);
            saveConfig();
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "  &e> &7未找到 &d&lVault&7, 扣费模块不可用."));
        }
    }

    public boolean getLoveLevelMode() {
        if(getConfig().getString("LoveLevel.Mode").equalsIgnoreCase("requirement")) {
            return true;
        }
        return false;
    }

    public int getExpireDelay(boolean isSocialize) {
        if(isSocialize) {
            return getConfig().getInt("Timer.Socialize");
        }
        return getConfig().getInt("Timer.MarryPropose");
    }

    public int getMarryRequirement() {
        return getConfig().getInt("MarryRequirement");
    }

}
