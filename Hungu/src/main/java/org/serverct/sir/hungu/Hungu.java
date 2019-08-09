package org.serverct.sir.hungu;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.serverct.sir.hungu.command.CommandHandler;
import org.serverct.sir.hungu.configuration.HunguManager;
import org.serverct.sir.hungu.enums.MessageType;
import org.serverct.sir.hungu.hooks.VaultHook;
import org.serverct.sir.hungu.utils.HunguUtil;
import org.serverct.sir.hungu.utils.LocaleUtil;

import java.io.File;

public final class Hungu extends JavaPlugin{

    private static Hungu instance;

    public static Hungu getInstance() {
        return instance;
    }

    private File configFile = new File(getDataFolder() + File.separator + "config.yml");
    @Getter private HunguManager hunguManager;
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
        hunguManager = new HunguManager(getConfig());
        hunguManager.loadHungu();
        Bukkit.getPluginCommand("hungu").setExecutor(new CommandHandler());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void reload(CommandSender sender) {
        reloadConfig();
        hunguManager = new HunguManager(getConfig());
        hunguManager.loadHungu();
        HunguUtil.getInstance().reloadHunguManager();
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
