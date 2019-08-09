package org.serverct.sir.tianfu;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.serverct.sir.tianfu.command.CommandHandler;
import org.serverct.sir.tianfu.config.GuiManager;
import org.serverct.sir.tianfu.config.PlayerDataManager;
import org.serverct.sir.tianfu.config.TalentManager;
import org.serverct.sir.tianfu.hooks.PlayerPointsHook;
import org.serverct.sir.tianfu.hooks.VaultHook;
import org.serverct.sir.tianfu.listener.InventoryClickListener;
import org.serverct.sir.tianfu.listener.PlayerAttackListener;
import org.serverct.sir.tianfu.listener.PlayerJoinListener;
import org.serverct.sir.tianfu.util.LocaleUtil;

public final class Tianfu extends JavaPlugin {

    @Getter private static Tianfu instance;
    @Getter private LocaleUtil locale;
    @Getter private String localeKey;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        init();

        Bukkit.getPluginManager().registerEvents(new PlayerAttackListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryClickListener(), this);

        Bukkit.getPluginCommand("tianfu").setExecutor(new CommandHandler());
    }

    public void init() {
        locale = new LocaleUtil(this);
        localeKey = getConfig().getString("Language");

        VaultHook.getInstance().loadVault();
        PlayerPointsHook.getInstance().loadPlayerPoints();

        TalentManager.getInstance().load();
        PlayerDataManager.getInstance().load();
        GuiManager.getInstance().load();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
