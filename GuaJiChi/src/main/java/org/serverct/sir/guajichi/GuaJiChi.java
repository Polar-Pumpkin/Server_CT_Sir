package org.serverct.sir.guajichi;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.serverct.sir.guajichi.command.CommandHandler;
import org.serverct.sir.guajichi.config.ConfigManager;
import org.serverct.sir.guajichi.hooks.VaultHook;
import org.serverct.sir.guajichi.listener.InteractListener;
import org.serverct.sir.guajichi.listener.MoveListener;
import org.serverct.sir.guajichi.utils.LocaleUtil;

public final class GuaJiChi extends JavaPlugin{

    @Getter private static GuaJiChi instance;
    @Getter private LocaleUtil locale = new LocaleUtil(this);

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        locale.init();
        VaultHook.getInstance().loadVault();
        ConfigManager.getInstance().load();
        Bukkit.getPluginCommand("gjc").setExecutor(new CommandHandler());
        Bukkit.getPluginManager().registerEvents(new MoveListener(), this);
        Bukkit.getPluginManager().registerEvents(new InteractListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
