package org.serverct.sir.hunhuan;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.serverct.sir.hunhuan.command.CommandHandler;
import org.serverct.sir.hunhuan.configuration.HunhuanManager;
import org.serverct.sir.hunhuan.enums.MessageType;
import org.serverct.sir.hunhuan.hooks.VaultHook;
import org.serverct.sir.hunhuan.listener.ItemHeldListener;
import org.serverct.sir.hunhuan.utils.HunhuanUtil;
import org.serverct.sir.hunhuan.utils.LocaleUtil;

import java.io.File;

public final class HunHuan extends JavaPlugin {

    private static HunHuan instance;

    public static HunHuan getInstance() {
        return instance;
    }

    private File configFile = new File(getDataFolder() + File.separator + "config.yml");
    @Getter private HunhuanManager hunhuanManager;
    @Getter private LocaleUtil locale;

    @Override
    public void onLoad() {

    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        locale = new LocaleUtil(this);
        VaultHook.getInstance().loadVault();
        if(!configFile.exists()) {
            saveDefaultConfig();
        }
        hunhuanManager = new HunhuanManager(getConfig());
        hunhuanManager.loadHungu();
        Bukkit.getPluginManager().registerEvents(new ItemHeldListener(), this);
        Bukkit.getPluginCommand("hunhuan").setExecutor(new CommandHandler());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void reload(CommandSender sender) {
        reloadConfig();
        hunhuanManager = new HunhuanManager(getConfig());
        hunhuanManager.loadHungu();
        HunhuanUtil.getInstance().reloadHunguManager();
        sender.sendMessage(locale.getMessage(MessageType.INFO, "Plugin", "ReloadSuccess"));
    }

    public boolean checkDebugMode() {
        return getConfig().getBoolean("Debug");
    }
    public int getAbsorbLimit() {
        return getConfig().getInt("Absorb.Limit");
    }
    public int getAbsorbCost() {
        return getConfig().getInt("Absorb.Cost");
    }
}
