package org.serverct.sir.citylifemood;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.serverct.sir.citylifecore.CityLifeCore;
import org.serverct.sir.citylifemood.command.CommandHandler;
import org.serverct.sir.citylifemood.configuration.*;
import org.serverct.sir.citylifemood.configuration.ItemManager;
import org.serverct.sir.citylifemood.enums.MoodChangeType;
import org.serverct.sir.citylifemood.hooks.PlaceholderAPIHook;
import org.serverct.sir.citylifemood.hooks.VaultHook;
import org.serverct.sir.citylifemood.listener.*;
import org.serverct.sir.citylifemood.runnable.tasks.CheckTask;
import org.serverct.sir.citylifemood.runnable.tasks.DataSaveTask;
import org.serverct.sir.citylifemood.runnable.tasks.MoodUpdateTask;
import org.serverct.sir.citylifecore.api.CityLifeCoreApi;

public final class CityLifeMood extends JavaPlugin {

    private static CityLifeMood instance;
    public static final String PLUGIN_VERSION = "1.0-RELEASE";
    @Getter @Setter private boolean vaultHook = false;
    @Getter private boolean debugMode = false;

    @Getter private CityLifeCoreApi coreApi;

    public static CityLifeMood getInstance() {
        return instance;
    }

    private String[] enableMsg = {
            "----------------------------------------",
            "",
            "  CityLife Mood | 都市人生 心情 >>>",
            "",
            "  作者: EntityParrot_",
            "  版本: " + CityLifeMood.PLUGIN_VERSION,
            "",
            "  正在启动 >>>",
            "",
            "----------------------------------------"
    };

    private String[] disableMsg = {
            "----------------------------------------",
            "",
            "  CityLife Mood | 都市人生 心情 >>>",
            "",
            "  作者: EntityParrot_",
            "  版本: " + CityLifeMood.PLUGIN_VERSION,
            "",
            "  正在关闭 >>>",
            "",
            "----------------------------------------"
    };

    private String loadEndSuffix = "----------------------------------------";

    private BukkitRunnable dataSaveTask;
    private BukkitRunnable playerCheckTask;
    private BukkitRunnable commonMoodDecreaseTask;
    private BukkitRunnable sunnyMoodDecreaseTask;
    private BukkitRunnable rainyMoodDecreaseTask;
    private BukkitRunnable snowyMoodDecreaseTask;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        coreApi = CityLifeCore.getAPI();

        init();

        runTasks();

        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerDamagedListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerDeathListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerInteractListener(), this);
        Bukkit.getPluginManager().registerEvents(new AreaEnterListener(), this);
        Bukkit.getPluginManager().registerEvents(new AreaLeaveListener(), this);
        Bukkit.getLogger().info("  > 已注册监听器.");
        Bukkit.getPluginCommand("citylifemood").setExecutor(new CommandHandler());
        Bukkit.getLogger().info("  > 已注册命令.");

        Bukkit.getLogger().info(loadEndSuffix);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        uninit();
    }

    public void init() {
        for(String msg : enableMsg) {
            Bukkit.getLogger().info(msg);
        }

        if(ConfigManager.getInstance().getData().getBoolean("Debug")) {
            debugMode = true;
            Bukkit.getLogger().info("  > 已启动 Debug 模式.");
        }

        LocaleManager.getInstance().loadLanguage();
        ConfigManager.getInstance().loadConfig();
        PlayerDataManager.getInstance().loadPlayerData();
        ItemManager.getInstance().loadItem();
        AreaManager.getInstance().loadArea();
        VaultHook.getInstance().loadVault();
        loadPlaceholderAPIExpansion();

        Bukkit.getLogger().info(loadEndSuffix);
    }

    public void uninit() {
        for(String msg : disableMsg) {
            Bukkit.getLogger().info(msg);
        }

        dataSaveTask.cancel();
        Bukkit.getLogger().info("  > 已结束定时保存任务.");

        playerCheckTask.cancel();
        Bukkit.getLogger().info("  > 已结束定时玩家数据监测任务.");

        commonMoodDecreaseTask.cancel();
        sunnyMoodDecreaseTask.cancel();
        rainyMoodDecreaseTask.cancel();
        snowyMoodDecreaseTask.cancel();
        Bukkit.getLogger().info("  > 已结束定时心情自减任务.");

        PlayerDataManager.getInstance().save();
        Bukkit.getLogger().info("  > 已保存玩家数据.");

        Bukkit.getLogger().info(loadEndSuffix);
    }

    private void runTasks() {
        dataSaveTask = new DataSaveTask();
        dataSaveTask.runTaskTimer(this, 1, ConfigManager.getInstance().getData().getInt("Delay.DataSave") * 20 * 60);
        Bukkit.getLogger().info("  > 已启动定时保存任务.");

        playerCheckTask = new CheckTask();
        playerCheckTask.runTaskTimer(this, 1, ConfigManager.getInstance().getData().getInt("Delay.MoodCheck") * 20 * 60);
        Bukkit.getLogger().info("  > 已启动定时玩家数据监测任务.");

        commonMoodDecreaseTask = new MoodUpdateTask(MoodChangeType.COMMON);
        sunnyMoodDecreaseTask = new MoodUpdateTask(MoodChangeType.SUNNY);
        rainyMoodDecreaseTask = new MoodUpdateTask(MoodChangeType.RAINY);
        snowyMoodDecreaseTask = new MoodUpdateTask(MoodChangeType.SNOWY);
        commonMoodDecreaseTask.runTaskTimer(this, 1, ConfigManager.getInstance().getMoodDecreasePeriod(MoodChangeType.COMMON));
        sunnyMoodDecreaseTask.runTaskTimer(this, 1, ConfigManager.getInstance().getMoodDecreasePeriod(MoodChangeType.SUNNY));
        rainyMoodDecreaseTask.runTaskTimer(this, 1, ConfigManager.getInstance().getMoodDecreasePeriod(MoodChangeType.RAINY));
        snowyMoodDecreaseTask.runTaskTimer(this, 1, ConfigManager.getInstance().getMoodDecreasePeriod(MoodChangeType.SNOWY));
        Bukkit.getLogger().info("  > 已启动定时心情自减任务.");
    }

    private void loadPlaceholderAPIExpansion() {
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
            new PlaceholderAPIHook().register();
            Bukkit.getLogger().info("  > 已连接 PlaceholderAPI.");
        } else {
            Bukkit.getLogger().info("  > 未找到 PlaceholderAPI, 变量模块不可用.");
        }
    }
}
