package org.serverct.sir.citylifecore;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.serverct.sir.citylifecore.api.CityLifeCoreApi;
import org.serverct.sir.citylifecore.command.CommandHandler;
import org.serverct.sir.citylifecore.configuration.ConfigData;
import org.serverct.sir.citylifecore.configuration.LanguageData;
import org.serverct.sir.citylifecore.listener.PlayerChatListener;
import org.serverct.sir.citylifecore.listener.PlayerInteractListener;
import org.serverct.sir.citylifecore.listener.PlayerMoveListener;

public final class CityLifeCore extends JavaPlugin {

    private static CityLifeCore instance;

    public static CityLifeCore getInstance() {
        return instance;
    }

    @Getter private static CityLifeCoreApi API;
    @Getter @Setter private boolean vaultHook = false;
    @Getter private static String PLUGIN_VERSION = "1.0-Release";

    private String[] enableMsg = {
            "----------------------------------------",
            "",
            "  CityLife Core | 都市人生核心 >>>",
            "",
            "  作者: EntityParrot_",
            "  版本: " + CityLifeCore.PLUGIN_VERSION,
            "",
            "  正在启动 >>>",
            "",
            "----------------------------------------"
    };
    String loadEndSuffix = "----------------------------------------";

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        API = new CityLifeCoreApi();

        init();

        Bukkit.getPluginManager().registerEvents(new PlayerMoveListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerChatListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerInteractListener() , this);
        Bukkit.getLogger().info("  > 已注册监听器.");

        Bukkit.getPluginCommand("citylifecore").setExecutor(new CommandHandler());
        Bukkit.getLogger().info("  > 已注册命令.");

        Bukkit.getLogger().info(loadEndSuffix);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void init() {
        for(String msg : enableMsg) {
            Bukkit.getLogger().info(msg);
        }

        LanguageData.getInstance().loadLanguage();
        ConfigData.getInstance().loadConfig();
        API.getVaultUtil().loadVault();
    }
}
