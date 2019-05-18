package org.serverct.sir.mood;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.serverct.sir.mood.command.CommandHandler;
import org.serverct.sir.mood.configuration.Area;
import org.serverct.sir.mood.configuration.Config;
import org.serverct.sir.mood.configuration.Item;
import org.serverct.sir.mood.configuration.Language;
import org.serverct.sir.mood.configuration.PlayerData;
import org.serverct.sir.mood.hooks.PlaceholderAPIHook;
import org.serverct.sir.mood.hooks.VaultHook;
import org.serverct.sir.mood.listener.*;
import org.serverct.sir.mood.runnable.tasks.CheckTask;
import org.serverct.sir.mood.runnable.tasks.DataSaveTask;
import org.serverct.sir.mood.runnable.tasks.MoodUpdateTask;

public final class Mood extends JavaPlugin {

    private static Mood instance;
    public static final String PLUGIN_VERSION = "1.0-SNAPSHOT";
    public static boolean vaultHook = false;
    public static boolean debugMode = false;

    public static Mood getInstance() {
        return instance;
    }

    private String[] enableMsg = {
            "----------------------------------------",
            "",
            "  Mood | 心情 >>>",
            "",
            "  作者: EntityParrot_",
            "  版本: " + Mood.PLUGIN_VERSION,
            "",
            "  正在启动 >>>",
            "",
            "----------------------------------------"
    };

    private String[] disableMsg = {
            "----------------------------------------",
            "",
            "  Mood | 心情 >>>",
            "",
            "  作者: EntityParrot_",
            "  版本: " + Mood.PLUGIN_VERSION,
            "",
            "  正在关闭 >>>",
            "",
            "----------------------------------------"
    };

    String loadEndSuffix = "----------------------------------------";

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

        init();

        runTasks();

        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerDamagedListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerDeathListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerInteractListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerMoveListener(), this);
        Bukkit.getLogger().info("  > 已注册监听器.");
        Bukkit.getPluginCommand("mood").setExecutor(new CommandHandler());
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

        if(Config.getInstance().getData().getBoolean("Debug")) {
            debugMode = true;
            Bukkit.getLogger().info("  > 已启动 Debug 模式.");
        }

        Language.getInstance().loadLanguage();
        Config.getInstance().loadConfig();
        PlayerData.getInstance().loadPlayerData();
        Item.getInstance().loadItem();
        Area.getInstance().loadArea();
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

        PlayerData.getInstance().save();
        Bukkit.getLogger().info("  > 已保存玩家数据.");

        Bukkit.getLogger().info(loadEndSuffix);
    }

    private void runTasks() {
        dataSaveTask = new DataSaveTask();
        dataSaveTask.runTaskTimer(this, 1, Config.getInstance().getData().getInt("Delay.DataSave") * 20 * 60);
        Bukkit.getLogger().info("  > 已启动定时保存任务.");

        playerCheckTask = new CheckTask();
        playerCheckTask.runTaskTimer(this, 1, Config.getInstance().getData().getInt("Delay.MoodCheck") * 20 * 60);
        Bukkit.getLogger().info("  > 已启动定时玩家数据监测任务.");

        commonMoodDecreaseTask = new MoodUpdateTask(MoodChangeType.COMMON);
        sunnyMoodDecreaseTask = new MoodUpdateTask(MoodChangeType.SUNNY);
        rainyMoodDecreaseTask = new MoodUpdateTask(MoodChangeType.RAINY);
        snowyMoodDecreaseTask = new MoodUpdateTask(MoodChangeType.SNOWY);
        commonMoodDecreaseTask.runTaskTimer(this, 1, Config.getInstance().getMoodDecreasePeriod(MoodChangeType.COMMON));
        sunnyMoodDecreaseTask.runTaskTimer(this, 1, Config.getInstance().getMoodDecreasePeriod(MoodChangeType.SUNNY));
        rainyMoodDecreaseTask.runTaskTimer(this, 1, Config.getInstance().getMoodDecreasePeriod(MoodChangeType.RAINY));
        snowyMoodDecreaseTask.runTaskTimer(this, 1, Config.getInstance().getMoodDecreasePeriod(MoodChangeType.SNOWY));
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

    public void setVaultHook(boolean bool) {
        vaultHook = bool;
    }
}
