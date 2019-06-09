package org.serverct.sir.citylifefriends;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.serverct.sir.citylifecore.CityLifeCore;
import org.serverct.sir.citylifecore.api.CityLifeCoreApi;
import org.serverct.sir.citylifefriends.configuration.PlayerDataManager;

public final class CityLifeFriends extends JavaPlugin {

    @Getter private static CityLifeFriends INSTANCE;
    @Getter private CityLifeCoreApi coreApi;
    public static final String PLUGIN_VERSION = "1.0-RELEASE";

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
        INSTANCE = this;
        coreApi = CityLifeCore.getAPI();

        init();
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

        /*if(ConfigManager.getInstance().getData().getBoolean("Debug")) {
            debugMode = true;
            Bukkit.getLogger().info("  > 已启动 Debug 模式.");
        }*/

        PlayerDataManager.getInstance().loadPlayerData();

        Bukkit.getLogger().info(loadEndSuffix);
    }

    private void uninit() {
        for(String msg : disableMsg) {
            Bukkit.getLogger().info(msg);
        }

        Bukkit.getLogger().info(loadEndSuffix);
    }
}
