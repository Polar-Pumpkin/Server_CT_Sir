package org.serverct.sir.duobao;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;
import org.serverct.sir.duobao.command.CommandHandler;
import org.serverct.sir.duobao.data.TreasureItem;
import org.serverct.sir.duobao.listener.PlayerInteractListener;
import org.serverct.sir.duobao.manager.GameManager;
import org.serverct.sir.duobao.manager.ItemManager;
import org.serverct.sir.duobao.util.LocaleUtil;

import java.io.File;

public final class Duobao extends JavaPlugin {

    @Getter private static Duobao instance;
    @Getter private LocaleUtil locale = new LocaleUtil(this);
    @Getter private String localeKey;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        if(!new File(getDataFolder() + File.separator + "config.yml").exists()) {
            saveDefaultConfig();
        }
        init();

        Bukkit.getPluginManager().registerEvents(new PlayerInteractListener(), this);
        Bukkit.getPluginCommand("treasure").setExecutor(new CommandHandler());
    }

    public void init() {
        locale.init();
        localeKey = getConfig().getString("Language");
        ConfigurationSerialization.registerClass(TreasureItem.class);

        GameManager.getInstance().loadArea();
        ItemManager.getInstance().loadItems();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        for(String id : GameManager.getInstance().getGameMap().keySet()) {
            GameManager.getInstance().stopGame(id);
        }
    }
}
