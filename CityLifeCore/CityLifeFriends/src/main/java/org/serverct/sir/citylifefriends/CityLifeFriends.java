package org.serverct.sir.citylifefriends;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.serverct.sir.citylifecore.CityLifeCore;
import org.serverct.sir.citylifecore.api.CityLifeCoreApi;
import org.serverct.sir.citylifecore.utils.LocaleUtil;
import org.serverct.sir.citylifefriends.command.CommandHandler;
import org.serverct.sir.citylifefriends.configuration.ConfigDataManager;
import org.serverct.sir.citylifefriends.configuration.InventoryConfigManager;
import org.serverct.sir.citylifefriends.configuration.PlayerDataManager;

public final class CityLifeFriends extends JavaPlugin {

    @Getter private static CityLifeFriends instance;
    @Getter private CityLifeCoreApi coreApi;
    public static final String PLUGIN_VERSION = "1.0-RELEASE";
    @Getter private LocaleUtil locale;

    private String[] enableMsg = {
            "----------------------------------------",
            "",
            "  CityLife Friends | 都市人生 社交 >>>",
            "",
            "  作者: EntityParrot_",
            "  版本: " + CityLifeFriends.PLUGIN_VERSION,
            "",
            "  正在启动 >>>",
            "",
            "----------------------------------------"
    };

    private String[] disableMsg = {
            "----------------------------------------",
            "",
            "  CityLife Friends | 都市人生 社交 >>>",
            "",
            "  作者: EntityParrot_",
            "  版本: " + CityLifeFriends.PLUGIN_VERSION,
            "",
            "  正在关闭 >>>",
            "",
            "----------------------------------------"
    };

    private String loadEndSuffix = "----------------------------------------";

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        coreApi = CityLifeCore.getAPI();

        init();

        Bukkit.getPluginCommand("citylifefriends").setExecutor(new CommandHandler());
        Bukkit.getLogger().info("  > 已注册命令.");

        Bukkit.getLogger().info(loadEndSuffix);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        uninit();
    }

    private void init() {
        for(String msg : enableMsg) {
            Bukkit.getLogger().info(msg);
        }

        if(coreApi.getLocaleManager().registerDebugMode(this)) {
            Bukkit.getLogger().info("  > 已启动 Debug 模式.");
        }

        locale = coreApi.getLocaleManager().registerLocaleUtil(this);
        ConfigDataManager.getInstance().loadConfig();
        InventoryConfigManager.getInstance().loadGuis();
        PlayerDataManager.getInstance().loadPlayerData();
    }

    private void uninit() {
        for(String msg : disableMsg) {
            Bukkit.getLogger().info(msg);
        }

        Bukkit.getLogger().info(loadEndSuffix);
    }
}
